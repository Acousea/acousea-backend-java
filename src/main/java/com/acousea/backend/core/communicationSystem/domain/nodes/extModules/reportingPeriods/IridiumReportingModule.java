package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;

public class IridiumReportingModule extends ReportingModule {
    public static final String name = "iridiumReporting";
    public static final byte TECHNOLOGY_ID = 0x02;
    private final String imei;

    public IridiumReportingModule(OperationModesModule operationModesModule, String imei) {
        super(TECHNOLOGY_ID, operationModesModule);
        this.imei = imei;
    }

    public String getImei() {
        return imei;
    }

    public static IridiumReportingModule create(OperationModesModule operationModesModule, String imei) {
        return new IridiumReportingModule(operationModesModule, imei);
    }

    public static IridiumReportingModule createEmpty() {
        return new IridiumReportingModule(null, "");
    }

}
