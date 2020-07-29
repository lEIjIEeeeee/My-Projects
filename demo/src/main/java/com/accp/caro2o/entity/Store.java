package com.accp.caro2o.entity;


import lombok.Data;

import java.util.Date;

@Data
public class Store {

    private int id;
    private String name;
    private int userId;
    private String address;
    private String picUrl;
    private String remark;
    private Date createData;
    private int visitNum;
    private String latitude;
    private String longitude;
    private String province;
    private String city;
    private String district;
    private String images;
}
