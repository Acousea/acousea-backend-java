package com.acousea.backend.core.communicationSystem.application.command;


import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.shared.application.services.StorageService;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;

import java.util.ArrayList;
import java.util.List;

public class GetAllNodeDevicesCommand extends Command<Void, List<NodeDeviceDTO>> {
    private final NodeDeviceRepository nodeDeviceRepository;
    private final StorageService storageService;

    public GetAllNodeDevicesCommand(NodeDeviceRepository nodeDeviceRepository, StorageService storageService) {
        this.nodeDeviceRepository = nodeDeviceRepository;
        this.storageService = storageService;
    }

    @Override
    public ApiResult<List<NodeDeviceDTO>> execute(Void none) {
        List<NodeDevice> nodeDevice = nodeDeviceRepository.findAll();
        List<NodeDevice> nodeDevicesWithIconUrl = new ArrayList<>();

        nodeDevice.forEach(node -> {
            nodeDevicesWithIconUrl.add(new NodeDevice(
                    node.getId(),
                    node.getName(),
                    storageService.getFileUrl(node.getIcon()),
                    node.getSerializableModulesMap()
            ));
        });


        return ApiResult.success(NodeDeviceDTO.fromNodeDevices(nodeDevicesWithIconUrl));
    }
}