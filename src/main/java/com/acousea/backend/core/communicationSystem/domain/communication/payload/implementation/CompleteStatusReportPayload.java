package com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation;

import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationPacket;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModuleFactory;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.util.List;

@Getter
@Setter
public class CompleteStatusReportPayload implements Payload {
    private List<SerializableModule> serializableModules;

    public CompleteStatusReportPayload(List<SerializableModule> serializableModules) {
        this.serializableModules = serializableModules;
    }

    @Override
    public short getBytesSize() {
        int size = serializableModules.stream().mapToInt(SerializableModule::getFullLength).sum();
        if (size > CommunicationPacket.MaxSizes.MAX_PAYLOAD_SIZE)  {
            throw new IllegalArgumentException(CompleteStatusReportPayload.class.getSimpleName() + "Payload size is too big: " + size + " bytes");
        }
        return (short) size;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getBytesSize());
        serializableModules.forEach(tag -> buffer.put(tag.toBytes()));
        return buffer.array();
    }

    public static Payload fromBytes(ByteBuffer buffer) {
        List<SerializableModule> serializableModules = SerializableModuleFactory.createModules(buffer);
        serializableModules.addAll(serializableModules);
        return new CompleteStatusReportPayload(serializableModules);
    }
}
