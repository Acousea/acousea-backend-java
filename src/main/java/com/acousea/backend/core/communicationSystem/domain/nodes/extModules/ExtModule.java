package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import lombok.Getter;

@Getter
public abstract class ExtModule {
    // Método para obtener el nombre del módulo
    private final String name;

    // Constructor que asigna el nombre del módulo
    protected ExtModule(String name) {
        this.name = name;
    }

    // Método para obtener el tamaño total del módulo
    public abstract int getFullSize();

}
