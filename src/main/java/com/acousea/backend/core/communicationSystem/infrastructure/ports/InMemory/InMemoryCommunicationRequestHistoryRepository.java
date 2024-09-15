package com.acousea.backend.core.communicationSystem.infrastructure.ports;

import com.acousea.backend.core.communicationSystem.application.ports.CommunicationRequestHistoryRepository;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.communicationSystem.domain.constants.RequestStatus;

import java.util.*;

public class InMemoryCommunicationRequestHistoryRepository implements CommunicationRequestHistoryRepository {

    private final Map<CommunicationRequest, RequestStatus> requestMap = new LinkedHashMap<>();

    @Override
    public void addCommunicationRequest(CommunicationRequest request) {
        requestMap.put(request, RequestStatus.PENDING);
    }

    @Override
    public CommunicationRequest getLatestResolvedCommunicationRequestForOpCode(int opCode) {
        return requestMap.entrySet().stream()
                .filter(entry -> entry.getValue() == RequestStatus.SUCCESS && entry.getKey().getOperationCode().getValue() == opCode)
                .map(Map.Entry::getKey)
                .max(Comparator.comparing(CommunicationRequest::getCreatedAt))
                .orElse(null);
    }

    @Override
    public CommunicationRequest getLatestUnresolvedCommunicationRequestForOpCode(int opCode) {
        return requestMap.entrySet().stream()
                .filter(entry -> entry.getValue() == RequestStatus.PENDING && entry.getKey().getOperationCode().getValue() == opCode)
                .map(Map.Entry::getKey)
                .max(Comparator.comparing(CommunicationRequest::getCreatedAt))
                .orElse(null);
    }

    @Override
    public CommunicationRequest getLatestResolvedCommunicationRequest() {
        return requestMap.entrySet().stream()
                .filter(entry -> entry.getValue() == RequestStatus.SUCCESS)
                .map(Map.Entry::getKey)
                .max(Comparator.comparing(CommunicationRequest::getCreatedAt))
                .orElse(null);
    }

    @Override
    public CommunicationRequest getLatestUnresolvedCommunicationRequest() {
        return requestMap.entrySet().stream()
                .filter(entry -> entry.getValue() == RequestStatus.PENDING)
                .map(Map.Entry::getKey)
                .max(Comparator.comparing(CommunicationRequest::getCreatedAt))
                .orElse(null);
    }

    @Override
    public List<CommunicationRequest> getAllCommunicationRequests() {
        return new ArrayList<>(requestMap.keySet());
    }

    @Override
    public void flushAllUnresolvedRequests() {
        requestMap.entrySet().stream()
                .filter(entry -> entry.getValue() == RequestStatus.PENDING)
                .forEach(entry -> requestMap.put(entry.getKey(), RequestStatus.SUCCESS));
    }

    @Override
    public void resolveCommunicationRequest(char opCode, int recipient) {
        requestMap.entrySet().stream()
                .filter(entry -> entry.getKey().getOperationCode().getValue() == opCode && entry.getValue() == RequestStatus.PENDING)
                .findFirst()
                .ifPresent(entry -> requestMap.put(entry.getKey(), RequestStatus.SUCCESS));
    }
}
