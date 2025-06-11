package com.parul.dockerspringserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PythonGreetingResponse {
	// @JsonProperty maps JSON key "message" to this Java field
    @JsonProperty("message")
    private String message;

    // Default constructor is required by Jackson for deserialization
    public PythonGreetingResponse() {
    }

    public PythonGreetingResponse(String message) {
        this.message = message;
    }

    // Getter
    public String getMessage() {
        return message;
    }

    // Setter
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PythonGreetingResponse{" +
               "message='" + message + '\'' +
               '}';
    }

}