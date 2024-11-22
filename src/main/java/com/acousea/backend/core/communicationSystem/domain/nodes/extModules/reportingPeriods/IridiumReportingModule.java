package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IridiumReportingModule extends ReportingModule {
    public static final byte TECHNOLOGY_ID = 0x02;
    private final String imei;

    public IridiumReportingModule(OperationModeModule operationModeModule, String imei) {
        super("IridiumReporting", TECHNOLOGY_ID, operationModeModule);
        this.imei = imei;
    }

    public static IridiumReportingModule create(OperationModeModule operationModeModule, String imei) {
        return new IridiumReportingModule(operationModeModule , imei);
    }

    public static ReportingModule createEmpty() {
        return new IridiumReportingModule(null, "");
    }
}