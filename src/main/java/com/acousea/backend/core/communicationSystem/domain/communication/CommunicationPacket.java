package com.acousea.backend.core.communicationSystem.domain.communication;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RoutingChunk;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.PayloadFactory;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import com.acousea.backend.core.shared.domain.crc.CRCUtils;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.HexFormat;

/**
 * Packet Structure:
 * <p>
 * | SYNC | OP_CODE | TOTAL_PAYLOAD_LEN | PAYLOAD | CRC |
 * |  1B  |   1B    |        1B         |    N    | 2B  |
 * </p>
 **/

@Getter
public class CommunicationPacket {

    public static class MaxSizes {
        public static final int PACKET_SIZE = 260; // Iridium MO: 340 bytes, Iridium MT: 260 bytes
        public static final int MAX_PAYLOAD_SIZE = PACKET_SIZE - 9; // 7 bytes for the auxiliary fields
    }
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
        // Get the last two bytes of the packetBytes as the CRC
        this.checksum = ByteBuffer.wrap(packetBytes).getShort(packetBytes.length - 2);
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
        ByteBuffer buffer = ByteBuffer.allocate((Byte.BYTES * 7) + payload.getBytesSize() + (Byte.BYTES * 2)); // +2 para el CRC de 16 bits
        buffer.put(SYNC_BYTE);
        buffer.put((byte) operationCode.getValue());
        buffer.put(routingChunk.toBytes());
        buffer.putShort(payload.getBytesSize());
        buffer.put(payload.toBytes());
        // Calcula el CRC y lo agrega al final del buffer
        short crc = CRCUtils.calculate16BitCRC(buffer.array());
        buffer.putShort(crc);
        return buffer.array();
    }

    public static CommunicationPacket fromBytes(byte[] data) throws InvalidPacketException {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        short receivedCRC = buffer.getShort(buffer.capacity() - 2);
        if (!CRCUtils.verifyCRC(buffer)) {
            throw new InvalidPacketException(CommunicationPacket.class.getSimpleName() + " -> Invalid CRC");
        }

        // Remove the CRC from the buffer
        buffer = buffer.slice( 0, buffer.capacity() - 2);
        byte syncByte = buffer.get();
        if (syncByte != SYNC_BYTE) {
            throw new IllegalArgumentException("Invalid sync byte");
        }
        OperationCode operationCode = OperationCode.fromValue(buffer.get());
        RoutingChunk routingChunk = RoutingChunk.fromBytes(buffer);

        int payloadLength = buffer.getShort();
        if (buffer.remaining() != payloadLength) {
            throw new InvalidPacketException("Invalid payload length");
        }

        // Get the remainder of the buffer as the payload
        Payload payload = PayloadFactory.from(operationCode, buffer);

        return new CommunicationPacket(operationCode, routingChunk, payload, receivedCRC);
    }

    public String encode() {
        return HexFormat.of().formatHex(this.toBytes());
    }
}

