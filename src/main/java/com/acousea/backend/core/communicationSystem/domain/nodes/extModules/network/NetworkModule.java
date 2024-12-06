package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network;

import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Setter
@Getter
public class NetworkModule extends ExtModule {
    public static final String name = "network";
    private final Address localAddress;
    private final RoutingTable routingTable;

    public NetworkModule(int localAddress) {
        this.localAddress = Address.of(localAddress);
        this.routingTable = new RoutingTable();
    }

    public NetworkModule(Address localAddress, RoutingTable routingTable) {
        this.localAddress = localAddress;
        this.routingTable = routingTable;
    }

    @Override
    public int getFullSize() {
        return Address.getSize() + (routingTable.getPeerRoutes().size() * Byte.BYTES * 2);
    }

    public static int getMinSize() {
        return Address.getSize();
    }

    public static NetworkModule create(int address) {
        return new NetworkModule(address);
    }


    public static NetworkModule fromDTO(NodeDeviceDTO.ExtModuleDto.NetworkModuleDto network) {
        NetworkModule module = new NetworkModule(Address.of(network.getLocalAddress()).getValue());
        network.getRoutingTable().getPeerRoutes().forEach((destination, nextHop) -> {
            module.getRoutingTable().addRoute(Address.of(destination), Address.of(nextHop));
        });
        return module;
    }

    @Getter
    @Setter
    public static class RoutingTable {
        private final Map<Address, Address> peerRoutes = new HashMap<>();
        private Address defaultGateway = Address.getBackend();

        public void addRoute(Address destination, Address nextHop) {
            peerRoutes.put(destination, nextHop);
        }

        public void removeRoute(Address destination) {
            peerRoutes.remove(destination);
        }

        public Optional<Address> getNextHop(Address destination) {
            return Optional.ofNullable(peerRoutes.getOrDefault(destination, defaultGateway));
        }

        public void clear() {
            peerRoutes.clear();
            defaultGateway = null;
        }
    }
}
