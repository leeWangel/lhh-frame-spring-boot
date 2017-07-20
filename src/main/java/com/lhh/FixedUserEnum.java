package com.lhh;

/**
 * Created by yejia on 2017/6/27.
 */
public enum FixedUserEnum {
    main //系统，记录系统启动、异常等日志信息用到
    ; 
    public static FixedUserEnum parse(String name) {
        for (FixedUserEnum fixedUserEnum: FixedUserEnum.values()) {
            if(fixedUserEnum.name().equals(name)) return fixedUserEnum;
        }
        return null;
    }
}
