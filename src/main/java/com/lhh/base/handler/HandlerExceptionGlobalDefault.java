package com.lhh.base.handler;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class HandlerExceptionGlobalDefault implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        // TODO Auto-generated method stub
        System.out.println("==============异常开始=============");
        //如果是shiro无权操作，因为shiro 在操作auno等一部分不进行转发至无权限url
        if(e instanceof UnauthorizedException){
            ModelAndView mv = new ModelAndView("unauthorized");
            return mv;
        }
        e.printStackTrace();
        System.out.println("==============异常结束=============");
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("exception", e.toString().replaceAll("\n", "<br/>"));
        return mv;
    }
}
