package com.acousea.backend.core.shared.infrastructure.events;

import com.acousea.backend.core.shared.application.events.EventBus;
import com.acousea.backend.core.shared.application.events.EventHandler;
import com.acousea.backend.core.shared.domain.events.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryEventBus implements EventBus {
    private final Map<Class<? extends DomainEvent<?>>, List<EventHandler<?>>> handlers = new HashMap<>();

    @Override
    public <T> void publish(DomainEvent<T> event) {
        List<EventHandler<?>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (EventHandler<?> handler : eventHandlers) {
                ((EventHandler<T>) handler).handle(event);
            }
        }
    }

    @Override
    public <T> void subscribe(Class<? extends DomainEvent<T>> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }
}
