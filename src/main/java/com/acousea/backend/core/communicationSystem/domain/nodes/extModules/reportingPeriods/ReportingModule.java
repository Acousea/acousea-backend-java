package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;


import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.shared.domain.UnsignedByte;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@Getter
public abstract class ReportingModule extends SerializableModule {
    private final byte technologyId; // ID de la tecnología
    private final Map<OperationMode, Short> reportingPeriodsPerOperationMode = new TreeMap<>(Comparator.comparingInt(OperationMode::getId));

    protected ReportingModule(byte technologyId, Map<OperationMode, Short> reportingPeriods) {
        super(ModuleCode.REPORTING);
        this.technologyId = technologyId;
        this.reportingPeriodsPerOperationMode.putAll(reportingPeriods);
    }

    @Override
    public byte[] getVALUE() {
        // Size: 1 byte for tech Id + number of operationModes * (1 byte for mode ID + 2 bytes for reporting period) (3 bytes per mode)
        int size = Byte.BYTES + reportingPeriodsPerOperationMode.size() * (Byte.BYTES + Short.BYTES);
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.put(technologyId);

        reportingPeriodsPerOperationMode.forEach((mode, period) -> {
            buffer.put(UnsignedByte.toByte(mode.getId()));
            buffer.putShort(period);
        });

        return buffer.array();
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

    public static ReportingModule fromBytes(ByteBuffer buffer) {
        byte techId = buffer.get();
        ReportingModule module = createEmptyModule(techId);

        while (buffer.hasRemaining()) {
            byte modeId = buffer.get();
            short period = buffer.getShort();
            module.setReportingPeriod(OperationMode.create(modeId, "no_name"), period);
        }
        return module;
    }

    private static ReportingModule createEmptyModule(byte technologyId) {
        return switch (technologyId) {
            case IridiumReportingModule.TECHNOLOGY_ID -> IridiumReportingModule.createEmpty();
            case LoRaReportingModule.TECHNOLOGY_ID -> LoRaReportingModule.createEmpty();
            default -> throw new IllegalArgumentException("Unknown technology ID: " + technologyId);
        };
    }


    public static int getMinSize() {
        return Byte.BYTES + (Byte.BYTES + Short.BYTES);
    }

    @Override
    public String toString() {
        return "ReportingModule{" +
                "technologyId=" + technologyId +
                ", reportingPeriodsPerOperationMode=" + reportingPeriodsPerOperationMode +
                '}';
    }
}
