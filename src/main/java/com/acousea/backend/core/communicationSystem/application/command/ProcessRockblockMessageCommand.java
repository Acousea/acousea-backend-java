package com.acousea.backend.core.communicationSystem.application.command;

import com.acousea.backend.core.communicationSystem.application.ports.RockblockMessageRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationService;
import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResponse;
import com.acousea.backend.core.shared.application.events.EventBus;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;

public class ProcessRockblockMessageCommand extends Command<RockBlockMessage, String> {
    private final RockblockMessageRepository messagesRepository;
    private final EventBus eventBus;

    public ProcessRockblockMessageCommand(RockblockMessageRepository messagesRepository, CommunicationService communicationService, EventBus eventBus) {
        this.messagesRepository = messagesRepository;
        this.eventBus = eventBus;
    }

    @Override
    public Result<String> execute(RockBlockMessage message) {
        if (message == null) {
            return Result.fail(422, "You need to specify a packet");
        }

        // Trigger the event only if data is > 0 and it is not a test message
        if (message.getData().isEmpty() || !message.getData().startsWith("20")) {
            System.out.println("ProcessRockblockMessageCommand::run() -> RockBlockMessage with empty data or test data");
            return Result.success("RockBlockMessage with empty data or test data");
        }

        // Create a CommunicationResponse from the hex data
        try {
            CommunicationResponse communicationResponse = CommunicationResponse.fromRockBlockMessage(message);
            RockBlockMessage save = messagesRepository.save(message);
        } catch (Exception e) {
            System.out.println("ProcessRockblockMessageCommand::run() -> Error creating CommunicationResponse from RockBlockMessage: " + e.getMessage());
            return Result.fail(422, "Error creating CommunicationResponse from RockBlockMessage: " + e.getMessage());
        }

        // TODO: Decode CommunicationResponse into a Specific Response

        // Trigger the event
//        eventBus.publish(...);

        return Result.success("RockBlockMessage processed successfully");
    }


}
