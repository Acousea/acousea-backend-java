package com.acousea.backend.core.shared.domain.notifications;

import lombok.Getter;
import org.springframework.web.socket.WebSocketMessage;

@Getter
enum NotificationType {
    INFO("info"),
    SUCCESS("success"),
    ERROR("error"),
    WARNING("warning");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }
}

@Getter
public class Notification {
    private String message;
    private NotificationType type;

    private Notification(String message, NotificationType type) {
        this.message = message;
        this.type = type;
    }

    public static Notification info(String message) {
        return new Notification(message, NotificationType.INFO);
    }

    public static Notification success(String message) {
        return new Notification(message, NotificationType.SUCCESS);
    }

    public static Notification error(String message) {
        return new Notification(message, NotificationType.ERROR);
    }

    public static Notification warning(String message) {
        return new Notification(message, NotificationType.WARNING);
    }

    public String dumpJson() {
        return String.format("{\"type\": \"%s\", \"message\": \"%s\"}", type.getValue(), message);
    }
}

