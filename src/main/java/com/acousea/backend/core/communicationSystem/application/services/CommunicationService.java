package com.acousea.backend.core.communicationSystem.application.services;

import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResponse;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResult;

public interface CommunicationService {
    void addCommunicator(String name, Communicator communicator);
    void selectCommunicator(String name);
    CommunicationResult send(CommunicationRequest packet);
    void processResponse(CommunicationResponse communicationResponse);
}

