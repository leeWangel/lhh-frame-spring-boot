package com.lhh.base.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lhh.base.entity.EntitySysLog;
import com.lhh.base.service.ServiceSysLog;
import com.lhh.core.enums.EnumEntity;
import com.lhh.core.enums.EnumOperation;

@Controller
public class CtrlSysLog {
    @Autowired
    private ServiceSysLog serviceSysLog;
    @RequiresPermissions("/sys_log")
    @RequestMapping("/sys_log")
    public String index(
            @PageableDefault(sort={"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
            String entity, String operation, Model model) {
        EntitySysLog query = new EntitySysLog();
        query.setEntity(EnumEntity.parse(entity));
        query.setOperation(EnumOperation.parse(operation));
        model.addAttribute("obj", query);
        Page<EntitySysLog> pageList = serviceSysLog.findAllByExample(query, pageable);
        model.addAttribute("pageList", pageList);
        pushDictToModel(model);
        return "core/sys_log";
    }
    private void pushDictToModel(Model model) {
        model.addAttribute("entityEnums", EnumEntity.values());
        model.addAttribute("operationEnums", EnumOperation.values());
    }
}
