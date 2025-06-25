package com.parul.docker_kafka_spring_python.dto;

public class GreetingRequest {
	private String fullName;

    public GreetingRequest() {}
    
    public GreetingRequest(String fullName) { 
    	this.fullName = fullName; 
    }
    
    public String getFullName() { 
    	return fullName; 
    }
    
    public void setFullName(String fullName) { 
    	this.fullName = fullName; 
    }
}    