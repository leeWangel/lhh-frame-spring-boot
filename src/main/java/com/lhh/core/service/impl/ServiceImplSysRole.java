package com.lhh.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhh.base.repository.RepositoryBase;
import com.lhh.base.service.impl.ServiceImplBase;
import com.lhh.core.entity.EntitySysRole;
import com.lhh.core.repository.RepositorySysRole;
import com.lhh.core.service.ServiceSysRole;

@Service
public class ServiceImplSysRole extends ServiceImplBase<EntitySysRole> implements ServiceSysRole {
    @Autowired
    private RepositorySysRole repositorySysRole;

    @Override
    protected RepositoryBase<EntitySysRole,String> getRepository() {
        return repositorySysRole;
    }
}
