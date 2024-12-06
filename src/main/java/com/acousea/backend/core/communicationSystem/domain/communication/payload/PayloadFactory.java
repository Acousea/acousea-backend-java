package com.acousea.backend.core.communicationSystem.domain.communication.payload;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.GetUpdatedNodeConfigurationPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.NewNodeConfigurationPayload;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PayloadFactory {
    private static final Map<OperationCode, Function<ByteBuffer, ? extends Payload>> fromBytesPayloadBuilders =
            new HashMap<>() {{
                put(OperationCode.SET_NODE_DEVICE_CONFIG, NewNodeConfigurationPayload::fromBytes);
                put(OperationCode.GET_UPDATED_NODE_DEVICE_CONFIG, GetUpdatedNodeConfigurationPayload::fromBytes);
            }};

    // Método principal para obtener el payload decodificado desde ByteBuffer
    public static Payload from(OperationCode operationCode, ByteBuffer payloadBytes) {
        Function<ByteBuffer, ? extends Payload> decoder = fromBytesPayloadBuilders.get(operationCode);
        if (decoder == null) {
            throw new IllegalArgumentException(PayloadFactory.class.getName() + ": No decoder registered for operation code: " + operationCode);
        }
        return decoder.apply(payloadBytes);
    }

}

