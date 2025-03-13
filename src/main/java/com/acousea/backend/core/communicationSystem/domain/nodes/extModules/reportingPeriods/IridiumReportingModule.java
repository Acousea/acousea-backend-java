package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;

import java.util.HashMap;
import java.util.Map;

public class IridiumReportingModule extends ReportingModule {
    public static final String name = "iridiumReporting";
    public static final byte TECHNOLOGY_ID = 0x02;
    private final String imei;

    public IridiumReportingModule(Map<OperationMode, Short> reportingPeriods, String imei) {
        super(TECHNOLOGY_ID, reportingPeriods);
        this.imei = imei;
    }

    public String getImei() {
        return imei;
    }

    public static IridiumReportingModule create(Map<OperationMode, Short> reportingPeriods, String imei) {
        return new IridiumReportingModule(reportingPeriods, imei);
    }

    public static IridiumReportingModule createEmpty() {
        return new IridiumReportingModule(new HashMap<>(), "");
    }

}
