package com.lhh.core.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.lhh.base.entity.EntityAbstract;
import com.lhh.core.enums.EnumEntity;

@Entity
@Table(name="sys_user")
public class EntitySysUser extends EntityAbstract {
	private static final long serialVersionUID = 1L;
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @Column(unique = true)
    private String username;
    private String password;
    private Date createTime;
    private int status = 1;
    @Cascade(CascadeType.SAVE_UPDATE)
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="sys_join_user_role",
            joinColumns={@JoinColumn(name="fk_user_id")},
            inverseJoinColumns={@JoinColumn(name="fk_role_id")})
    private Set<EntitySysRole> sysRoleSet;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public EnumEntity getEntity() {
        return EnumEntity.sysuser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<EntitySysRole> getSysRoleSet() {
        return sysRoleSet;
    }

    public void setSysRoleSet(Set<EntitySysRole> sysRoleSet) {
        this.sysRoleSet = sysRoleSet;
    }
}
