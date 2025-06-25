package com.parul.docker_kafka_spring_python.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.parul.docker_kafka_spring_python.service.PythonGreetingService;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController 
@RequestMapping("/api") 
public class GreetingController {
	private final com.parul.docker_kafka_spring_python.service.PythonGreetingService pythonGreetingService;
	
	public GreetingController(PythonGreetingService pythonGreetingService) {
        this.pythonGreetingService = pythonGreetingService;
    }
	
	@GetMapping("/greet-from-java")
	public Mono<String> greetFromJava(@RequestParam String fullName) {
		System.out.println("Received request in Spring Boot for fullName: " + fullName);
		return Mono.fromFuture(pythonGreetingService.getGreetingFromPython(fullName))
				.onErrorResume(e -> {
                    System.err.println("Error calling Python server: " + e.getMessage());
                    return Mono.just("Error: Could not get greeting from Python server. " + e.getMessage());
				});					
	}

}