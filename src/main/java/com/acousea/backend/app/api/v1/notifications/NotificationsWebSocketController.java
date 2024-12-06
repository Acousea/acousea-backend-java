package com.acousea.backend.app.api.v1.notifications;


import com.acousea.backend.core.shared.application.notifications.NotificationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

@Component
public class NotificationsWebSocketController extends TextWebSocketHandler {
    private final NotificationService notificationService;

    @Autowired
    public NotificationsWebSocketController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        String sessionId = UUID.randomUUID().toString();
        notificationService.addClient(sessionId, session);
        System.out.println("Client connected: " + sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        notificationService.removeClient(session.getId());
        System.out.println("Client disconnected: " + session.getId());
    }
}
