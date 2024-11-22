package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class PamModule {
    protected String serialNumber;
    private String name;

    public PamModule(String serialNumber, String name) {
        this.serialNumber = serialNumber;
        this.name = name;
    }
}
