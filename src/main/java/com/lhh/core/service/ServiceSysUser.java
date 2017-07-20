package com.lhh.core.service;

import com.lhh.base.service.ServiceBase;
import com.lhh.core.entity.EntitySysUser;

public interface ServiceSysUser extends ServiceBase<EntitySysUser> {
    EntitySysUser findByUsername(String username);
    EntitySysUser save(EntitySysUser entity, String roleIds);
}
