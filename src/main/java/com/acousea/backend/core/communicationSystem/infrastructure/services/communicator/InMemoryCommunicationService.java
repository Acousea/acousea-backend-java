package com.acousea.backend.core.communicationSystem.infrastructure.services.communicator;

import com.acousea.backend.core.communicationSystem.application.services.CommunicationService;
import com.acousea.backend.core.communicationSystem.application.services.Communicator;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResponse;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResult;
import com.acousea.backend.core.shared.application.events.EventBus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryCommunicationService implements CommunicationService {
    private final Map<String, Communicator> communicators = new HashMap<>();
    private String selectedCommunicator;
    private EventBus eventBus;


    public InMemoryCommunicationService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void addCommunicator(String name, Communicator communicator) {
        communicators.put(name, communicator);
    }

    @Override
    public void selectCommunicator(String name) {
        if (!communicators.containsKey(name)) {
            throw new IllegalArgumentException("Communicator not found: " + name);
        }
        selectedCommunicator = name;
    }

    @Override
    public CommunicationResult send(CommunicationRequest packet) {
        if (selectedCommunicator == null) {
            throw new IllegalStateException("No communicator selected");
        }
        Communicator communicator = communicators.get(selectedCommunicator);
        if (communicator == null) {
            throw new IllegalStateException("Selected communicator is not available: " + selectedCommunicator);
        }

        return communicator.send(packet);
    }

    @Override
    public void processResponse(CommunicationResponse communicationResponse) {
        // Should use the response builder to process the response


        // Should use the built response to trigger an event. This event will bee
        // listened to by the notification event handler and will be used to send a notification

    }

}
