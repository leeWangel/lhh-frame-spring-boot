package com.lhh.base.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lhh.base.entity.EntityBase;

public interface ServiceBase<T extends EntityBase> {
    T save(T entity);
    void delete(String id);
    void deleteByIds(String ids);
    List<T> findAll();
    Page<T> findAll(Pageable pageable);
    T findOne(String id);
}
