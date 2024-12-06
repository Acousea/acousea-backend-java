
package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoRaReportingModule extends ReportingModule {
    public static final String name = "loRaReporting";
    public static final byte TECHNOLOGY_ID = 0x01;

    public LoRaReportingModule(OperationModeModule operationModeModule) {
        super(TECHNOLOGY_ID, operationModeModule);
    }

    public static LoRaReportingModule create(OperationModeModule operationModeModule) {
        return new LoRaReportingModule(operationModeModule);
    }

    public static LoRaReportingModule createEmpty() {
        return new LoRaReportingModule(null);
    }
}