package com.acousea.backend.core.shared.application.ports;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {
    T save(T entity);                      // Crear o actualizar una entidad
    Optional<T> findById(ID id);           // Leer una entidad por ID
    List<T> findAll();                     // Leer todas las entidades
    void deleteById(ID id);                // Eliminar una entidad por ID
    void deleteAll();                      // Eliminar todas las entidades
}

