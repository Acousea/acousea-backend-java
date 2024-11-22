package com.acousea.backend.core.communicationSystem.infrastructure.ports.SQL;

import com.acousea.backend.core.communicationSystem.application.ports.CommunicationRequestHistoryRepository;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.RequestStatus;
import com.acousea.backend.core.communicationSystem.infrastructure.SQLCommunicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class SQLCommunicationRequestHistoryRepository implements CommunicationRequestHistoryRepository {

    private final JPACommunicationRequestRepository repository;

    @Autowired
    public SQLCommunicationRequestHistoryRepository(JPACommunicationRequestRepository repository) {
        this.repository = repository;
    }


    @Override
    public CommunicationRequest getLatestResolvedCommunicationRequestForOpCode(int opCode) {
        return repository.findByOperationCodeAndStatus(opCode, RequestStatus.SUCCESS.getValue()).stream()
                .max((req1, req2) -> req1.getCreatedAt().compareTo(req2.getCreatedAt()))
                .map(SQLCommunicationRequest::toDomain)
                .orElse(null);
    }

    @Override
    public CommunicationRequest getLatestUnresolvedCommunicationRequestForOpCode(int opCode) {
        return repository.findByOperationCodeAndStatus(opCode, RequestStatus.PENDING.getValue()).stream()
                .max((req1, req2) -> req1.getCreatedAt().compareTo(req2.getCreatedAt()))
                .map(SQLCommunicationRequest::toDomain)
                .orElse(null);
    }

    @Override
    public CommunicationRequest getLatestResolvedCommunicationRequest() {
        return repository.findByStatus(RequestStatus.SUCCESS.getValue()).stream()
                .max((req1, req2) -> req1.getCreatedAt().compareTo(req2.getCreatedAt()))
                .map(SQLCommunicationRequest::toDomain)
                .orElse(null);
    }

    @Override
    public CommunicationRequest getLatestUnresolvedCommunicationRequest() {
        return repository.findByStatus(RequestStatus.PENDING.getValue()).stream()
                .max((req1, req2) -> req1.getCreatedAt().compareTo(req2.getCreatedAt()))
                .map(SQLCommunicationRequest::toDomain)
                .orElse(null);
    }

    @Override
    public void flushAllUnresolvedRequests() {
        List<SQLCommunicationRequest> unresolvedRequests = repository.findByStatus(RequestStatus.PENDING.getValue());
        for (SQLCommunicationRequest req : unresolvedRequests) {
            req.setStatus(RequestStatus.SUCCESS.getValue());
            repository.save(req);
        }
    }

    @Override
    public void resolveCommunicationRequest(char opCode, int recipient) {
        List<SQLCommunicationRequest> requests = repository.findByOperationCodeAndStatus(opCode, RequestStatus.PENDING.getValue());
        if (!requests.isEmpty()) {
            SQLCommunicationRequest req = requests.get(0);
            req.setStatus(RequestStatus.SUCCESS.getValue());
            repository.save(req);
        }
    }

    @Override
    public CommunicationRequest save(CommunicationRequest entity) {
        return null;
    }

    @Override
    public Optional<CommunicationRequest> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public List<CommunicationRequest> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public void deleteAll() {

    }
}