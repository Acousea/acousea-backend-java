package com.acousea.backend.core.shared.domain;

public final class UnsignedByte implements Comparable<UnsignedByte> {
    public static final int SIZE = 1;
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 255;

    private final int value;

    private UnsignedByte(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Value must be between 0 and 255, inclusive.");
        }
        this.value = value;
    }

    public static UnsignedByte of(int value) {
        return new UnsignedByte(value);
    }

    public static UnsignedByte of(byte value) {
        return new UnsignedByte(Byte.toUnsignedInt(value));
    }

    public int toInt() {
        return value;
    }

    public byte toByte() {
        return (byte) value;
    }

    public UnsignedByte add(UnsignedByte other) {
        return UnsignedByte.of(this.value + other.value);
    }

    public UnsignedByte subtract(UnsignedByte other) {
        return UnsignedByte.of(this.value - other.value);
    }

    @Override
    public int compareTo(UnsignedByte other) {
        return Integer.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UnsignedByte that = (UnsignedByte) obj;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}

