package com.acousea.backend.core.communicationSystem.infrastructure.ports.InMemory;

import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
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
import com.acousea.backend.core.shared.infrastructure.ports.InMemoryIRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Repository
public class InMemoryNodeDeviceRepository extends InMemoryIRepository<NodeDevice, UUID> implements NodeDeviceRepository {
    private final Environment environment;

    @Override
    public Optional<NodeDevice> findByNetworkAddress(Address networkAddress) {
        return this.findAll().stream()
                .filter(nodeDevice -> nodeDevice.getExtModules().values().stream()
                        .anyMatch(module -> module instanceof NetworkModule && ((NetworkModule) module).getLocalAddress() == networkAddress))
                .findFirst();
    }

    public InMemoryNodeDeviceRepository(Environment environment) {
        super(NodeDevice::getId);
        this.environment = environment;
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

        this.save(
                new NodeDevice(
                        UUID.randomUUID(),
                        "Drifter",
                        "buoy.svg",
                        Map.of(
                                rtcModule.getName(), rtcModule,
                                batteryModule.getName(), batteryModule,
                                locationModule.getName(), locationModule,
                                ambientModule.getName(), ambientModule,
                                storageModule.getName(), storageModule,
                                operationModeModule.getName(), operationModeModule,
                                loRaReportingModule.getName(), loRaReportingModule,
                                iridiumReportingModuleDrifter.getName(), iridiumReportingModuleDrifter,
                                networkModule1.getName(), networkModule1
                        ),
                        List.of(
                                new ICListenHF("RB9-ETH")
                        )
                )
        );
        this.save(
                new NodeDevice(
                        UUID.randomUUID(),
                        "Localizer",
                        "compass.svg",
                        Map.of(
                                rtcModule.getName(), rtcModule,
                                batteryModule.getName(), batteryModule,
                                locationModule.getName(), locationModule,
                                ambientModule.getName(), ambientModule,
                                storageModule.getName(), storageModule,
                                operationModeModule.getName(), operationModeModule,
                                loRaReportingModule.getName(), loRaReportingModule,
                                iridiumReportingModuleLocalizer.getName(), iridiumReportingModuleLocalizer,
                                networkModule2.getName(), networkModule2
                        ),
                        List.of()
                )
        );

        System.out.println("InMemoryNodeDeviceRepository created with nodes: " + this.findAll().toString());
    }

}