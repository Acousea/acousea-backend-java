package com.acousea.backend.core.communicationSystem.domain.events;

import com.acousea.backend.core.shared.domain.events.DomainEvent;

public class UpdatedNodeConfigurationEvent extends DomainEvent<UpdatedNodeConfigurationEvent.Payload> {
    public UpdatedNodeConfigurationEvent(Payload payload) {
        super(UpdatedNodeConfigurationEvent.class.getSimpleName(), payload);
    }

    public static record Payload(String nodeName) {

    }
}
