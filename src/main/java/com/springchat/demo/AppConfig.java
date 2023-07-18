package com.springchat.demo;

import com.springchat.demo.entity.Candidate;
import com.springchat.demo.entity.CandidateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.Arrays;

@Configuration
public class AppConfig {

    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private final CandidateRepository candidateRepository;

    public AppConfig(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Bean
    @Profile("demo")
    CommandLineRunner init() {
        Candidate candidate1 = new Candidate("John", "George");
        candidate1.setResume(readResourceAsString("cv_it_support_1.txt"));
        Candidate candidate2 = new Candidate("Aline", "Barbie");
        candidate2.setResume(readResourceAsString("cv_mkt_specialist_2.txt"));
        candidateRepository.saveAll( //
                Arrays.asList( //
                        candidate1, //
                        candidate2,
                        new Candidate("Max", "Nov")
                ));
        return args -> logger.warn("Hello World!");
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
