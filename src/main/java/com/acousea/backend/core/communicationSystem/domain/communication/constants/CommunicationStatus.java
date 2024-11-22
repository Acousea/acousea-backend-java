package com.acousea.backend.core.communicationSystem.domain.communication.constants;

public enum CommunicationStatus {
    SUCCESS("OK"),
    FAILED("FAILED"),
    UNKNOWN("UNKNOWN");

    private final String value;

    CommunicationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

