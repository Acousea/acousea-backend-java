package com.acousea.backend.core.shared.application.notifications;

import com.acousea.backend.core.shared.domain.notifications.Notification;

public interface NotificationService {
    void sendSuccessNotification(String message);

    void sendErrorNotification(String message);

    void sendWarningNotification(String message);

    void sendInfoNotification(String message);

    void sendNotification(Notification notification);
}

