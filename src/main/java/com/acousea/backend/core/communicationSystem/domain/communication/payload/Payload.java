package com.acousea.backend.core.communicationSystem.domain.communication.payload;


public interface Payload {
    short getBytesSize();

    byte[] toBytes();
}
