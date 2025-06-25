package com.parul.docker_kafka_spring_python.dto;

public class GreetingResponse {
	private String message;

    public GreetingResponse() {}
    
    public GreetingResponse(String message) { 
    	this.message = message; 
    }
    
    public String getMessage() { 
    	return message; 
    }
    
    public void setMessage(String message) { 
    	this.message = message;
    }
}    
