package com.accp.caro2o.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private int id;
    private String name;
    private String username;
    private String password;
    private int type;
    private String phone;
    private String picUrl;
    private String address;
    private Date createTime;
    private String province;
    private String city;
    private String district;
    private String longitude;
    private String latitude;

}
