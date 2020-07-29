package com.accp.caro2o.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("orders")
public class Order {
    private int id;
    private String code;
    private int userId;
    private int storeUserId;
    private int storeId;
    private int type;
    private double price;
    private Date createTime;
    private Date overTime;

}
