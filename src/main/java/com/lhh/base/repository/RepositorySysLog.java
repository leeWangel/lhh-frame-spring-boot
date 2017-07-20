package com.lhh.base.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import com.lhh.base.entity.EntitySysLog;

public interface RepositorySysLog extends CrudRepository<EntitySysLog, String> {
    Page<EntitySysLog> findAll(Pageable pageable);
    Page<EntitySysLog> findAll(Specification<EntitySysLog> var1, Pageable var2);
}
