package com.acousea.backend.core.communicationSystem.application.command;


import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.shared.application.services.StorageService;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;

import java.util.Optional;
import java.util.UUID;


public class GetNodeDeviceCommand extends Command<String, NodeDevice> {
    private final NodeDeviceRepository nodeDeviceRepository;
    private final StorageService storageService;

    public GetNodeDeviceCommand(NodeDeviceRepository nodeDeviceRepository, StorageService storageService) {
        this.nodeDeviceRepository = nodeDeviceRepository;
        this.storageService = storageService;
    }

    @Override
    public ApiResult<NodeDevice> execute(String id) {
        Optional<NodeDevice> nodeDeviceInfo = nodeDeviceRepository.findById(UUID.fromString(id));
        if (nodeDeviceInfo.isEmpty()) {
            return ApiResult.fail(404, "Node not found");
        }

        // Obtener la URL completa usando StorageService
        String iconUrl = storageService.getFileUrl(nodeDeviceInfo.get().getIcon());
        if (iconUrl == null) {
            return ApiResult.fail(500, "Error getting icon URL");
        }

        var node = new NodeDevice(
                nodeDeviceInfo.get().getId(),
                nodeDeviceInfo.get().getName(),
                iconUrl,
                nodeDeviceInfo.get().getExtModules(),
                nodeDeviceInfo.get().getPamModules()
        );
        return ApiResult.success(node);
    }
}