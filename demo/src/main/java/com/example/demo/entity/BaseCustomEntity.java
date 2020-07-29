package com.example.demo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* 自定义BaseEntity，对diboot-core的BaseEntity做差异化定义
* @author MyName
* @version 1.0
* @date 2020-07-24
* Copyright © MyCompany
*/
@Getter @Setter @Accessors(chain = true)
public abstract class BaseCustomEntity extends BaseEntity {
    private static final long serialVersionUID = -8540797974526447484L;


}
