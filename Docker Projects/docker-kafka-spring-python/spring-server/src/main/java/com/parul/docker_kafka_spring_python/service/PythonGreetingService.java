package com.parul.docker_kafka_spring_python.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.KafkaHeaders;

import com.parul.docker_kafka_spring_python.dto.GreetingRequest;
import com.parul.docker_kafka_spring_python.dto.GreetingResponse;

@Service
public class PythonGreetingService {
	private final KafkaTemplate<String, GreetingRequest> kafkaTemplate;
	private ConcurrentHashMap<String, CompletableFuture<String>> responseFuture = new ConcurrentHashMap<>();
	
	public PythonGreetingService(KafkaTemplate<String, GreetingRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
		
	public CompletableFuture<String> getGreetingFromPython(String fullName) {
		 String requestId =  UUID.randomUUID().toString();		 
		 CompletableFuture<String> future = new CompletableFuture<>(); 
		 responseFuture.put(requestId, future); // the HashMap create key value pair with key equal requestId and value equal to empty CompletableFuture and will be filled later after receiving the response  
		 
		 kafkaTemplate.send("greeting-requests", requestId, new GreetingRequest(fullName)); 
		 return future;   // returns an empty future (stores as <requestID, future>) that will be later filled when response will be available
	}
	
	@KafkaListener(topics = "greeting-responses", groupId = "spring-group")			 
	public void handleKafkaResponse(GreetingResponse response, @Header(KafkaHeaders.RECEIVED_KEY) String requestId) {
		System.out.println("Received response for requestId: " + requestId + ", message: " + response.getMessage());
		CompletableFuture<String> future = responseFuture.remove(requestId);
		if(future != null)
			future.complete(response.getMessage());
	}
	
}	