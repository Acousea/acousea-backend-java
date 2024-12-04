package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.BatteryStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatteryModule extends ExtModule {
    public static final String name = "battery";
    private final int batteryPercentage;
    private final BatteryStatus batteryStatus;

    public BatteryModule(int batteryPercentage, BatteryStatus batteryStatus) {

        this.batteryPercentage = batteryPercentage;
        this.batteryStatus = batteryStatus;
    }

    public static BatteryModule create() {
        return new BatteryModule(0, BatteryStatus.fromInt(0));
    }


    @Override
    public int getFullSize() {
        return getMinSize();
    }

    public static int getMinSize() {
        return Byte.SIZE + Byte.SIZE;
    }
}