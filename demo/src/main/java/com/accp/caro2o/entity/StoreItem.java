package com.accp.caro2o.entity;


import lombok.Data;

@Data
public class StoreItem {

    private int id;
    private String name;
    private double price;
    private int pid;
    private int storeId;

}
