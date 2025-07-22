package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes;

import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;

import com.acousea.backend.core.shared.domain.UnsignedByte;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class OperationModesModule extends SerializableModule{
    public static final String name = "operationModes";
    private static final int MAX_MODES = 256; // Número máximo de modos permitidos
    private final Short activeOperationModeIdx;
    private final Map<Short, OperationMode> modes = new TreeMap<>(); // Mapa para almacenar modos

    public OperationModesModule(Map<Short, OperationMode> modes) {
        super(ModuleCode.OPERATION_MODES);
        this.modes.putAll(modes);
        this.activeOperationModeIdx = !modes.isEmpty() ? modes.keySet().iterator().next() : 0;
    }

    public OperationModesModule(Map<Short, OperationMode> modes, Short activeOperationModeIdx) {
        super(ModuleCode.OPERATION_MODES);
        this.modes.putAll(modes);
        this.activeOperationModeIdx = activeOperationModeIdx;
    }


    @Override
    public byte[] getVALUE() {
        ByteBuffer buffer = ByteBuffer.allocate(modes.size() + Byte.BYTES);
        for (OperationMode mode : modes.values()) {
            buffer.put(UnsignedByte.toByte(mode.getId()));
        }
        buffer.put(UnsignedByte.toByte(activeOperationModeIdx));
        return buffer.array();
    }

    public static OperationModesModule fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < getMinSize()) {
            throw new IllegalArgumentException("Invalid byte array for OperationModeModule");
        }
        Map<Short, OperationMode> modes = IntStream.range(0, buffer.remaining() - Byte.BYTES)
                .mapToObj(i -> {
                    short id = buffer.get();
                    return OperationMode.create(Short.valueOf(id), "operationMode" + id);
                })
                .collect(Collectors.toMap(OperationMode::getId, mode -> mode, (a, b) -> b, TreeMap::new));

        short activeOperationModeIdx = buffer.get();
        return new OperationModesModule(modes, activeOperationModeIdx);
    }

    public static int getMinSize() {
        return Byte.BYTES; // Tamaño mínimo es 1 byte
    }

    public static OperationModesModule createWithModes(List<OperationMode> operationModes) {
        Map<Short, OperationMode> modes = new TreeMap<>();
        for (OperationMode mode : operationModes) {
            modes.put(mode.getId(), mode);
        }
        return new OperationModesModule(modes);
    }

    public void addOperationMode(String name) {
        short nextId = getNextAvailableId();
        if (nextId == -1) {
            throw new IllegalStateException("Maximum number of operation modes reached.");
        }
        OperationMode mode = OperationMode.create(nextId, name);
        modes.put(nextId, mode);
    }

    public OperationMode getOperationMode(byte id) {
        int unsignedId = Byte.toUnsignedInt(id);
        OperationMode mode = modes.get(unsignedId);
        if (mode == null) {
            throw new IllegalArgumentException("Operation mode ID not found: " + id);
        }
        return mode;
    }

    public void removeOperationMode(byte id) {
        short unsignedId = (short) Byte.toUnsignedInt(id);
        if (!modes.containsKey(unsignedId)) {
            throw new IllegalArgumentException("Operation mode ID not found: " + id);
        }
        modes.remove(unsignedId);
    }

    public short getNextAvailableId() {
        for (short i = 0; i < MAX_MODES; i++) {
            if (!modes.containsKey(i)) {
                return i;
            }
        }
        return -1; // Indica que no hay espacio disponible
    }

    @Override
    public String toString() {
        return "OperationModeModule{" +
                "operationModes=" + modes +
                '}';
    }
}
