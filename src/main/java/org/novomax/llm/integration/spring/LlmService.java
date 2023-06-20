package org.novomax.llm.integration.spring;

import org.springframework.stereotype.Service;

@Service
public interface LlmService {
    double[] getEmbeddingVector(String document);
}
