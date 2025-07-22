package com.acousea.backend.core.communicationSystem.domain.nodes;

import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Getter
@Setter
public class NodeDevice {

    @NotNull
    private final UUID id;
    @NotNull
    private String name;
    @NotNull
    private String icon; // Campo nuevo para almacenar la URL completa del icono
    @NotNull
    private final Map<String, SerializableModule> serializableModulesMap;

    public NodeDevice(
            @NotNull UUID id,
            @NotNull String name,
            @NotNull String icon,
            @NotNull Map<String, SerializableModule> serializableModules) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.serializableModulesMap = serializableModules;

    }


    // Método para obtener un módulo específico
    public <T extends SerializableModule> Optional<T> getModule(Class<T> moduleType) {
        return serializableModulesMap.values().stream()
                .filter(moduleType::isInstance)
                .map(moduleType::cast)
                .findFirst();
    }

    @Override
    public String toString() {
        return "NodeDevice {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", extModules=" + serializableModulesMap +
                '}';
    }
}
