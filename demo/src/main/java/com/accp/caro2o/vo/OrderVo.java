package com.accp.caro2o.vo;

import com.accp.caro2o.entity.Order;
import lombok.Data;

@Data
public class OrderVo extends Order {
    private String items;
    private String storeName;
    private String storeUserName;
    private String storeUsrePhone;
    private String username;
    private String usrePhone;
}
