package com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation;


import com.acousea.backend.core.communicationSystem.domain.communication.tags.Tag;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagType;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;

import java.nio.ByteBuffer;

public class StorageTag extends Tag {
    public StorageTag(byte[] value) {
        super(TagType.STORAGE, value);
    }

    public static StorageTag fromStorageModule(StorageModule module) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * 2)
                .putInt(module.getStorageUsedMegabytes())
                .putInt(module.getStorageTotalMegabytes());

        return new StorageTag(buffer.array());
    }

    public StorageModule toStorageModule() {
        ByteBuffer buffer = ByteBuffer.wrap(this.VALUE);
        return new StorageModule(buffer.getInt(), buffer.getInt());
    }

    public static StorageTag fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < Integer.BYTES * 2) {
            throw new IllegalArgumentException(StorageTag.class.getName() + "Invalid byte array for StorageTag");
        }
        int used = buffer.getInt();
        int total = buffer.getInt();
        return fromStorageModule(new StorageModule(used, total));
    }
}

