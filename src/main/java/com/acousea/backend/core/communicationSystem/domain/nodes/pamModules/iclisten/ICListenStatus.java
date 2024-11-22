package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.time.LocalDateTime;

@Getter
@Setter
public class ICListenStatus {
    private UUID id;
    private int unitStatus;
    private int batteryStatus;
    private double batteryPercentage;
    private double temperature;
    private double humidity;
    private LocalDateTime timestamp;

    public ICListenStatus(UUID id, int unitStatus, int batteryStatus, double batteryPercentage, double temperature, double humidity, LocalDateTime timestamp) {
        this.id = id;
        this.unitStatus = unitStatus;
        this.batteryStatus = batteryStatus;
        this.batteryPercentage = batteryPercentage;
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
    }

    public static ICListenStatus createDefault() {
        return new ICListenStatus(UUID.randomUUID(), 0, 0, 0.0, 0.0, 0.0, LocalDateTime.now());
    }

    // Métodos Getters y Setters
}
