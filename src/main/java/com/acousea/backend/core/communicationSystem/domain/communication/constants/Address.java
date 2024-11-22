package com.acousea.backend.core.communicationSystem.domain.communication.constants;

import com.acousea.backend.core.shared.domain.UnsignedByte;
import lombok.Getter;

import java.util.Set;

@Getter
public class Address {
    private static final UnsignedByte BACKEND = UnsignedByte.of(0x00); // 0b00000000
    private static final UnsignedByte BROADCAST_ADDRESS = UnsignedByte.of(0xFF); // 0b11111111
    private static final Set<UnsignedByte> RESERVED_ADDRESSES = Set.of(BACKEND, BROADCAST_ADDRESS);

    private final UnsignedByte value;

    private Address(UnsignedByte value) {
        this.value = value;
    }

    public static int getSize() {
        return UnsignedByte.SIZE;
    }

    public static Address of(int value) {
        if (isReserved(value)) {
            throw new IllegalArgumentException("Address value " + value + " is reserved.");
        }
        if (!isValidRange(value)) {
            throw new IllegalArgumentException("Address value " + value + " is out of the valid range.");
        }
        return new Address(UnsignedByte.of(value));
    }

    public static boolean isReserved(int value) {
        return RESERVED_ADDRESSES.contains(UnsignedByte.of(value));
    }

    public static boolean isValidRange(int value) {
        // Ejemplo: Rango permitido entre 0x01 y 0xFE (excluyendo los reservados)
        return value >= 0x01 && value <= 0xFE;
    }

    public static Address getBackend() {
        return new Address(BACKEND);
    }

    public static Address getBroadcastAddress() {
        return new Address(BROADCAST_ADDRESS);
    }

    @Override
    public String toString() {
        return "Address{" +
                "value=" + String.format("0x%02X", value.toInt()) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return value == address.value;
    }
}
