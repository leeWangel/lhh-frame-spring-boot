package com.lhh.base.entity;

import java.io.Serializable;

import com.lhh.core.enums.EnumEntity;

public interface EntityBase extends Serializable {
    String getId();
    void setId(String id);
    EnumEntity getEntity();
}
