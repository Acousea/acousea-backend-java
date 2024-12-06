package com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation;

import com.acousea.backend.core.communicationSystem.domain.communication.tags.Tag;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagType;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;

import java.nio.ByteBuffer;

public class AmbientTag extends Tag {
    public AmbientTag(byte[] value) {
        super(TagType.AMBIENT, value);
    }

    public static AmbientTag fromAmbientModule(AmbientModule module) {
        return new AmbientTag(
                ByteBuffer.allocate(Byte.BYTES * 2)
                        .put((byte) module.getTemperature())
                        .put((byte) module.getHumidity())
                        .array());
    }

    public AmbientModule toAmbientModule() {
        ByteBuffer buffer = ByteBuffer.wrap(this.VALUE);
        return new AmbientModule(buffer.get(), buffer.get());
    }

    public static AmbientTag fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < Byte.BYTES * 2) {
            throw new IllegalArgumentException("Invalid byte array for AmbientTag");
        }
        int temperature = buffer.get();
        int humidity = buffer.get();
        return fromAmbientModule(new AmbientModule(temperature, humidity));
    }
}
