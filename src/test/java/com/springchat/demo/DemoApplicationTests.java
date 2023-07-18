package com.springchat.demo;

import com.springchat.demo.entity.Candidate;
import com.springchat.demo.entity.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.novomax.llm.integration.AiService;
import org.novomax.llm.integration.LlmService;
import org.novomax.llm.integration.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = TestApp.class)
@ActiveProfiles("demo")
class DemoApplicationTests {
    @Autowired
    private LlmService llmService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private AiService freeTextSearch;


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

    @Test
    void updateCv() throws InterruptedException {
        List<Candidate> allCandidates = candidateRepository.findAll();
        Candidate candidate1 = allCandidates.get(0);
        candidate1.setResume(readResourceAsString("cv_it_support_1.txt"));
        Candidate candidate2 = allCandidates.get(1);
        candidate2.setResume(readResourceAsString("cv_mkt_specialist_2.txt"));
        candidateRepository.saveAll(List.of(candidate1, candidate2));
        Thread.sleep(3000);
        List<SearchResult> results = freeTextSearch.findByFreeText("find me a SW engineer", 20);
        assertEquals(2, results.size());
        SearchResult searchResult = results.get(0);
        assertEquals(String.valueOf(candidate1.getId()), searchResult.entityId());
        assertEquals(candidate1.getClass().getName(), searchResult.entityClass());
        assertTrue(0.5 < results.get(0).score());
        assertTrue(results.get(0).score() > results.get(1).score());
    }

    public String readResourceAsString(String resourceName) {
        try {
            byte[] fileData = FileCopyUtils.copyToByteArray(this.getClass().getResourceAsStream(resourceName));
            return new String(fileData);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }
}
