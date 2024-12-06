package com.acousea.backend.core.communicationSystem.application.command;

import com.acousea.backend.core.communicationSystem.application.ports.RockblockMessageRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationResponseProcessor;
import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResponse;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.NewNodeConfigurationPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.SummaryReportPayload;
import com.acousea.backend.core.shared.application.events.EventBus;
import com.acousea.backend.core.shared.application.notifications.NotificationService;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;

public class ProcessRockblockMessageCommand extends Command<RockBlockMessage, String> {
    private final RockblockMessageRepository messagesRepository;
    private final NotificationService notificationService;
    private final CommunicationResponseProcessor communicationResponseProcessor;
    private final EventBus eventBus;

    public ProcessRockblockMessageCommand(
            RockblockMessageRepository messagesRepository,
            CommunicationResponseProcessor communicationResponseProcessor,
            NotificationService notificationService,
            EventBus eventBus) {
        this.messagesRepository = messagesRepository;
        this.communicationResponseProcessor = communicationResponseProcessor;
        this.notificationService = notificationService;
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
        CommunicationResponse communicationResponse;
        try {
            communicationResponse = CommunicationResponse.fromRockBlockMessage(message);
            RockBlockMessage save = messagesRepository.save(message);
        } catch (Exception e) {
            System.out.println("ProcessRockblockMessageCommand::run() -> Error creating CommunicationResponse from RockBlockMessage: " + e.getMessage());
            return Result.fail(422, "Error creating CommunicationResponse from RockBlockMessage: " + e.getMessage());
        }

        // Process the response for the different operation codes
        switch (communicationResponse.getOperationCode()) {
            case SET_NODE_DEVICE_CONFIG, GET_UPDATED_NODE_DEVICE_CONFIG:
                communicationResponseProcessor.processNodeDeviceConfigurationResponse(
                        communicationResponse.getRoutingChunk(),
                        (NewNodeConfigurationPayload) communicationResponse.getPayload()
                );
                break;
            case SUMMARY_REPORT:
                communicationResponseProcessor.processSummaryReportResponse(
                        communicationResponse.getRoutingChunk(),
                        (SummaryReportPayload) communicationResponse.getPayload()
                );
                break;

            default:
                System.out.println("ProcessRockblockMessageCommand::run() -> Unknown operation code: " + communicationResponse.getOperationCode());
                return Result.fail(422, "Unknown operation code: " + communicationResponse.getOperationCode());


        }

        // Trigger the event
//        eventBus.publish(...);

        return Result.success("RockBlockMessage processed successfully");
    }


}
