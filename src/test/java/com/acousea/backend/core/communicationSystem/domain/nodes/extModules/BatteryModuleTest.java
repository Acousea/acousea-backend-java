package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.BatteryStatus;
import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BatteryModuleTest {
    @Test
    void testSerialization() {
        // Given: A known battery percentage and status
        int batteryPercentage = 85;
        BatteryStatus batteryStatus = BatteryStatus.FULL;
        BatteryModule batteryModule = new BatteryModule(batteryPercentage, batteryStatus);

        // When: We serialize the BatteryModule
        byte[] serializedBytes = batteryModule.toBytes();

        // Then: The length should match expectations (1 byte for TYPE, 1 byte for length, 2 bytes for values)
        Assertions.assertEquals(4, serializedBytes.length);

        // Checking TYPE and length byte
        Assertions.assertEquals(serializedBytes[0], (byte) ModuleCode.BATTERY.getValue());
        Assertions.assertEquals(serializedBytes[1], (byte) 2); // Length of data

        // Checking serialized battery percentage and status
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, 2);
        int deserializedBatteryPercentage = buffer.get();
        int deserializedBatteryStatus = buffer.get();

        assertEquals(deserializedBatteryPercentage, batteryPercentage);
        assertEquals(deserializedBatteryStatus, batteryStatus.getValue());
    }

    @Test
    void testDeserialization() {
        // Given: A known battery percentage and status
        int batteryPercentage = 60;
        BatteryStatus batteryStatus = BatteryStatus.CHARGING;
        ByteBuffer buffer = ByteBuffer.allocate(2).put((byte) batteryPercentage).put((byte) batteryStatus.getValue());
        buffer.flip(); // Prepare buffer for reading

        // When: We deserialize the bytes
        BatteryModule batteryModule = BatteryModule.fromBytes(buffer);

        // Then: The reconstructed BatteryModule should match the original
        assertEquals(batteryModule.getBatteryPercentage(), batteryPercentage);
        assertEquals(batteryModule.getBatteryStatus(), batteryStatus);
    }

    @Test
    void testDeserializationWithInvalidData() {
        // Given: A buffer that is too short
        ByteBuffer buffer = ByteBuffer.allocate(1); // Not enough bytes

        // Expect: An exception due to insufficient data
        assertThrows(IllegalArgumentException.class, () -> BatteryModule.fromBytes(buffer));
    }

    @Test
    void testDeserializationWithInvalidStatus() {
        // Given: A buffer with an invalid BatteryStatus value
//        int batteryPercentage = 50;
//        int invalidStatusValue = 99; // Assuming 99 is not a valid BatteryStatus
//        ByteBuffer buffer = ByteBuffer.allocate(2).put((byte) batteryPercentage).put((byte) invalidStatusValue);
//        buffer.flip(); // Prepare buffer for reading
//
//        // Expect: An exception due to invalid status value
//        assertThrows(IllegalArgumentException.class, () -> BatteryModule.fromBytes(buffer));
    }
}
