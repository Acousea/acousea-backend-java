package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageModule extends ExtModule {
    private int storageUsedMegabytes;
    private int storageTotalMegabytes;

    public StorageModule(int storageUsedMegabytes, int storageTotalMegabytes) {
        super("storage");
        this.storageUsedMegabytes = storageUsedMegabytes;
        this.storageTotalMegabytes = storageTotalMegabytes;

    }

    public static StorageModule create(int capacity) {
        return new StorageModule(0, capacity);
    }

    @Override
    public int getFullSize() {
        return getMinSize();
    }

    public static int getMinSize(){
        return Integer.BYTES * 2;
    }
}
