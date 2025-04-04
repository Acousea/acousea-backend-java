package com.acousea.backend.core.communicationSystem.infrastructure.ports.InMemory;

import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.infrastructure.mocks.MockNodeDevices;
import com.acousea.backend.core.shared.infrastructure.ports.InMemoryIRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class InMemoryNodeDeviceRepository extends InMemoryIRepository<NodeDevice, UUID> implements NodeDeviceRepository {

    @Override
    public Optional<NodeDevice> findByNetworkAddress(Address networkAddress) {
        return this.findAll().stream()
                .filter(
                        nodeDevice -> ((NetworkModule) nodeDevice.getExtModules().get(NetworkModule.name)).
                                getLocalAddress().equals(networkAddress))
                .findFirst();


    }

    public InMemoryNodeDeviceRepository(MockNodeDevices mockNodeDevices) {
        super(NodeDevice::getId);
        List<NodeDevice> mockNodes = mockNodeDevices.generate();
        mockNodes.forEach(this::save);
        System.out.println("InMemoryNodeDeviceRepository created with nodes: " + this.findAll().toString());
    }

}