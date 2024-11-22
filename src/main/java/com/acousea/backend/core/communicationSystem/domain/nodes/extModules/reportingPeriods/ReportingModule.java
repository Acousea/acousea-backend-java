package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ReportingModule extends ExtModule {
    private final byte technologyId; // ID de la tecnología
    private final Map<Byte, Short> reportingPeriodsPerOperationMode = new HashMap<>(); // Mapa de modo de operación y periodo


    protected ReportingModule(String name, byte technologyId, OperationModeModule operationModeModule) {
        super(name);
        this.technologyId = technologyId;
        // Asegurar que cada modo de operación tenga un periodo de reporte inicializado
        if (operationModeModule != null) {
            initializeReportingPeriods(operationModeModule);
        }
    }

    private void initializeReportingPeriods(OperationModeModule operationModeModule) {
        operationModeModule.getOperationModes().forEach((id, mode) -> {
            reportingPeriodsPerOperationMode.put(mode.getId().toByte(), (short) 0);
        });
    }

    public void setReportingPeriod(byte modeId, int period) {
        reportingPeriodsPerOperationMode.put(modeId, (short) period);
    }

    public int getReportingPeriod(byte modeId) {
        return reportingPeriodsPerOperationMode.getOrDefault(modeId, (short) 0);
    }

    @Override
    public int getFullSize() {
        return Byte.SIZE + reportingPeriodsPerOperationMode.size() * (Byte.SIZE + Short.SIZE);
    }

    public static int getMinSize() {
        return Byte.BYTES + (Byte.BYTES + Short.BYTES);
    }
}
