package com.parul.docker_kafka_spring_python;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class DockerKafkaSpringPythonApplication {

	public static void main(String[] args) {
		SpringApplication.run(DockerKafkaSpringPythonApplication.class, args);
	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}
}
