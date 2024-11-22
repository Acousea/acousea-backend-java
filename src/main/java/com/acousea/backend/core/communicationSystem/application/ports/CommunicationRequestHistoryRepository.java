package com.acousea.backend.core.communicationSystem.application.ports;


import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.shared.application.ports.IRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommunicationRequestHistoryRepository extends IRepository<CommunicationRequest, UUID> {
    CommunicationRequest getLatestResolvedCommunicationRequestForOpCode(int opCode);
    CommunicationRequest getLatestUnresolvedCommunicationRequestForOpCode(int opCode);
    void resolveCommunicationRequest(char opCode, int recipient);
    CommunicationRequest getLatestResolvedCommunicationRequest();
    CommunicationRequest getLatestUnresolvedCommunicationRequest();
    void flushAllUnresolvedRequests();
}
