package com.acousea.backend.core.communicationSystem.application.command.DTO;
import lombok.Data;
import java.util.List;

@Data
public class GetUpdatedNodeDeviceConfigurationDTO {
    private String nodeId; // Identificación única del nodo
    private List<String> requestedModules; // Nombres de los módulos solicitados

    public GetUpdatedNodeDeviceConfigurationDTO(String nodeId, List<String> requestedModules) {
        this.nodeId = nodeId;
        this.requestedModules = requestedModules;
    }
}
