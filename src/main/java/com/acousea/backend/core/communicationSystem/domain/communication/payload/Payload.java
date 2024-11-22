package com.acousea.backend.core.communicationSystem.domain.communication.payload;


public interface Payload {
    public String encode();

    int getFullLength();

    byte[] toBytes();
}
