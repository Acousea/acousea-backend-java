package com.acousea.backend.core.communicationSystem.application.ports;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.shared.application.ports.IRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface NodeDeviceRepository extends IRepository<NodeDevice, UUID> {
    Optional<NodeDevice> findByNetworkAddress(Address networkAddress);
}
