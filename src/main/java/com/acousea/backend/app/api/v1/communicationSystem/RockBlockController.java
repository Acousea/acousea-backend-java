package com.acousea.backend.app.api.v1.communicationSystem;

import com.acousea.backend.core.communicationSystem.application.command.ProcessRockblockMessageCommand;
import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.application.ports.RockblockMessageRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationResponseProcessor;
import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.shared.application.events.EventBus;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/rockblock")
public class RockBlockController {

    private final CommunicationResponseProcessor communicationResponseProcessor;
    private final RockblockMessageRepository rockblockMessageRepository;
    private final EventBus eventBus;
    private final NodeDeviceRepository nodeDeviceRepository;

    @Autowired
    public RockBlockController(
            CommunicationResponseProcessor communicationResponseProcessor,
            RockblockMessageRepository rockblockMessageRepository,
            EventBus eventBus,
            NodeDeviceRepository nodeDeviceRepository) {
        this.communicationResponseProcessor = communicationResponseProcessor;
        this.rockblockMessageRepository = rockblockMessageRepository;
        this.eventBus = eventBus;
        this.nodeDeviceRepository = nodeDeviceRepository;
    }

    @GetMapping("/messages/paginated")
    public ResponseEntity<ApiResult<List<RockBlockMessage>>> getPaginatedMessages(
            @RequestParam("page") int page,
            @RequestParam("rows_per_page") int rowsPerPage) {
        return ResponseEntity.ok(ApiResult.success(rockblockMessageRepository.getPaginatedMessages(page, rowsPerPage)));
    }

    @PostMapping("/webhook")
    public ResponseEntity<ApiResult<String>> receiveRockblockPacket(
            @RequestParam("imei") String imei,
            @RequestParam("serial") String serial,
            @RequestParam("momsn") int momsn,
            @RequestParam("transmit_time") String transmitTime,
            @RequestParam("iridium_latitude") double iridiumLatitude,
            @RequestParam("iridium_longitude") double iridiumLongitude,
            @RequestParam("iridium_cep") double iridiumCep,
            @RequestParam("data") String data) {
        try {
            // Crear un objeto RockBlockMessage con los parámetros recibidos
            RockBlockMessage rockBlockMessage = new RockBlockMessage(
                    imei,
                    serial,
                    momsn,
                    transmitTime,
                    iridiumLatitude,
                    iridiumLongitude,
                    iridiumCep,
                    data
            );

            // Procesar el comando
            var command = new ProcessRockblockMessageCommand(
                    rockblockMessageRepository, communicationResponseProcessor,
                    nodeDeviceRepository, eventBus
            );
            return ResponseEntity.ok(command.run(rockBlockMessage));
        } catch (Exception e) {
            System.out.println("RockBlockMessage processing error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ApiResult.fail(422, "Error processing the message"));
        }
    }

}