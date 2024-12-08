package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes;

import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.domain.communication.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import com.acousea.backend.core.shared.domain.UnsignedByte;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class OperationModeModule extends SerializableModule implements ExtModule {
    public static final String name = "operationMode";
    private static final int MAX_MODES = 256; // Número máximo de modos permitidos
    private final Map<Integer, OperationMode> operationModes = new TreeMap<>(); // Mapa para almacenar modos

    public OperationModeModule(Map<Integer, OperationMode> operationModes) {
        super(ModuleCode.OPERATION_MODES, serialize(operationModes));
        this.operationModes.putAll(operationModes);
    }

    private static byte[] serialize(Map<Integer, OperationMode> operationModes) {
        ByteBuffer buffer = ByteBuffer.allocate(operationModes.size());
        for (OperationMode mode : operationModes.values()) {
            buffer.put(UnsignedByte.toByte(mode.getId()));
        }
        return buffer.array();
    }

    public static OperationModeModule fromDTO(List<NodeDeviceDTO.ExtModuleDto.ReportingPeriodDto> reportingPeriods) {
        Map<Integer, OperationMode> modes = new TreeMap<>();
        for (NodeDeviceDTO.ExtModuleDto.ReportingPeriodDto period : reportingPeriods) {
            modes.put(period.getKey().getId(), OperationMode.create(period.getKey().getId(), period.getKey().getName()));
        }
        return new OperationModeModule(modes);
    }

    public static OperationModeModule fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < getMinSize()) {
            throw new IllegalArgumentException("Invalid byte array for OperationModeModule");
        }
        Map<Integer, OperationMode> modes = IntStream.range(0, buffer.remaining())
                .mapToObj(i -> {
                    int id = UnsignedByte.toUnsignedInt(buffer.get());
                    return OperationMode.create(id, "operationMode" + id);
                })
                .collect(Collectors.toMap(OperationMode::getId, mode -> mode, (a, b) -> b, TreeMap::new));
        return new OperationModeModule(modes);
    }

    @Override
    public int getFullSize() {
        return operationModes.size();
    }

    public static int getMinSize() {
        return Byte.BYTES; // Tamaño mínimo es 1 byte
    }

    public static OperationModeModule createWithModes(List<OperationMode> names) {
        Map<Integer, OperationMode> modes = new TreeMap<>();
        for (OperationMode name : names) {
            modes.put(name.getId(), name);
        }
        return new OperationModeModule(modes);
    }

    public void addOperationMode(String name) {
        int nextId = getNextAvailableId();
        if (nextId == -1) {
            throw new IllegalStateException("Maximum number of operation modes reached.");
        }
        OperationMode mode = OperationMode.create(nextId, name);
        operationModes.put(nextId, mode);
    }

    public OperationMode getOperationMode(byte id) {
        int unsignedId = Byte.toUnsignedInt(id);
        OperationMode mode = operationModes.get(unsignedId);
        if (mode == null) {
            throw new IllegalArgumentException("Operation mode ID not found: " + id);
        }
        return mode;
    }

    public void removeOperationMode(byte id) {
        int unsignedId = Byte.toUnsignedInt(id);
        if (!operationModes.containsKey(unsignedId)) {
            throw new IllegalArgumentException("Operation mode ID not found: " + id);
        }
        operationModes.remove(unsignedId);
    }

    public int getNextAvailableId() {
        for (int i = 0; i < MAX_MODES; i++) {
            if (!operationModes.containsKey(i)) {
                return i;
            }
        }
        return -1; // Indica que no hay espacio disponible
    }

    @Override
    public String toString() {
        return "OperationModeModule{" +
                "operationModes=" + operationModes +
                '}';
    }
}
