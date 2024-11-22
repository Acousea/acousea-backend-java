package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes;

import com.acousea.backend.core.shared.domain.UnsignedByte;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public final class OperationMode {
    private final UnsignedByte id;
    private final String name;

    private OperationMode(UnsignedByte id, String name) {
        this.id = id;
        this.name = name;
    }

    public static OperationMode create(UnsignedByte id, String name) {
        return new OperationMode(id, name);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OperationMode) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "OperationMode[" +
                "id=" + id + ", " +
                "name=" + name + ']';
    }
}
