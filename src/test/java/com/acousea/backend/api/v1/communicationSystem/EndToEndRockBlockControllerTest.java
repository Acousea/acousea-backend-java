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
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModeGraph.OperationModesGraphModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
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
import java.util.*;

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
        // Create a routing table with random routes
        NetworkModule.RoutingTable routingTable = new NetworkModule.RoutingTable();
        routingTable.addRoute(Address.fromValue(2), Address.fromValue(3));
        routingTable.addRoute(Address.fromValue(4), Address.fromValue(5));
        routingTable.setDefaultGateway(Address.fromValue(1));

        // Create a NetworkModule with a random local address and the routing table
        NetworkModule initialNetworkModule = new NetworkModule(Address.fromValue(1), routingTable);

        NodeDeviceDTO nodeDeviceDTO = testWebHookEndpointWithModule(List.of(initialNetworkModule));

        // Retrieve and validate the NetworkModule DTO from the API response
        NodeDeviceDTO.ExtModuleDto.NetworkModuleDto networkModuleDto =
                (NodeDeviceDTO.ExtModuleDto.NetworkModuleDto) nodeDeviceDTO.getExtModules().get(NetworkModule.name);

        assertThat(networkModuleDto).isNotNull();
        assertThat(networkModuleDto.getLocalAddress()).isEqualTo(initialNetworkModule.getLocalAddress().getValue());

        // Validate routing table data
        assertThat(networkModuleDto.getRoutingTable()).isNotNull();
        assertThat(networkModuleDto.getRoutingTable().getDefaultGateway()).isEqualTo(routingTable.getDefaultGateway().getValue());

        // Ensure all routes are correctly mapped
        routingTable.getPeerRoutes().forEach((destination, nextHop) -> {
            assertThat(networkModuleDto.getRoutingTable().getPeerRoutes())
                    .containsEntry((int) destination.getValue(), (int) nextHop.getValue());
        });
    }

    @Test
    void testWebHookEndpointWithOperationModesModule() throws Exception {
        // Create a set of operation modes
        OperationMode mode1 = OperationMode.create((short) 1, "Normal Mode");
        OperationMode mode2 = OperationMode.create((short) 2, "Power Saving Mode");
        OperationMode mode3 = OperationMode.create((short) 3, "Emergency Mode");

        // Create a map for the operation modes
        Map<Short, OperationMode> operationModesMap = new TreeMap<>();
        operationModesMap.put(mode1.getId(), mode1);
        operationModesMap.put(mode2.getId(), mode2);
        operationModesMap.put(mode3.getId(), mode3);

        // Select an active operation mode
        Short activeModeId = mode2.getId();

        // Create the OperationModesModule
        OperationModesModule initialOperationModesModule = new OperationModesModule(operationModesMap, activeModeId);

        NodeDeviceDTO nodeDeviceDTO = testWebHookEndpointWithModule(List.of(initialOperationModesModule));

        // Retrieve and validate the OperationModesModule DTO from the API response
        NodeDeviceDTO.ExtModuleDto.OperationModeModuleDto operationModesModuleDto =
                (NodeDeviceDTO.ExtModuleDto.OperationModeModuleDto) nodeDeviceDTO.getExtModules().get(OperationModesModule.name);

        assertThat(operationModesModuleDto).isNotNull();
        assertThat(operationModesModuleDto.getActiveOperationModeIdx()).isEqualTo(activeModeId);

        // Ensure all operation modes are correctly mapped
        // FIXME: Cannot compare the operation Mode names, since they're not serialized and are lost in the process.
        operationModesMap.forEach((id, mode) -> {
            assertThat(operationModesModuleDto.getModes())
                    .containsKey(id);
        });
    }


    @Test
    void testWebHookEndpointWithOperationModesGraphModule() throws Exception {
        // Create a transition graph with various operation mode transitions
        Map<Integer, OperationModesGraphModule.Transition> transitionGraph = new HashMap<>();
        transitionGraph.put(1, new OperationModesGraphModule.Transition(2, 5000)); // Mode 1 -> Mode 2 (5 sec)
        transitionGraph.put(2, new OperationModesGraphModule.Transition(3, 8000)); // Mode 2 -> Mode 3 (8 sec)
        transitionGraph.put(3, new OperationModesGraphModule.Transition(1, 3000)); // Mode 3 -> Mode 1 (3 sec)

        // Create the OperationModesGraphModule instance
        OperationModesGraphModule initialOperationModesGraphModule = new OperationModesGraphModule(transitionGraph);

        NodeDeviceDTO nodeDeviceDTO = testWebHookEndpointWithModule(List.of(initialOperationModesGraphModule));

        // Retrieve and validate the OperationModesGraphModule DTO from the API response
        NodeDeviceDTO.ExtModuleDto.OperationModesGraphModuleDto operationModesGraphModuleDto =
                (NodeDeviceDTO.ExtModuleDto.OperationModesGraphModuleDto) nodeDeviceDTO.getExtModules().get(OperationModesGraphModule.name);

        assertThat(operationModesGraphModuleDto).isNotNull();
        assertThat(operationModesGraphModuleDto.getGraph()).isNotNull();

        // Ensure all transitions are correctly mapped
        transitionGraph.forEach((currentMode, transition) -> {
            assertThat(operationModesGraphModuleDto.getGraph())
                    .containsKey(currentMode);

            NodeDeviceDTO.ExtModuleDto.OperationModesGraphModuleDto.TransitionDto transitionDto =
                    operationModesGraphModuleDto.getGraph().get(currentMode);

            assertThat(transitionDto.getTargetMode()).isEqualTo(transition.getTargetMode());
            assertThat(transitionDto.getDuration()).isEqualTo(transition.getDuration());
        });
    }


    @Test
    void testWebHookEndpointWithLoRaReportingModule() throws Exception {
        // Create a set of operation modes
        OperationMode mode1 = OperationMode.create((short) 1, "Mode A");
        OperationMode mode2 = OperationMode.create((short) 2, "Mode B");

        // Create an OperationModesModule
        Map<OperationMode, Short> reportingPeriods = new HashMap<>(
                Map.of(mode1, (short) 1000, mode2, (short) 5000)
        );

        // Create a LoRaReportingModule with default reporting periods
        LoRaReportingModule initialLoRaReportingModule = new LoRaReportingModule(reportingPeriods);

        // Set reporting periods for specific operation modes
        initialLoRaReportingModule.setReportingPeriod(mode1, 1000);
        initialLoRaReportingModule.setReportingPeriod(mode2, 5000);

        NodeDeviceDTO nodeDeviceDTO = testWebHookEndpointWithModule(List.of(initialLoRaReportingModule));

        // Retrieve and validate the LoRaReportingModule DTO from the API response
        NodeDeviceDTO.ExtModuleDto.LoRaReportingModuleDto loRaReportingModuleDto =
                (NodeDeviceDTO.ExtModuleDto.LoRaReportingModuleDto) nodeDeviceDTO.getExtModules().get(LoRaReportingModule.name);

        assertThat(loRaReportingModuleDto).isNotNull();
        assertThat(loRaReportingModuleDto.getTechnologyId()).isEqualTo(LoRaReportingModule.TECHNOLOGY_ID);

        // Ensure reporting periods are correctly mapped
        assertThat(loRaReportingModuleDto.getReportingPeriodsPerOperationModeIdx()).containsEntry(mode1.getId(), (short) 1000);
        assertThat(loRaReportingModuleDto.getReportingPeriodsPerOperationModeIdx()).containsEntry(mode2.getId(), (short) 5000);
    }

    @Test
    void testWebHookEndpointWithIridiumReportingModule() throws Exception {
        // Create a set of operation modes
        OperationMode mode1 = OperationMode.create((short) 1, "Mode A");
        OperationMode mode2 = OperationMode.create((short) 2, "Mode B");

        // Create an OperationModesModule
        Map<OperationMode, Short> reportingPeriods = new HashMap<>(
                Map.of(mode1, (short) 2000, mode2, (short) 7000)
        );

        // Define an IMEI number
        String imei = "300234063897210";

        // Create an IridiumReportingModule with default reporting periods
        IridiumReportingModule initialIridiumReportingModule = new IridiumReportingModule(reportingPeriods, imei);

        // Set reporting periods for specific operation modes
        initialIridiumReportingModule.setReportingPeriod(mode1, 2000);
        initialIridiumReportingModule.setReportingPeriod(mode2, 7000);

        NodeDeviceDTO nodeDeviceDTO = testWebHookEndpointWithModule(List.of(initialIridiumReportingModule));

        // Retrieve and validate the IridiumReportingModule DTO from the API response
        NodeDeviceDTO.ExtModuleDto.IridiumReportingModuleDto iridiumReportingModuleDto =
                (NodeDeviceDTO.ExtModuleDto.IridiumReportingModuleDto) nodeDeviceDTO.getExtModules().get(IridiumReportingModule.name);

        assertThat(iridiumReportingModuleDto).isNotNull();
        assertThat(iridiumReportingModuleDto.getTechnologyId()).isEqualTo(IridiumReportingModule.TECHNOLOGY_ID);
        // FIXME: Cannot compare the IMEI, since it's not serialized and is lost in the process.
//        assertThat(iridiumReportingModuleDto.getImei()).isEqualTo(imei);

        // Ensure reporting periods are correctly mapped
        assertThat(iridiumReportingModuleDto.getReportingPeriodsPerOperationModeIdx()).containsEntry(mode1.getId(), (short) 2000);
        assertThat(iridiumReportingModuleDto.getReportingPeriodsPerOperationModeIdx()).containsEntry(mode2.getId(), (short) 7000);
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


