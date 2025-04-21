package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.BatteryStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Getter
@Setter
public class BatteryModule extends SerializableModule implements ExtModule {
    public static final String name = "battery";
    private int batteryPercentage;
    private BatteryStatus batteryStatus;

    public BatteryModule(int batteryPercentage, BatteryStatus batteryStatus) {
        super(ModuleCode.BATTERY);
        this.batteryPercentage = batteryPercentage;
        this.batteryStatus = batteryStatus;
    }

    @Override
    public byte[] getVALUE() {
        return ByteBuffer.allocate(2)
                .put((byte) batteryPercentage)
                .put((byte) batteryStatus.getValue())
                .array();
    }

    @Override
    public int getFullSize() {
        return getMinSize();
    }

    public static int getMinSize() {
        return Byte.BYTES * 2; // 1 byte for batteryPercentage, 1 byte for batteryStatus
    }

    public static BatteryModule create() {
        return new BatteryModule(0, BatteryStatus.fromInt(0));
    }

    public static BatteryModule fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < getMinSize()) {
            throw new IllegalArgumentException("Invalid byte array for BatteryModule");
        }
        int batteryPercentage = buffer.get();
        BatteryStatus batteryStatus = BatteryStatus.fromInt(buffer.get());
        return new BatteryModule(batteryPercentage, batteryStatus);
    }

    @Override
    public String toString() {
        return "BatteryModule{" +
                "batteryPercentage=" + batteryPercentage +
                ", batteryStatus=" + batteryStatus +
                '}';
    }
}
