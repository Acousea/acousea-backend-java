package com.acousea.backend.core.communicationSystem.domain.communication.constants;

import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import lombok.Getter;

@Getter
public enum OperationCode {
    COMPLETE_STATUS_REPORT('S'), // 53
    BASIC_STATUS_REPORT('s'), // 73
    SET_NODE_DEVICE_CONFIG('C'), // 43
    GET_UPDATED_NODE_DEVICE_CONFIG('U'), // 55
    DEFAULT_WITH_EMPTY_PAYLOAD('D'); // 44


    private final char value;

    OperationCode(char value) {
        this.value = value;
    }

    public static int getSize() {
        return Byte.BYTES;
    }


    // Método estático para obtener un OperationCode a partir de un valor byte
    public static OperationCode fromValue(byte code) throws InvalidPacketException {
        char charCode = (char) code;
        for (OperationCode operationCode : OperationCode.values()) {
            if (operationCode.value == charCode) {
                return operationCode;
            }
        }
        throw new InvalidPacketException("Invalid operation code: " + code);
    }
}


