package com.acousea.backend.core.communicationSystem.domain;

import com.acousea.backend.core.communicationSystem.domain.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.constants.RoutingChunk;

import java.util.Arrays;
import java.util.Objects;

public class CommunicationPacket {
    private static final char START_BYTE = 0x20;
    OperationCode operationCode;
    RoutingChunk routingChunk;
    byte[] payload;

    public CommunicationPacket(OperationCode operationCode, RoutingChunk routingChunk, byte[] payload) {
        this.operationCode = Objects.requireNonNull(operationCode, "Operation code cannot be null");
        this.routingChunk = Objects.requireNonNull(routingChunk, "Routing chunk cannot be null");
        this.payload = payload != null ? Arrays.copyOf(payload, payload.length) : new byte[0];
    }

    public static char getStartByte() {
        return START_BYTE;
    }

    public OperationCode getOperationCode() {
        return operationCode;
    }

    public RoutingChunk getRoutingChunk() {
        return routingChunk;
    }

    public byte[] getPayload() {
        return Arrays.copyOf(payload, payload.length);
    }

    @Override
    public String toString() {
        return "CommunicationPacket{" +
                "START_BYTE=" + START_BYTE +
                ", operationCode=" + operationCode +
                ", routingChunk=" + routingChunk +
                ", payload=" + Arrays.toString(payload) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunicationPacket that = (CommunicationPacket) o;
        return  operationCode == that.operationCode &&
                routingChunk == that.routingChunk &&
                Arrays.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(operationCode, routingChunk) + Arrays.hashCode(payload);
    }
}