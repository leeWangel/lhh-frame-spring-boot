package com.lhh.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhh.base.repository.RepositoryBase;
import com.lhh.base.service.impl.ServiceImplBase;
import com.lhh.core.entity.EntitySysResource;
import com.lhh.core.repository.RepositorySysResource;
import com.lhh.core.service.ServiceSysResource;

@Service
public class ServiceImplSysResource extends ServiceImplBase<EntitySysResource> implements ServiceSysResource {
    @Autowired
    private RepositorySysResource repositorySysResource;

    @Override
    protected RepositoryBase<EntitySysResource,String> getRepository() {
        return repositorySysResource;
    }
}
