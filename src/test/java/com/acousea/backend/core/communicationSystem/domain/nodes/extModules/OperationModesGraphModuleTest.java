package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModeGraph.OperationModesGraphModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OperationModesGraphModuleTest {

    @Test
    void testSerialization() {
        // Given: A known graph with transitions
        Map<Integer, OperationModesGraphModule.Transition> graph = new HashMap<>();
        graph.put(1, new OperationModesGraphModule.Transition(2, 300));
        graph.put(2, new OperationModesGraphModule.Transition(3, 600));

        OperationModesGraphModule operationModesGraphModule = new OperationModesGraphModule(graph);

        // When: We serialize the OperationModesGraphModule
        byte[] serializedBytes = operationModesGraphModule.toBytes();

        // Then: The length should match expectations (1 byte TYPE, 1 byte length, 4 bytes per transition)
        Assertions.assertEquals(serializedBytes.length, 10); // 2 bytes for TYPE and length, 8 bytes for 2 transitions (4 bytes each)

        // Checking TYPE and length byte
        Assertions.assertEquals(serializedBytes[0], (byte) ModuleCode.OPERATION_MODES_GRAPH.getValue());
        Assertions.assertEquals(serializedBytes[1], (byte) (graph.size() * 4)); // Length of data (4 bytes per transition)

        // Checking serialized transitions
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, graph.size() * 4);
        int mode1 = buffer.get();
        int nextMode1 = buffer.get();
        int duration1 = (buffer.get() << 8) | buffer.get();

        int mode2 = buffer.get();
        int nextMode2 = buffer.get();
        int duration2 = (buffer.get() << 8) | buffer.get();

        assertEquals(mode1, 1);
        assertEquals(nextMode1, 2);
        assertEquals(duration1, 300);

        assertEquals(mode2, 2);
        assertEquals(nextMode2, 3);
        assertEquals(duration2, 600);
    }

    @Test
    void testDeserialization() {
        // Given: A known graph structure
        ByteBuffer buffer = ByteBuffer.allocate(8)
                .put((byte) 1).put((byte) 2).put((byte) (300 >> 8)).put((byte) (300 & 0xFF))  // Transition 1
                .put((byte) 2).put((byte) 3).put((byte) (600 >> 8)).put((byte) (600 & 0xFF)); // Transition 2
        buffer.flip(); // Prepare buffer for reading

        // When: We deserialize the bytes
        OperationModesGraphModule operationModesGraphModule = OperationModesGraphModule.fromBytes(buffer);

        // Then: The reconstructed OperationModesGraphModule should match the original
        assertEquals(operationModesGraphModule.getGraph().size(), 2);
        assertTrue(operationModesGraphModule.getGraph().containsKey(1));
        assertTrue(operationModesGraphModule.getGraph().containsKey(2));

        OperationModesGraphModule.Transition transition1 = operationModesGraphModule.getGraph().get(1);
        OperationModesGraphModule.Transition transition2 = operationModesGraphModule.getGraph().get(2);

        assertEquals(transition1.targetMode(), 2);
        assertEquals(transition1.duration(), 300);
        assertEquals(transition2.targetMode(), 3);
        assertEquals(transition2.duration(), 600);
    }

    @Test
    void testDeserializationWithInvalidData() {
        // Given: A buffer that is too short
        ByteBuffer buffer = ByteBuffer.allocate(2); // Not enough bytes for even one transition

        // Expect: An exception due to insufficient data
        assertThrows(IllegalArgumentException.class, () -> OperationModesGraphModule.fromBytes(buffer));
    }

    @Test
    void testEmptyGraphSerialization() {
        // Given: An empty graph
        Map<Integer, OperationModesGraphModule.Transition> emptyGraph = new HashMap<>();
        OperationModesGraphModule module = new OperationModesGraphModule(emptyGraph);

        // When: We serialize the module
        byte[] serializedBytes = module.toBytes();

        // Then: It should contain only the TYPE and length byte
        Assertions.assertEquals(serializedBytes.length, 2);
        Assertions.assertEquals(serializedBytes[0], (byte) ModuleCode.OPERATION_MODES_GRAPH.getValue());
        Assertions.assertEquals(serializedBytes[1], (byte) 0); // No data means length is zero
    }
}
