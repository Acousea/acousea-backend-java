package com.acousea.backend.core.shared.infrastructure.notification;

import com.acousea.backend.core.shared.application.notifications.NotificationService;
import com.acousea.backend.core.shared.domain.notifications.Notification;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketNotificationService implements NotificationService {
    private final ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    @Override
    public void sendSuccessNotification(String message) {
        sendNotification(Notification.success(message));
    }

    @Override
    public void sendErrorNotification(String message) {
        sendNotification(Notification.error(message));
    }

    @Override
    public void sendWarningNotification(String message) {
        sendNotification(Notification.warning(message));
    }

    @Override
    public void sendInfoNotification(String message) {
        sendNotification(Notification.info(message));
    }

    @Override
    public void addClient(String sessionId, WebSocketSession session) {
        clients.put(sessionId, session);
        sendSuccessNotification("Welcome to the notification service!");
    }

    @Override
    public void removeClient(String sessionId) {
        clients.remove(sessionId);
    }

    private void sendNotification(Notification notification) {
        clients.values().forEach(client -> {
            try {
                if (client.isOpen()) {
                    client.sendMessage(new TextMessage(notification.dumpJson()));
                } else {
                    removeClient(client.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
