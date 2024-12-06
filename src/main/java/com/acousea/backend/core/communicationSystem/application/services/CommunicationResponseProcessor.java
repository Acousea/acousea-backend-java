package com.acousea.backend.core.communicationSystem.application.services;

import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RoutingChunk;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.GetUpdatedNodeConfigurationPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.NewNodeConfigurationPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.SummaryReportPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagType;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation.*;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.ReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommunicationResponseProcessor {
    private final NodeDeviceRepository nodeDeviceRepository;

    // Interfaz funcional para el procesamiento de tags
    @FunctionalInterface
    private interface TagProcessor {
        void process(NodeDevice nodeDevice, Object tag) throws InvalidPacketException;
    }
    // Mapeo de TagType a acciones específicas
    private final Map<TagType, TagProcessor> tagProcessors = Map.of(
            TagType.BATTERY, (nodeDevice, tag) ->
                    nodeDevice.getExtModules().put(BatteryModule.name, ((BatteryTag) tag).toBatteryModule()),
            TagType.LOCATION, (nodeDevice, tag) ->
                    nodeDevice.getExtModules().put(LocationModule.name, ((LocationTag) tag).toLocationModule()),
            TagType.AMBIENT, (nodeDevice, tag) ->
                    nodeDevice.getExtModules().put(AmbientModule.name, ((AmbientTag) tag).toAmbientModule()),
            TagType.NETWORK, (nodeDevice, tag) ->
                    nodeDevice.getExtModules().put(NetworkModule.name, ((NetworkTag) tag).toNetworkModule()),
            TagType.STORAGE, (nodeDevice, tag) ->
                    nodeDevice.getExtModules().put(StorageModule.name, ((StorageTag) tag).toStorageModule()),
            TagType.OPERATION_MODES, (nodeDevice, tag) ->
                    nodeDevice.getExtModules().put(OperationModeModule.name, ((OperationModeTag) tag).toOperationModeModule()),
            TagType.RTC, (nodeDevice, tag) ->
                    nodeDevice.getExtModules().put(RTCModule.name, ((RTCTag) tag).toRTCModule()),
            TagType.REPORTING, (nodeDevice, tag) -> {
                ReportingModule reportingModule = ((ReportingPeriodTag) tag).toReportingModule();
                switch (reportingModule.getTechnologyId()) {
                    case IridiumReportingModule.TECHNOLOGY_ID ->
                            nodeDevice.getExtModules().put(IridiumReportingModule.name, reportingModule);
                    case LoRaReportingModule.TECHNOLOGY_ID ->
                            nodeDevice.getExtModules().put(LoRaReportingModule.name, reportingModule);
                    default -> throw new IllegalArgumentException(
                            ReportingPeriodTag.class.getName() + " -> Unknown technology id: " + reportingModule.getTechnologyId());
                }
            }
    );

    public CommunicationResponseProcessor(NodeDeviceRepository nodeDeviceRepository) {
        this.nodeDeviceRepository = nodeDeviceRepository;
    }

    private NodeDevice getNodeDevice(Address address) {
        return nodeDeviceRepository.findByNetworkAddress(address).orElseThrow(
                () -> new RuntimeException(this.getClass().getSimpleName() + " NodeDevice not found for address: " + address)
        );
    }

    public void processNodeDeviceConfigurationResponse(RoutingChunk routingChunk, NewNodeConfigurationPayload payload) {
        NodeDevice nodeDevice = getNodeDevice(routingChunk.receiver());

        payload.getTags().forEach(tag -> {
                try {
                    TagType tagType = TagType.fromValue(tag.getTYPE());
                    TagProcessor processor = tagProcessors.get(tagType);
                    if (processor == null) {
                        throw new IllegalArgumentException("Unknown tag type: " + tagType);
                    }
                    processor.process(nodeDevice, tag);
                } catch (InvalidPacketException e) {
                    throw new RuntimeException("Error processing tag of type: " + tag.getTYPE(), e);
                }

        });

        System.out.println("Processing SetNodeDeviceConfigurationPayload");
    }

    public void processSummaryReportResponse(RoutingChunk routingChunk, SummaryReportPayload payload) {

    }


}
