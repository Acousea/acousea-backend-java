package com.acousea.backend.core.communicationSystem.domain.mother;

import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationPacket;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RoutingChunk;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.PayloadFactory;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.BasicStatusReportPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.CompleteStatusReportPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.GetUpdatedNodeConfigurationPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.NewNodeConfigurationPayload;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;

import java.nio.ByteBuffer;

public class CommunicationPacketMother {

    /**
     * Creates a packet with a given operation code, routing chunk, and payload.
     *
     * @param operationCode The operation code of the packet.
     * @param routingChunk  The routing information of the packet.
     * @param payload       The payload to include in the packet.
     * @return A new CommunicationPacket instance.
     */
    public static CommunicationPacket create(OperationCode operationCode, RoutingChunk routingChunk, Payload payload) {
        return new CommunicationPacket(operationCode, routingChunk, payload);
    }

    /**
     * Creates a packet from raw bytes, useful for testing parsing.
     *
     * @param bytes The raw byte array of the packet.
     * @return A CommunicationPacket parsed from the bytes.
     * @throws InvalidPacketException If the packet is invalid.
     */
    public static CommunicationPacket fromBytes(byte[] bytes) throws InvalidPacketException {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return CommunicationPacket.fromBytes(buffer);
    }

    /**
     * Creates a basic status report packet with default parameters.
     *
     * @return A CommunicationPacket for BASIC_STATUS_REPORT.
     */
    public static CommunicationPacket createBasicStatusReport(
            RoutingChunk routingChunk,
            BasicStatusReportPayload payload) {
        return create(
                OperationCode.BASIC_STATUS_REPORT,
                routingChunk,
                payload
        );
    }

    public static CommunicationPacket createBasicStatusReport(BasicStatusReportPayload payload) {
        return create(
                OperationCode.BASIC_STATUS_REPORT,
                new RoutingChunk(Address.getBackend(), Address.getBroadcastAddress()),
                payload
        );
    }

    /**
     * Creates a complete status report packet with default parameters.
     *
     * @return A CommunicationPacket for COMPLETE_STATUS_REPORT.
     */
    public static CommunicationPacket createCompleteStatusReport(CompleteStatusReportPayload payload) {
        return create(
                OperationCode.COMPLETE_STATUS_REPORT,
                new RoutingChunk(Address.getBackend(), Address.getBroadcastAddress()),
                payload
        );
    }

    public static CommunicationPacket createCompleteStatusReport(
            CompleteStatusReportPayload payload,
            RoutingChunk routingChunk
    ) {
        return create(
                OperationCode.COMPLETE_STATUS_REPORT,
                routingChunk,
                payload
        );
    }

    /**
     * Creates a packet for setting a node device configuration.
     *
     * @param routingChunk The routing chunk to use.
     * @param payload      The payload containing the configuration data.
     * @return A CommunicationPacket for setting node device configuration.
     */
    public static CommunicationPacket createSetNodeDeviceConfig(
            RoutingChunk routingChunk,
            NewNodeConfigurationPayload payload) {
        return create(OperationCode.SET_NODE_DEVICE_CONFIG, routingChunk, payload);
    }

    public static CommunicationPacket createSetNodeDeviceConfig(
            NewNodeConfigurationPayload payload) {
        return create(
                OperationCode.SET_NODE_DEVICE_CONFIG,
                new RoutingChunk(Address.getBackend(), Address.getBroadcastAddress()),
                payload);
    }

    /**
     * Creates a packet for getting updated node device configuration.
     *
     * @param routingChunk The routing chunk to use.
     * @return A CommunicationPacket for getting node device configuration.
     */
    public static CommunicationPacket createGetUpdatedNodeDeviceConfig(
            RoutingChunk routingChunk,
            GetUpdatedNodeConfigurationPayload payload
    ) {
        return create(OperationCode.GET_UPDATED_NODE_DEVICE_CONFIG, routingChunk, payload);
    }

    public static CommunicationPacket createGetUpdatedNodeDeviceConfig(
            GetUpdatedNodeConfigurationPayload payload
    ) {
        return create(
                OperationCode.GET_UPDATED_NODE_DEVICE_CONFIG,
                new RoutingChunk(Address.getBackend(), Address.getBroadcastAddress()),
                payload);
    }
}
