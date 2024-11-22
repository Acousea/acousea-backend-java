package com.acousea.backend.core.communicationSystem.domain.nodes;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.PamModule;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Getter
@Setter
public class NodeDevice {

    @NotNull
    private final UUID id;
    @NotNull
    private final String name;
    @NotNull
    private String icon; // Campo nuevo para almacenar la URL completa del icono
    @NotNull
    private final Map<String, ExtModule> extModules;
    @NotNull
    private final List<PamModule> pamModules;

    public NodeDevice(@NotNull UUID id, @NotNull String name, @NotNull String icon, @NotNull Map<String, ExtModule> extModules, @NotNull List<PamModule> pamModules) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.extModules = extModules;
        this.pamModules = pamModules;
    }

    // Método para obtener un módulo específico
    public <T extends ExtModule> Optional<T> getModule(Class<T> moduleType) {
        return extModules.values().stream()
                .filter(moduleType::isInstance)
                .map(moduleType::cast)
                .findFirst();
    }

    @Override
    public String toString() {
        return "NodeDevice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", extModules=" + extModules +
                ", pamModules=" + pamModules +
                '}';
    }
}
