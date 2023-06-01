package com.springchat.demo;

import com.springchat.demo.entity.Candidate;
import com.springchat.demo.entity.CandidateRepository;
import jakarta.jms.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.SimpleMessageConverter;

import java.util.Arrays;

@Configuration
public class AppConfig {

    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private final CandidateRepository candidateRepository;

    public AppConfig(CandidateRepository candidateRepository) {
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


    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory factory) {
        return new JmsTemplate(factory);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }
}
