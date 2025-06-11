package com.parul.dockerspringserver.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.parul.dockerspringserver.dto.PythonGreetingResponse;

import reactor.core.publisher.Mono;

@Service
public class PythonGreetingService {
	private final WebClient webClient;
	
	@Value("${python.server.url}")
	private String pythonServerBaseUrl;
	
	public PythonGreetingService(WebClient.Builder webClientBuilder) {
		// We'll build the WebClient instance in the method where pythonServerBaseUrl is available
        // as @Value fields are injected after constructor.
        // Alternatively, we can inject Environment and get the property here.
        // For simplicity in this example, we'll build it in getGreetingFromPython
        // OR define it in a @PostConstruct or a dedicated @Bean for WebClient.
        // Let's refine the WebClient setup for the SSL part.
        this.webClient = webClientBuilder.build(); // Build a default WebClient, will add base URL later
    }
		
	public Mono<String> getGreetingFromPython(String fullName) {
		// Construct the request body as a Map, which WebClient will convert to JSON
        // The Python server expects {"fullName": "..."}
        // Note: Using Collections.singletonMap is for simplicity for a single key-value JSON.
        // For more complex JSON, you'd use a dedicated DTO (Plain Old Java Object) or a Map.
        java.util.Map<String, String> requestBody = Collections.singletonMap("fullName", fullName);

        System.out.println("Calling Python server at: " + pythonServerBaseUrl + "/welcome with body: " + requestBody);
           
        return webClient.post()
        		.uri(pythonServerBaseUrl + "/welcome")
        		.bodyValue(requestBody)
        		.retrieve()
        		.bodyToMono(PythonGreetingResponse.class)
        		.map(responseDto -> responseDto.getMessage());
	}

}