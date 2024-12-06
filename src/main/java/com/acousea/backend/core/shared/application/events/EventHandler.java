package com.acousea.backend.core.shared.application.events;

import com.acousea.backend.core.shared.domain.events.DomainEvent;

@FunctionalInterface
public interface EventHandler<T> {
    void handle(DomainEvent<T> event);
}