package org.novomax.llm.integration.spring;

import java.util.List;

public interface VectorStorage {
    List<SearchResult> search(float[] vector, int limit);

    void upcert(final String entityClass, final String entityId, float[] embedding);

    void delete(final String entityClass, final String entityId);

}
