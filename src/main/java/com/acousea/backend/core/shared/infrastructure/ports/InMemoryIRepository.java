package com.acousea.backend.core.shared.infrastructure.ports;


import com.acousea.backend.core.shared.application.ports.IRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InMemoryIRepository<T, ID> implements IRepository<T, ID> {

    protected final Map<ID, T> storage = new ConcurrentHashMap<>();  // Almacenamiento en memoria
    private final Function<T, ID> idExtractor;                      // Función para extraer el ID de la entidad

    public InMemoryIRepository(Function<T, ID> idExtractor) {
        this.idExtractor = idExtractor;
    }

    @Override
    public T save(T entity) {
        ID id = idExtractor.apply(entity);  // Obtener el ID usando la función
        storage.put(id, entity);            // Guardar o actualizar la entidad
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));  // Obtener una entidad por ID
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());     // Obtener todas las entidades
    }

    @Override
    public void deleteById(ID id) {
        storage.remove(id);                            // Eliminar una entidad por ID
    }

    @Override
    public void deleteAll() {
        storage.clear();                               // Eliminar todas las entidades
    }
}

