package com.accp.caro2o.mapper;

import com.accp.caro2o.entity.OrdersItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrdersItemMapper extends BaseMapper<OrdersItem> {

    @Delete("delete from orders_item where oid = #{pid}")
    int dleByPid(@Param("pid") int pid);
}
