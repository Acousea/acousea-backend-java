package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;

public class LoRaReportingModule extends ReportingModule {
    public static final String name = "loRaReporting";
    public static final byte TECHNOLOGY_ID = 0x01;

    public LoRaReportingModule(OperationModesModule operationModesModule) {
        super(TECHNOLOGY_ID, operationModesModule);
    }

    public static LoRaReportingModule create(OperationModesModule operationModesModule) {
        return new LoRaReportingModule(operationModesModule);
    }

    public static LoRaReportingModule createEmpty() {
        return new LoRaReportingModule(null);
    }
}
