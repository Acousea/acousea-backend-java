package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StorageModuleTest {

    @Test
    void testSerialization() {
        // Given: A known storage usage and capacity
        int storageUsed = 512; // 512 MB used
        int storageTotal = 2048; // 2048 MB total
        StorageModule storageModule = new StorageModule(storageUsed, storageTotal);

        // When: We serialize the StorageModule
        byte[] serializedBytes = storageModule.toBytes();

        // Then: The length should match expectations (1 byte for TYPE, 1 byte for length, 8 bytes for values)
        Assertions.assertEquals(serializedBytes.length, 10);

        // Checking TYPE and length byte
        Assertions.assertEquals(serializedBytes[0], (byte) ModuleCode.STORAGE.getValue());
        Assertions.assertEquals(serializedBytes[1], (byte) 8); // Length of data (4 bytes used + 4 bytes total)

        // Checking serialized storage values
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, 8);
        int deserializedUsed = buffer.getInt();
        int deserializedTotal = buffer.getInt();

        assertEquals(deserializedUsed, storageUsed);
        assertEquals(deserializedTotal, storageTotal);
    }

    @Test
    void testDeserialization() {
        // Given: A known storage usage and total capacity
        int storageUsed = 1024;
        int storageTotal = 4096;
        ByteBuffer buffer = ByteBuffer.allocate(8).putInt(storageUsed).putInt(storageTotal);
        buffer.flip(); // Prepare buffer for reading

        // When: We deserialize the bytes
        StorageModule storageModule = StorageModule.fromBytes(buffer);

        // Then: The reconstructed StorageModule should match the original
        assertEquals(storageModule.getStorageUsedMegabytes(), storageUsed);
        assertEquals(storageModule.getStorageTotalMegabytes(), storageTotal);
    }

    @Test
    void testDeserializationWithInvalidData() {
        // Given: A buffer that is too short
        ByteBuffer buffer = ByteBuffer.allocate(4); // Not enough bytes (should be 8)

        // Expect: An exception due to insufficient data
        assertThrows(IllegalArgumentException.class, () -> StorageModule.fromBytes(buffer));
    }

    @Test
    void testZeroStorage() {
        // Given: A storage module with zero used space
        StorageModule storageModule = new StorageModule(0, 1024);

        // When: We serialize and deserialize
        byte[] serializedBytes = storageModule.toBytes();
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, 8);
        StorageModule deserializedModule = StorageModule.fromBytes(buffer);

        // Then: The reconstructed module should have zero used storage
        assertEquals(deserializedModule.getStorageUsedMegabytes(), 0);
        assertEquals(deserializedModule.getStorageTotalMegabytes(), 1024);
    }

    @Test
    void testStorageCapacityLimits() {
        // Given: A large storage capacity
        int storageUsed = Integer.MAX_VALUE;
        int storageTotal = Integer.MAX_VALUE;
        StorageModule storageModule = new StorageModule(storageUsed, storageTotal);

        // When: We serialize and deserialize
        byte[] serializedBytes = storageModule.toBytes();
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, 8);
        StorageModule deserializedModule = StorageModule.fromBytes(buffer);

        // Then: The values should be correctly reconstructed
        assertEquals(deserializedModule.getStorageUsedMegabytes(), Integer.MAX_VALUE);
        assertEquals(deserializedModule.getStorageTotalMegabytes(), Integer.MAX_VALUE);
    }
}
