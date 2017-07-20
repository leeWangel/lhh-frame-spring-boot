package com.lhh.core.shiro;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import com.lhh.core.entity.EntitySysResource;
import com.lhh.core.entity.EntitySysRole;
import com.lhh.core.entity.EntitySysUser;
import com.lhh.core.service.ServiceSysUser;

/**
 * shiro缓存管理器
 * @author hwaggLee
 * 2017年7月20日 下午1:48:59
 */
@Component
public class ShiroCacheManager {
    private final Logger log = LoggerFactory.getLogger(ShiroCacheManager.class);
    @Autowired
    private ServiceSysUser sysUserService;
    @Autowired
    private Cache<String, ShiroUser> userCache;
    
    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;
    @PostConstruct
    public void afterPropertiesSet() {
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                //PUT YOUR CALL TO SERVICE HERE
                log.info("Initializing ShiroCacheManager");
                initUser();
            }
        });

    }
    
    private void initUser() {
        log.info("Initializing userCache");
        Assert.notNull(sysUserService,"SysUserService should not be null");
        Assert.notNull(userCache,"UserCache should not be null");

        List<EntitySysUser> sysUserList = null;
        try {
        	sysUserList = sysUserService.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
        if(sysUserList==null || sysUserList.isEmpty()) return;
        for (EntitySysUser sysUser: sysUserList)
            processUser(sysUser);
    }
    
    public void reInitUser() {
        userCache.clear();
        initUser();
    }
    public void reInit(EntitySysUser sysUser) {
    	processUser(sysUser);
    }
    /**
     * 设置用户的角色信息
     * @param sysUser
     */
    private void processUser(EntitySysUser sysUser){
        // 获取权限信息
        Set<String> sysRoleCodes = new HashSet<>();
        Set<String> urls = new HashSet<>();
        Set<EntitySysRole> sysRoleSet = sysUser.getSysRoleSet();
        
        if(sysRoleSet!=null && sysRoleSet.size()>0) {
            for (EntitySysRole sysRole: sysRoleSet) {
                sysRoleCodes.add(sysRole.getRoleCode());
                Set<EntitySysResource> sysResourceSet = sysRole.getSysResourceSet();
                if(sysResourceSet!=null && sysResourceSet.size()>0) {
                    for (EntitySysResource sysResource:sysResourceSet) {
                        urls.add(sysResource.getUrl());
                    }
                }
            }
        }
        ShiroUser shiroUser = new ShiroUser();
        try {
            BeanUtils.copyProperties(shiroUser, sysUser);
            shiroUser.setUserId(sysUser.getId());
            shiroUser.setRoleCodes(sysRoleCodes);
            shiroUser.setResourceUrls(urls);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        userCache.put(shiroUser.getUsername(),shiroUser);
    }
}
