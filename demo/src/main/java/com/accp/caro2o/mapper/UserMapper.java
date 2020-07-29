package com.accp.caro2o.mapper;


import com.accp.caro2o.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert("insert into user(name, username, password, type, create_time, pic_url) value('用户哈哈', #{username}, #{password}, #{type}, SYSDATE(), '/imges/getAvatar.do.jpg')")
    public int add(User user);

    @Select("select * from user where username = #{username} and password = #{password} limit 1")
    public User login(User user);

    @Update("update user set name=#{name}, phone=#{phone}, pic_url=#{picUrl}, address=#{address}, province=#{province}, city=#{city}, district=#{district}, longitude=#{longitude}, latitude=#{latitude} where id = #{id}")
    public int updateUser(User user);

    @Update("update user set username=#{username}, password=#{password}, type=#{type} where id = #{id}")
    public int updateUserByAdmin(User user);

    @Select("select * from user where name like CONCAT('%',#{name},'%') or username like CONCAT('%',#{name},'%')")
    public List<User> getListByName(@Param("name") String name);      //想要添加用户  提交页面
}
