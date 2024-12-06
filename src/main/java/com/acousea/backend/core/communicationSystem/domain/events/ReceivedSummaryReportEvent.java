package com.acousea.backend.core.communicationSystem.domain.events;

import com.acousea.backend.core.shared.domain.events.DomainEvent;

public class ReceivedSummaryReportEvent extends DomainEvent<ReceivedSummaryReportEvent.Payload> {
    public ReceivedSummaryReportEvent(Payload payload) {
        super(ReceivedSummaryReportEvent.class.getSimpleName(), payload);
    }

    public static record Payload(String nodeName) {
    }
}
