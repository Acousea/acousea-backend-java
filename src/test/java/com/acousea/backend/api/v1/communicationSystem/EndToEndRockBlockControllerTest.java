package com.acousea.backend.api.v1.communicationSystem;

import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationPacket;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.BatteryStatus;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RoutingChunk;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.BasicStatusReportPayload;
import com.acousea.backend.core.communicationSystem.domain.mother.CommunicationPacketMother;
import com.acousea.backend.core.communicationSystem.domain.mother.RockBlockMessageMother;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

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

    private NodeDeviceDTO testWebHookEndpointWithModule(
            List<SerializableModule> modules
    ) throws Exception {
        // Given: A valid RockBlockMessage
        BasicStatusReportPayload payload = new BasicStatusReportPayload(modules);
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
        assertThat(apiResult.error()).isNull();

        // Now we need to get the nodeDevice info and check that the module data was updated.
        EntityExchangeResult<ApiResult<NodeDeviceDTO>> nodeDeviceResponse = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/communication-system/node-device")
                        .queryParam("networkAddress", "1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResult<NodeDeviceDTO>>() {
                }) // Deserialize JSON correctly
                .returnResult();

        ApiResult<NodeDeviceDTO> nodeDeviceApiResult = nodeDeviceResponse.getResponseBody();
        assert nodeDeviceApiResult != null;

        // Debug: Print API Response
        System.out.println("API Response: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodeDeviceApiResult));

        // Then: Validate response properties
        assertThat(nodeDeviceApiResult.success()).isTrue();
        assertThat(nodeDeviceApiResult.value()).isNotNull();

        // Extract and validate the module
        return nodeDeviceApiResult.value();
    }


    @Test
    void testWebHookEndpointWithAmbientModule() throws Exception {
        AmbientModule initialAmbientModule = new AmbientModule(
                new Random().nextInt(101) - 50,
                new Random().nextInt(100)
        );

        NodeDeviceDTO nodeDeviceDTO = testWebHookEndpointWithModule(List.of(initialAmbientModule));
        NodeDeviceDTO.ExtModuleDto.AmbientModuleDTO ambientModule = (NodeDeviceDTO.ExtModuleDto.AmbientModuleDTO) nodeDeviceDTO.getExtModules().get(AmbientModule.name);
        assertThat(ambientModule).isNotNull();
        assertThat(ambientModule.getTemperature()).isEqualTo(initialAmbientModule.getTemperature());
        assertThat(ambientModule.getHumidity()).isEqualTo(initialAmbientModule.getHumidity());

    }

    @Test
    void testWebHookEndpointWithBatteryModule() throws Exception {
        BatteryStatus initialBatteryStatus = BatteryStatus.values()[new Random().nextInt(BatteryStatus.values().length)];
        BatteryModule initialBatteryModule = new BatteryModule(new Random().nextInt(100), initialBatteryStatus);


        NodeDeviceDTO nodeDeviceDTO = testWebHookEndpointWithModule(List.of(initialBatteryModule));
        NodeDeviceDTO.ExtModuleDto.BatteryModuleDto batteryModule = (NodeDeviceDTO.ExtModuleDto.BatteryModuleDto) nodeDeviceDTO.getExtModules().get(BatteryModule.name);
        assertThat(batteryModule).isNotNull();
        assertThat(batteryModule.getBatteryStatus()).isEqualTo(initialBatteryModule.getBatteryStatus().getValue());
        assertThat(batteryModule.getBatteryPercentage()).isEqualTo(initialBatteryModule.getBatteryPercentage());
    }

    @Test
    void testWebHookEndpointWithLocationModule() throws Exception {
        LocationModule initialLocationModule = new LocationModule(
                new Random().nextFloat() * 180 - 90,
                new Random().nextFloat() * 360 - 180
        );

        NodeDeviceDTO nodeDeviceDTO = testWebHookEndpointWithModule(
                List.of(initialLocationModule)
        );
        NodeDeviceDTO.ExtModuleDto.LocationModuleDto locationModule = (NodeDeviceDTO.ExtModuleDto.LocationModuleDto) nodeDeviceDTO.getExtModules().get(LocationModule.name);
        assertThat(locationModule).isNotNull();
        assertThat(locationModule.getLongitude()).isEqualTo(initialLocationModule.getLongitude());
        assertThat(locationModule.getLatitude()).isEqualTo(initialLocationModule.getLatitude());
    }

    @Test
    void testWebHookEndpointWithNetworkModule() throws Exception {

    }

    @Test
    void testWebHookEndpointWithOperationModeGraph() throws Exception {

    }

    @Test
    void testWebHookEndpointWithReportingPeriods() throws Exception {

    }

    @Test
    void testWebHookEndpointWithRTCModule() throws Exception {
        RTCModule initialRTCModule = new RTCModule(LocalDateTime.ofEpochSecond(new Random().nextInt((int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), 0, ZoneOffset.UTC));

        NodeDeviceDTO nodeDeviceDTO = testWebHookEndpointWithModule(
                List.of(initialRTCModule)
        );

        NodeDeviceDTO.ExtModuleDto.RtcModuleDto rtcModule = (NodeDeviceDTO.ExtModuleDto.RtcModuleDto) nodeDeviceDTO.getExtModules().get(RTCModule.name);
        assertThat(rtcModule).isNotNull();
        assertThat(LocalDateTime.parse(rtcModule.getCurrentTime())).isEqualTo(initialRTCModule.getCurrentTime());

    }

    @Test
    void testWebHookEndpointWithStorageModule() throws Exception {
        StorageModule initialStorageModule = new StorageModule(new Random().nextInt(100), new Random().nextInt(100));

        NodeDeviceDTO nodeDeviceDTO = testWebHookEndpointWithModule(
                List.of(initialStorageModule)
        );

        NodeDeviceDTO.ExtModuleDto.StorageModuleDto storageModule = (NodeDeviceDTO.ExtModuleDto.StorageModuleDto) nodeDeviceDTO.getExtModules().get(StorageModule.name);
        assertThat(storageModule).isNotNull();
        assertThat(storageModule.getStorageUsedMegabytes()).isEqualTo(initialStorageModule.getStorageUsedMegabytes());
        assertThat(storageModule.getStorageTotalMegabytes()).isEqualTo(initialStorageModule.getStorageTotalMegabytes());
    }


}


