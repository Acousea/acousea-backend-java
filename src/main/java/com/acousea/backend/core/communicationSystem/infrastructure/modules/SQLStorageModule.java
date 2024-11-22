package com.acousea.backend.core.communicationSystem.infrastructure.modules;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "storage_module")
public class SQLStorageModule {

    @Id
    private UUID nodeId;

    @Column(name = "storage_used_megabytes")
    private int storageUsedMegabytes;

    @Column(name = "storage_total_megabytes")
    private int storageTotalMegabytes;

    // Constructor sin parámetros necesario para JPA
    public SQLStorageModule() {
    }

    // Constructor completo
    public SQLStorageModule(UUID nodeId, int storageUsedMegabytes, int storageTotalMegabytes) {
        this.nodeId = nodeId;
        this.storageUsedMegabytes = storageUsedMegabytes;
        this.storageTotalMegabytes = storageTotalMegabytes;
    }

    

    public UUID getNodeId() {
        return nodeId;
    }

    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId;
    }

    public int getStorageUsedMegabytes() {
        return storageUsedMegabytes;
    }

    public void setStorageUsedMegabytes(int storageUsedMegabytes) {
        this.storageUsedMegabytes = storageUsedMegabytes;
    }

    public int getStorageTotalMegabytes() {
        return storageTotalMegabytes;
    }

    public void setStorageTotalMegabytes(int storageTotalMegabytes) {
        this.storageTotalMegabytes = storageTotalMegabytes;
    }
}
