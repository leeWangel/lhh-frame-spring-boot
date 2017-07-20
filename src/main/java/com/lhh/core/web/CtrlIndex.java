package com.lhh.core.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lhh.core.shiro.ShiroUser;
import com.lhh.core.shiro.ShiroUtils;

@Controller
public class CtrlIndex {
    @RequestMapping("/index")
    public String index(Model model) {
        ShiroUser shiroUser = ShiroUtils.getShiroUser();

        if(shiroUser==null) {
            System.out.println("未登录");
            return "login_view";
        }
        model.addAttribute("shiroUser",shiroUser);
        return "index";
    }
    @RequestMapping("default")
    public String defaultView() {
        return "default";
    }
}
