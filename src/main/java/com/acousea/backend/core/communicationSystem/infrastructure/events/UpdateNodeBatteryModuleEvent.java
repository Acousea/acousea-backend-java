package com.acousea.backend.core.communicationSystem.infrastructure.events;

import com.acousea.backend.core.shared.domain.events.DomainEvent;

public class UpdateNodeBatteryModuleEvent extends DomainEvent<UpdateNodeBatteryModuleEvent.Payload> {
    public UpdateNodeBatteryModuleEvent(Payload payload) {
        super("UpdateNodeBatteryModuleEvent", payload);
    }

    public static record Payload(String nodeId, String batteryModuleId, int batteryPercentage, int batteryStatus) {
    }
}
