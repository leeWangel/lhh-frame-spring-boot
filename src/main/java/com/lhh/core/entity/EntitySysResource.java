package com.lhh.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lhh.base.entity.EntityAbstract;
import com.lhh.core.enums.EnumEntity;

@Entity
@Table(name="sys_resource")
public class EntitySysResource extends EntityAbstract {
	private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    /**父资源*/
    @ManyToOne
    private EntitySysResource parent;
    /**资源名称*/
    private String name;
    /**资源URL*/
    private String url;
    /**
     * 类型   1:菜单 2：按扭
     */
    private int type = 1;
    /**排序*/
    private int orderNum = 0;
    /**描述*/
    private String remark;
    /**状态*/
    private int status = 1;
    /**创建时间*/
    private Date createTime;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public EntitySysResource getParent() {
        return parent;
    }

    public void setParent(EntitySysResource parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
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

    @Override
    public EnumEntity getEntity() {
        return EnumEntity.sysresource;
    }
}
