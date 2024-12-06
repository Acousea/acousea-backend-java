package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes;

import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
public class OperationModeModule extends ExtModule {
    public static final String name = "operationMode";
    private static final int MAX_MODES = 256; // Número máximo de modos permitidos
    private final Map<Integer, OperationMode> operationModes = new TreeMap<>(); // Mapa para almacenar modos

    public static OperationModeModule fromDTO(List<NodeDeviceDTO.ExtModuleDto.ReportingPeriodDto> reportingPeriods) {
        OperationModeModule module = new OperationModeModule();
        for (NodeDeviceDTO.ExtModuleDto.ReportingPeriodDto period : reportingPeriods) {
            module.operationModes.put(period.getKey().getId(), period.getKey());
        }
        return module;
    }


    @Override
    public int getFullSize() {
        return operationModes.size() * Byte.BYTES;
    }

    public static int getMinSize() {
        return Byte.BYTES;
    }

    public static OperationModeModule createWithModes(List<OperationMode> names) {
        OperationModeModule module = new OperationModeModule();
        for (OperationMode name : names) {
            module.addOperationMode(name.getName());
        }
        return module;
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
        int unsignedId = Byte.toUnsignedInt(id); // Convertir a entero sin signo
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

    public int getCurrentModeCount() {
        return operationModes.size();
    }
}
