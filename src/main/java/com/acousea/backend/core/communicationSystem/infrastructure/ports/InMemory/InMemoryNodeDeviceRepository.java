package com.acousea.backend.core.communicationSystem.infrastructure.ports;

import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.domain.NodeDeviceInfo;
import com.acousea.backend.core.communicationSystem.domain.NodeOperationMode;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;


@Repository
public class InMemoryNodeDeviceRepository implements NodeDeviceRepository {
    @Override
    public NodeDeviceInfo getNodeDeviceInfo() {
        return new NodeDeviceInfo(
                UUID.randomUUID(),
                "FirstNode",
                NodeOperationMode.LAUNCHING,
                LocalDateTime.now(),
                new ArrayList<>(0)
        );
    }
}