package com.acousea.backend.core.communicationSystem.application.ports;

import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.shared.application.ports.IRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface RockblockMessageRepository extends IRepository<RockBlockMessage, UUID> {
}
