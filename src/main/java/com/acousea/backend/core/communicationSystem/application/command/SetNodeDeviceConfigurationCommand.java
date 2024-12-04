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
import com.acousea.backend.core.shared.domain.httpWrappers.Result;

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
    public Result<CommunicationResult> execute(NodeDeviceDTO dto) {
        System.out.println("UpdateNodeDeviceCommand.execute -> node: " + dto);
        NodeDevice nodeDevice = nodeDeviceRepository.findById(UUID.fromString(dto.getId())).orElseThrow(
                () -> new NullPointerException("Node not found")
        );
        NetworkModule networkModule = nodeDevice.getModule(NetworkModule.class).orElseThrow(
                () -> new NullPointerException("Network module not found")
        );

        // TODO: Set the new parameters to the nodeDevice so it automatically checks if the configuration is valid
        //  but do NOT SAVE the nodeDevice to the repository yet. The new configuration will be saved only if
        //  the communication is successful

        if (dto.getExtModules().getNetwork() != null) {
            NetworkModule newNetworkModule = NetworkModule.fromDTO(dto.getExtModules().getNetwork());
            nodeDevice.getExtModules().put(NetworkModule.name, newNetworkModule);
        }

//        if (dto.getExtModules().getOperationModes() != null) {
//            OperationModeModule newOperationModeModule = OperationModeModule.fromDTO(dto.getExtModules().getOperationModes());
//            nodeDevice.getExtModules().put(OperationModeModule.name, newOperationModeModule);
//        }

//        if (dto.getExtModules().getIridiumReporting() != null) {
//            OperationModeModule newOperationModeModule = OperationModeModule.fromDTO(dto.getExtModules().getOperationModes());
//            nodeDevice.getExtModules().put(OperationModeModule.name, newOperationModeModule);
//        }


        // Build the Communication Request and send it
        CommunicationRequest request = CommunicationRequest.createUpdateNodeDeviceRequest(
                networkModule.getLocalAddress(),
                nodeDevice
        );

        return Result.success(communicationService.send(request));
    }
}