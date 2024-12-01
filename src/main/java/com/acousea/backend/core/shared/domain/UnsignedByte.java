package com.acousea.backend.core.shared.domain;


public final class UnsignedByte {
    private UnsignedByte() {
        // Constructor privado para evitar instanciación
    }

    // Constantes para los límites de un unsigned byte
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 255;

    public static int toUnsignedInt(byte value) {
        return Byte.toUnsignedInt(value);
    }

    public static int toUnsignedInt(Byte value) {
        if (value == null) {
            throw new IllegalArgumentException("Byte value cannot be null");
        }
        return toUnsignedInt(value.byteValue());
    }

    public static byte toByte(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Value must be between " + MIN_VALUE + " and " + MAX_VALUE);
        }
        return (byte) value;
    }

    public static boolean isValidUnsignedByte(int value) {
        return value >= MIN_VALUE && value <= MAX_VALUE;
    }
}
