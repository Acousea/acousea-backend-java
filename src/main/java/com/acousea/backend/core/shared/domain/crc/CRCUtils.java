package com.acousea.backend.core.shared.domain.crc;


public class CRCUtils {

    // Polinomio CRC-16-CCITT estándar
    private static final int POLYNOMIAL = 0x1021;
    private static final int INITIAL_VALUE = 0xFFFF;

    private CRCUtils() {
        // Constructor privado para evitar instanciación
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

    public static boolean verifyCRC(byte[] data) {
        int length = data.length;
        if (length < 2) {
            throw new IllegalArgumentException("Data length must be at least 2 bytes for CRC verification.");
        }

        // Extraer el CRC del paquete (últimos dos bytes)
        int receivedCRC = ((data[length - 2] & 0xFF) << 8) | (data[length - 1] & 0xFF);

        // Calcular el CRC del paquete excluyendo los últimos dos bytes
        int calculatedCRC = calculateCRC(subArray(data, 0, length - 2));

        return receivedCRC == calculatedCRC;
    }

    private static byte[] subArray(byte[] array, int start, int end) {
        byte[] result = new byte[end - start];
        System.arraycopy(array, start, result, 0, end - start);
        return result;
    }
}

