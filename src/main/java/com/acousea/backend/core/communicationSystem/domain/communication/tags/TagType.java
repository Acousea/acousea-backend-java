package com.acousea.backend.core.communicationSystem.domain.communication.tags;

import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import lombok.Getter;

@Getter
public enum TagType {
    BATTERY('B'),
    LOCATION('L'),
    NETWORK('N'),
    OPERATION_MODES('O'),
    REPORTING('P'),
    RTC('R'),
    STORAGE('S'),
    AMBIENT('T');

    private final char value;

    TagType(char value) {
        this.value = value;
    }

    public static int getAmountOfBytes() {
        return Byte.BYTES;
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


    public static TagType fromModuleName(String moduleName) {
        return switch (moduleName) {
            case BatteryModule.name -> BATTERY;
            case LocationModule.name -> LOCATION;
            case NetworkModule.name -> NETWORK;
            case OperationModeModule.name -> OPERATION_MODES;
            case LoRaReportingModule.name, IridiumReportingModule.name -> REPORTING;
            case RTCModule.name -> RTC;
            case StorageModule.name -> STORAGE;
            case AmbientModule.name -> AMBIENT;
            default -> throw new IllegalArgumentException("Invalid module name: " + moduleName);
        };
    }
}
