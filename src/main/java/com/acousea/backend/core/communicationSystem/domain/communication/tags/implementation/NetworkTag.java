package com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.Tag;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagType;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;

import java.nio.ByteBuffer;

public class NetworkTag extends Tag {
    private NetworkTag(byte[] value) {
        super(TagType.NETWORK, value);
    }

    public static NetworkTag fromNetworkModule(NetworkModule module) {
        ByteBuffer buffer = ByteBuffer.allocate(1 + module.getRoutingTable().getPeerRoutes().size() * 2);
        buffer.put(module.getLocalAddress().getValue().toByte());
        module.getRoutingTable().getPeerRoutes().forEach((destination, nextHop) -> {
            buffer.put(destination.getValue().toByte());
            buffer.put(nextHop.getValue().toByte());
        });
        return new NetworkTag(buffer.array());
    }

    public NetworkModule toNetworkModule() {
        ByteBuffer buffer = ByteBuffer.wrap(this.VALUE);
        byte localAddress = buffer.get();
        NetworkModule module = new NetworkModule(localAddress);
        while (buffer.hasRemaining()) {
            byte destination = buffer.get();
            byte nextHop = buffer.get();
            module.getRoutingTable().addRoute(Address.of(destination), Address.of(nextHop));
        }
        return module;
    }

    public static NetworkTag fromBytes(ByteBuffer buffer) {
        int localAddress = buffer.get();
        NetworkModule module = new NetworkModule(localAddress);
        while (buffer.hasRemaining()) {
            Address destination = Address.of(buffer.get());
            Address nextHop = Address.of(buffer.get());
            module.getRoutingTable().addRoute(destination, nextHop);
        }
        return fromNetworkModule(module);
    }
}
