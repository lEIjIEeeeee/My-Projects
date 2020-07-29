package com.accp.caro2o.mapper;

import com.accp.caro2o.entity.Store;
import com.accp.caro2o.entity.Store;
import com.accp.caro2o.vo.StoreVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StoreMapper extends BaseMapper<Store> {

    @Select("select s.*, (select count(*) from orders where store_id = s.id) as 'consumptionNum' from store as s where s.name like CONCAT('%',#{name},'%') and user_id = #{userId}")
    List<StoreVo> getList(@Param("name") String name, @Param("userId") int userId);

    @Select("select s.*, (select count(*) from orders where store_id = s.id) as 'consumptionNum' from store as s where s.name like CONCAT('%',#{name},'%')")
    List<StoreVo> getListByName(@Param("name") String name);

    @Select("select s.*, cs.consumptionNum from store as s left join (select store_id, count(*) as 'consumptionNum'  from orders  group by store_id) as cs on cs.store_id = s.id where s.name like CONCAT('%',#{name},'%') order by cs.consumptionNum desc")
    List<StoreVo> getListByConsumptionNum(@Param("name") String name);   //根据消费排序

    @Select("select s.*, (select count(*) from orders where store_id = s.id) as 'consumptionNum' from store as s where s.name like CONCAT('%',#{name},'%') order by visit_num desc")
    List<StoreVo> getListByVisitNum(@Param("name") String name);       //根据访问数排序

    @Update("update store set visit_num = visit_num + 1 where id = #{id}")
    int addVisitNum(@Param("id") int id);

    @Select("select s.*,u.*, (select count(*) from orders where store_id = s.id) as 'consumptionNum' from store as s inner join user as u on s.user_id = u.id where s.id = #{id}")
    StoreVo getDetails(@Param("id") int id);
}
