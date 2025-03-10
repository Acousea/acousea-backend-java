package com.acousea.backend.core.communicationSystem.domain.nodes.serialization;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.ReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.ICListenHF;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenLoggingConfig;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenRecordingStats;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStreamingConfig;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializableModuleFactory {

    @FunctionalInterface
    private interface TagCreatorFromBytes {
        SerializableModule create(ByteBuffer buffer);
    }


    private static final Map<ModuleCode, TagCreatorFromBytes> tagCreatorsFromBytes = new HashMap<>() {{
        put(ModuleCode.BATTERY, BatteryModule::fromBytes);
        put(ModuleCode.LOCATION, LocationModule::fromBytes);
        put(ModuleCode.NETWORK, NetworkModule::fromBytes);
        put(ModuleCode.OPERATION_MODES, OperationModesModule::fromBytes);
        put(ModuleCode.REPORTING, ReportingModule::fromBytes);
        put(ModuleCode.RTC, RTCModule::fromBytes);
        put(ModuleCode.STORAGE, StorageModule::fromBytes);
        put(ModuleCode.AMBIENT, AmbientModule::fromBytes);
        put(ModuleCode.ICLISTEN_COMPLETE, ICListenHF::fromBytes);
        put(ModuleCode.ICLISTEN_STATUS, ICListenStatus::fromBytes);
        put(ModuleCode.ICLISTEN_LOGGING_CONFIG,  ICListenLoggingConfig::fromBytes);
        put(ModuleCode.ICLISTEN_STREAMING_CONFIG, ICListenStreamingConfig::fromBytes);
        put(ModuleCode.ICLISTEN_RECORDING_STATS, ICListenRecordingStats::fromBytes);

    }};

    @SneakyThrows
    public static SerializableModule createModule(ByteBuffer buffer) {
        buffer.limit(buffer.capacity());

        byte typeCode = buffer.get();
        ModuleCode moduleCode = ModuleCode.fromValue(typeCode);

        int tagLength = buffer.get();
        if (buffer.remaining() < tagLength) {
            throw new IllegalArgumentException("Invalid byte array for " + moduleCode);
        }

        TagCreatorFromBytes tagCreatorFromBytes = tagCreatorsFromBytes.get(moduleCode);
        if (tagCreatorFromBytes == null) {
            throw new IllegalArgumentException("Tag type not supported: " + moduleCode);
        }

        buffer.limit(buffer.position() + tagLength);
        SerializableModule serializableModule = tagCreatorFromBytes.create(buffer);
        buffer.limit(buffer.capacity());
        return serializableModule;
    }

    public static List<SerializableModule> createModules(ByteBuffer buffer) {
        buffer.limit(buffer.capacity());
        List<SerializableModule> serializableModules = new ArrayList<>();
        while (buffer.hasRemaining()) {
            serializableModules.add(createModule(buffer));
        }
        return serializableModules;
    }


}
