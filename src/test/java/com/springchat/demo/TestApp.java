package com.springchat.demo;

import com.springchat.demo.entity.Candidate;
import com.springchat.demo.entity.CandidateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication()
//@EnableJpaRepositories(basePackages = {"com.springchat.demo"})
class TestApp {

    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private final CandidateRepository candidateRepository;

    public TestApp(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Bean
    CommandLineRunner init() {
        candidateRepository.saveAll( //
                Arrays.asList(new Candidate("Max", "Nov"), //
                        new Candidate("John", "George"), //
                        new Candidate("Aline", "Barbie")
                ));
        return args -> logger.warn("Hello World!");
    }
}
