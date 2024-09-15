package com.acousea.backend.core.communicationSystem.infrastructure.modules;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "battery_module")
public class SQLBatteryModule {

    @Id
    private UUID nodeId;

    @Column(name = "battery_percentage")
    private int batteryPercentage;

    @Column(name = "battery_status")
    private int batteryStatus;

    // Constructor sin parámetros necesario para JPA
    public SQLBatteryModule() {
    }

    // Constructor completo
    public SQLBatteryModule(UUID nodeId, int batteryPercentage, int batteryStatus) {
        this.nodeId = nodeId;
        this.batteryPercentage = batteryPercentage;
        this.batteryStatus = batteryStatus;
    }

    // Getters y setters...

    public UUID getNodeId()

