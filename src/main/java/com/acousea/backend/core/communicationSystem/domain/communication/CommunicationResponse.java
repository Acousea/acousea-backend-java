package com.acousea.backend.core.communicationSystem.domain.communication;

import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RoutingChunk;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HexFormat;

public class CommunicationResponse extends CommunicationPacket {
    public CommunicationResponse(OperationCode operationCode, RoutingChunk routingChunk, Payload payload, short checksum) {
        super(operationCode, routingChunk, payload, checksum);
    }

    public static CommunicationResponse fromRockBlockMessage(RockBlockMessage rockBlockMessage) throws InvalidPacketException {
        ByteBuffer responseData = ByteBuffer.wrap(HexFormat.of().parseHex(rockBlockMessage.getData()));
        CommunicationPacket packet = CommunicationPacket.fromBytes(responseData);
        return new CommunicationResponse(
                packet.getOperationCode(),
                packet.getRoutingChunk(),
                packet.getPayload(),
                packet.getChecksum()
        );
    }

}
