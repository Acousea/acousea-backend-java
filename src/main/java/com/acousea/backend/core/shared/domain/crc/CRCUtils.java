package com.acousea.backend.core.shared.domain.crc;


import java.nio.ByteBuffer;

public class CRCUtils {

    // Polinomio CRC-16-CCITT estándar
    private static final int POLYNOMIAL = 0x1021;
    private static final int INITIAL_VALUE = 0xFFFF;

    private CRCUtils() {
        // Constructor privado para evitar instanciación
    }

    public static short calculate16BitCRC(byte[] data) {
        return (short) calculateCRC(data);
    }

    public static int calculateCRC(byte[] data) {
        int crc = INITIAL_VALUE;
        for (byte b : data) {
            crc ^= (b << 8);
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ POLYNOMIAL;
                } else {
                    crc <<= 1;
                }
            }
        }
        return crc & 0xFFFF; // Asegura que el CRC esté en un rango de 16 bits
    }

    public static boolean verifyCRC(ByteBuffer buffer) {
        if (buffer.remaining() < 2) {
            throw new IllegalArgumentException("Data length must be at least 2 bytes for CRC verification.");
        }

        // Extraer el CRC del paquete (últimos dos bytes)
        int receivedCRC = buffer.getShort(buffer.capacity() - 2) & 0xFFFF;

        // Calcular el CRC del paquete excluyendo los últimos dos bytes
        byte[] data = new byte[buffer.capacity()];
        // Set the last two bytes to 0
        buffer.get(data, 0, buffer.capacity() - 2);
        buffer.rewind();
        int calculatedCRC = calculateCRC(data);
        return receivedCRC == calculatedCRC;
    }


}

