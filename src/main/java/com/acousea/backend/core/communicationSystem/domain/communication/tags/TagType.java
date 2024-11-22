package com.acousea.backend.core.communicationSystem.domain.communication.tags;

import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import lombok.Getter;

@Getter
public enum TagType {
    BATTERY('B'),
    LOCATION('L'),
    NETWORK('N'),
    OPERATION_MODES('O'),
    REPORTING_PERIODS('P'),
    RTC('R'),
    STORAGE('S'),
    TEMPERATURE('T');

    private final char value;

    TagType(char value) {
        this.value = value;
    }

    public static TagType fromValue(byte code) throws InvalidPacketException {
        char charCode = (char) code;
        for (TagType tagType : TagType.values()) {
            if (tagType.value == charCode) {
                return tagType;
            }
        }
        throw new InvalidPacketException("Invalid operation code: " + code);
    }
}
