package com.acousea.backend.core.communicationSystem.infrastructure.ports.SQL;

import com.acousea.backend.core.communicationSystem.infrastructure.SQLCommunicationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JPACommunicationRequestRepository extends JpaRepository<SQLCommunicationRequest, Long> {
    List<SQLCommunicationRequest> findByOperationCodeAndStatus(int operationCode, int status);
    List<SQLCommunicationRequest> findByStatus(int status);
}