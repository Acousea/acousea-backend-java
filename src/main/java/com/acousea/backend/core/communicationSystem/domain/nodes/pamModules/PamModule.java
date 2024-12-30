package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules;

public interface PamModule {
    String getSerialNumber();

    void setSerialNumber(String serialNumber);

    String getName();

    void setName(String name);
}
