package com.acousea.backend.core.communicationSystem.domain.communication.constants;

import lombok.Getter;

@Getter
public enum BatteryStatus {
    CHARGING(0),
    DISCHARGING(1),
    FULL(2),
    EMPTY(3),
    UNKNOWN(4);

    private final int value;

    BatteryStatus(int value) {
        this.value = value;
    }


    public static BatteryStatus fromInt(int status) {
        return switch (status) {
            case 0 -> CHARGING;
            case 1 -> DISCHARGING;
            case 2 -> FULL;
            case 3 -> EMPTY;
            default -> UNKNOWN;
        };
    }

}
