package com.lhh.core.web;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhh.base.entity.EntitySysLog;
import com.lhh.base.service.ServiceSysLog;
import com.lhh.base.utils.Result;
import com.lhh.core.enums.EnumOperation;
import com.lhh.core.shiro.ShiroUser;
import com.lhh.core.shiro.ShiroUtils;

@Controller
public class CtrlLogin {
    @Autowired
    private ServiceSysLog serviceSysLog;

    @RequestMapping("/")
    public String loginView() {
        return "login_view";
    }
    @RequestMapping("login")
    @ResponseBody
    public Result login(String u, String p) {
        if(StringUtils.isBlank(u)) {
            return Result.failure("用户名不能为空");
        }
        if(StringUtils.isBlank(p)) {
            return Result.failure("密码不能为空");
        }
        String password = DigestUtils.md5Hex(p);
        try {
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(u, password);
            subject.login(token);
            saveLoginLog();
            return Result.success();
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            return Result.failure(e.getMessage());
        } catch (AuthenticationException e) {
            return Result.failure("账号验证失败");
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
    /**
     * 退出
     *
     * @return redirect URL
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        ShiroUtils.logout();
        return this.loginView();
    }
    @Async
    private void saveLoginLog() {
        ShiroUser shiroUser = ShiroUtils.getShiroUser();
        if (shiroUser == null) {
            return;
        }

        EntitySysLog sysLog = new EntitySysLog();
        sysLog.setOperation(EnumOperation.signin);
        sysLog.setUserName(shiroUser.getUsername());
        sysLog.setCreateTime(new Date());
        serviceSysLog.save(sysLog);
    }
}
