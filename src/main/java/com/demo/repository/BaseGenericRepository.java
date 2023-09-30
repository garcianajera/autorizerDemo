package com.demo.repository;

import com.demo.exception.RepositoryException;
import com.demo.model.BaseEntity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BaseGenericRepository<T extends BaseEntity> implements GenericRepository<T, UUID> {
    // LinkedHashMap preserves insertion order
    private Map<UUID, T> _localStorage = new LinkedHashMap<>();

    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new RepositoryException("Entity should not be null");
        }
        // Assign an Id to new entities
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        getLocalStorage().put(entity.getId(), entity);
        return entity;
    }

    @Override
    public T findById(UUID id) {
        return _localStorage.get(id);
    }

    @Override
    public Collection<T> findAll() {
        return _localStorage.values();
    }

    private Map<UUID, T> getLocalStorage() {
        return _localStorage;
    }

}
