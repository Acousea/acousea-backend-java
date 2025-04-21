package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten;


import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ICListenRecordingStats extends SerializableModule {
    private UUID id;
    private LocalDateTime epochTime;
    private int numberOfClicks;
    private int recordedMinutes;
    private int numberOfFiles;

    public ICListenRecordingStats(UUID id, LocalDateTime epochTime, int numberOfClicks, int recordedMinutes, int numberOfFiles) {
        super(ModuleCode.ICLISTEN_RECORDING_STATS);
        this.id = id;
        this.epochTime = epochTime;
        this.numberOfClicks = numberOfClicks;
        this.recordedMinutes = recordedMinutes;
        this.numberOfFiles = numberOfFiles;
    }

    @Override
    public byte[] getVALUE() {
        ByteBuffer buffer = ByteBuffer.allocate(8 + 3);
        buffer.putLong(epochTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
        buffer.put((byte) numberOfClicks);
        buffer.put((byte) recordedMinutes);
        buffer.put((byte) numberOfFiles);
        buffer.flip();
        return buffer.array();
    }


    public static ICListenRecordingStats fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < 8 + 3) {
            throw new IllegalArgumentException("Invalid data size for ICListenRecordingStats");
        }

        UUID id = UUID.randomUUID();
        long epochTimeMillis = buffer.getLong();
        LocalDateTime epochTime = LocalDateTime.ofEpochSecond(epochTimeMillis / 1000, 0, java.time.ZoneOffset.UTC);
        int numberOfClicks = buffer.get() & 0xFF;
        int recordedMinutes = buffer.get() & 0xFF;
        int numberOfFiles = buffer.get() & 0xFF;

        return new ICListenRecordingStats(id, epochTime, numberOfClicks, recordedMinutes, numberOfFiles);
    }

    public static ICListenRecordingStats createDefault() {
        return new ICListenRecordingStats(UUID.randomUUID(), LocalDateTime.now(), 0, 0, 0);
    }
}