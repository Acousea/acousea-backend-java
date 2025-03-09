package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class OperationModesModuleTest {

    @Test
    void testSerialization() {
        // Given: A map of operation modes
        Map<Short, OperationMode> modes = new TreeMap<>();
        modes.put((short) 1, OperationMode.create((short) 1, "Mode1"));
        modes.put((short) 2, OperationMode.create((short) 2, "Mode2"));

        OperationModesModule operationModesModule = new OperationModesModule(modes, (short) 1);

        // When: We serialize the OperationModesModule
        byte[] serializedBytes = operationModesModule.toBytes();

        // Then: The length should match expectations (1 byte for TYPE, 1 byte for length, size of modes + 1 for active mode)
        Assertions.assertEquals(serializedBytes.length, 5);

        // Checking TYPE and length byte
        Assertions.assertEquals(serializedBytes[0], (byte) ModuleCode.OPERATION_MODES.getValue());
        Assertions.assertEquals(serializedBytes[1], (byte) (modes.size() + 1)); // Length of data

        // Checking serialized operation modes
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, modes.size() + 1);
        short deserializedMode1 = buffer.get();
        short deserializedMode2 = buffer.get();
        short activeMode = buffer.get();

        assertEquals(deserializedMode1, 1);
        assertEquals(deserializedMode2, 2);
        assertEquals(activeMode, 1);
    }

    @Test
    void testDeserialization() {
        // Given: A known set of operation modes
        short activeMode = 2;
        ByteBuffer buffer = ByteBuffer.allocate(3).put((byte) 1).put((byte) 2).put((byte) activeMode);
        buffer.flip(); // Prepare buffer for reading

        // When: We deserialize the bytes
        OperationModesModule operationModesModule = OperationModesModule.fromBytes(buffer);

        // Then: The reconstructed OperationModesModule should match the original
        assertEquals(operationModesModule.getModes().size(), 2);
        assertTrue(operationModesModule.getModes().containsKey((short) 1));
        assertTrue(operationModesModule.getModes().containsKey((short) 2));
        assertEquals(operationModesModule.getActiveOperationModeIdx(), activeMode);
    }

    @Test
    void testDeserializationWithInvalidData() {
        // Given: A buffer that is too short
        ByteBuffer buffer = ByteBuffer.allocate(1); // Not enough bytes

        // Expect: An exception due to insufficient data
        assertThrows(IllegalArgumentException.class, () -> OperationModesModule.fromBytes(buffer));
    }

    @Test
    void testAddingOperationMode() {
        // Given: A module with initial modes
        OperationModesModule operationModesModule = new OperationModesModule(new TreeMap<>());

        // When: Adding a new operation mode
        operationModesModule.addOperationMode("NewMode");

        // Then: The mode should be present
        assertEquals(operationModesModule.getModes().size(), 1);
        assertTrue(operationModesModule.getModes().containsKey((short) 0)); // First ID should be 0
        assertEquals(operationModesModule.getModes().get((short) 0).getName(), "NewMode");
    }

    @Test
    void testRemovingOperationMode() {
        // Given: A module with predefined modes
        Map<Short, OperationMode> modes = new TreeMap<>();
        modes.put((short) 1, OperationMode.create((short) 1, "Mode1"));
        OperationModesModule operationModesModule = new OperationModesModule(modes);

        // When: Removing an existing mode
        operationModesModule.removeOperationMode((byte) 1);

        // Then: The mode should be removed
        assertEquals(operationModesModule.getModes().size(), 0);
    }

    @Test
    void testRemovingNonExistingOperationMode() {
        // Given: An empty module
        OperationModesModule operationModesModule = new OperationModesModule(new TreeMap<>());

        // Expect: An exception when trying to remove a non-existing mode
        assertThrows(IllegalArgumentException.class, () -> operationModesModule.removeOperationMode((byte) 1));
    }

    @Test
    void testMaxOperationModesReached() {
        // Given: A module with the maximum number of operation modes
        OperationModesModule operationModesModule = new OperationModesModule(new TreeMap<>());

        for (int i = 0; i < 256; i++) {
            operationModesModule.addOperationMode("Mode" + i);
        }

        // Expect: Adding another mode should throw an exception
        assertThrows(IllegalStateException.class, () -> operationModesModule.addOperationMode("OverflowMode"));
    }
}
