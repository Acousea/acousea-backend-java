package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import lombok.Getter;

@Getter
public abstract class ExtModule {

    // Método para obtener el tamaño total del módulo
    public abstract int getFullSize();

}
