package com.acousea.backend.core.communicationSystem.application.command;


import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.shared.application.services.StorageService;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;

import java.util.Optional;
import java.util.UUID;

public class GetNodeDeviceCommand extends Command<GetNodeDeviceCommand.NodeDeviceIdentifier, NodeDeviceDTO> {
    public record NodeDeviceIdentifier(Optional<String> id, Optional<String> networkAddress) {
    }

    private final NodeDeviceRepository nodeDeviceRepository;
    private final StorageService storageService;

    public GetNodeDeviceCommand(NodeDeviceRepository nodeDeviceRepository, StorageService storageService) {
        this.nodeDeviceRepository = nodeDeviceRepository;
        this.storageService = storageService;
    }

    @Override
    public ApiResult<NodeDeviceDTO> execute(NodeDeviceIdentifier identifier) {
        Optional<NodeDevice> nodeDeviceInfo;
        if (identifier.id().isPresent()) {
            nodeDeviceInfo = nodeDeviceRepository.findById(UUID.fromString(identifier.id().get()));
        } else if (identifier.networkAddress().isPresent()) {
            nodeDeviceInfo = nodeDeviceRepository.findByNetworkAddress(
                    Address.fromValue(Integer.parseInt(identifier.networkAddress().get()))
            );
        } else {
            return ApiResult.fail(400, "You need to specify an id or a network address");
        }

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

        return ApiResult.success(NodeDeviceDTO.fromNodeDevice(node));
    }
}