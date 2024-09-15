package com.acousea.backend.core.communicationSystem.infrastructure;


import com.acousea.backend.core.communicationSystem.domain.NodeDevice;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "drifter_device_info")
public class SQLDrifterDeviceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "epoch_time")
    private LocalDateTime epochTime;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "battery_percentage")
    private int batteryPercentage;

    @Column(name = "battery_status")
    private int batteryStatus;

    @Column(name = "temperature")
    private double temperature;

    @Column(name = "operation_mode")
    private int operationMode;

    @Column(name = "storage_total")
    private int storageTotal;

    @Column(name = "storage_used")
    private int storageUsed;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    // Constructor sin parámetros necesario para JPA
    public SQLDrifterDeviceInfo() {
    }

    // Constructor completo
    public SQLDrifterDeviceInfo(UUID id, LocalDateTime epochTime, double latitude, double longitude, int batteryPercentage, int batteryStatus, double temperature, int operationMode, int storageTotal, int storageUsed, LocalDateTime timestamp) {
        this.id = id;
        this.epochTime = epochTime;
        this.operationMode = operationMode;
        this.timestamp = timestamp;
    }

    // Convertir de dominio a entidad ORM
    public static SQLDrifterDeviceInfo fromDomain(NodeDevice deviceInfo) {
        return new SQLDrifterDeviceInfo(
                deviceInfo.id(),
                deviceInfo.epochTime(),
                deviceInfo.operationMode(),
                deviceInfo.timestamp()
        );
    }

    // Convertir de entidad ORM a dominio
    public NodeDevice toDomain() {
        return new NodeDevice(
                id,
                "name",
                operationMode,
                epochTime,
                new ArrayList<>(),
                timestamp
        );
    }

    // Getters y setters...

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(LocalDateTime epochTime) {
        this.epochTime = epochTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(int batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    public int getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(int batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(int operationMode) {
        this.operationMode = operationMode;
    }

    public int getStorageTotal() {
        return storageTotal;
    }

    public void setStorageTotal(int storageTotal) {
        this.storageTotal = storageTotal;
    }

    public int getStorageUsed() {
        return storageUsed;
    }

    public void setStorageUsed(int storageUsed) {
        this.storageUsed = storageUsed;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
