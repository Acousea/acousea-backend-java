package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RTCModuleTest {
    @Test
    void testSerialization() {
        // Given: A known LocalDateTime instance
        LocalDateTime now = LocalDateTime.of(2024, 3, 7, 12, 30, 45);
        RTCModule rtcModule = new RTCModule(now);

        // When: We serialize the RTCModule
        byte[] serializedBytes = rtcModule.toBytes();

        // Then: The length should match expectations (1 byte for TYPE, 1 byte for length, 8 bytes for time)
        Assertions.assertEquals(serializedBytes.length, 10);

        // Checking TYPE and length byte
        Assertions.assertEquals(serializedBytes[0], (byte) ModuleCode.RTC.getValue());
        Assertions.assertEquals(serializedBytes[1], (byte) 8);

        // Checking serialized time
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, 8);
        long epochSeconds = buffer.getLong();
        assertEquals(epochSeconds, now.toEpochSecond(ZoneOffset.UTC));
    }

    @Test
    void testDeserialization() {
        // Given: A known LocalDateTime instance
        LocalDateTime now = LocalDateTime.of(2024, 3, 7, 12, 30, 45);
        long epochSeconds = now.toEpochSecond(ZoneOffset.UTC);
        ByteBuffer buffer = ByteBuffer.allocate(8).putLong(epochSeconds);
        buffer.flip(); // Prepare buffer for reading

        // When: We deserialize the bytes
        RTCModule rtcModule = RTCModule.fromBytes(buffer);

        // Then: The reconstructed RTCModule should match the original
        assertEquals(rtcModule.getCurrentTime(), now);
    }

    @Test
    void testDeserializationWithInvalidData() {
        // Given: A buffer that is too short
        ByteBuffer buffer = ByteBuffer.allocate(4); // Not enough bytes

        // Expect: An exception due to insufficient data
        assertThrows(IllegalArgumentException.class, () -> RTCModule.fromBytes(buffer));
    }
}
