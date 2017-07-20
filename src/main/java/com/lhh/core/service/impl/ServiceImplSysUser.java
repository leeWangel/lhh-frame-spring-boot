package com.lhh.core.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhh.base.exception.ExceptionMsg;
import com.lhh.base.repository.RepositoryBase;
import com.lhh.base.service.impl.ServiceImplBase;
import com.lhh.base.utils.UtilsModel;
import com.lhh.core.entity.EntitySysRole;
import com.lhh.core.entity.EntitySysUser;
import com.lhh.core.repository.RepositorySysUser;
import com.lhh.core.service.ServiceSysUser;

@Service
public class ServiceImplSysUser extends ServiceImplBase<EntitySysUser> implements ServiceSysUser {
    @Autowired
    private RepositorySysUser repositorySysUser;

    @Override
    protected RepositoryBase<EntitySysUser,String> getRepository() {
        return repositorySysUser;
    }
    @Override
    public EntitySysUser save(EntitySysUser entity, String roleIds) {
        String id = entity.getId();
        synchronized (entity.getUsername()) {
            if(StringUtils.isBlank(id)) {
                EntitySysUser sysUser = repositorySysUser.findByUsername(entity.getUsername());
                if(sysUser!=null)
                    throw new ExceptionMsg(ExceptionMsg.UserExists);
                //新增
                saveLog(null, entity);
                pushRelRoles(entity, roleIds);
                sysUser = repositorySysUser.save(entity);
                return sysUser;
            }
            //修改
            EntitySysUser original = this.findOne(id);
            saveLog(original, entity);
            UtilsModel.transferValue(entity, original);
            pushRelRoles(original, roleIds);
            repositorySysUser.save(original);
            return original;
        }
    }
    private void pushRelRoles(EntitySysUser entity, String roleIds) {
        List<String> roleIdList = Arrays.asList(roleIds.split(","));
        Set<EntitySysRole> sysRoleSet = new HashSet<>();
        for (String roleId: roleIdList ) {
            EntitySysRole sysRole = new EntitySysRole();
            sysRole.setId(roleId);
            sysRoleSet.add(sysRole);
        }
        entity.setSysRoleSet(sysRoleSet);
    }

    @Override
    public void delete(String id) {
        EntitySysUser sysUser = this.findOne(id);
        if(sysUser==null) return;
        if("1".equals(sysUser.getId()) || "admin".equals(sysUser.getUsername()))
            throw new ExceptionMsg(ExceptionMsg.AdminNotDelete);
        saveLog(sysUser, null);
        getRepository().delete(id);
    }
    @Override
    public EntitySysUser findByUsername(String username) {
        return repositorySysUser.findByUsername(username);
    }

}
