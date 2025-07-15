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
        if (isNotValidUnsignedByte(value)) {
            throw new IllegalArgumentException("Value " + value + " must be between "
                    + Byte.MIN_VALUE + " and " + Byte.MAX_VALUE
                    + "to be converted to an UnsignedByte[" + MIN_VALUE + "-" + MAX_VALUE + "]");
        }
        return (byte) Byte.toUnsignedInt((byte) value);
    }

    public static boolean isNotValidUnsignedByte(int value) {
        return value < UnsignedByte.MIN_VALUE || value > UnsignedByte.MAX_VALUE;
    }
}
