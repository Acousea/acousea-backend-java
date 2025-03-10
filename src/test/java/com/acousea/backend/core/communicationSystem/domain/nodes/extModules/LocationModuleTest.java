package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocationModuleTest {

    @Test
    void testSerialization() {
        // Given: A known latitude and longitude value
        float latitude = 37.7749f;
        float longitude = -122.4194f;
        LocationModule locationModule = new LocationModule(latitude, longitude);

        // When: We serialize the LocationModule
        byte[] serializedBytes = locationModule.toBytes();

        // Then: The length should match expectations (1 byte for TYPE, 1 byte for length, 8 bytes for values)
        Assertions.assertEquals(serializedBytes.length, 10);

        // Checking TYPE and length byte
        Assertions.assertEquals(serializedBytes[0], (byte) ModuleCode.LOCATION.getValue());
        Assertions.assertEquals(serializedBytes[1], (byte) 8); // Length of data (4 bytes lat + 4 bytes lon)

        // Checking serialized latitude and longitude
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, 8);
        float deserializedLatitude = buffer.getFloat();
        float deserializedLongitude = buffer.getFloat();

        assertEquals(deserializedLatitude, latitude, 0.0001);
        assertEquals(deserializedLongitude, longitude, 0.0001);
    }

    @Test
    void testDeserialization() {
        // Given: A known latitude and longitude
        float latitude = -15.7923f;
        float longitude = 47.5254f;
        ByteBuffer buffer = ByteBuffer.allocate(8).putFloat(latitude).putFloat(longitude);
        buffer.flip(); // Prepare buffer for reading

        // When: We deserialize the bytes
        LocationModule locationModule = LocationModule.fromBytes(buffer);

        // Then: The reconstructed LocationModule should match the original
        assertEquals(locationModule.getLatitude(), latitude, 0.0001);
        assertEquals(locationModule.getLongitude(), longitude, 0.0001);
    }

    @Test
    void testDeserializationWithInvalidData() {
        // Given: A buffer that is too short
        ByteBuffer buffer = ByteBuffer.allocate(4); // Not enough bytes

        // Expect: An exception due to insufficient data
        assertThrows(IllegalArgumentException.class, () -> LocationModule.fromBytes(buffer));
    }
}
