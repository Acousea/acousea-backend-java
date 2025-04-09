package com.acousea.backend.core.shared.infrastructure.notification;

import com.acousea.backend.core.shared.application.notifications.NotificationService;
import com.acousea.backend.core.shared.application.websockets.WebSocketMessageService;
import com.acousea.backend.core.shared.domain.notifications.Notification;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class WebSocketNotificationService extends WebSocketMessageService implements NotificationService {
    @Override
    protected void onClientConnected(WebSocketSession session) {
        sendSuccessNotification("Welcome to the notification service!");
    }

    @Override
    public void sendSuccessNotification(String message) {
        broadcastMessage("notification", Notification.success(message));
    }

    @Override
    public void sendErrorNotification(String message) {
        broadcastMessage("notification", Notification.error(message));
    }

    @Override
    public void sendWarningNotification(String message) {
        broadcastMessage("notification", Notification.warning(message));
    }

    @Override
    public void sendInfoNotification(String message) {
        broadcastMessage("notification", Notification.info(message));
    }

    @Override
    public void sendNotification(Notification notification) {
        broadcastMessage("notification", notification.dumpJson());
    }

    public void sendPongMessage(WebSocketSession session) {
        sendMessageTo(session.getId(), "ping", "pong");
    }
}
