package org.springframework.samples.petclinic.api.application;

import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomersServiceClientIntegrationTest {

    private MockWebServer server;
    private CustomersServiceClient customersServiceClient;

    @BeforeEach
    void setUp() {
        server = new MockWebServer();
        customersServiceClient = new CustomersServiceClient(WebClient.builder(), server.url("/").toString());
    }

    @AfterEach
    void shutdown() throws IOException {
        server.close();
    }

    @Test
    void getOwnerUsesConfiguredCustomersServiceUrl() throws InterruptedException {
        server.enqueue(new MockResponse.Builder()
            .addHeader("Content-Type", "application/json")
            .body("{\"id\":7,\"firstName\":\"George\",\"lastName\":\"Franklin\",\"pets\":[]}")
            .build());

        var owner = customersServiceClient.getOwner(7).block();

        assertEquals(7, owner.id());
        assertEquals("/owners/7", server.takeRequest().getPath());
    }
}
