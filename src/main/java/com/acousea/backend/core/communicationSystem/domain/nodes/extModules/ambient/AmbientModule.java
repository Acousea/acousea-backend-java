package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient;


import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Getter
@Setter
public class AmbientModule extends SerializableModule {
    public static final String name = "ambient";
    private int temperature;
    private int humidity;

    public AmbientModule(int temperature, int humidity) {
        super(ModuleCode.AMBIENT);
        this.temperature = temperature;
        this.humidity = humidity;
    }

    @Override
    public byte[] getVALUE() {
        return ByteBuffer.allocate(Byte.BYTES * 2)
                .put((byte) temperature)
                .put((byte) humidity)
                .array();
    }


    public static int getMinSize() {
        return Integer.BYTES * 2;
    }

    public static AmbientModule create() {
        return new AmbientModule(0, 0);
    }

    public static AmbientModule fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < Byte.BYTES * 2) {
            throw new IllegalArgumentException("Invalid byte array for AmbientModule");
        }
        int temperature = buffer.get();
        int humidity = buffer.get();
        return new AmbientModule(temperature, humidity);
    }
}
