package com.lhh.core.repository;

import com.lhh.base.repository.RepositoryBase;
import com.lhh.core.entity.EntitySysUser;

public interface RepositorySysUser extends RepositoryBase<EntitySysUser, String> {
    EntitySysUser findByUsername(String username);
}
