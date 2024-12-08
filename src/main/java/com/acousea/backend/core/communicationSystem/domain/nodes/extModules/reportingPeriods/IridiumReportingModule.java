package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;

public class IridiumReportingModule extends ReportingModule {
    public static final String name = "iridiumReporting";
    public static final byte TECHNOLOGY_ID = 0x02;
    private final String imei;

    public IridiumReportingModule(OperationModeModule operationModeModule, String imei) {
        super(TECHNOLOGY_ID, operationModeModule);
        this.imei = imei;
    }

    public String getImei() {
        return imei;
    }

    public static IridiumReportingModule create(OperationModeModule operationModeModule, String imei) {
        return new IridiumReportingModule(operationModeModule, imei);
    }

    public static IridiumReportingModule createEmpty() {
        return new IridiumReportingModule(null, "");
    }

}
