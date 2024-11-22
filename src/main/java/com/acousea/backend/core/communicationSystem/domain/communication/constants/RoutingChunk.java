package com.acousea.backend.core.communicationSystem.domain.communication.constants;

import java.nio.ByteBuffer;
import java.util.Objects;

public final class RoutingChunk {
    private final Address sender;
    private final Address receiver;
    private final byte TTL;


    public RoutingChunk(Address sender, Address receiver) {
        this.sender = Objects.requireNonNull(sender, "Sender address cannot be null");
        this.receiver = Objects.requireNonNull(receiver, "Receiver address cannot be null");
        this.TTL = 3;
    }

    public RoutingChunk(Address sender, Address receiver, byte TTL) {
        this.sender = Objects.requireNonNull(sender, "Sender address cannot be null");
        this.receiver = Objects.requireNonNull(receiver, "Receiver address cannot be null");
        this.TTL = TTL;
    }

    public static int getSize() {
        return Address.getSize() * 2 + Byte.BYTES; // 1 byte for TTL
    }

    public static RoutingChunk fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < getSize()) {
            throw new IllegalArgumentException("Buffer does not have enough bytes to create a RoutingChunk");
        }
        return new RoutingChunk(Address.of(buffer.get()), Address.of(buffer.get()), buffer.get());
    }

    public static RoutingChunk fromBackendToNode(Address receiver) {
        return new RoutingChunk(Address.getBackend(), receiver);
    }


    @Override
    public String toString() {
        return "RoutingChunk{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", TTL=" + TTL +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutingChunk that = (RoutingChunk) o;
        return sender.equals(that.sender) &&
                receiver.equals(that.receiver) &&
                TTL == that.TTL;
    }

    public String encode() {
        return Integer.toHexString(sender.getValue().toInt())
                + Integer.toHexString(receiver.getValue().toInt())
                + Integer.toHexString(TTL);
    }

    public Address sender() {
        return sender;
    }

    public Address receiver() {
        return receiver;
    }

    public byte TTL() {
        return TTL;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, TTL);
    }

}
