package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModeGraph;



import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.shared.domain.UnsignedByte;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class OperationModesGraphModule extends SerializableModule  {

    public record Transition(int targetMode, int duration) { }

    public static final String name = "operationModesGraph";
    private final Map<Integer, Transition> graph; // Nodo actual -> (Nodo siguiente, Duración)

    public OperationModesGraphModule(Map<Integer, Transition> graph) {
        super(ModuleCode.OPERATION_MODES_GRAPH);
        this.graph = graph;
    }

    public static OperationModesGraphModule fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < 3) { // Cada nodo necesita al menos 3 bytes (actual, siguiente, duración)
            throw new IllegalArgumentException("Invalid byte array for OperationModesGraphModule");
        }

        Map<Integer, Transition> graph = new HashMap<>();
        while (buffer.remaining() >= 3) {
            int currentMode = UnsignedByte.toUnsignedInt(buffer.get());
            int nextMode = UnsignedByte.toUnsignedInt(buffer.get());
            int duration = (UnsignedByte.toUnsignedInt(buffer.get()) << 8) | UnsignedByte.toUnsignedInt(buffer.get());
            graph.put(currentMode, new Transition(nextMode, duration));
        }

        return new OperationModesGraphModule(graph);
    }

    @Override
    public byte[] getVALUE() {
        List<Byte> bytes = new ArrayList<>();

        for (Map.Entry<Integer, Transition> entry : graph.entrySet()) {
            int currentMode = entry.getKey();
            Transition transition = entry.getValue();

            bytes.add((byte) currentMode);
            bytes.add((byte) transition.targetMode());
            bytes.add((byte) (transition.duration() >> 8)); // High byte of duration
            bytes.add((byte) (transition.duration() & 0xFF)); // Low byte of duration
        }

        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }
        return result;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("OperationModesGraphModule{\n");
        graph.forEach((currentMode, transition) -> builder.append("  Mode ")
                .append(currentMode)
                .append(" -> ")
                .append(transition)
                .append("\n"));
        builder.append("}");
        return builder.toString();
    }
}


class Main {
    public static void main(String[] args) {
        // Example usage
        Map<Integer, OperationModesGraphModule.Transition> graph = new HashMap<>();
        graph.put(0, new OperationModesGraphModule.Transition(1, 1000));
        graph.put(1, new OperationModesGraphModule.Transition(2, 2000));
        graph.put(2, new OperationModesGraphModule.Transition(0, 1500));

        OperationModesGraphModule module = new OperationModesGraphModule(graph);
        System.out.println(module);
    }
}

