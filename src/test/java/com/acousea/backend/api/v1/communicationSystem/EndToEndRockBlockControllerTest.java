package com.acousea.backend.api.v1.communicationSystem;

import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.domain.mother.RockBlockMessageMother;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({com.acousea.backend.app.config.TestConfig.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class EndToEndRockBlockControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper; // For JSON serialization

    @Test
    void testWebhookEndpoint() throws Exception {
        // Given: A valid RockBlockMessage
        RockBlockMessage message = RockBlockMessageMother.random();

        // When: Sending a POST request to /webhook with query parameters
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/rockblock/webhook")
                        .queryParam("imei", message.getImei())
                        .queryParam("serial", message.getSerial())
                        .queryParam("momsn", message.getMomsn())
                        .queryParam("transmit_time", message.getTransmitTime())
                        .queryParam("iridium_latitude", message.getIridiumLatitude())
                        .queryParam("iridium_longitude", message.getIridiumLongitude())
                        .queryParam("iridium_cep", message.getIridiumCep())
                        .queryParam("data", message.getData())
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.value").exists();
    }
}


