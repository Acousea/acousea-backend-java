package com.acousea.backend.app.config.websockets;

import com.acousea.backend.app.api.v1.notifications.NotificationsWebSocketController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${apiPrefix}")
    private String apiPrefix;

    private final NotificationsWebSocketController notificationsWebSocketHandler;

    public WebSocketConfig(NotificationsWebSocketController notificationsWebSocketHandler) {
        this.notificationsWebSocketHandler = notificationsWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(
                notificationsWebSocketHandler, apiPrefix + "/ws/notifications"
        ).setAllowedOrigins("*");
    }
}
