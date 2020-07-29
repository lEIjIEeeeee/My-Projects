package com.accp.caro2o.mapper;

import com.accp.caro2o.entity.Order;
import com.accp.caro2o.vo.OrderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    List<OrderVo> getMyOrder(@Param("userId") int userId, @Param("type") int type);

    List<OrderVo> getListByCode(@Param("name") String name);

    @Update("update orders set type = #{type} where id = #{id}")
    void updateStatus(@Param("type") int type, @Param("id") int id);
}
