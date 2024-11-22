package com.acousea.backend.core.communicationSystem.infrastructure.ports.InMemory;

import com.acousea.backend.core.communicationSystem.application.ports.RockblockMessageRepository;
import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.shared.infrastructure.ports.InMemoryIRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public class InMemoryRockblockMessageRepository extends InMemoryIRepository<RockBlockMessage, UUID> implements RockblockMessageRepository {

    public InMemoryRockblockMessageRepository() {
        super(RockBlockMessage::getId);
        System.out.println("InMemoryRockblockMessageRepository created with");
    }

}