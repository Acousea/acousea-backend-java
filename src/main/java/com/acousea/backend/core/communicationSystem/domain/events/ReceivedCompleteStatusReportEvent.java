package com.acousea.backend.core.communicationSystem.domain.events;

import com.acousea.backend.core.shared.domain.events.DomainEvent;

public class ReceivedCompleteStatusReportEvent extends DomainEvent<ReceivedCompleteStatusReportEvent.Payload> {
    public ReceivedCompleteStatusReportEvent(Payload payload) {
        super(ReceivedCompleteStatusReportEvent.class.getSimpleName(), payload);
    }

    public static record Payload(String nodeName) {
    }
}
