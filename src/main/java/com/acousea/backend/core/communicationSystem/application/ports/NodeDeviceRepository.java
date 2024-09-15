package com.acousea.backend.core.communicationSystem.application.ports;

import com.acousea.backend.core.communicationSystem.domain.NodeDevice;
import com.acousea.backend.core.communicationSystem.drifter.domain.DrifterDeviceInfo;
import org.springframework.stereotype.Repository;


@Repository
public interface DrifterDeviceRepository {
    NodeDevice getDrifterDeviceInfo();
}
