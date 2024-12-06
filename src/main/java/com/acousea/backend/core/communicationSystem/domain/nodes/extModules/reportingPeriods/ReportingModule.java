package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;
import com.acousea.backend.core.shared.domain.UnsignedByte;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ReportingModule extends ExtModule {

    private final byte technologyId; // ID de la tecnología
    private final Map<OperationMode, Short> reportingPeriodsPerOperationMode = new HashMap<>(); // Mapa de modo de operación y periodo


    protected ReportingModule(byte technologyId, OperationModeModule operationModeModule) {

        this.technologyId = technologyId;
        // Asegurar que cada modo de operación tenga un periodo de reporte inicializado
        if (operationModeModule != null) {
            initializeReportingPeriods(operationModeModule);
        }
    }

    private void initializeReportingPeriods(OperationModeModule operationModeModule) {
        operationModeModule.getOperationModes().forEach((id, mode) -> {
            reportingPeriodsPerOperationMode.put(mode, (short) 0);
        });
    }

    public void setReportingPeriod(OperationMode mode, int period) {
        reportingPeriodsPerOperationMode.put(mode, (short) period);
    }

    public int getReportingPeriod(byte modeId) {
        int unsignedId = UnsignedByte.toUnsignedInt(modeId);
        OperationMode mode = reportingPeriodsPerOperationMode.keySet().stream()
                .filter(m -> m.getId() == unsignedId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Operation mode ID not found: " + modeId));
        return reportingPeriodsPerOperationMode.get(mode);
    }

    @Override
    public int getFullSize() {
        return Byte.BYTES + reportingPeriodsPerOperationMode.size() * (Byte.BYTES + Short.BYTES);
    }

    public static int getMinSize() {
        return Byte.BYTES + (Byte.BYTES + Short.BYTES);
    }
}
