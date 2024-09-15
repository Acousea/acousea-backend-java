package com.acousea.backend.core.shared.domain.events;

public class UpdateNodeOperationModeEvent extends DomainEvent<UpdateNodeOperationModeEvent.Payload> {
    public UpdateNodeOperationModeEvent(Payload payload) {
        super("UpdateNodeOperationModeEvent", payload);
    }

    public static record Payload(String nodeId, String operationMode) {
    }
}
