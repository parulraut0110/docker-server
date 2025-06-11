package com.parul.dockerspringserver;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.parul.dockerspringserver.controller.GreetingController;
import com.parul.dockerspringserver.service.PythonGreetingService;

import reactor.core.publisher.Mono;

@WebFluxTest(GreetingController.class)
public class GreetingControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PythonGreetingService pythonGreetingService;

    @Test
    void testGreetFromJava() {
        when(pythonGreetingService.getGreetingFromPython("John Doe"))
                .thenReturn(Mono.just("Hello John"));

        webTestClient.get()
                .uri("/api/greet-from-java?fullName=John%20Doe")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello John");
    }

    @Test
    void testGreetFromJavaError() {
        when(pythonGreetingService.getGreetingFromPython("John Doe"))
                .thenReturn(Mono.error(new RuntimeException("Python server unavailable")));

        webTestClient.get()
                .uri("/api/greet-from-java?fullName=John%20Doe")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(s -> s.startsWith("Error: Could not get greeting from Python server."));
    }
}