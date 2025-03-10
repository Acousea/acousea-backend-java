package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage;

import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Getter
@Setter
public class StorageModule extends SerializableModule implements ExtModule {
    public static final String name = "storage";
    private int storageUsedMegabytes;
    private int storageTotalMegabytes;

    public StorageModule(int storageUsedMegabytes, int storageTotalMegabytes) {
        super(ModuleCode.STORAGE, serialize(storageUsedMegabytes, storageTotalMegabytes));
        this.storageUsedMegabytes = storageUsedMegabytes;
        this.storageTotalMegabytes = storageTotalMegabytes;
    }

    public static StorageModule create(int capacity) {
        return new StorageModule(0, capacity);
    }

    private static byte[] serialize(int storageUsed, int storageTotal) {
        ByteBuffer buffer = ByteBuffer.allocate(getMinSize());
        buffer.putInt(storageUsed);
        buffer.putInt(storageTotal);
        return buffer.array();
    }

    public static StorageModule fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < getMinSize()) {
            throw new IllegalArgumentException("Invalid byte array for StorageModule");
        }
        int storageUsed = buffer.getInt();
        int storageTotal = buffer.getInt();
        return new StorageModule(storageUsed, storageTotal);
    }

    @Override
    public int getFullSize() {
        return getMinSize();
    }

    public static int getMinSize() {
        return Integer.BYTES * 2; // 4 bytes for storageUsed + 4 bytes for storageTotal
    }

    @Override
    public String toString() {
        return "StorageModule{" +
                "storageUsedMegabytes=" + storageUsedMegabytes +
                ", storageTotalMegabytes=" + storageTotalMegabytes +
                '}';
    }
}
