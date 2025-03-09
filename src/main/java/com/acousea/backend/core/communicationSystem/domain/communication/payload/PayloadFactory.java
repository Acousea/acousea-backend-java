package com.acousea.backend.core.communicationSystem.domain.communication.payload;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.*;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class PayloadFactory {

    @FunctionalInterface
    public interface PayloadBuilder {
        Payload build(ByteBuffer payloadBytes) throws InvalidPacketException;
    }

    private static final Map<OperationCode, PayloadBuilder> fromBytesPayloadBuilders =
            new HashMap<>() {{
                put(OperationCode.SET_NODE_DEVICE_CONFIG, NewNodeConfigurationPayload::fromBytes);
                put(OperationCode.GET_UPDATED_NODE_DEVICE_CONFIG, GetUpdatedNodeConfigurationPayload::fromBytes);
                put(OperationCode.BASIC_STATUS_REPORT, BasicStatusReportPayload::fromBytes);
                put(OperationCode.COMPLETE_STATUS_REPORT, CompleteStatusReportPayload::fromBytes);
                put(OperationCode.DEFAULT_WITH_EMPTY_PAYLOAD, EmptyPayload::fromBytes);
            }};

    // Método principal para obtener el payload decodificado desde ByteBuffer
    public static Payload from(OperationCode operationCode, ByteBuffer payloadBytes) throws InvalidPacketException {
        PayloadBuilder decoder = fromBytesPayloadBuilders.get(operationCode);
        if (decoder == null) {
            throw new IllegalArgumentException(PayloadFactory.class.getName() + ": No decoder registered for operation code: " + operationCode);
        }
        return decoder.build(payloadBytes);
    }

}

