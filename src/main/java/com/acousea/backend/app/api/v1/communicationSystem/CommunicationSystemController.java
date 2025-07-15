package com.acousea.backend.app.api.v1.communicationSystem;


import com.acousea.backend.core.communicationSystem.application.command.DTO.GetUpdatedNodeDeviceConfigurationDTO;
import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.application.command.*;
import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationService;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResult;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.shared.application.services.StorageService;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${apiPrefix}/communication-system")
public class CommunicationSystemController {
    private final CommunicationService communicationService;
    private final NodeDeviceRepository nodeDeviceRepository;
    private final StorageService storageService;

    @Autowired
    public CommunicationSystemController(
            CommunicationService communicationService,
            NodeDeviceRepository nodeDeviceRepository,
            StorageService storageService
    ) {
        this.communicationService = communicationService;
        this.nodeDeviceRepository = nodeDeviceRepository;
        this.storageService = storageService;
    }

    @GetMapping("/node-device")
    public ResponseEntity<ApiResult<NodeDeviceDTO>> getNodeStatus(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String networkAddress
    ) {
        GetNodeDeviceCommand query = new GetNodeDeviceCommand(
                nodeDeviceRepository, storageService
        );
        return ResponseEntity.ok(
                query.run(
                        new GetNodeDeviceCommand.NodeDeviceIdentifier(
                                Optional.ofNullable(id),
                                Optional.ofNullable(networkAddress)
                        )
                )
        );
    }

    @GetMapping("/node-device/all")
    public ResponseEntity<ApiResult<List<NodeDeviceDTO>>> getAllNodesStatus() {
        GetAllNodeDevicesCommand query = new GetAllNodeDevicesCommand(
                nodeDeviceRepository, storageService
        );
        return ResponseEntity.ok(query.run(null));
    }

    @PostMapping("/node-device")
    public ResponseEntity<ApiResult<NodeDevice>> createNodeDevice(@RequestBody NodeDevice nodeDevice) {
        nodeDeviceRepository.save(nodeDevice);
        return ResponseEntity.ok(ApiResult.success(nodeDevice));
    }

    @PutMapping("/node-device/set")
    public ResponseEntity<ApiResult<CommunicationResult>> setNodeDeviceConfiguration(
            @RequestBody NodeDeviceDTO dto
    ) {
        if (dto.getId() == null || dto.getId().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResult.fail("Node ID is required"));
        }
        SetNodeDeviceConfigurationCommand query = new SetNodeDeviceConfigurationCommand(
                nodeDeviceRepository, storageService, communicationService
        );
        return ResponseEntity.ok(query.run(dto));
    }

    @PutMapping("/node-device/update")
    public ResponseEntity<ApiResult<CommunicationResult>> getUpdatedNodeDeviceConfiguration(
            @RequestBody GetUpdatedNodeDeviceConfigurationDTO dto
    ) {
        if (dto.getNodeId() == null || dto.getNodeId().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResult.fail("Node ID is required"));
        }
        GetUpdatedNodeDeviceConfigurationCommand query = new GetUpdatedNodeDeviceConfigurationCommand(
                nodeDeviceRepository, communicationService
        );
        return ResponseEntity.ok(query.run(dto));
    }

    @PostMapping("/estimate-packet-cost")
    public ResponseEntity<ApiResult<EstimateConfigurationPacketCostCommand.NodeCostEstimationPayload>> estimatePacketCost(
            @RequestBody NodeDeviceDTO dto
    ) {
        EstimateConfigurationPacketCostCommand query = new EstimateConfigurationPacketCostCommand();
        return ResponseEntity.ok(query.run(
                dto
        ));
    }
}
