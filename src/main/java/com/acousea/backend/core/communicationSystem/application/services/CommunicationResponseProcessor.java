package com.acousea.backend.core.communicationSystem.application.services;

import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.BasicStatusReportPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.CompleteStatusReportPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.communication.serialization.SerializableModule;
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

    // Mapeo de TagType a acciones específicas
    private final Map<ModuleCode, TagProcessor> tagProcessors = Map.of(
            ModuleCode.BATTERY, (nodeDevice, serializableModule) ->
                    nodeDevice.getExtModules().put(BatteryModule.name, (BatteryModule) serializableModule),
            ModuleCode.LOCATION, (nodeDevice, serializableModule) ->
                    nodeDevice.getExtModules().put(LocationModule.name, (LocationModule) serializableModule),
            ModuleCode.AMBIENT, (nodeDevice, serializableModule) ->
                    nodeDevice.getExtModules().put(AmbientModule.name, (AmbientModule) serializableModule),
            ModuleCode.NETWORK, (nodeDevice, serializableModule) ->
                    nodeDevice.getExtModules().put(NetworkModule.name, (NetworkModule) serializableModule),
            ModuleCode.STORAGE, (nodeDevice, serializableModule) ->
                    nodeDevice.getExtModules().put(StorageModule.name, (StorageModule) serializableModule),
            ModuleCode.OPERATION_MODES, (nodeDevice, serializableModule) ->
                    nodeDevice.getExtModules().put(OperationModesModule.name, (OperationModesModule) serializableModule),
            ModuleCode.RTC, (nodeDevice, serializableModule) ->
                    nodeDevice.getExtModules().put(RTCModule.name, ((RTCModule) serializableModule)),
            ModuleCode.REPORTING, (nodeDevice, tag) -> {
                ReportingModule reportingModule = ((ReportingModule) tag);
                switch (reportingModule.getTechnologyId()) {
                    case IridiumReportingModule.TECHNOLOGY_ID ->
                            nodeDevice.getExtModules().put(IridiumReportingModule.name, reportingModule);
                    case LoRaReportingModule.TECHNOLOGY_ID ->
                            nodeDevice.getExtModules().put(LoRaReportingModule.name, reportingModule);
                    default -> throw new IllegalArgumentException(
                            ReportingModule.class.getName() + " -> Unknown technology id: " + reportingModule.getTechnologyId());
                }
            }
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
