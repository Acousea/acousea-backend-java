package com.acousea.backend.core.communicationSystem.domain.events;

import com.acousea.backend.core.shared.domain.events.DomainEvent;

public class ReceivedBasicStatusReportEvent extends DomainEvent<ReceivedBasicStatusReportEvent.Payload> {
    public ReceivedBasicStatusReportEvent(Payload payload) {
        super(ReceivedBasicStatusReportEvent.class.getSimpleName(), payload);
    }

    public static record Payload(String nodeName) {
    }
}
