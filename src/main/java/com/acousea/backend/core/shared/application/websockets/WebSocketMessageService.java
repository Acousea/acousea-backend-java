package com.acousea.backend.core.shared.application.websockets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public abstract class WebSocketMessageService {
    private final ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<>();


    public void addClient(String sessionId, WebSocketSession session) {
        clients.put(sessionId, session);
        onClientConnected(session);
    }

    public void removeClient(String sessionId) {
        clients.remove(sessionId);
    }

    public void broadcastMessage(String type, Object payload) {
        WebSocketMessage message = new WebSocketMessage(type, payload);
        String jsonMessage = message.toJson();

        clients.values().forEach(client -> {
            try {
                if (client.isOpen()) {
                    client.sendMessage(new TextMessage(jsonMessage));
                } else {
                    removeClient(client.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    protected abstract void onClientConnected(WebSocketSession session);


    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected static class WebSocketMessage {
        private final ObjectMapper objectMapper = new ObjectMapper();

        @JsonProperty("type")
        private final String type;

        @JsonProperty("payload")
        private final Object payload;

        public WebSocketMessage(String type, Object payload) {
            this.type = type;
            this.payload = payload;
        }

        public String toJson() {
            try {
                return objectMapper.writeValueAsString(this);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize WebSocket message", e);
            }
        }
    }
}
