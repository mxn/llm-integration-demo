package com.springchat.demo;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.KnnFloatVectorField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.KnnFloatVectorQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestLucene {

    public static final String EMBEDDING_FIELD_NAME = "embedding";
    private Path luceneIndexDirectory;

    @BeforeEach
    void setUp() throws IOException {
        luceneIndexDirectory = Files.createTempDirectory("tempLuceneIndex");
    }

    @Test
    void testLucene() throws IOException {
        Document docementToSearch = null;

        try (IndexWriter indexWriter = new IndexWriter(new MMapDirectory(luceneIndexDirectory),
                new IndexWriterConfig())) {
            for (int i = 0; i < 100; i++) {
                Document document = createDocument();
                if (i == 41) {
                    docementToSearch = document;
                }
                indexWriter.addDocument(document);
            }
        }
        //KnnFloatVectorQueryTest
        IndexSearcher indexSearcher;
        FSDirectory fsDirectory = MMapDirectory.open(luceneIndexDirectory);
        DirectoryReader directoryReader = DirectoryReader.open(fsDirectory);
        indexSearcher = new IndexSearcher(directoryReader);
        float[] searchVector = ((KnnFloatVectorField) docementToSearch.getField(EMBEDDING_FIELD_NAME)).vectorValue();
        KnnFloatVectorQuery knnFloatVectorQuery = new KnnFloatVectorQuery(EMBEDDING_FIELD_NAME, searchVector, 1);
        TopDocs topDocs = indexSearcher.search(knnFloatVectorQuery, 1);
        Document document = indexSearcher.getIndexReader().storedFields().document(topDocs.scoreDocs[0].doc);
        assertEquals(docementToSearch.get("id"), document.get("id"));
    }

    private Document createDocument() {
        Document document = new Document();
        document.add(new StringField("entityClass", "com.hurra.cls.MyEntity", Field.Store.YES));
        document.add(new StringField("entityId", String.valueOf(Math.round(Math.random() * 10000)), Field.Store.YES));
        document.add(new KnnFloatVectorField(EMBEDDING_FIELD_NAME, genFloatArray(1024)));
        return document;
    }

    private float[] genFloatArray(final int dim) {
        float[] result = new float[dim];
        IntStream.range(0, dim).forEach(i -> result[i] = (float) Math.random());
        return result;
    }

}
