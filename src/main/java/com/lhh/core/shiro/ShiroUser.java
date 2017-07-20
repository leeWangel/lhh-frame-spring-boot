package com.lhh.core.shiro;

import java.util.Date;
import java.util.Set;

public class ShiroUser {

    private String userId;
    private String username;
    private transient String password;
    private Integer status;
    private Date createTime;

    private Set<String> roleCodes;
    private Set<String> resourceUrls;


    public Set<String> getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(Set<String> roleCodes) {
        this.roleCodes = roleCodes;
    }

    public Set<String> getResourceUrls() {
        return resourceUrls;
    }

    public void setResourceUrls(Set<String> resourceUrls) {
        this.resourceUrls = resourceUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
