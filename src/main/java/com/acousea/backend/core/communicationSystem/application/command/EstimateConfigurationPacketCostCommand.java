package com.acousea.backend.core.communicationSystem.application.command;


import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationService;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;

import java.util.UUID;


public class EstimateConfigurationPacketCostCommand extends Command<NodeDeviceDTO, EstimateConfigurationPacketCostCommand.NodeCostEstimationPayload> {

    public record NodeCostEstimationPayload(int bytes, int credits) {
    }

    public EstimateConfigurationPacketCostCommand(
    ) {
    }


    @Override
    public ApiResult<EstimateConfigurationPacketCostCommand.NodeCostEstimationPayload> execute(NodeDeviceDTO dto) {
        System.out.println("EstimateConfigurationPacketCostCommand.execute -> node: " + dto);

        dto.setId(UUID.randomUUID().toString());
        NodeDevice nodeChanges = dto.toNodeDevice();

        // Check if the node device has extModules or pamModules
        if (nodeChanges.getExtModules().isEmpty() && nodeChanges.getPamModules().isEmpty()) {
            return ApiResult.success(
                    new NodeCostEstimationPayload(
                            0,
                            0
                    )
            );
        }

        // Build the Communication Request and send it
        CommunicationRequest request = CommunicationRequest.createSetNodeDeviceConfigurationRequest(
                Address.getBroadcastAddress(),
                nodeChanges
        );

        var configurationPacketSize = request.toBytes().length;
        return ApiResult.success(
                new NodeCostEstimationPayload(
                        configurationPacketSize,
                        (int) Math.ceil((double) configurationPacketSize / 50)
                )
        );
    }
}