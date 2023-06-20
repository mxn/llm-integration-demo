package org.novomax.llm.integration.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@EnableJms
@ComponentScan
@EnableAutoConfiguration
@EntityScan
@EnableJpaRepositories
@Configuration
public class LlmConfig {
    private final String destinationName;

    private final int embedVectorDim;

    private final byte[] apiKeyBytes;

    public LlmConfig(
            @Value("${org.novomax.llm.integration.destinationName:llmProcessing}")
            String destinationName,
            @Value("${org.novomax.llm.integration.embedVectorDim:1536}")
            int embedVectorDim,
            @Value("${org.novomax.llm.integration.spring.openai.api.key}")
            String apiKey) {
        this.destinationName = destinationName;
        this.embedVectorDim = embedVectorDim;
        this.apiKeyBytes = apiKey.getBytes(StandardCharsets.UTF_8);
    }

    @Bean
    public String getDestinationName() {
        return destinationName;
    }

    public int getEmbedVectorDim() {
        return embedVectorDim;
    }

    @Bean
    RestTemplate restTemplateOpenAi() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getInterceptors().add((req, body, execution) -> {
            req.getHeaders().add("Authorization", //
                    "Bearer " + new String(this.apiKeyBytes, StandardCharsets.UTF_8));
            return execution.execute(req, body);
        });
        return restTemplate;
    }

}
