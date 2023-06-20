package com.springchat.demo;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.novomax.llm.integration.spring.CosineDistanceFunction;
import org.novomax.llm.integration.spring.LlmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = TestApp.class)
class DemoApplicationTests {
    @Autowired
    private LlmService llmService;

    @Test
    void contextLoads() {
    }

    @Test
    void openAiRequest() {
        String text = "    Proficient in programming languages: Python, Java, C++\n" +
                "    Web development: HTML, CSS, JavaScript\n" +
                "    Database management: SQL\n" +
                "    Object-oriented design and development\n" +
                "    Software testing and debugging\n" +
                "    Version control systems: Git\n" +
                "    Strong problem-solving and analytical skills\n" +
                "    Excellent communication and teamwork abilities\n";
        double[] embedVector1 = llmService.getEmbeddingVector(text);
        double[] embedVector2 = llmService.getEmbeddingVector(text + " Art Designer");
        double similarity = CosineDistanceFunction.cosineSimilarity(embedVector2, embedVector1);
        Assert.assertNotNull(embedVector1);
        Assert.assertNotNull(embedVector2);
        Assert.assertTrue(similarity > 0.95);
    }
}
