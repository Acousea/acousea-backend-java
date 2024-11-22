package com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation;


import com.acousea.backend.core.communicationSystem.domain.communication.tags.Tag;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagType;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;
import com.acousea.backend.core.shared.domain.UnsignedByte;

import java.nio.ByteBuffer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OperationModeTag extends Tag {

    private OperationModeTag(byte[] value) {
        super(TagType.OPERATION_MODES, value);
    }

    public static OperationModeTag fromOperationModeModule(OperationModeModule module) {
        ByteBuffer buffer = ByteBuffer.allocate(module.getOperationModes().size());
        for (OperationMode operationMode : module.getOperationModes().values()) {
            buffer.put(operationMode.getId().toByte());
        }
        return new OperationModeTag(buffer.array());
    }

    public OperationModeModule toOperationModeModule() {
        ByteBuffer buffer = ByteBuffer.wrap(this.VALUE);
        return OperationModeModule.createWithModes(
                IntStream.range(0, buffer.remaining())
                        .mapToObj(i -> OperationMode.create(UnsignedByte.of(buffer.get()), "operationMode" + i))
                        .collect(Collectors.toList())
        );
    }


    public static OperationModeTag fromBytes(ByteBuffer buffer) {
        OperationModeModule operationModeModule = OperationModeModule.createWithModes(
                IntStream.range(0, buffer.remaining())
                        .mapToObj(i -> OperationMode.create(UnsignedByte.of(buffer.get()), "operationMode" + i))
                        .collect(Collectors.toList())
        );
        return fromOperationModeModule(operationModeModule);
    }
}
