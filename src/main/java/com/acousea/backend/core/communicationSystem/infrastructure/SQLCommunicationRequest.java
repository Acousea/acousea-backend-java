package com.acousea.backend.core.communicationSystem.infrastructure;

import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.OperationCode;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RoutingChunk;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.PayloadFactory;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "communication_request_history")
public class SQLCommunicationRequest {
    private static final char START_BYTE = 0x20;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "operation_code", nullable = false)
    private byte operationCode;

    @Column(name = "sender_address", nullable = false)
    private int senderAddress;

    @Column(name = "receiver_address", nullable = false)
    private int receiverAddress;

    @Column(name = "ttl", nullable = false)
    private int ttl;

    @Column(name = "payload", nullable = false)
    private byte[] payload;

    @Column(name = "status", nullable = false)
    private int status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructors, getters, setters, and other methods

    public SQLCommunicationRequest() {
    }

    public SQLCommunicationRequest(OperationCode operationCode,
                                   Address senderAddress,
                                   Address receiverAddress,
                                   byte ttl,
                                   byte[] payload) {
        this.operationCode = (byte) operationCode.getValue();
        this.senderAddress = senderAddress.getValue();
        this.receiverAddress = receiverAddress.getValue();
        this.ttl = ttl;
        this.payload = payload != null ? payload : new byte[0];
    }


    public static SQLCommunicationRequest fromDomain(CommunicationRequest communicationRequest) {
        SQLCommunicationRequest sqlRequest = new SQLCommunicationRequest(
                communicationRequest.getOperationCode(),
                communicationRequest.getRoutingChunk().sender(),
                communicationRequest.getRoutingChunk().receiver(),
                communicationRequest.getRoutingChunk().TTL(),
                communicationRequest.getPayload().toBytes()
        );
        sqlRequest.setCreatedAt(communicationRequest.getCreatedAt());
        return sqlRequest;
    }

    public CommunicationRequest toDomain() {
        try {
            return new CommunicationRequest(
                    OperationCode.valueOf(OperationCode.class, String.valueOf(operationCode)),
                    new RoutingChunk(
                            Address.of(senderAddress),
                            Address.of(receiverAddress),
                            (byte) ttl
                    ),
                    PayloadFactory.from(OperationCode.fromValue(this.operationCode), ByteBuffer.wrap(this.payload)),
                    this.createdAt
            );
        } catch (InvalidPacketException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "SQLCommunicationRequest{" +
                "id=" + id +
                ", operationCode=" + operationCode +
                ", senderAddress=" + senderAddress +
                ", receiverAddress=" + receiverAddress +
                ", routingProtocol=" + ttl +
                ", payload=" + Arrays.toString(payload) +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLCommunicationRequest that = (SQLCommunicationRequest) o;
        return operationCode == that.operationCode &&
                senderAddress == that.senderAddress &&
                receiverAddress == that.receiverAddress &&
                ttl == that.ttl &&
                Arrays.equals(payload, that.payload) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, operationCode, senderAddress, receiverAddress, ttl) + Arrays.hashCode(payload);
    }
}