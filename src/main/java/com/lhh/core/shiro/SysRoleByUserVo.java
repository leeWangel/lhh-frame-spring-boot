package com.lhh.core.shiro;

import com.lhh.core.entity.EntitySysRole;

public class SysRoleByUserVo {
    public SysRoleByUserVo(EntitySysRole sysRole, boolean checked) {
        this.id = sysRole.getId();
        this.name = sysRole.getName();
        this.status = sysRole.getStatus();
        this.checked = checked;
    }
    private String id;
    private String name;
    private int status;
    private boolean checked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
