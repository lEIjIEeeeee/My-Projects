package com.accp.caro2o.mapper;

import com.accp.caro2o.entity.Comment;
import com.accp.caro2o.vo.CommentVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("select c.*, u.pic_url as 'userPic', u.name as 'userName' from comment as c left join user as u on c.user_id = u.id where c.store_id = #{storeId}")
    List<CommentVo> getListByStore(@Param("storeId") int storeId);

    @Select("select c.*, u.pic_url as 'userPic', u.name as 'userName' from comment as c inner join user as u on c.user_id = u.id where u.name like CONCAT('%',#{name},'%')")
    List<CommentVo> getListByName(String name);
}
