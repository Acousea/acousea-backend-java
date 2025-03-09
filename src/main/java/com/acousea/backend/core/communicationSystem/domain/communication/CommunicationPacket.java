package com.acousea.backend.core.communicationSystem.domain.communication;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RoutingChunk;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.PayloadFactory;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import com.acousea.backend.core.shared.domain.UnsignedByte;
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
        // Get the last two bytes of the packetBytes as the CRC (CRC gets calculated in the toBytes method)
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
        ByteBuffer buffer = ByteBuffer.allocate(
                Byte.BYTES + // SYNC
                        OperationCode.getSize() + // OP_CODE
                        RoutingChunk.getSize() + // ROUTING_CHUNK
                        Byte.BYTES + // TOTAL_PAYLOAD_LEN
                        payload.getBytesSize() + // PAYLOAD
                        Short.BYTES
        ); // CRC (2 bytes)
        buffer.put(SYNC_BYTE);
        buffer.put((byte) operationCode.getValue());
        buffer.put(routingChunk.toBytes());
        buffer.put((byte) payload.getBytesSize());
        buffer.put(payload.toBytes());
        // Calcula el CRC y lo agrega al final del buffer
        // Calculate CRC using only the bytes BEFORE the CRC field
        byte[] bytesForCRC = new byte[buffer.position()];
        buffer.rewind(); // Reset position to read
        buffer.get(bytesForCRC); // Read only the relevant part

        short crc = CRCUtils.calculate16BitCRC(bytesForCRC);
        buffer.putShort(crc);
        return buffer.array();
    }

    public static CommunicationPacket fromBytes(ByteBuffer buffer) throws InvalidPacketException {
        short receivedCRC = buffer.getShort(buffer.capacity() - 2);
        if (!CRCUtils.verifyCRC(buffer)) {
            throw new InvalidPacketException(CommunicationPacket.class.getSimpleName() + " -> Invalid CRC");
        }

        // Remove the CRC from the buffer
        buffer = buffer.slice(0, buffer.capacity() - 2);
        byte syncByte = buffer.get();
        if (syncByte != SYNC_BYTE) {
            throw new IllegalArgumentException("Invalid sync byte");
        }
        OperationCode operationCode = OperationCode.fromValue(buffer.get());
        RoutingChunk routingChunk = RoutingChunk.fromBytes(buffer);

        int payloadLength = UnsignedByte.toUnsignedInt(buffer.get());
        if (buffer.remaining() != payloadLength) {
            throw new InvalidPacketException("Invalid payload length, Expected: " + payloadLength + " bytes, Received: " + buffer.remaining() + " bytes");
        }

        // Get the remainder of the buffer as the payload
        Payload payload = PayloadFactory.from(operationCode, buffer);

        return new CommunicationPacket(operationCode, routingChunk, payload, receivedCRC);
    }

    public String encode() {
        return HexFormat.of().formatHex(this.toBytes());
    }
}

