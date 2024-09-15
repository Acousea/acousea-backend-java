package com.acousea.backend.core.communicationSystem.domain.communication;

import com.acousea.backend.core.communicationSystem.domain.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.constants.RoutingChunk;

public class ResponsePacket extends CommunicationPacket {
    public ResponsePacket(OperationCode operationCode, RoutingChunk routingChunk, byte[] payload) {
        super(operationCode, routingChunk, payload);
    }
}