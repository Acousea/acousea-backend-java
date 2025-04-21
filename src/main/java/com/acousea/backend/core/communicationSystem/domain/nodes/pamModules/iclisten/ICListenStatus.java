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
public class ICListenStatus extends SerializableModule {
    private UUID id;
    private int unitStatus;
    private int batteryStatus;
    private float batteryPercentage;
    private float temperature;
    private float humidity;
    private LocalDateTime timestamp;

    public ICListenStatus(UUID id, int unitStatus, int batteryStatus, float batteryPercentage, float temperature, float humidity, LocalDateTime timestamp) {
        super(ModuleCode.ICLISTEN_STATUS, serializeValues(unitStatus, batteryStatus, batteryPercentage, temperature, humidity, timestamp));
        this.id = id;
        this.unitStatus = unitStatus;
        this.batteryStatus = batteryStatus;
        this.batteryPercentage = batteryPercentage;
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
    }

    public static byte[] serializeValues(int unitStatus, int batteryStatus, float batteryPercentage, float temperature, float humidity, LocalDateTime timestamp) {
        ByteBuffer buffer = ByteBuffer.allocate(16 + 1 + 1 + 1 + 1 + 1 + 8);
        buffer.put((byte) unitStatus);
        buffer.put((byte) batteryStatus);
        buffer.put((byte) Math.min(255, Math.max(0, (int) batteryPercentage)));
        buffer.put((byte) Math.min(127, Math.max(-128, (int) temperature)));
        buffer.put((byte) Math.min(255, Math.max(0, (int) humidity)));
        buffer.putLong(timestamp.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
        return buffer.array();
    }

    public static ICListenStatus fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < 24) {
            throw new IllegalArgumentException("Invalid data size for ICListenStatus");
        }

        UUID id = UUID.randomUUID();
        int unitStatus = Byte.toUnsignedInt(buffer.get());
        int batteryStatus = Byte.toUnsignedInt(buffer.get());
        float batteryPercentage = Byte.toUnsignedInt(buffer.get());
        float temperature = buffer.get(); // Signed byte
        float humidity = Byte.toUnsignedInt(buffer.get());
        long timestampMillis = buffer.getLong();
        LocalDateTime timestamp = LocalDateTime.ofEpochSecond(timestampMillis / 1000, 0, java.time.ZoneOffset.UTC);

        return new ICListenStatus(id, unitStatus, batteryStatus, batteryPercentage, temperature, humidity, timestamp);
    }

    public static ICListenStatus createDefault() {
        return new ICListenStatus(UUID.randomUUID(), 0, 0, 0.0f, 0.0f, 0.0f, LocalDateTime.now());
    }
}
