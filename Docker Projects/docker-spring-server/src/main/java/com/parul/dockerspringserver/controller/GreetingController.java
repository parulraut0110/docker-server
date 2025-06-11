package com.parul.dockerspringserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import com.parul.dockerspringserver.service.PythonGreetingService;

@RestController 
@RequestMapping("/api") 
public class GreetingController {
	private final PythonGreetingService pythonGreetingService;
	
	public GreetingController(PythonGreetingService pythonGreetingService) {
        this.pythonGreetingService = pythonGreetingService;
    }
	
	@GetMapping("/greet-from-java")
	public Mono<String> greetFromJava(@RequestParam String fullName) {
		System.out.println("Received request in Spring Boot for fullName: " + fullName);
		return pythonGreetingService.getGreetingFromPython(fullName) 
				.doOnNext(greeting -> System.out.println("Response from python server " + greeting))
				.onErrorResume(e -> {
                    System.err.println("Error calling Python server: " + e.getMessage());
                    return Mono.just("Error: Could not get greeting from Python server. " + e.getMessage());
				});
					
	}

}