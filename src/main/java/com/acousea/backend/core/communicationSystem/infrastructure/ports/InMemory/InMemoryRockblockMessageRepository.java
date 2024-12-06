package com.acousea.backend.core.communicationSystem.infrastructure.ports.InMemory;

import com.acousea.backend.core.communicationSystem.application.ports.RockblockMessageRepository;
import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.infrastructure.mocks.MockRockBlockMessages;
import com.acousea.backend.core.shared.infrastructure.ports.InMemoryIRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Repository
public class InMemoryRockblockMessageRepository extends InMemoryIRepository<RockBlockMessage, UUID> implements RockblockMessageRepository {

    public InMemoryRockblockMessageRepository(MockRockBlockMessages mockRockBlockMessages) {
        super(RockBlockMessage::getId);
        System.out.println("InMemoryRockblockMessageRepository created with");
        mockRockBlockMessages.generate().forEach(this::save);
    }

    @Override
    public List<RockBlockMessage> getPaginatedMessages(int page, int rowsPerPage) {
        int start = (page - 1) * rowsPerPage;
        return storage.values().stream()
                .skip(start)
                .limit(rowsPerPage)
                .collect(Collectors.toList());
    }
}