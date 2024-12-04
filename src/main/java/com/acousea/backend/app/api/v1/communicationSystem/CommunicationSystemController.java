package com.acousea.backend.app.api.v1.communicationSystem;


import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.application.command.GetAllNodeDevicesCommand;
import com.acousea.backend.core.communicationSystem.application.command.GetNodeDeviceCommand;
import com.acousea.backend.core.communicationSystem.application.command.SetNodeDeviceConfigurationCommand;
import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationService;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResult;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.shared.application.services.StorageService;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Result<NodeDevice>> getNodeStatus(@RequestParam String id) {
        GetNodeDeviceCommand query = new GetNodeDeviceCommand(
                nodeDeviceRepository, storageService
        );
        return ResponseEntity.ok(query.run(id));
    }

    @GetMapping("/node-device/all")
    public ResponseEntity<Result<List<NodeDeviceDTO>>> getAllNodesStatus() {
        GetAllNodeDevicesCommand query = new GetAllNodeDevicesCommand(
                nodeDeviceRepository, storageService
        );
        return ResponseEntity.ok(query.run(null));
    }

    @PostMapping("/node-device")
    public ResponseEntity<Result<NodeDevice>> createNodeDevice(@RequestBody NodeDevice nodeDevice) {
        nodeDeviceRepository.save(nodeDevice);
        return ResponseEntity.ok(Result.success(nodeDevice));
    }

    @PutMapping("/node-device/set")
    public ResponseEntity<Result<CommunicationResult>> setNodeDeviceConfiguration(@RequestBody NodeDeviceDTO nodeDevice) {
        SetNodeDeviceConfigurationCommand query = new SetNodeDeviceConfigurationCommand(
                nodeDeviceRepository, storageService, communicationService
        );
        return ResponseEntity.ok(query.run(nodeDevice));
    }

    @PutMapping("/node-device/update")
    public ResponseEntity<Result<CommunicationResult>> getUpdatedNodeDeviceConfiguration() {
//        SetNodeDeviceConfigurationCommand query = new SetNodeDeviceConfigurationCommand(
//                nodeDeviceRepository, storageService, communicationService
//        );
        return ResponseEntity.ok(Result.success(null));
    }
}
