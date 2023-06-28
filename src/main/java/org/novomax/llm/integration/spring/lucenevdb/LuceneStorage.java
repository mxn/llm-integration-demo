package org.novomax.llm.integration.spring.lucenevdb;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.KnnFloatVectorField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.novomax.llm.integration.spring.SearchResult;
import org.novomax.llm.integration.spring.VectorStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.min;

@Service
public class LuceneStorage implements VectorStorage {
    public static final String ENTITY_CLASS_FIELD_NAME = "entityClass";
    public static final String ENTITY_ID_FIELD_NAME = "entityId";
    private static final String EMBEDDING_FIELD_NAME = "embedding";
    private static Logger LOGGER = LoggerFactory.getLogger(LuceneStorage.class);
    private static AtomicReference<IndexSearcher> indexSearcherAtomicReference = new AtomicReference<>();
    private final Path luceneIndexDirectory;

    public LuceneStorage(
            @Value("${org.novomax.llm.integration.spring.lucenevdb.uri:lucene_storage}")
            String luceneIndexDirectory) {
        this.luceneIndexDirectory = Path.of(luceneIndexDirectory);
    }

    private static void deleteWithIndexWriter(IndexWriter indexWriter, String entityClass, String entityId) throws IOException {
        indexWriter.deleteDocuments(new BooleanQuery.Builder() //
                .add(new BooleanClause(new TermQuery(new Term(ENTITY_CLASS_FIELD_NAME, entityClass)), BooleanClause.Occur.MUST)) //
                .add(new BooleanClause(new TermQuery(new Term(ENTITY_ID_FIELD_NAME, entityId)), BooleanClause.Occur.MUST)) //
                .build());
    }

    @Override
    public List<SearchResult> search(float[] vector, int limit) {

        try {
            KnnFloatVectorQuery knnFloatVectorQuery = new KnnFloatVectorQuery(EMBEDDING_FIELD_NAME, vector, limit);
            IndexSearcher searcher = getOrCreateIndexSearcher();
            TopDocs topDocs = searcher.search(knnFloatVectorQuery, limit);
            List<SearchResult> result = new ArrayList<>();
            for (int i = 0; i < min(topDocs.totalHits.value, limit); i++) {
                Document document = searcher.getIndexReader().storedFields().document(topDocs.scoreDocs[i].doc);
                result.add(new SearchResultImpl(document.get(ENTITY_CLASS_FIELD_NAME), document.get(ENTITY_ID_FIELD_NAME)));
            }
            return result;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void upcert(String entityClass, String entityId, float[] embedding) {
        try (IndexWriter indexWriter = createIndexWriter()) {
            deleteWithIndexWriter(indexWriter, entityClass, entityId);
            indexWriter.addDocument(createDocument(entityClass, entityId, embedding));
            indexWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String entityClass, String entityId) {
        try (IndexWriter indexWriter = createIndexWriter()) {
            deleteWithIndexWriter(indexWriter, entityClass, entityId);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private IndexWriter createIndexWriter() throws IOException {
        return new IndexWriter(new MMapDirectory(luceneIndexDirectory),
                new IndexWriterConfig());
    }


    private Document createDocument(String entityType, String entityId, float[] embedding) {
        Document document = new Document();
        document.add(new StringField(ENTITY_CLASS_FIELD_NAME, entityType, Field.Store.YES));
        document.add(new StringField(ENTITY_ID_FIELD_NAME, entityId, Field.Store.YES));
        document.add(new KnnFloatVectorField(EMBEDDING_FIELD_NAME, embedding));
        return document;
    }

    private IndexSearcher getOrCreateIndexSearcher() {
        try {
            if (indexSearcherAtomicReference.get() == null) {
                FSDirectory fsDirectory = MMapDirectory.open(luceneIndexDirectory);
                DirectoryReader directoryReader = DirectoryReader.open(fsDirectory);
                indexSearcherAtomicReference.set(new IndexSearcher(directoryReader));
            }
            return indexSearcherAtomicReference.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    void closeResources() {
        if (indexWriterAtomicReference.get() != null) {
            try {
                indexWriterAtomicReference.get().close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    record SearchResultImpl(String entityClass, String entityId) implements SearchResult {
    }


}
