package com.acousea.backend.core.communicationSystem.infrastructure.events;

import com.acousea.backend.core.shared.domain.events.DomainEvent;

public class UpdateNodeOperationModeEvent extends DomainEvent<UpdateNodeOperationModeEvent.Payload> {
    public UpdateNodeOperationModeEvent(Payload payload) {
        super("UpdateNodeOperationModeEvent", payload);
    }

    public static record Payload(String nodeId, String operationMode) {
    }
}
