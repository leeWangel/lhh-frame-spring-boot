package com.lhh.core.enums;

/**
 * 功能权限配置列表
 * @author hwaggLee
 *
 */
public enum EnumEntity {
    sysuser("用户管理"),
    sysrole("角色管理"),
    sysresource("资源管理");
    private String text;
    private EnumEntity(String text) {
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
    public static EnumEntity parse(String name) {
        for (EnumEntity entity:EnumEntity.values()) {
            if(entity.name().equals(name)) return entity;
        }
        return null;
    }
}
