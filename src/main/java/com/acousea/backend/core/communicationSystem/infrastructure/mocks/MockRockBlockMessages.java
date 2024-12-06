package com.acousea.backend.core.communicationSystem.infrastructure.mocks;

import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.ReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.ICListenHF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class MockRockBlockMessages {
    private final Environment environment;

    @Autowired
    public MockRockBlockMessages(Environment environment) {
        this.environment = environment;
    }

    // Método estático para generar las sesiones mock
    public List<RockBlockMessage> generate() {

        RockBlockMessage message1 = new RockBlockMessage(
                environment.getProperty("localizer.imei"),
                "12345",
                1,
                "2024-10-01T12:00:00Z",
                37.7749,
                -122.4194,
                2.5,
                "2043000103001a52080000000067530fd6500a010100000200000000005402000035d0"
        );

        RockBlockMessage message2 = new RockBlockMessage(
                environment.getProperty("drifter.imei"),
                "12346",
                2,
                "2024-10-02T12:00:00Z",
                34.0522,
                -118.2437,
                3.0,
                "2053000103001a52080000000067530fd6500a010100000200000000005402000035d0"
        );

        RockBlockMessage message3 = new RockBlockMessage(
                environment.getProperty("drifter.imei"),
                "12346",
                2,
                "2024-10-02T12:00:00Z",
                34.0522,
                -118.2437,
                3.0,
                "2055000103001a52080000000067530fd6500a010100000200000000005402000035d0"

        );

        return List.of(message1, message2, message3);
    }
}
