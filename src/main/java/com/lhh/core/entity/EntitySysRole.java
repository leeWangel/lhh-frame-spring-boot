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
@Table(name="sys_role")
public class EntitySysRole extends EntityAbstract {
	private static final long serialVersionUID = 1L;
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    private String roleCode;
    private String name;
    private String remark;
    private int status = 1;
    private Date createTime;

    @Cascade(CascadeType.SAVE_UPDATE)
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="sys_join_role_resource",
            joinColumns={@JoinColumn(name="fk_role_id")},
            inverseJoinColumns={@JoinColumn(name="fk_resource_id")})
    private Set<EntitySysResource> sysResourceSet;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Set<EntitySysResource> getSysResourceSet() {
        return sysResourceSet;
    }

    public void setSysResourceSet(Set<EntitySysResource> sysResourceSet) {
        this.sysResourceSet = sysResourceSet;
    }

    @Override
    public EnumEntity getEntity() {
        return EnumEntity.sysrole;
    }

}
