package com.lhh.core.enums;

/**
 * 功能操作枚举
 * @author hwaggLee
 * 2017年7月20日 下午12:36:15
 */
public enum EnumOperation {
    create("创建"),
    delete("删除"),
    update("修改"),
    signin("登录");
    private String text;
    private EnumOperation(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.name();
    }
    public String getText() {
        return this.text;
    }
    public String getName() {
        return this.name();
    }
    public static EnumOperation parse(String name) {
        for (EnumOperation entity:EnumOperation.values()) {
            if(entity.name().equals(name)) return entity;
        }
        return null;
    }
}
