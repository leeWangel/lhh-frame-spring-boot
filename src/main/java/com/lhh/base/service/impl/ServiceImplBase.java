package com.lhh.base.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lhh.FixedUserEnum;
import com.lhh.base.entity.EntityBase;
import com.lhh.base.entity.InterfaceLogSet;
import com.lhh.base.entity.EntitySysLog;
import com.lhh.base.exception.ExceptionMsg;
import com.lhh.base.repository.RepositoryBase;
import com.lhh.base.service.ServiceBase;
import com.lhh.base.service.ServiceSysLog;
import com.lhh.base.utils.UtilsModel;
import com.lhh.core.enums.EnumOperation;
import com.lhh.core.shiro.ShiroUser;
import com.lhh.core.shiro.ShiroUtils;

@Transactional(rollbackFor = Exception.class)
public abstract class ServiceImplBase<T extends EntityBase> implements ServiceBase<T> {

    private final Logger log = LoggerFactory.getLogger(ServiceImplBase.class);
    @Autowired
    private ServiceSysLog sysLogService;

    protected abstract RepositoryBase<T,String> getRepository();

    @Override
    public T save(T entity) {
        String id = entity.getId();
        if(StringUtils.isBlank(id)) {
            saveLog(null, entity);
            return (T)getRepository().save(entity);
        }
        T original = this.findOne(id);
        saveLog(original, entity);
        UtilsModel.transferValue(entity, original);
        return (T)getRepository().save(original);
    }

    @Override
    public void delete(String id) {
        saveLog(this.findOne(id), null);
        getRepository().delete(id);
    }

    @Override
    public void deleteByIds(String ids) {
        if(StringUtils.isBlank(ids)) return;
        String[] ss = ids.split(",");
        if(ss==null || ss.length==0) return;
        for (String id: ss) {
            this.delete(id);
        }
    }


    @Transactional(readOnly = true)
    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<T> findAll(Pageable pageable) {

        return getRepository().findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public T findOne(String id) {
        return (T)getRepository().findOne(id);
    }

    protected void saveLog(T originalEntity, T newEntity) {
        if(originalEntity==null && newEntity == null) return;
        EntitySysLog sysLog = new EntitySysLog();
        sysLog.setCreateTime(new Date());
        String threadName = Thread.currentThread().getName();
        FixedUserEnum fixedUserEnum = FixedUserEnum.parse(threadName);
        if(fixedUserEnum!=null) {
            sysLog.setUserName(fixedUserEnum.name());
        } else {
            try {
                ShiroUser shiroUser = ShiroUtils.getShiroUser();
                if (shiroUser == null) throw new ExceptionMsg(ExceptionMsg.NotSignin);
                sysLog.setUserName(shiroUser.getUsername());
            } catch (UnavailableSecurityManagerException e) {
                log.error(e.getMessage());
                throw new ExceptionMsg(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExceptionMsg(e.getMessage());
            }
        }
        if(originalEntity==null) { //保存
            InterfaceLogSet logSet = (InterfaceLogSet)newEntity.getClass().getAnnotation(InterfaceLogSet.class);
            if(logSet!=null && !logSet.add()) return;
            sysLog.setEntity(newEntity.getEntity());
            sysLog.setOperation(EnumOperation.create);
            sysLog.setTargetValue(JSONObject.toJSONString(newEntity));
        } else if(newEntity == null) { //删除
            InterfaceLogSet logSet = (InterfaceLogSet)originalEntity.getClass().getAnnotation(InterfaceLogSet.class);
            if(logSet!=null && !logSet.delete()) return;
            sysLog.setEntity(originalEntity.getEntity());
            sysLog.setOperation(EnumOperation.delete);
            sysLog.setOriginalValue(JSONObject.toJSONString(originalEntity));
        } else { //修改
            InterfaceLogSet logSet = (InterfaceLogSet)originalEntity.getClass().getAnnotation(InterfaceLogSet.class);
            if(logSet!=null && !logSet.update()) return;
            sysLog.setEntity(originalEntity.getEntity());
            sysLog.setOperation(EnumOperation.update);
            sysLog.setOriginalValue(JSONObject.toJSONString(originalEntity));
            sysLog.setTargetValue(JSONObject.toJSONString(newEntity));
        }
        sysLogService.save(sysLog);
    }
}
