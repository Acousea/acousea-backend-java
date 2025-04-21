package com.acousea.backend.core.communicationSystem.domain.nodes.serialization;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Getter
@Setter
public abstract class SerializableModule {
    protected final byte TYPE;

    public SerializableModule(ModuleCode type) {
        this.TYPE = (byte) type.getValue();
    }

    public abstract byte[] getVALUE();

    public byte[] toBytes() {
        byte[] value = getVALUE();
        return ByteBuffer
                .allocate(value.length + 2)
                .put(TYPE)
                .put((byte) value.length)
                .put(value)
                .array();
    }

    public int getFullLength() {
        return getVALUE().length + 2;
    }

    public String encode() {
        byte[] value = getVALUE();
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(value.length + 1));
        sb.append(Integer.toHexString(TYPE));
        for (byte b : value) {
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }
}

