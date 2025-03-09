package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AmbientModuleTest {

    @Test
    void testSerialization() {
        // Given: A known temperature and humidity value
        int temperature = 25;
        int humidity = 65;
        AmbientModule ambientModule = new AmbientModule(temperature, humidity);

        // When: We serialize the AmbientModule
        byte[] serializedBytes = ambientModule.toBytes();

        // Then: The length should match expectations (1 byte for TYPE, 1 byte for length, 2 bytes for values)
        Assertions.assertEquals(4, serializedBytes.length);

        // Checking TYPE and length byte
        Assertions.assertEquals((byte) ModuleCode.AMBIENT.getValue(), serializedBytes[0]);
        Assertions.assertEquals((byte) 2, serializedBytes[1]); // Length of data

        // Checking serialized temperature and humidity
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, 2);
        int deserializedTemperature = buffer.get();
        int deserializedHumidity = buffer.get();

        assertEquals(deserializedTemperature, temperature);
        assertEquals(deserializedHumidity, humidity);
    }

    @Test
    void testDeserialization() {
        // Given: A known temperature and humidity
        int temperature = 30;
        int humidity = 50;
        ByteBuffer buffer = ByteBuffer.allocate(2).put((byte) temperature).put((byte) humidity);
        buffer.flip(); // Prepare buffer for reading

        // When: We deserialize the bytes
        AmbientModule ambientModule = AmbientModule.fromBytes(buffer);

        // Then: The reconstructed AmbientModule should match the original
        assertEquals(ambientModule.getTemperature(), temperature);
        assertEquals(ambientModule.getHumidity(), humidity);
    }

    @Test
    void testDeserializationWithInvalidData() {
        // Given: A buffer that is too short
        ByteBuffer buffer = ByteBuffer.allocate(1); // Not enough bytes

        // Expect: An exception due to insufficient data
        assertThrows(IllegalArgumentException.class, () -> AmbientModule.fromBytes(buffer));
    }
}
