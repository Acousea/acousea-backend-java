package com.acousea.backend.core.communicationSystem.infrastructure.services;

import com.acousea.backend.core.communicationSystem.application.services.CommunicationService;
import com.acousea.backend.core.communicationSystem.application.services.Communicator;
import com.acousea.backend.core.communicationSystem.domain.CommunicationPacket;

import java.util.HashMap;
import java.util.Map;

public class CommunicatorManagerImpl implements CommunicationService {
    private final Map<String, Communicator> communicators = new HashMap<>();
    private String selectedCommunicator;

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
    public void sendPacket(CommunicationPacket packet) {
        if (selectedCommunicator == null) {
            throw new IllegalStateException("No communicator selected");
        }
        Communicator communicator = communicators.get(selectedCommunicator);
        if (communicator == null) {
            throw new IllegalStateException("Selected communicator is not available: " + selectedCommunicator);
        }
        communicator.send(packet);
    }
}
