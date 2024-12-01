package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes;

import com.acousea.backend.core.shared.domain.UnsignedByte;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public final class OperationMode {
    private final byte id; // Usamos byte directamente
    private final String name;

    private OperationMode(byte id, String name) {
        if (!UnsignedByte.isValidUnsignedByte(id)) {
            throw new IllegalArgumentException("ID must be between 0 and 255, inclusive.");
        }
        this.id = id;
        this.name = name;
    }

    public static OperationMode create(int id, String name) {
        return new OperationMode(UnsignedByte.toByte(id), name);
    }

    public static OperationMode create(byte id, String name) {
        return new OperationMode(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        OperationMode that = (OperationMode) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UnsignedByte.toUnsignedInt(id), name);
    }

    @Override
    public String toString() {
        return "OperationMode[" +
                "id=" + UnsignedByte.toUnsignedInt(id) + ", " +
                "name=" + name + ']';
    }
}
