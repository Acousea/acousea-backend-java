package com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation;

import com.acousea.backend.core.communicationSystem.domain.communication.tags.Tag;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagType;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.ReportingModule;
import lombok.Getter;

import java.nio.ByteBuffer;


@Getter
public class ReportingPeriodTag extends Tag {

    private ReportingPeriodTag(byte[] value) {
        super(TagType.REPORTING, value);
    }

    public static ReportingPeriodTag fromReportingModule(ReportingModule module) {
        ByteBuffer buffer = ByteBuffer.allocate(module.getFullSize());
        byte technologyId = module.getTechnologyId();
        buffer.put(technologyId);

        module.getReportingPeriodsPerOperationMode()
                .forEach((modeId, period) -> {
                    buffer.put(modeId.getId());
                    buffer.putShort(period);
                });

        return new ReportingPeriodTag(buffer.array());
    }

    public ReportingModule toReportingModule() {
        ByteBuffer buffer = ByteBuffer.wrap(this.VALUE);
        ReportingModule module = switch (buffer.get()) { // Get the TECHNOLOGY_ID
            case IridiumReportingModule.TECHNOLOGY_ID -> IridiumReportingModule.createEmpty();
            case LoRaReportingModule.TECHNOLOGY_ID -> LoRaReportingModule.createEmpty();
            default -> throw new IllegalArgumentException(
                    ReportingPeriodTag.class.getName() + " -> Unknown technology id: " + this.VALUE[0]);
        };

        while (buffer.hasRemaining()) {
            byte modeId = buffer.get();
            short period = buffer.getShort();
            module.setReportingPeriod(OperationMode.create(modeId, "no_name"), period);
        }
        return module;
    }

    public static ReportingPeriodTag fromBytes(ByteBuffer buffer) {
        byte technologyId = buffer.get();
        ReportingModule module = switch (technologyId) {
            case IridiumReportingModule.TECHNOLOGY_ID -> IridiumReportingModule.createEmpty();
            case LoRaReportingModule.TECHNOLOGY_ID -> LoRaReportingModule.createEmpty();
            default -> throw new IllegalArgumentException(
                    ReportingPeriodTag.class.getName() + " -> Unknown technology id: " + technologyId);
        };

        while (buffer.hasRemaining()) {
            byte modeId = buffer.get();
            short period = buffer.getShort();
            module.setReportingPeriod(OperationMode.create(modeId, "no_name"), period);
        }

        return fromReportingModule(module);
    }


}
