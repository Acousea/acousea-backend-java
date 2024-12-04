package com.acousea.backend.core.communicationSystem.application.services;

import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;


public interface CommunicationRequestLoggerService {
    void log(CommunicationRequest packet);
}
