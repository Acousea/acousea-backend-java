package com.acousea.backend.core.communicationSystem.infrastructure.mocks;

import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModeGraph.OperationModesGraphModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.ReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.ICListenHF;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenLoggingConfig;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenRecordingStats;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStreamingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


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
        OperationModesModule operationModesModule = OperationModesModule.createWithModes(
                List.of(
                        OperationMode.create((short) 1, "Launching"),
                        OperationMode.create((short) 2, "Working"),
                        OperationMode.create((short) 3, "Recovering")
                )
        );

        ReportingModule loRaReportingModule = LoRaReportingModule.create(Map.ofEntries(
                Map.entry(OperationMode.create((short) 1, "Launching"), (short) 1),
                Map.entry(OperationMode.create((short) 2, "Working"), (short) 2),
                Map.entry(OperationMode.create((short) 3, "Recovering"), (short) 1)
        ));
        OperationModesGraphModule graph = new OperationModesGraphModule(Map.ofEntries(
                Map.entry(1, new OperationModesGraphModule.Transition(2, 3)), // Launching -> Working (3 cycles duration)
                Map.entry(2, new OperationModesGraphModule.Transition(2, 1)) // Working -> Working (1 cycle duration) (loop)
        ));

        IridiumReportingModule iridiumReportingModuleLocalizer = IridiumReportingModule.create(
                Map.ofEntries(
                        Map.entry(OperationMode.create((short) 1, "Launching"), (short) 1),
                        Map.entry(OperationMode.create((short) 2, "Working"), (short) 2),
                        Map.entry(OperationMode.create((short) 3, "Recovering"), (short) 1)
                )
                , environment.getProperty("localizer.imei"));
        IridiumReportingModule iridiumReportingModuleDrifter = IridiumReportingModule.create(
                Map.ofEntries(
                        Map.entry(OperationMode.create((short) 1, "Launching"), (short) 1),
                        Map.entry(OperationMode.create((short) 2, "Working"), (short) 2),
                        Map.entry(OperationMode.create((short) 3, "Recovering"), (short) 1)
                )
                , environment.getProperty("drifter.imei"));

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
                                OperationModesModule.name, operationModesModule,
                                OperationModesGraphModule.name, graph,
                                LoRaReportingModule.name, loRaReportingModule,
                                IridiumReportingModule.name, iridiumReportingModuleDrifter,
                                NetworkModule.name, networkModule1
                        )),
                        new HashMap<>(Map.of(
                                ICListenHF.name, new ICListenHF(
                                        ICListenStatus.createDefault(),
                                        ICListenLoggingConfig.createDefault(),
                                        ICListenStreamingConfig.createDefault(),
                                        ICListenRecordingStats.createDefault()
                                )
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
                                OperationModesModule.name, operationModesModule,
                                OperationModesGraphModule.name, graph,
                                LoRaReportingModule.name, loRaReportingModule,
                                IridiumReportingModule.name, iridiumReportingModuleLocalizer,
                                NetworkModule.name, networkModule2
                        )),
                        Map.of()
                )

        );
    }
}
