package com.acousea.backend.core.communicationSystem.domain.nodes.serialization;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Getter
@Setter
public abstract class SerializableModule {
    protected final byte TYPE; // Campo común para el tipo de parámetro
    protected final byte[] VALUE; // Campo común para el valor del parámetro

    public SerializableModule(ModuleCode type, byte[] value) {
        this.TYPE = (byte) type.getValue();
        this.VALUE = value;
    }

    public byte[] toBytes() {
        return ByteBuffer
                .allocate(VALUE.length + 2)
                .put(TYPE)
                .put((byte) VALUE.length)
                .put(VALUE)
                .array();
    }

    public int getFullLength() {
        return VALUE.length + 2;
    }

    public String encode() {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(VALUE.length + 1));
        sb.append(Integer.toHexString(TYPE));
        for (byte b : VALUE) {
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();

    }
}
