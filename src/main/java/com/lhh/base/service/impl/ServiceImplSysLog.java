package com.lhh.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhh.base.entity.EntitySysLog;
import com.lhh.base.repository.RepositorySysLog;
import com.lhh.base.service.ServiceSysLog;
import com.lhh.base.utils.UtilsModel;

/**
 * 日志文件单独做记录
 * @author hwaggLee
 * 2017年7月20日 下午1:37:52
 */
@Service
public class ServiceImplSysLog implements ServiceSysLog {
    @Autowired
    private RepositorySysLog sysLogRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EntitySysLog save(EntitySysLog entity) {
        String id = entity.getId();
        if(StringUtils.isBlank(id)) {
            return sysLogRepository.save(entity);
        }
        EntitySysLog original = sysLogRepository.findOne(id);
        UtilsModel.transferValue(entity, original);
        return sysLogRepository.save(original);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EntitySysLog> findAll(Pageable pageable) {
        return sysLogRepository.findAll(pageable);
    }

    @Override
    public Page<EntitySysLog> findAllByExample(EntitySysLog sysLog, Pageable pageable) {
        if(sysLog==null) return sysLogRepository.findAll(pageable);

        Specification<EntitySysLog> sf = (Root<EntitySysLog> root, CriteriaQuery<?> cq, CriteriaBuilder cb)-> {
            List<Predicate> list = new ArrayList<>();
            if(sysLog.getEntity()!=null) list.add(cb.equal(root.get("entity"),sysLog.getEntity()));
            if(sysLog.getOperation()!=null) list.add(cb.equal(root.get("operation"),sysLog.getOperation()));
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return sysLogRepository.findAll(sf, pageable);
    }
}
