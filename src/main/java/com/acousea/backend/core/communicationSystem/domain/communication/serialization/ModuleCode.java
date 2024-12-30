package com.acousea.backend.core.communicationSystem.domain.communication.serialization;

import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import lombok.Getter;

@Getter
public enum ModuleCode {
    BATTERY('B'),
    LOCATION('L'),
    NETWORK('N'),
    OPERATION_MODES('O'),
    OPERATION_MODES_GRAPH('G'),
    REPORTING('P'),
    RTC('R'),
    STORAGE('S'),
    AMBIENT('T'),
    PAM_MODULE('M'),
    ICLISTEN_COMPLETE('i'),
    ICLISTEN_STATUS('s'),
    ICLISTEN_LOGGING_CONFIG('l'),
    ICLISTEN_STREAMING_CONFIG('c'),
    ICLISTEN_RECORDING_STATS('r'),
    ;

    private final char value;

    ModuleCode(char value) {
        this.value = value;
    }

    public static int getAmountOfBytes() {
        return Byte.BYTES;
    }

    public static ModuleCode fromValue(byte code) throws InvalidPacketException {
        char charCode = (char) code;
        for (ModuleCode moduleCode : ModuleCode.values()) {
            if (moduleCode.value == charCode) {
                return moduleCode;
            }
        }
        throw new InvalidPacketException("Invalid module code: " + String.format("%02X", code));
    }


    public static ModuleCode fromModuleName(String moduleName) {
        return switch (moduleName) {
            case BatteryModule.name -> BATTERY;
            case LocationModule.name -> LOCATION;
            case NetworkModule.name -> NETWORK;
            case OperationModesModule.name -> OPERATION_MODES;
            case LoRaReportingModule.name, IridiumReportingModule.name -> REPORTING;
            case RTCModule.name -> RTC;
            case StorageModule.name -> STORAGE;
            case AmbientModule.name -> AMBIENT;
            default -> throw new IllegalArgumentException("Invalid module name: " + moduleName);
        };
    }
}
