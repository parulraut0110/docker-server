package com.parul.docker_kafka_spring_python;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.parul.docker_kafka_spring_python.controller.GreetingController;
import com.parul.docker_kafka_spring_python.service.PythonGreetingService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@WebFluxTest(GreetingController.class)
public class GreetingControllerTest {

    // WebTestClient is used to test WebFlux controller endpoints (HTTP layer),
    // not related to WebClient, which is not used in this application.
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PythonGreetingService pythonGreetingService;

    @Test
    void testGreetFromJavaSuccess() {
        CompletableFuture<String> future = CompletableFuture.completedFuture("Hello John");
        when(pythonGreetingService.getGreetingFromPython("John Doe")).thenReturn(future);

        webTestClient.get()
                .uri("/api/greet-from-java?fullName=John%20Doe")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello John");
    }

    @Test
    void testGreetFromJavaError() {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Python server unavailable"));
        when(pythonGreetingService.getGreetingFromPython("John Doe")).thenReturn(future);

        webTestClient.get()
                .uri("/api/greet-from-java?fullName=John%20Doe")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(s -> assertThat(s).startsWith("Error: Timeout or failed to get greeting: "));
    }

    @Test
    void testGreetFromJavaTimeout() {
        CompletableFuture<String> future = new CompletableFuture<>();
        when(pythonGreetingService.getGreetingFromPython("John Doe")).thenReturn(future);

        webTestClient.get()
                .uri("/api/greet-from-java?fullName=John%20Doe")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(s -> assertThat(s).startsWith("Error: Timeout or failed to get greeting: "));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public WebTestClient webTestClient(ApplicationContext context) {
            return WebTestClient.bindToApplicationContext(context)
                    .configureClient()
                    .baseUrl("https://localhost:8443")
                    .build();
        }
    }
    
}   