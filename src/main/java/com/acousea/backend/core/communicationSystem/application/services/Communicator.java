package com.acousea.backend.core.communicationSystem.application.services;

import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResult;

import java.util.UUID;

public interface Communicator {
    CommunicationResult send(CommunicationRequest packet);
    CommunicationResult flushRequestQueue(UUID nodeId);
}
