package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;

import java.util.HashMap;
import java.util.Map;

public class LoRaReportingModule extends ReportingModule {
    public static final String name = "loRaReporting";
    public static final byte TECHNOLOGY_ID = 0x01;

    public LoRaReportingModule(Map<OperationMode, Short> reportingPeriods) {
        super(TECHNOLOGY_ID, reportingPeriods);
    }

    public static LoRaReportingModule create(Map<OperationMode, Short> reportingPeriods) {
        return new LoRaReportingModule(reportingPeriods);
    }

    public static LoRaReportingModule createEmpty() {
        return new LoRaReportingModule(new HashMap<>());
    }
}
