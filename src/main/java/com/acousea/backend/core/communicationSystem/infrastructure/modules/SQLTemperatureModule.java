package com.acousea.backend.core.communicationSystem.infrastructure.modules;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "temperature_module")
public class SQLTemperatureModule {

    @Id
    private UUID nodeId;

    @Column(name = "temperature")
    private double temperature;

    // Constructor sin parámetros necesario para JPA
    public SQLTemperatureModule() {
    }

    // Constructor completo
    public SQLTemperatureModule(UUID nodeId, double temperature) {
        this.nodeId = nodeId;
        this.temperature = temperature;
    }

    public UUID getNodeId() {
        return nodeId;
    }

    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}

