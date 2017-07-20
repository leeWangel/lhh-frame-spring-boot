package com.lhh.base.entity;

import org.hibernate.annotations.GenericGenerator;

import com.lhh.core.enums.EnumEntity;
import com.lhh.core.enums.EnumOperation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="sys_log")
public class EntitySysLog implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @Column(length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    private EnumOperation operation;
    private EnumEntity entity;
    @Column(length = 4000)
    private String originalValue;
    @Column(length = 4000)
    private String targetValue;
    @NotNull
    private Date createTime;
    @NotNull
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EnumOperation getOperation() {
        return operation;
    }

    public void setOperation(EnumOperation operation) {
        this.operation = operation;
    }

    public EnumEntity getEntity() {
        return entity;
    }

    public void setEntity(EnumEntity entity) {
        this.entity = entity;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
