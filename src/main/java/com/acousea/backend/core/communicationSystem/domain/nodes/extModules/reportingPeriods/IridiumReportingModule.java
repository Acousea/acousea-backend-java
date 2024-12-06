package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IridiumReportingModule extends ReportingModule {
    public static final String name = "iridiumReporting";
    public static final byte TECHNOLOGY_ID = 0x02;
    private final String imei;

    public IridiumReportingModule(OperationModeModule operationModeModule, String imei) {
        super(TECHNOLOGY_ID, operationModeModule);
        this.imei = imei;
    }

    public static IridiumReportingModule create(OperationModeModule operationModeModule, String imei) {
        return new IridiumReportingModule(operationModeModule , imei);
    }

    public static ReportingModule createEmpty() {
        return new IridiumReportingModule(null, "");
    }


    public static IridiumReportingModule fromDTO(NodeDeviceDTO.ExtModuleDto.IridiumReportingModuleDto iridiumReporting) {
        OperationModeModule operationModeModule = OperationModeModule.fromDTO(iridiumReporting.getReportingPeriods());
        return new IridiumReportingModule(operationModeModule, iridiumReporting.getImei());
    }
}