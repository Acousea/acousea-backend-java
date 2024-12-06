package com.acousea.backend.core.communicationSystem.domain.communication;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.CommunicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class CommunicationResult {
    private CommunicationStatus status;
    private Integer errorCode;
    private String message;

    private CommunicationResult(CommunicationStatus status, String message) {
        this(status, message, null);
    }

    private CommunicationResult(CommunicationStatus status, String message, Integer errorCode) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static CommunicationResult success(String message) {
        return new CommunicationResult(CommunicationStatus.SUCCESS, message);
    }

    public static CommunicationResult failed(String message, Integer errorCode) {
        return new CommunicationResult(CommunicationStatus.FAILED, message, errorCode);
    }

    public static CommunicationResult unknown(String message) {
        return new CommunicationResult(CommunicationStatus.UNKNOWN, message);
    }

    @Override
    public String toString() {
        if (status == CommunicationStatus.SUCCESS) {
            return String.format("Status: %s, Message: %s", status.getValue(), message);
        } else if (status == CommunicationStatus.FAILED) {
            return String.format("Status: %s, Error Code: %d, Error Message: %s", status.getValue(), errorCode, message);
        } else {
            return String.format("Status: %s, Message: %s", status.getValue(), message);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunicationResult that = (CommunicationResult) o;
        return status == that.status && Objects.equals(errorCode, that.errorCode) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, errorCode, message);
    }
}