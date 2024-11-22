package com.acousea.backend.core.communicationSystem.domain.communication.payload;

import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.GetUpdatedNodeConfigurationPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.SetNodeConfigurationPayload;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PayloadFactory {
    private static final Map<OperationCode, Function<ByteBuffer, Payload>> payloadRegistry = new HashMap<>();

    // Registro estático para asociar cada OperationCode con su lógica de decodificación
    static {
        register(OperationCode.SET_NODE_DEVICE_CONFIG, SetNodeConfigurationPayload::fromBytes);
        register(OperationCode.GET_UPDATED_NODE_DEVICE_CONFIG, GetUpdatedNodeConfigurationPayload::fromBytes);
    }

    // Método para registrar una nueva función de decodificación
    public static void register(OperationCode operationCode, Function<ByteBuffer, Payload> decoder) {
        payloadRegistry.put(operationCode, decoder);
    }

    // Método principal para obtener el payload decodificado
    public static Payload from(OperationCode operationCode, ByteBuffer payloadBytes) {
        Function<ByteBuffer, Payload> decoder = payloadRegistry.get(operationCode);
        if (decoder == null) {
            throw new IllegalArgumentException(PayloadFactory.class.getName() + ": No decoder registered for operation code: " + operationCode);
        }
        return decoder.apply(payloadBytes);
    }

}

