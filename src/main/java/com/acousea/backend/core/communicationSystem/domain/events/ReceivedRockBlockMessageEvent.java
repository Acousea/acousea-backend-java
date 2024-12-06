package com.acousea.backend.core.communicationSystem.domain.events;

import com.acousea.backend.core.shared.domain.events.DomainEvent;

public class ReceivedRockBlockMessageEvent extends DomainEvent<Void> {
    public ReceivedRockBlockMessageEvent() {
        super(ReceivedRockBlockMessageEvent.class.getSimpleName(), null);
    }

}
