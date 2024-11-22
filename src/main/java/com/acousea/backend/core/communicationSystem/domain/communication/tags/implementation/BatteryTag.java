package com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation;


import com.acousea.backend.core.communicationSystem.domain.communication.constants.BatteryStatus;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.Tag;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagType;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;

import java.nio.ByteBuffer;

public class BatteryTag extends Tag {
    public BatteryTag(byte[] value) {
        super(TagType.BATTERY, value);
    }

    public static BatteryTag fromBatteryModule(BatteryModule module) {
        ByteBuffer buffer = ByteBuffer.allocate(2)
                .put((byte) module.getBatteryPercentage())
                .put((byte) module.getBatteryStatus().getValue());
        return new BatteryTag(buffer.array());
    }


    public BatteryModule toBatteryModule() {
        ByteBuffer buffer = ByteBuffer.wrap(this.VALUE);
        int batteryPercentage = buffer.get();
        BatteryStatus batteryStatus = BatteryStatus.fromInt(buffer.get());
        return new BatteryModule(batteryPercentage, batteryStatus);
    }

    public static BatteryTag fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < BatteryModule.getMinSize()) {
            throw new IllegalArgumentException("Invalid byte array for BatteryTag");
        }
        int batteryPercentage = buffer.get();
        BatteryStatus batteryStatus = BatteryStatus.fromInt(buffer.get());
        return fromBatteryModule(new BatteryModule(batteryPercentage, batteryStatus));
    }


}

