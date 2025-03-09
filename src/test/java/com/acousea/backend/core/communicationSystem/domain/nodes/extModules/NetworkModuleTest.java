package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkModuleTest {

    @Test
    void testSerialization() {
        // Given: A known network configuration
        Address localAddress = Address.fromValue((byte) 5);
        NetworkModule.RoutingTable routingTable = new NetworkModule.RoutingTable();
        routingTable.addRoute(Address.fromValue((byte) 10), Address.fromValue((byte) 20));
        routingTable.addRoute(Address.fromValue((byte) 15), Address.fromValue((byte) 25));
        routingTable.setDefaultGateway(Address.fromValue((byte) 1));

        NetworkModule networkModule = new NetworkModule(localAddress, routingTable);

        // When: We serialize the NetworkModule
        byte[] serializedBytes = networkModule.toBytes();

        // Then: The length should match expectations (1 byte for TYPE, 1 byte for length, address + routes + default gateway)
        Assertions.assertEquals(serializedBytes.length, 9);

        // Checking TYPE and length byte
        Assertions.assertEquals(serializedBytes[0], (byte) ModuleCode.NETWORK.getValue());
        Assertions.assertEquals(serializedBytes[1], (byte) 7); // 1 byte local, 4 bytes routes, 1 byte default gateway

        // Checking serialized values
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, 7);
        Address deserializedLocal = Address.fromValue(buffer.get());
        Address dest1 = Address.fromValue(buffer.get());
        Address nextHop1 = Address.fromValue(buffer.get());
        Address dest2 = Address.fromValue(buffer.get());
        Address nextHop2 = Address.fromValue(buffer.get());
        Address defaultGateway = Address.fromValue(buffer.get());

        assertEquals(deserializedLocal.getValue(), localAddress.getValue());
        assertEquals(dest1.getValue(), 10);
        assertEquals(nextHop1.getValue(), 20);
        assertEquals(dest2.getValue(), 15);
        assertEquals(nextHop2.getValue(), 25);
        assertEquals(defaultGateway.getValue(), 1);
    }

    @Test
    void testDeserialization() {
        // Given: A known network configuration
        ByteBuffer buffer = ByteBuffer.allocate(7)
                .put((byte) 5)  // Local address
                .put((byte) 10).put((byte) 20)  // Route 1: 10 -> 20
                .put((byte) 15).put((byte) 25)  // Route 2: 15 -> 25
                .put((byte) 1); // Default gateway
        buffer.flip(); // Prepare buffer for reading

        // When: We deserialize the bytes
        NetworkModule networkModule = NetworkModule.fromBytes(buffer);

        // Then: The reconstructed NetworkModule should match the original
        assertEquals(networkModule.getLocalAddress().getValue(), 5);
        assertEquals(networkModule.getRoutingTable().getPeerRoutes().size(), 2);
        assertEquals(networkModule.getRoutingTable().getPeerRoutes().get(Address.fromValue((byte) 10)).getValue(), 20);
        assertEquals(networkModule.getRoutingTable().getPeerRoutes().get(Address.fromValue((byte) 15)).getValue(), 25);
        assertEquals(networkModule.getRoutingTable().getDefaultGateway().getValue(), 1);
    }

    @Test
    void testDeserializationWithInvalidData() {
        // Given: A buffer that is too short
        ByteBuffer buffer = ByteBuffer.allocate(0); // Not enough bytes

        // Expect: An exception due to insufficient data
        assertThrows(IllegalArgumentException.class, () -> NetworkModule.fromBytes(buffer));
    }

    @Test
    void testAddingAndRemovingRoutes() {
        // Given: A routing table
        NetworkModule.RoutingTable routingTable = new NetworkModule.RoutingTable();
        Address dest = Address.fromValue((byte) 10);
        Address nextHop = Address.fromValue((byte) 20);

        // When: Adding a route
        routingTable.addRoute(dest, nextHop);

        // Then: Route should be present
        assertEquals(routingTable.getPeerRoutes().size(), 1);
        assertEquals(routingTable.getPeerRoutes().get(dest).getValue(), nextHop.getValue());

        // When: Removing the route
        routingTable.removeRoute(dest);

        // Then: Route should be removed
        assertEquals(routingTable.getPeerRoutes().size(), 0);
    }

    @Test
    void testDefaultGatewayBehavior() {
        // Given: A routing table with a default gateway
        NetworkModule.RoutingTable routingTable = new NetworkModule.RoutingTable();
        routingTable.setDefaultGateway(Address.fromValue((byte) 42));

        // When: Looking up a non-existent route
        Optional<Address> nextHop = routingTable.getNextHop(Address.fromValue((byte) 99));

        // Then: It should return the default gateway
        assertTrue(nextHop.isPresent());
        assertEquals(nextHop.get().getValue(), 42);
    }
}
