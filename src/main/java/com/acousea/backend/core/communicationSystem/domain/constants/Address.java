package com.acousea.backend.core.shared.domain.constants;

public enum Address {
    BACKEND(0x00),       // 0b00000000
    LOCALIZER(0x01),     // 0b00000001
    DRIFTER(0x02),       // 0b00000010
    PI3(0x03),           // 0b00000011
    SENDER_MASK(0xC0),   // 0b11000000
    RECEIVER_MASK(0x30); // 0b00110000

    private final int value;

    Address(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
