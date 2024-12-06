package com.acousea.backend.core.communicationSystem.application.command;


import com.acousea.backend.core.communicationSystem.application.command.DTO.GetUpdatedNodeDeviceConfigurationDTO;
import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationService;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResult;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;

import java.util.UUID;


public class GetUpdatedNodeDeviceConfigurationCommand extends Command<GetUpdatedNodeDeviceConfigurationDTO, CommunicationResult> {
    private final NodeDeviceRepository nodeDeviceRepository;
    private final CommunicationService communicationService;

    public GetUpdatedNodeDeviceConfigurationCommand(
            NodeDeviceRepository nodeDeviceRepository,
            CommunicationService communicationService
    ) {
        this.nodeDeviceRepository = nodeDeviceRepository;
        this.communicationService = communicationService;
    }

    @Override
    public Result<CommunicationResult> execute(GetUpdatedNodeDeviceConfigurationDTO dto) {
        System.out.println("UpdateNodeDeviceCommand.execute -> node: " + dto);
        NodeDevice nodeDevice = nodeDeviceRepository.findById(UUID.fromString(dto.getNodeId())).orElseThrow(
                () -> new NullPointerException("Node not found")
        );

        NetworkModule networkModule = nodeDevice.getModule(NetworkModule.class).orElseThrow(
                () -> new NullPointerException("Network module not found")
        );

        // Build the Communication Request and send it
        CommunicationRequest request = CommunicationRequest.createGetUpdatedNodeConfigurationRequest(
                networkModule.getLocalAddress(),
                dto
        );


        return Result.success(communicationService.send(request));
    }
}