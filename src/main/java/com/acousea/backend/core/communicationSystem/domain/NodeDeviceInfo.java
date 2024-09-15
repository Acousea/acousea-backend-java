package com.acousea.backend.core.communicationSystem.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record NodeDevice(
        UUID id,
        String name,
        NodeOperationMode operationMode,
        LocalDateTime epochTime,
        List<Module> modules,
        LocalDateTime timestamp
) {

}

