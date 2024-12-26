package com.acousea.backend.app.config;

import com.acousea.backend.core.communicationSystem.domain.events.ReceivedRockBlockMessageEvent;
import com.acousea.backend.core.communicationSystem.domain.events.ReceivedCompleteStatusReportEvent;
import com.acousea.backend.core.communicationSystem.domain.events.UpdatedNodeConfigurationEvent;
import com.acousea.backend.core.shared.application.events.EventBus;
import com.acousea.backend.core.shared.application.notifications.NotificationService;
import com.acousea.backend.core.shared.application.utils.PasswordHasher;
import com.acousea.backend.core.shared.infrastructure.events.InMemoryEventBus;
import com.acousea.backend.core.shared.infrastructure.utils.passwords.BCryptPasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class BaseConfig {

    @Bean
    public PasswordHasher passwordHasher() {
        return new BCryptPasswordHasher();
    }

    @Bean
    public EventBus eventBus(NotificationService notificationService) {
        EventBus eventBus = new InMemoryEventBus();
        eventBus.subscribe(ReceivedRockBlockMessageEvent.class, event -> {
            notificationService.sendInfoNotification("Received RockBlock message");
        });
        eventBus.subscribe(ReceivedCompleteStatusReportEvent.class, event -> {
            notificationService.sendSuccessNotification(
                    "Received " + event.getPayload().nodeName() + " summary report");
        });
        eventBus.subscribe(UpdatedNodeConfigurationEvent.class, event -> {
            notificationService.sendSuccessNotification(
                    "Updated " + event.getPayload().nodeName() + " configuration");
        });
        return eventBus;
    }
}