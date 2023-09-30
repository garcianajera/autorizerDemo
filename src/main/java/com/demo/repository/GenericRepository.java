package com.demo.repository;

import com.demo.model.BaseEntity;

import java.util.Collection;

public interface GenericRepository<T extends BaseEntity, U> {

    T save(T entity);

    T findById(U id);

    Collection<T> findAll();
}
