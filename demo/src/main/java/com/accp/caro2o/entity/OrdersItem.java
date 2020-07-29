package com.accp.caro2o.entity;


import lombok.Data;

import java.util.Date;

@Data
public class OrdersItem {

    private long id;
    private long oid;
    private String ordersItem;
    private double price;
    private Date createTime;

}
