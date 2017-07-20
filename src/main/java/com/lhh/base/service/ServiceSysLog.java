package com.lhh.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lhh.base.entity.EntitySysLog;

public interface ServiceSysLog {
    EntitySysLog save(EntitySysLog entity);
    Page<EntitySysLog> findAll(Pageable pageable);
    Page<EntitySysLog> findAllByExample(EntitySysLog sysLog, Pageable pageable);
}
