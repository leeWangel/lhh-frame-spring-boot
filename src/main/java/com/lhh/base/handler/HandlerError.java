package com.lhh.base.handler;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.lhh.base.utils.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * 发现异常接收机制
 * @author hwaggLee
 * 2017年7月20日 下午1:40:01
 */
@ControllerAdvice
public class HandlerError {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 默认异常跳转
     * @param req
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception ex) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("exception", ex);
        mv.addObject("url", req.getRequestURL());
        mv.setViewName("error");

        logger.error(ex.getMessage(), ex);
        return mv;
    }

    /**
     * 运行时异常跳转
     * @param req
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Result jsonErrorHandler(HttpServletRequest req, Exception ex) throws Exception {
        Result result = Result.exception(req.getRequestURL().toString(), ex.getMessage());

        logger.error("url: {}", req.getRequestURL().toString());
        logger.error(ex.getMessage(), ex);
        return result;
    }
    
    /**
     * 无权限访问时跳转页面
     * @param req
     * @param ex
     * @param model
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorizedErrorHandler(HttpServletRequest req, Exception ex, Model model) throws Exception {
        model.addAttribute("url", req.getRequestURL().toString());
        return "unauthorized";
    }
}
