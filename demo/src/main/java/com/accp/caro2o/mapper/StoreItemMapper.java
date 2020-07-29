package com.accp.caro2o.mapper;

import com.accp.caro2o.entity.StoreItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StoreItemMapper extends BaseMapper<StoreItem> {

    @Select("select * from store_item where store_id = #{storeId}")
    public List<StoreItem> getListByStoreId(@Param("storeId") int storeId);

    @Select("select * from store_item where store_id = #{storeId}")
    List<StoreItem> queryByPidAndStoreId(@Param("storeId") int storeId);

}
