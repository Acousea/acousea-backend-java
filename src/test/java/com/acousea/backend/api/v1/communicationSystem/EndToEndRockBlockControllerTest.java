package com.acousea.backend.api.v1.communicationSystem;

import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationPacket;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RoutingChunk;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.BasicStatusReportPayload;
import com.acousea.backend.core.communicationSystem.domain.mother.CommunicationPacketMother;
import com.acousea.backend.core.communicationSystem.domain.mother.RockBlockMessageMother;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({com.acousea.backend.app.config.TestConfig.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class EndToEndRockBlockControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper; // For JSON serialization

    @BeforeEach
    void setUp() {
        // Disable webTestClient timeouts
        webTestClient = webTestClient.mutate()
                .responseTimeout(Duration.ofDays(10)) // Effectively disables timeout
                .build();
    }

    @Test
    void testWebHookEndpointInvalidData() throws Exception {
        // Given: A valid RockBlockMessage
        RockBlockMessage message = RockBlockMessageMother.withInvalidData();

        // When: Sending a POST request to /webhook with query parameters
        EntityExchangeResult<ApiResult<String>> response = webTestClient.post()
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
                .expectBody(new ParameterizedTypeReference<ApiResult<String>>() {
                }) // Deserialize JSON correctly
                .returnResult();

        ApiResult<String> apiResult = response.getResponseBody();
        assert apiResult != null;

        // Debug: Print API Response
        System.out.println("API Response: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiResult));

        // Then: Validate response properties
        assertThat(apiResult.success()).isTrue();
        assertThat(apiResult.value()).isNotEmpty().contains("RockBlockMessage");

        // Optional: Check that error field is null
        assertThat(apiResult.error()).isNull();
    }

    @Test
    void testWebHookEndpointWithBasicStatusReport() throws Exception {
        // Given: A valid RockBlockMessage
        BasicStatusReportPayload payload = new BasicStatusReportPayload(
                List.of(
                        new LocationModule(20.5f, 30.0f)
                )
        );
        CommunicationPacket packet = CommunicationPacketMother.createBasicStatusReport(
                new RoutingChunk(Address.fromValue(1), Address.getBroadcastAddress()),
                payload
        );
        RockBlockMessage message = RockBlockMessageMother.fromPacket(packet);

        // When: Sending a POST request to /webhook with query parameters
        EntityExchangeResult<ApiResult<String>> response = webTestClient.post()
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
                .expectBody(new ParameterizedTypeReference<ApiResult<String>>() {
                }) // Deserialize JSON correctly
                .returnResult();

        ApiResult<String> apiResult = response.getResponseBody();
        assert apiResult != null;

        // Debug: Print API Response
        System.out.println("API Response: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiResult));

        // Then: Validate response properties
        assertThat(apiResult.success()).isTrue();
        assertThat(apiResult.value()).isNotEmpty().contains("RockBlockMessage processed successfully");

        // Optional: Check that error field is null
        assertThat(apiResult.error()).isNull();
    }
}


