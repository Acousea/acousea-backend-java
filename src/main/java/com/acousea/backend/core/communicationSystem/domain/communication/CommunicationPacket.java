package com.acousea.backend.core.communicationSystem.domain.communication;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RoutingChunk;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.PayloadFactory;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import com.acousea.backend.core.shared.domain.crc.CRCUtils;
import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * Packet Structure:
 * <p>
 * | SYNC | OP_CODE | TOTAL_PAYLOAD_LEN | PAYLOAD | CRC |
 * |  1B  |   1B    |        1B         |    N    | 2B  |
 * </p>
 **/

@Getter
public class CommunicationPacket {
    private static final byte SYNC_BYTE = 0x20; // 1 byte
    private final OperationCode operationCode; // 1 byte
    private final RoutingChunk routingChunk; // 3 bytes (1 bytes sender, 1 byte receiver, 1 byte ttl)
    private final Payload payload; // N bytes
    private final short checksum; // 2 bytes

    public CommunicationPacket(
            OperationCode operationCode,
            RoutingChunk routingChunk,
            Payload payload
    ) {
        this.operationCode = operationCode;
        this.routingChunk = routingChunk;
        this.payload = payload;

        // Calcula el CRC y lo agrega al final del buffer
        byte[] packetBytes = this.toBytes();
        this.checksum = ByteBuffer.wrap(packetBytes, packetBytes.length - 2, 2).getShort();
    }

    public CommunicationPacket(
            OperationCode operationCode,
            RoutingChunk routingChunk,
            Payload payload,
            short checksum) {
        this.operationCode = operationCode;
        this.routingChunk = routingChunk;
        this.payload = payload;
        this.checksum = checksum;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(5 + payload.getFullLength() + 2); // +2 para el CRC de 16 bits
        buffer.put(SYNC_BYTE);
        buffer.put((byte) operationCode.getValue());
        buffer.put((byte) payload.getFullLength());
        buffer.put(payload.toBytes());
        // Calcula el CRC y lo agrega al final del buffer
        int crc = CRCUtils.calculateCRC(buffer.array());
        buffer.putShort((short) crc);

        return buffer.array();
    }

    public static CommunicationPacket fromBytes(byte[] data) throws InvalidPacketException {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        // Check the CRC before parsing the packet
        int calculatedCRC = CRCUtils.calculateCRC(data);
        short receivedCRC = buffer.getShort(buffer.capacity() - 2);

        if (receivedCRC != calculatedCRC) {
            throw new InvalidPacketException("Invalid Packet received -> CRC validation failed");
        }

        // Reset the buffer to the beginning
        buffer.limit(buffer.capacity() - 2);
        byte syncByte = buffer.get();
        if (syncByte != SYNC_BYTE) {
            throw new IllegalArgumentException("Invalid sync byte");
        }
        OperationCode operationCode = OperationCode.fromValue(buffer.get());
        RoutingChunk routingChunk = RoutingChunk.fromBytes(buffer);

        int payloadLength = buffer.get();
        if (buffer.remaining() != payloadLength) {
            throw new InvalidPacketException("Invalid payload length");
        }

        // Get the remainder of the buffer as the payload
        Payload payload = PayloadFactory.from(operationCode, buffer);

        return new CommunicationPacket(operationCode, routingChunk, payload, receivedCRC);
    }

    public String encode() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02x", SYNC_BYTE));
        sb.append(String.format("%02x", (int) (operationCode.getValue())));
        sb.append(routingChunk.encode());
        sb.append(payload.encode());
        sb.append(String.format("%04x", checksum));
        return sb.toString();


    }
}

