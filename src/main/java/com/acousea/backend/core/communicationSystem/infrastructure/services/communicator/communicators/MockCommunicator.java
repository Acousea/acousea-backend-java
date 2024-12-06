package com.acousea.backend.core.communicationSystem.infrastructure.services.communicator.communicators;

import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.application.services.CommunicationRequestLoggerService;
import com.acousea.backend.core.communicationSystem.application.services.Communicator;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationRequest;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationResult;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockCommunicator implements Communicator {
    private static final String BASE_URL = "https://rockblock.rock7.com/rockblock/MT";
    private final String username;
    private final String password;

    private final CommunicationRequestLoggerService logger;
    private final Environment environment;
    private final NodeDeviceRepository nodeDeviceRepository;

    public MockCommunicator(
            CommunicationRequestLoggerService logger,
            NodeDeviceRepository nodeDeviceRepository,
            Environment environment
    ) {
        this.logger = logger;
        this.environment = environment;
        this.nodeDeviceRepository = nodeDeviceRepository;
        this.username = environment.getProperty("iridium.username");
        this.password = environment.getProperty("iridium.password");
    }

    @Override
    public CommunicationResult send(CommunicationRequest packet) {
        logger.log(packet);
        String imei = getImeiByNetworkAddress(packet.getRoutingChunk().receiver());
        String url = String.format(
                "%s?imei=%s&username=%s&password=%s&data=%s",
                BASE_URL, imei, username, password, packet.encode()
        );

        System.out.println("MockCommunicator.send -> url: " + url);

        return CommunicationResult.success("MOCK: Message sent successfully");
    }

    @Override
    public CommunicationResult flushRequestQueue(UUID nodeId) {
        String imei = getImeiByNodeId(nodeId);
        String url = String.format("%s?imei=%s&username=%s&password=%s&flush=yes", BASE_URL, imei, username, password);
        return CommunicationResult.success("MOCK: Request queue flushed successfully");
    }

    private String getImeiByNodeId(UUID nodeId) {
        return getImei(nodeId, null);
    }

    private String getImeiByNetworkAddress(Address networkAddress) {
        return getImei(null, networkAddress);
    }

    private String getImei(UUID nodeId, Address networkAddress) {
        NodeDevice node;
        if (nodeId != null) {
            node = nodeDeviceRepository.findById(nodeId).orElseThrow(() -> new IllegalArgumentException("Node not found"));
        } else {
            node = nodeDeviceRepository.findByNetworkAddress(networkAddress).orElseThrow(() -> new IllegalArgumentException("Node not found"));
        }
        IridiumReportingModule iridiumReportingModule = node.getModule(IridiumReportingModule.class).orElseThrow(() -> new IllegalArgumentException("IridiumCommunicator: Node does not have an IridiumReportingModule"));
        return iridiumReportingModule.getImei();
    }


}
