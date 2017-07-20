package com.lhh.base.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 继承map，实现结果统一规范
 * @author hwaggLee
 * 2017年7月20日 下午2:08:18
 */
public class Result extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public Result() {
		put("code", 0);
	}

	public static Result exception(String url, String errorMsg) {
		Result result = new Result();
		result.put("code", -1);
		result.put("url", url);
		result.put("error", errorMsg);
		return  result;
	}

	public static Result failure() {
		return failure(500, "未知异常，请联系管理员");
	}

	public static Result failure(String msg) {
		return failure(500, msg);
	}

	public static Result failure(int code, String msg) {
		Result result = new Result();
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	public static Result success(String msg) {
		Result result = new Result();
		result.put("code", 0);
		result.put("msg", msg);
		return result;
	}

	public static Result success(Map<String, Object> map) {
		Result result = new Result();
		result.put("code", 0);
		result.putAll(map);
		return result;
	}

	public static Result success() {
		return new Result();
	}

	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
