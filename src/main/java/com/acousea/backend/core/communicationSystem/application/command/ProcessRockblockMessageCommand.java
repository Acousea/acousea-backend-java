package com.acousea.backend.core.communicationSystem.application.command;

import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.application.ports.RockblockMessageRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationResponseProcessor;
import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResponse;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.BasicStatusReportPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.CompleteStatusReportPayload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation.NewNodeConfigurationPayload;
import com.acousea.backend.core.communicationSystem.domain.events.ReceivedCompleteStatusReportEvent;
import com.acousea.backend.core.communicationSystem.domain.events.ReceivedRockBlockMessageEvent;
import com.acousea.backend.core.communicationSystem.domain.events.UpdatedNodeConfigurationEvent;
import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.shared.application.events.EventBus;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;

public class ProcessRockblockMessageCommand extends Command<RockBlockMessage, String> {
    private final RockblockMessageRepository messagesRepository;
    private final NodeDeviceRepository nodeDeviceRepository;
    private final CommunicationResponseProcessor communicationResponseProcessor;
    private final EventBus eventBus;

    public ProcessRockblockMessageCommand(
            RockblockMessageRepository messagesRepository,
            CommunicationResponseProcessor communicationResponseProcessor,
            NodeDeviceRepository nodeDeviceRepository,
            EventBus eventBus) {
        this.messagesRepository = messagesRepository;
        this.communicationResponseProcessor = communicationResponseProcessor;
        this.nodeDeviceRepository = nodeDeviceRepository;
        this.eventBus = eventBus;
    }

    @Override
    public ApiResult<String> execute(RockBlockMessage message) throws InvalidPacketException {
        if (message == null) {
            return ApiResult.fail(422, "You need to specify a packet");
        }

        // Trigger the event only if data is > 0 and it is not a test message
        if (message.getData().isEmpty() || !message.getData().startsWith("20")) {
            System.out.println("ProcessRockblockMessageCommand::run() -> RockBlockMessage with empty data or test data");
            return ApiResult.success("RockBlockMessage with empty data or test data");
        }

        // Create a CommunicationResponse from the hex data
        CommunicationResponse communicationResponse = CommunicationResponse.fromRockBlockMessage(message);
        RockBlockMessage save = messagesRepository.save(message);
        eventBus.publish(new ReceivedRockBlockMessageEvent());

        // Trigger the event
        NodeDevice nodeDevice = nodeDeviceRepository.findByNetworkAddress(communicationResponse.getRoutingChunk().sender())
                .orElseThrow(
                        () -> new RuntimeException(this.getClass().getSimpleName() +
                                " NodeDevice not found for address: " +
                                communicationResponse.getRoutingChunk().sender())
                );

        // Process the response for the different operation codes
        switch (communicationResponse.getOperationCode()) {
            case SET_NODE_DEVICE_CONFIG, GET_UPDATED_NODE_DEVICE_CONFIG:
                communicationResponseProcessor.processSerializableModulesForNode(
                        nodeDevice,
                        ((NewNodeConfigurationPayload) communicationResponse.getPayload()).getSerializableModules()
                );
                eventBus.publish(
                        new UpdatedNodeConfigurationEvent(
                                new UpdatedNodeConfigurationEvent.Payload(
                                        nodeDevice.getName()
                                )
                        )
                );
                break;

            case COMPLETE_STATUS_REPORT:
                communicationResponseProcessor.processSerializableModulesForNode(
                        nodeDevice,
                        ((CompleteStatusReportPayload) communicationResponse.getPayload()).getSerializableModules()
                );
                eventBus.publish(
                        new ReceivedCompleteStatusReportEvent(
                                new ReceivedCompleteStatusReportEvent.Payload(
                                        nodeDevice.getName()
                                )
                        )
                );
                break;

            case BASIC_STATUS_REPORT:
                System.out.println(ProcessRockblockMessageCommand.class.getSimpleName() + "-> BASIC_STATUS_REPORT not implemented yet");
                communicationResponseProcessor.processSerializableModulesForNode(
                        nodeDevice, ((BasicStatusReportPayload) communicationResponse.getPayload()).getSerializableModules()
                );
                eventBus.publish(
                        new ReceivedCompleteStatusReportEvent(
                                new ReceivedCompleteStatusReportEvent.Payload(
                                        nodeDevice.getName()
                                )
                        )
                );
                break;

            default:
                System.out.println(ProcessRockblockMessageCommand.class.getSimpleName() + "-> Unknown operation code: " + communicationResponse.getOperationCode());
                return ApiResult.fail(422, "Unknown operation code: " + communicationResponse.getOperationCode());
        }


        return ApiResult.success("RockBlockMessage processed successfully");
    }


}
