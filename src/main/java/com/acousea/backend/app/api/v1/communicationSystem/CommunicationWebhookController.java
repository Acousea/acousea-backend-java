package com.acousea.backend.app.api.v1.communicationSystem;

import com.acousea.backend.core.communicationSystem.application.command.ProcessRockblockMessageCommand;
import com.acousea.backend.core.communicationSystem.application.ports.RockblockMessageRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationResponseProcessor;
import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.shared.application.events.EventBus;
import com.acousea.backend.core.shared.application.notifications.NotificationService;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiPrefix}/rockblock")
public class CommunicationWebhookController {

    private final CommunicationResponseProcessor communicationResponseProcessor;
    private final RockblockMessageRepository rockblockMessageRepository;
    private final NotificationService notificationService;
    private final EventBus eventBus;

    @Autowired
    public CommunicationWebhookController(
            CommunicationResponseProcessor communicationResponseProcessor,
            RockblockMessageRepository rockblockMessageRepository,
            NotificationService notificationService,
            EventBus eventBus
    ) {
        this.communicationResponseProcessor = communicationResponseProcessor;
        this.rockblockMessageRepository = rockblockMessageRepository;
        this.notificationService = notificationService;
        this.eventBus = eventBus;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Result<String>> receiveRockblockPacket(
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
                    rockblockMessageRepository,
                    communicationResponseProcessor,
                    notificationService,
                    eventBus);
            return ResponseEntity.ok(command.run(rockBlockMessage));
        } catch (Exception e) {
            System.out.println("RockBlockMessage processing error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Result.fail(422, "Error processing the message"));
        }
    }

}