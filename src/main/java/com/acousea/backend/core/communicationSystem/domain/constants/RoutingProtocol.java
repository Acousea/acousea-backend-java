package com.acousea.backend.core.communicationSystem.domain.constants;

public enum PacketType {
    LORA_PACKET(0x08),          // 0b00001000
    IRIDIUM_PACKET(0x04),       // 0b00000100
    CLEAN_PACKET_TYPE(0xF3),    // 0b11110011
    GET_PACKET_TYPE_MASK(0x0C); // 0b00001100

    private final int value;

    PacketType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}