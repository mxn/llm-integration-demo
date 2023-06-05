package com.springchat.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication(scanBasePackages = {DemoApplication.LLM_PACKAGE, DemoApplication.DEMO_PACKAGE})
@EnableJpaRepositories({DemoApplication.LLM_PACKAGE, DemoApplication.DEMO_PACKAGE})
@EntityScan({DemoApplication.LLM_PACKAGE, DemoApplication.DEMO_PACKAGE})
@EnableJms()
public class DemoApplication {
	static final String LLM_PACKAGE = "org.novomax.llm.integration.spring";
	static final String DEMO_PACKAGE = "com.springchat.demo";


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}