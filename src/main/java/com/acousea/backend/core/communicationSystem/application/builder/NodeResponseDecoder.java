package com.acousea.backend.core.communicationSystem.application.builder;


import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResponse;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NodeResponseDecoder {

    private static final Map<OperationCode, Function<CommunicationResponse, ?>> decoders = new HashMap<>();

    static {
//        decoders.put(OperationCode.CHANGE_OP_MODE, LocationModule::new);
//        decoders.put(OperationCode.SUMMARY_REPORT, BatteryModule::new);
        // Agrega otros decodificadores según sea necesario
    }

    public static <T> T decode(CommunicationResponse response, Class<T> targetType) {
        Function<CommunicationResponse, ?> decoder = decoders.get(response.getOperationCode());

        if (decoder == null) {
            throw new UnsupportedOperationException("Operation code not supported: " + response.getOperationCode());
        }

        Object decodedObject = decoder.apply(response);

        if (!targetType.isInstance(decodedObject)) {
            throw new IllegalArgumentException("Decoded object is not of the expected type: " + targetType.getName());
        }

        return targetType.cast(decodedObject);
    }
}
