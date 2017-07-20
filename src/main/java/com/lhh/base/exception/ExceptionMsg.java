package com.lhh.base.exception;

/**
 * 异常信息类
 * @author hwaggLee
 *
 */
public class ExceptionMsg extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public static final String NotSignin = "未取到登录信息，请重新登录";
    public static final String UserExists = "用户已存在";
    public static final String AdminNotDelete="管理员不能被删除";
    public ExceptionMsg(String message) {
        super(message);
    }
}
