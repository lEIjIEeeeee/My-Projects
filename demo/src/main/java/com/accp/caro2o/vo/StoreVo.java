package com.accp.caro2o.vo;

import com.accp.caro2o.entity.Store;
import lombok.Data;

@Data
public class StoreVo extends Store implements Comparable<StoreVo> {
    private String username;
    private String phone;
    private int consumptionNum;
    private double range;
    private int sortrange;

    public void setRange(double score) {
        this.sortrange = (int) (score * 100000);
        this.range = score;
    }

    @Override
    public int compareTo(StoreVo o) {
        return this.getSortrange() - (o.getSortrange());
    }
}
