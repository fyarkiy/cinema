package com.cinema.service;

import java.util.List;

public interface GenericService<T> {
    T add(T entity);

    List<T> getAll();

    T getById(Long id);
}
