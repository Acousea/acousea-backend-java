package com.acousea.backend.core.communicationSystem.application.services;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModeGraph.OperationModesGraphModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.ReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommunicationResponseProcessor {

    // Interfaz funcional para el procesamiento de tags
    @FunctionalInterface
    private interface TagProcessor {
        void process(NodeDevice nodeDevice, SerializableModule tag) throws InvalidPacketException;
    }

    private final Map<ModuleCode, TagProcessor> tagProcessors = Map.ofEntries(
            Map.entry(ModuleCode.BATTERY,
                    (nodeDevice, serializableModule) -> nodeDevice.getExtModules().put(BatteryModule.name, (BatteryModule) serializableModule)
            ),
            Map.entry(ModuleCode.LOCATION,
                    (nodeDevice, serializableModule) -> nodeDevice.getExtModules().put(LocationModule.name, (LocationModule) serializableModule)
            ),
            Map.entry(ModuleCode.AMBIENT,
                    (nodeDevice, serializableModule) -> nodeDevice.getExtModules().put(AmbientModule.name, (AmbientModule) serializableModule)
            ),
            Map.entry(ModuleCode.NETWORK,
                    (nodeDevice, serializableModule) -> nodeDevice.getExtModules().put(NetworkModule.name, (NetworkModule) serializableModule)
            ),
            Map.entry(ModuleCode.STORAGE, (nodeDevice, serializableModule) ->
                    nodeDevice.getExtModules().put(StorageModule.name, (StorageModule) serializableModule)
            ),
            Map.entry(ModuleCode.OPERATION_MODES,
                    (nodeDevice, serializableModule) -> nodeDevice.getExtModules().put(OperationModesModule.name, (OperationModesModule) serializableModule)
            ),
            Map.entry(ModuleCode.OPERATION_MODES_GRAPH,
                    (nodeDevice, serializableModule) -> nodeDevice.getExtModules().put(OperationModesGraphModule.name, (OperationModesGraphModule) serializableModule)
            ),
            Map.entry(ModuleCode.RTC,
                    (nodeDevice, serializableModule) -> nodeDevice.getExtModules().put(RTCModule.name, ((RTCModule) serializableModule))
            ),
            Map.entry(ModuleCode.ICLISTEN_COMPLETE,
                    (nodeDevice, serializableModule) -> {
                        System.out.println("ICLISTEN_COMPLETE: TODO implement");
                    }),
            Map.entry(ModuleCode.ICLISTEN_STATUS, (nodeDevice, serializableModule) -> {
                System.out.println("ICLISTEN_STATUS: TODO implement");
            }),
            Map.entry(ModuleCode.ICLISTEN_LOGGING_CONFIG, (nodeDevice, serializableModule) -> {
                System.out.println("ICLISTEN_LOGGING_CONFIG: TODO implement");
            }),
            Map.entry(ModuleCode.ICLISTEN_STREAMING_CONFIG, (nodeDevice, serializableModule) -> {
                System.out.println("ICLISTEN_STREAMING_CONFIG: TODO implement");
            }),
            Map.entry(ModuleCode.ICLISTEN_RECORDING_STATS, (nodeDevice, serializableModule) -> {
                System.out.println("ICLISTEN_RECORDING_STATS: TODO implement");
            }),
            Map.entry(ModuleCode.REPORTING, (nodeDevice, serializableModule) -> {
                ReportingModule reportingModule = ((ReportingModule) serializableModule);
                switch (reportingModule.getTechnologyId()) {
                    case IridiumReportingModule.TECHNOLOGY_ID ->
                            nodeDevice.getExtModules().put(IridiumReportingModule.name, reportingModule);
                    case LoRaReportingModule.TECHNOLOGY_ID ->
                            nodeDevice.getExtModules().put(LoRaReportingModule.name, reportingModule);
                    default -> throw new IllegalArgumentException(
                            ReportingModule.class.getName() + " -> Unknown technology id: " + reportingModule.getTechnologyId());
                }
            })
    );


    public void processSerializableModulesForNode(NodeDevice nodeDevice, List<SerializableModule> modules) {
        modules.forEach(tag -> {
            try {
                ModuleCode moduleCode = ModuleCode.fromValue(tag.getTYPE());
                TagProcessor processor = tagProcessors.get(moduleCode);
                if (processor == null) {
                    throw new IllegalArgumentException("Unknown tag type: " + moduleCode);
                }
                processor.process(nodeDevice, tag);
            } catch (InvalidPacketException e) {
                throw new RuntimeException("Error processing tag of type: " + tag.getTYPE(), e);
            }

        });
    }


}
