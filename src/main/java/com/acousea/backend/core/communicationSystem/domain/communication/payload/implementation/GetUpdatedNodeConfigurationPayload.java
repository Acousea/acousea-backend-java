package com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation;

import com.acousea.backend.core.communicationSystem.application.command.DTO.GetUpdatedNodeDeviceConfigurationDTO;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationPacket;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetUpdatedNodeConfigurationPayload implements Payload {
    private List<ModuleCode> moduleCodes;

    public GetUpdatedNodeConfigurationPayload(List<ModuleCode> moduleCodes) {
        this.moduleCodes = moduleCodes;
    }

    public static Payload fromDTO(GetUpdatedNodeDeviceConfigurationDTO dto) {
        List<ModuleCode> moduleCodes = new ArrayList<>();
        dto.getRequestedModules().forEach(module -> {
            try {
                moduleCodes.add(ModuleCode.fromModuleName(module));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid tag type: " + e.getMessage());
            }
        });
        return new GetUpdatedNodeConfigurationPayload(moduleCodes);
    }

    @Override
    public short getBytesSize() {
        int size = moduleCodes.size() * ModuleCode.getAmountOfBytes();
        // Check if size fits in short
        if (size > CommunicationPacket.MaxSizes.MAX_PAYLOAD_SIZE) {
            throw new IllegalArgumentException(GetUpdatedNodeConfigurationPayload.class.getSimpleName() + "Payload size is too big: " + size + " bytes");
        }
        return (short) size;
    }

    @Override
    public byte[] toBytes() {
        byte[] bytes = new byte[moduleCodes.size()];
        for (int i = 0; i < moduleCodes.size(); i++) {
            bytes[i] = (byte) moduleCodes.get(i).getValue();
        }
        return bytes;
    }

    public static Payload fromBytes(ByteBuffer buffer) throws InvalidPacketException {
        List<ModuleCode> moduleCodes = new ArrayList<>();
        while (buffer.hasRemaining()) {
            moduleCodes.add(ModuleCode.fromValue(buffer.get()));
        }
        return new GetUpdatedNodeConfigurationPayload(moduleCodes);
    }
}
