package com.acousea.backend.core.communicationSystem.domain.communication.constants;

public enum RequestStatus {
    SUCCESS(1),
    PENDING(0),
    ERROR(2);

    private final int value;

    RequestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RequestStatus fromValue(int value) {
        for (RequestStatus status : values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown RequestStatus value: " + value);
    }
}
