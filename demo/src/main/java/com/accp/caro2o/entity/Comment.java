package com.accp.caro2o.entity;


import lombok.Data;

import java.util.Date;

@Data                   //通过注解的形式自动生成构造器、getter/setter、equals、hashcode、toString等方法
public class Comment {

    private int id;
    private int storeId;
    private int userId;
    private String text;
    private Date createTime;
    private int score;
    private String images;
    private int orderId;

}
