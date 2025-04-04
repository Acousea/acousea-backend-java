package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods;

import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;
import com.acousea.backend.core.shared.domain.UnsignedByte;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ReportingModule extends SerializableModule implements ExtModule {
    private final byte technologyId; // ID de la tecnología
    private final Map<OperationMode, Short> reportingPeriodsPerOperationMode = new HashMap<>();

    protected ReportingModule(byte technologyId, Map<OperationMode, Short> reportingPeriods) {
        super(ModuleCode.REPORTING, serialize(technologyId, reportingPeriods));
        this.technologyId = technologyId;
        this.reportingPeriodsPerOperationMode.putAll(reportingPeriods);
    }

    private static byte[] serialize(
            byte technologyId,
            Map<OperationMode, Short> reportingPeriods
    ) {
        int size = Byte.BYTES + reportingPeriods.size() * (Byte.BYTES + Short.BYTES);
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.put(technologyId);

        reportingPeriods.forEach((mode, period) -> {
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

    @Override
    public int getFullSize() {
        return Byte.BYTES + reportingPeriodsPerOperationMode.size() * (Byte.BYTES + Short.BYTES);
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
