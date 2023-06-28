package com.springchat.demo;

import org.junit.jupiter.api.Test;
import org.novomax.llm.integration.spring.CosineDistanceFunction;
import org.novomax.llm.integration.spring.LlmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = TestApp.class)
@ActiveProfiles("demo")
class DemoApplicationTests {
    @Autowired
    private LlmService llmService;

    @Test
    void contextLoads() {
    }

    @Test
    void openAiRequest() {
        String text = """
                    Proficient in programming languages: Python, Java, C++
                    Web development: HTML, CSS, JavaScript
                    Database management: SQL
                    Object-oriented design and development
                    Software testing and debugging
                    Version control systems: Git
                    Strong problem-solving and analytical skills
                    Excellent communication and teamwork abilities
                """;
        double[] embedVector1 = llmService.getEmbeddingVector(text);
        double[] embedVector2 = llmService.getEmbeddingVector(text + " Art Designer");
        double similarity = CosineDistanceFunction.cosineSimilarity(embedVector2, embedVector1);
        assertNotNull(embedVector1);
        assertNotNull(embedVector2);
        assertTrue(similarity > 0.95);
    }
}
