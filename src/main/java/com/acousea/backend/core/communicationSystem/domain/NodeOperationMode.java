package com.acousea.backend.core.communicationSystem.domain;

public enum OperationMode {
    LAUNCHING(1),
    WORKING(2),
    RECOVERING(3),
    IDLE(4);

    private final int value;

    OperationMode(int value) {
        this.value = value;
    }
}
