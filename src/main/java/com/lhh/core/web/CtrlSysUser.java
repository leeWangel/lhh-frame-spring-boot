package com.lhh.core.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhh.base.exception.ExceptionMsg;
import com.lhh.base.utils.Result;
import com.lhh.core.entity.EntitySysRole;
import com.lhh.core.entity.EntitySysUser;
import com.lhh.core.service.ServiceSysRole;
import com.lhh.core.service.ServiceSysUser;
import com.lhh.core.shiro.ShiroCacheManager;
import com.lhh.core.shiro.SysRoleByUserVo;

@Controller
@RequestMapping("/sys_user")
@RequiresPermissions("/sys_user")
public class CtrlSysUser {
    private final Logger log = LoggerFactory.getLogger(CtrlSysUser.class);
    @Autowired
    private ServiceSysUser serviceSysUser;
    @Autowired
    private ServiceSysRole serviceSysRole;
    @Autowired
    private ShiroCacheManager shiroCacheManager;

    @RequestMapping("")
    public String index(
            @PageableDefault(sort={"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        Page<EntitySysUser> pageList = serviceSysUser.findAll(pageable);
        model.addAttribute("pageList", pageList);
        List<EntitySysRole> sysRoleList = serviceSysRole.findAll();
        model.addAttribute("sysRoleList", sysRoleList);
        return "core/sys_user/sys_user";
    }

    @RequestMapping("edit_view")
    public String editView(String id, Model model) {
        EntitySysUser entity = new EntitySysUser();
        if(StringUtils.isNotBlank(id)) {
            entity = serviceSysUser.findOne(id);
        }
        model.addAttribute("entity", entity);
        Set<EntitySysRole> sysRoleSet = entity.getSysRoleSet();
        List<EntitySysRole> sysRoleList = serviceSysRole.findAll();
        List<SysRoleByUserVo> voList = new ArrayList<>();
        for (EntitySysRole sysRole:sysRoleList) {
            boolean checked = sysRoleSet!=null && sysRoleSet.contains(sysRole);
            voList.add(new SysRoleByUserVo(sysRole, checked));
        }
        model.addAttribute("sysRoleList", voList);
        return "core/sys_user/sys_user_edit";
    }
    @RequestMapping("save")
    @ResponseBody
    public Result save(EntitySysUser param, String roleIds) {
        if(StringUtils.isBlank(param.getUsername())) {
            return Result.failure("用户名不能为空");
        }
        if(StringUtils.isBlank(roleIds)) {
            return Result.failure("必须选中至少一个角色");
        }
        if(StringUtils.isNotBlank(param.getPassword())) {
            param.setPassword(DigestUtils.md5Hex(param.getPassword()));
        } else param.setPassword(null);

        try {
            if(StringUtils.isBlank(param.getId())) {
                param.setCreateTime(new Date());
                serviceSysUser.save(param, roleIds);
            } else {
                serviceSysUser.save(param, roleIds);
            }
            shiroCacheManager.reInitUser();
        } catch(ExceptionMsg e) {
            log.error(e.getMessage());
            return Result.failure(e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            return Result.failure(e.getMessage());
        }
        return Result.success();
    }
    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Result delete(String ids) {
        try {
            serviceSysUser.deleteByIds(ids);
        } catch(ExceptionMsg e) {
            log.error(e.getMessage());
            return Result.failure(e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            return Result.failure(e.getMessage());
        }
        return Result.success();
    }

}
