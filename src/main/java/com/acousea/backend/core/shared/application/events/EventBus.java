package com.acousea.backend.core.shared.application;

import com.acousea.backend.core.shared.domain.events.DomainEvent;

public interface EventBus {
    <T> void publish(DomainEvent<T> event);
    <T> void subscribe(Class<? extends DomainEvent<T>> eventType, EventHandler<T> handler);
}