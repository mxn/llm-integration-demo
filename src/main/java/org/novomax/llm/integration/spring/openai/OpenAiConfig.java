package org.novomax.llm.integration.spring.openai;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class OpenAiConfig {
    private final byte[] apiKeyBytes;

    public OpenAiConfig(@Value("${org.novomax.llm.integration.spring.openai.api.key}")
                        String apiKey) {
        this.apiKeyBytes = apiKey.getBytes(StandardCharsets.UTF_8);
    }

    @Bean
    @Qualifier("openAi")
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