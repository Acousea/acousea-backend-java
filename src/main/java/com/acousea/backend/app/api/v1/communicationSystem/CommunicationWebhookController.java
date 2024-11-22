package com.acousea.backend.app.api.v1.communicationSystem;

import com.acousea.backend.core.communicationSystem.application.command.ProcessRockblockMessageCommand;
import com.acousea.backend.core.communicationSystem.application.ports.RockblockMessageRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationService;
import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.shared.application.events.EventBus;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiPrefix}/webhook")
public class CommunicationWebhookController {

    private final CommunicationService communicationService;
    private final EventBus eventBus;
    private final RockblockMessageRepository rockblockMessageRepository;

    @Autowired
    public CommunicationWebhookController(
            CommunicationService communicationService,
            EventBus eventBus,
            RockblockMessageRepository rockblockMessageRepository
    ) {
        this.communicationService = communicationService;
        this.eventBus = eventBus;
        this.rockblockMessageRepository = rockblockMessageRepository;
    }

    @PostMapping("/rockblock-packets")
    public ResponseEntity<Result<Void>> receiveRockblockPacket(@RequestBody RockBlockMessage rockBlockMessage) {
        try {
            var command = new ProcessRockblockMessageCommand(rockblockMessageRepository, communicationService, eventBus);
            return ResponseEntity.ok(command.run(rockBlockMessage));
        } catch (Exception e) {
            System.out.println("RockBlockMessage processing error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Result.fail(422, "Error processing the message"));
        }
    }
}