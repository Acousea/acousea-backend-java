package com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation;

import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationPacket;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.communication.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.communication.serialization.SerializableModuleFactory;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class NewNodeConfigurationPayload implements Payload {
    private List<SerializableModule> serializableModules;

    public NewNodeConfigurationPayload(List<SerializableModule> serializableModules) {
        this.serializableModules = serializableModules;
    }

    public static NewNodeConfigurationPayload fromNodeDevice(@NotNull NodeDevice nodeDevice) {
        List<SerializableModule> serializableModules = nodeDevice.getExtModules().values().stream()
                .filter(SerializableModule.class::isInstance)
                .map(SerializableModule.class::cast)
                .collect(Collectors.toList());
        return new NewNodeConfigurationPayload(serializableModules);
    }

    @Override
    public short getBytesSize() {
        int size = serializableModules.stream().mapToInt(SerializableModule::getFullLength).sum();
        if (size > CommunicationPacket.MaxSizes.MAX_PAYLOAD_SIZE) {
            throw new IllegalArgumentException(NewNodeConfigurationPayload.class.getSimpleName() + "Payload size is too big");
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
        return new NewNodeConfigurationPayload(serializableModules);
    }
}
