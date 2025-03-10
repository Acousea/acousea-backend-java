package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class NetworkModule extends SerializableModule implements ExtModule {
    public static final String name = "network";
    private Address localAddress;
    private RoutingTable routingTable;

    public NetworkModule(Address localAddress, RoutingTable routingTable) {
        super(ModuleCode.NETWORK, serialize(localAddress, routingTable));
        this.localAddress = localAddress;
        this.routingTable = routingTable;
    }

    public NetworkModule(int localAddress) {
        this(Address.fromValue(localAddress), new RoutingTable());
    }

    @Override
    public int getFullSize() {
        return Address.getSize() + routingTable.getPeerRoutes().size() * 2 * Byte.BYTES + Byte.BYTES;
    }

    public static int getMinSize() {
        return Address.getSize();
    }

    public static NetworkModule create(int address) {
        return new NetworkModule(address);
    }

    private static byte[] serialize(Address localAddress, RoutingTable routingTable) {
        int size = Address.getSize() + routingTable.getPeerRoutes().size() * 2 * Byte.BYTES + Byte.BYTES;
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.put(localAddress.getValue());
        routingTable.getPeerRoutes().forEach((destination, nextHop) -> {
            buffer.put(destination.getValue());
            buffer.put(nextHop.getValue());
        });
        buffer.put(routingTable.getDefaultGateway().getValue());
        return buffer.array();
    }

    public static NetworkModule fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < getMinSize()) {
            throw new IllegalArgumentException("Invalid byte array for NetworkModule");
        }
        Address localAddress = Address.fromValue(buffer.get());
        RoutingTable routingTable = new RoutingTable();
        while (buffer.remaining() > Byte.BYTES) {
            Address destination = Address.fromValue(buffer.get());
            Address nextHop = Address.fromValue(buffer.get());
            routingTable.addRoute(destination, nextHop);
        }
        routingTable.setDefaultGateway(Address.fromValue(buffer.get()));
        return new NetworkModule(localAddress, routingTable);
    }

    @Override
    public String toString() {
        return "NetworkModule{" +
                "localAddress=" + localAddress +
                ", routingTable=" + routingTable +
                '}';
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

        @Override
        public String toString() {
            return "RoutingTable{" +
                    "peerRoutes=" + peerRoutes +
                    ", defaultGateway=" + defaultGateway +
                    '}';
        }
    }
}
