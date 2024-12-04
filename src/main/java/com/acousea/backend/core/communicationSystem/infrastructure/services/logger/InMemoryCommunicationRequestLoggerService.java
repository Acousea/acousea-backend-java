package com.acousea.backend.core.communicationSystem.infrastructure.services.logger;

import com.acousea.backend.core.communicationSystem.application.ports.CommunicationRequestHistoryRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationRequestLoggerService;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import org.springframework.stereotype.Service;

@Service
public class InMemoryCommunicationRequestLoggerService implements CommunicationRequestLoggerService {

    private final CommunicationRequestHistoryRepository communicationRequestHistoryRepository;


    public InMemoryCommunicationRequestLoggerService(CommunicationRequestHistoryRepository communicationRequestHistoryRepository) {
        this.communicationRequestHistoryRepository = communicationRequestHistoryRepository;
    }


    @Override
    public void log(CommunicationRequest packet) {
        communicationRequestHistoryRepository.save(packet);
    }
}
