package com.acousea.backend.core.shared.application.notifications;

import org.springframework.web.socket.WebSocketSession;

public interface NotificationService {
    void sendSuccessNotification(String message);
    void sendErrorNotification(String message);
    void sendWarningNotification(String message);
    void sendInfoNotification(String message);
    void addClient(String sessionId, WebSocketSession session);
    void removeClient(String sessionId);
}

