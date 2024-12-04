package com.acousea.backend.core.communicationSystem.domain.communication;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RoutingChunk;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.SetNodeConfigurationPayload;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class CommunicationRequest extends CommunicationPacket {
    private UUID id = UUID.randomUUID();
    private LocalDateTime createdAt = LocalDateTime.now();

    public CommunicationRequest(OperationCode operationCode, RoutingChunk routingChunk, Payload payload, LocalDateTime createdAt) {
        super(operationCode, routingChunk, payload);
        this.createdAt = createdAt;
    }

    public static CommunicationRequest createUpdateNodeDeviceRequest(Address nodeAddress, NodeDevice nodeDevice) {
        return new CommunicationRequest(
                OperationCode.SET_NODE_DEVICE_CONFIG,
                RoutingChunk.fromBackendToNode(nodeAddress),
                SetNodeConfigurationPayload.fromNodeDevice(nodeDevice),
                LocalDateTime.now()
        );
    }


}
