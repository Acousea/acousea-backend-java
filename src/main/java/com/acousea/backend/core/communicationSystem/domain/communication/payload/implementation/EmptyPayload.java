package com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation;


import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;

import java.nio.ByteBuffer;

public class EmptyPayload implements Payload {
    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public short getBytesSize() {
        return 0;
    }

    public static EmptyPayload fromBytes(ByteBuffer buffer) {
        return new EmptyPayload();
    }
}
