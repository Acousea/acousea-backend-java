package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.BatteryStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import io.lettuce.core.StrAlgoArgs;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatteryModule extends ExtModule {
    private final int batteryPercentage;
    private final BatteryStatus batteryStatus;

    public BatteryModule(int batteryPercentage, BatteryStatus batteryStatus) {
        super("battery");
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