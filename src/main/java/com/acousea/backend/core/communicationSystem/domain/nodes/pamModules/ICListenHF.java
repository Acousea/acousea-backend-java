package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules;

import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.communication.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenLoggingConfig;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenRecordingStats;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStreamingConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;

@Getter
@Setter
public class ICListenHF extends SerializableModule implements PamModule {
    String serialNumber, name;
    private ICListenStatus status;
    private ICListenLoggingConfig loggingConfig;
    private ICListenStreamingConfig streamingConfig;
    private ICListenRecordingStats recordingStats;

    public ICListenHF(String serialNumber) {
        super(ModuleCode.ICLISTEN_COMPLETE, serializeValues(serialNumber, ICListenStatus.createDefault(), ICListenLoggingConfig.createDefault(), ICListenStreamingConfig.createDefault(), ICListenRecordingStats.createDefault()));
        this.serialNumber = serialNumber;
        this.name = "ICListenHF";
        this.status = ICListenStatus.createDefault();
        this.loggingConfig = ICListenLoggingConfig.createDefault();
        this.streamingConfig = ICListenStreamingConfig.createDefault();
        this.recordingStats = ICListenRecordingStats.createDefault();
    }

    public ICListenHF(ICListenStatus status, ICListenLoggingConfig loggingConfig, ICListenStreamingConfig streamingConfig, ICListenRecordingStats recordingStats) {
        super(ModuleCode.ICLISTEN_COMPLETE, serializeValues("RB9-ETH", ICListenStatus.createDefault(), ICListenLoggingConfig.createDefault(), ICListenStreamingConfig.createDefault(), ICListenRecordingStats.createDefault()));
        this.status = status;
        this.loggingConfig = loggingConfig;
        this.streamingConfig = streamingConfig;
        this.recordingStats = recordingStats;
    }

    public static byte[] serializeValues(String serialNumber, ICListenStatus status, ICListenLoggingConfig loggingConfig, ICListenStreamingConfig streamingConfig, ICListenRecordingStats recordingStats) {
        ByteBuffer buffer = ByteBuffer.allocate(1024); // Estimate size, adjust as needed
        byte[] serialNumberBytes = serialNumber.getBytes();
        buffer.putInt(serialNumberBytes.length);
        buffer.put(serialNumberBytes);
        buffer.put(status.toBytes());
        buffer.put(loggingConfig.toBytes());
        buffer.put(streamingConfig.toBytes());
        buffer.put(recordingStats.toBytes());
        buffer.flip();
        byte[] serializedData = new byte[buffer.remaining()];
        buffer.get(serializedData);
        return serializedData;
    }

    @SneakyThrows
    public static ICListenHF fromBytes(ByteBuffer buffer) {
        ModuleCode moduleCode = ModuleCode.fromValue(buffer.get());
        int tagLength = buffer.get() & 0xFF;
        if (moduleCode != ModuleCode.ICLISTEN_STATUS) {
            throw new IllegalArgumentException("Invalid data for ICListenHF: ICListenStatus, found tag type: " + moduleCode);
        }
        ICListenStatus status = ICListenStatus.fromBytes(buffer);

        moduleCode = moduleCode = ModuleCode.fromValue(buffer.get());
        tagLength = buffer.get() & 0xFF;
        if (moduleCode != ModuleCode.ICLISTEN_LOGGING_CONFIG) {
            throw new IllegalArgumentException("Invalid data for ICListenHF: ICListenLoggingConfig, found tag type: " + moduleCode);
        }
        ICListenLoggingConfig loggingConfig = ICListenLoggingConfig.fromBytes(buffer);

        moduleCode = ModuleCode.fromValue(buffer.get());
        tagLength = buffer.get() & 0xFF;
        if (moduleCode != ModuleCode.ICLISTEN_STREAMING_CONFIG) {
            throw new IllegalArgumentException("Invalid data for ICListenHF: ICListenStreamingConfig, found tag type: " + moduleCode);
        }
        ICListenStreamingConfig streamingConfig = ICListenStreamingConfig.fromBytes(buffer);

        moduleCode = ModuleCode.fromValue(buffer.get());
        tagLength = buffer.get() & 0xFF;
        if (moduleCode != ModuleCode.ICLISTEN_RECORDING_STATS) {
            throw new IllegalArgumentException("Invalid data for ICListenHF: ICListenRecordingStats, found tag type: " + moduleCode);
        }
        ICListenRecordingStats recordingStats = ICListenRecordingStats.fromBytes(buffer);
        return new ICListenHF(status, loggingConfig, streamingConfig, recordingStats);
    }
}
