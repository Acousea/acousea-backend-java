package com.acousea.backend.core.communicationSystem.domain.communication.constants;

import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import lombok.Getter;

@Getter
public enum OperationCode {
    SUMMARY_REPORT('S'),
    SUMMARY_SIMPLE_REPORT('s'),
    SET_NODE_DEVICE_CONFIG('C'),
    GET_UPDATED_NODE_DEVICE_CONFIG('U');


    private final char value;

    OperationCode(char value) {
        this.value = value;
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


