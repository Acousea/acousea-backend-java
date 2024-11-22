package com.acousea.backend.core.communicationSystem.application.services;

import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import org.springframework.stereotype.Service;

@Service
public interface CommunicationRequestLoggerService {
    void log(CommunicationRequest packet);
}
