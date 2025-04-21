package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules;

import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenLoggingConfig;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenRecordingStats;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStreamingConfig;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;

@Getter
@Setter
public class ICListenHF extends SerializableModule implements PamModule {
    public static final String serialNumber = "RB9-ETH";
    public static final String name = "ICListenHF";

    @Nullable
    private ICListenStatus status;
    @Nullable
    private ICListenLoggingConfig loggingConfig;
    @Nullable
    private ICListenStreamingConfig streamingConfig;
    @Nullable
    private ICListenRecordingStats recordingStats;

    @Override
    public String getName() {
        return name;
    }

    public ICListenHF() {
        super(ModuleCode.ICLISTEN_COMPLETE);
    }

    public ICListenHF(ICListenStatus status, ICListenLoggingConfig loggingConfig, ICListenStreamingConfig streamingConfig, ICListenRecordingStats recordingStats) {
        super(ModuleCode.ICLISTEN_COMPLETE);
        this.status = status;
        this.loggingConfig = loggingConfig;
        this.streamingConfig = streamingConfig;
        this.recordingStats = recordingStats;
    }

    @Override
    public byte[] getVALUE() {
        ByteBuffer buffer = ByteBuffer.allocate(1024); // Puedes ajustar este tamaño según tus necesidades

        if (status != null) {
            byte[] statusBytes = status.toBytes();
            buffer.put(statusBytes);
        }

        if (loggingConfig != null) {
            byte[] logBytes = loggingConfig.toBytes();
            buffer.put(logBytes);
        }

        if (streamingConfig != null) {
            byte[] streamBytes = streamingConfig.toBytes();
            buffer.put(streamBytes);
        }

        if (recordingStats != null) {
            byte[] statsBytes = recordingStats.toBytes();
            buffer.put(statsBytes);
        }

        buffer.flip();
        byte[] serializedData = new byte[buffer.remaining()];
        buffer.get(serializedData);
        return serializedData;
    }


    @SneakyThrows
    public static ICListenHF fromBytes(ByteBuffer buffer) {
        ICListenStatus status = null;
        ICListenLoggingConfig loggingConfig = null;
        ICListenStreamingConfig streamingConfig = null;
        ICListenRecordingStats recordingStats = null;

        while (buffer.hasRemaining()) {
            ModuleCode moduleCode = ModuleCode.fromValue(buffer.get());
            int length = buffer.get() & 0xFF;

            byte[] data = new byte[length];
            buffer.get(data);
            ByteBuffer moduleBuffer = ByteBuffer.wrap(data);

            switch (moduleCode) {
                case ICLISTEN_STATUS:
                    status = ICListenStatus.fromBytes(moduleBuffer);
                    break;
                case ICLISTEN_LOGGING_CONFIG:
                    loggingConfig = ICListenLoggingConfig.fromBytes(moduleBuffer);
                    break;
                case ICLISTEN_STREAMING_CONFIG:
                    streamingConfig = ICListenStreamingConfig.fromBytes(moduleBuffer);
                    break;
                case ICLISTEN_RECORDING_STATS:
                    recordingStats = ICListenRecordingStats.fromBytes(moduleBuffer);
                    break;
                default:
                    // Puedes registrar un warning o ignorar el módulo desconocido
                    break;
            }
        }

        return new ICListenHF(status, loggingConfig, streamingConfig, recordingStats);
    }
}
