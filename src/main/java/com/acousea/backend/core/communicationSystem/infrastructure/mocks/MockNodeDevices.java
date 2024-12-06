package com.acousea.backend.core.communicationSystem.infrastructure.mocks;

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
public class MockNodeDevices {
    private final Environment environment;

    @Autowired
    public MockNodeDevices(Environment environment) {
        this.environment = environment;
    }

    // Método estático para generar las sesiones mock
    public List<NodeDevice> generate() {

        RTCModule rtcModule = RTCModule.createDefault();
        NetworkModule networkModule1 = NetworkModule.create((byte) 1);
        NetworkModule networkModule2 = NetworkModule.create((byte) 2);
        BatteryModule batteryModule = BatteryModule.create();
        LocationModule locationModule = LocationModule.create();
        AmbientModule ambientModule = AmbientModule.create();
        StorageModule storageModule = StorageModule.create(1000);
        OperationModeModule operationModeModule = OperationModeModule.createWithModes(
                List.of(
                        OperationMode.create(1, "Launching"),
                        OperationMode.create(2, "Working"),
                        OperationMode.create(3, "Recovering")
                )
        );
        ReportingModule loRaReportingModule = LoRaReportingModule.create(operationModeModule);

        IridiumReportingModule iridiumReportingModuleLocalizer = IridiumReportingModule.create(operationModeModule, environment.getProperty("localizer.imei"));
        IridiumReportingModule iridiumReportingModuleDrifter = IridiumReportingModule.create(operationModeModule, environment.getProperty("drifter.imei"));

        return List.of(new NodeDevice(
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        "Drifter",
                        "buoy.svg",
                        new HashMap<>(Map.of(
                                RTCModule.name, rtcModule,
                                BatteryModule.name, batteryModule,
                                LocationModule.name, locationModule,
                                AmbientModule.name, ambientModule,
                                StorageModule.name, storageModule,
                                OperationModeModule.name, operationModeModule,
                                LoRaReportingModule.name, loRaReportingModule,
                                IridiumReportingModule.name, iridiumReportingModuleDrifter,
                                NetworkModule.name, networkModule1
                        )),
                        new ArrayList<>(List.of(
                                new ICListenHF("RB9-ETH")
                        ))
                ),
                new NodeDevice(
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        "Localizer",
                        "compass.svg",
                        new HashMap<>(Map.of(
                                RTCModule.name, rtcModule,
                                BatteryModule.name, batteryModule,
                                LocationModule.name, locationModule,
                                AmbientModule.name, ambientModule,
                                StorageModule.name, storageModule,
                                OperationModeModule.name, operationModeModule,
                                LoRaReportingModule.name, loRaReportingModule,
                                IridiumReportingModule.name, iridiumReportingModuleLocalizer,
                                NetworkModule.name, networkModule2
                        )),
                        List.of()
                )

        );
    }
}
