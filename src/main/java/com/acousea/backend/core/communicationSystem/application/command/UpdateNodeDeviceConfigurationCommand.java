package com.acousea.backend.core.communicationSystem.application.command;


import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.shared.application.services.StorageService;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;

import java.util.Optional;
import java.util.UUID;


public class UpdateNodeDeviceConfigurationCommand extends Command<NodeDeviceDTO, Void> {
    private final NodeDeviceRepository nodeDeviceRepository;
    private final StorageService storageService;

    public UpdateNodeDeviceConfigurationCommand(NodeDeviceRepository nodeDeviceRepository, StorageService storageService) {
        this.nodeDeviceRepository = nodeDeviceRepository;
        this.storageService = storageService;
    }

    @Override
    public Result<Void> execute(NodeDeviceDTO node) {
        System.out.println("UpdateNodeDeviceCommand.execute -> node: " + node);
        Optional<NodeDevice> nodeDeviceInfo = nodeDeviceRepository.findById(UUID.fromString(node.getId()));
        if (nodeDeviceInfo.isEmpty()) {
            return Result.fail(404, "Node not found");
        }



        // TODO: Check the existence of each field on the

        return Result.success(null);
    }
}