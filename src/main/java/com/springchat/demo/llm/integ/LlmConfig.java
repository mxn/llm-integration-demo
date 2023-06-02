package com.springchat.demo.llm.integ;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LlmConfig {
    @Value("${llm.integ.destinationName:llmProcessing}")
    private String destinationName;


    @Bean
    public String getDestinationName() {
        return destinationName;
    }
}
