package com.acousea.backend.core.communicationSystem.application.command;


import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationService;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResult;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.shared.application.services.StorageService;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;

import java.util.UUID;


public class SetNodeDeviceConfigurationCommand extends Command<NodeDeviceDTO, CommunicationResult> {
    private final NodeDeviceRepository nodeDeviceRepository;
    private final StorageService storageService;
    private final CommunicationService communicationService;

    public SetNodeDeviceConfigurationCommand(
            NodeDeviceRepository nodeDeviceRepository,
            StorageService storageService,
            CommunicationService communicationService
    ) {
        this.nodeDeviceRepository = nodeDeviceRepository;
        this.storageService = storageService;
        this.communicationService = communicationService;
    }

    @Override
    public ApiResult<CommunicationResult> execute(NodeDeviceDTO dto) {
        System.out.println("SetNodeDeviceCommand.execute -> node: " + dto);
        NodeDevice nodeDevice = nodeDeviceRepository.findById(UUID.fromString(dto.getId())).orElseThrow(
                () -> new NullPointerException("Node not found")
        );
        NetworkModule networkModule = nodeDevice.getModule(NetworkModule.class).orElseThrow(
                () -> new NullPointerException("Network module not found")
        );

        // Change the name of the nodeDevice if present
        if (dto.getName() != null) {
            nodeDevice.setName(dto.getName());
        }

        // FIXME: Use the file handler to store the icon and get the URL
        if (dto.getIcon() != null) {
            nodeDevice.setIcon(dto.getIcon());
        }

        // Creates a new nodeDevice from the DTO, so it automatically checks if the configuration is valid
        //  but do NOT SAVE the new nodeDevice to the repository. The new configuration will be saved only if
        //  the communication is successful
        NodeDevice nodeChanges = dto.toNodeDevice();

        // Build the Communication Request and send it
        CommunicationRequest request = CommunicationRequest.createSetNodeDeviceConfigurationRequest(
                networkModule.getLocalAddress(),
                nodeChanges
        );

        return ApiResult.success(communicationService.send(request));
    }
}