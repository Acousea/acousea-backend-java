package com.acousea.backend.core.shared.domain.events;

public class DomainEvent<T> {
    private final String name;
    private final T payload;

    public DomainEvent(String name, T payload) {
        this.name = name;
        this.payload = payload;
    }

    public String getName() {
        return name;
    }

    public T getPayload() {
        return payload;
    }
}
