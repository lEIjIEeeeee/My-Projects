package com.accp.caro2o.service;

import com.accp.caro2o.entity.User;
import com.accp.caro2o.mapper.UserMapper;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public boolean insert(User user) {
        return userMapper.insert(user) > 0;
    }

    public boolean register(User user) {
        return userMapper.add(user) > 0;
    }

    public User login(User user) {
        return userMapper.login(user);
    }

    public User queryById(int id) {
        return userMapper.selectById(id);
    }

    public boolean delete(int id) {
        return userMapper.deleteById(id) > 0;
    }

    public int update(User user) {
        return userMapper.updateUser(user);
    }

    public int updateUserByAdmin(User user) {
        return userMapper.updateUserByAdmin(user);
    }

    public List<User> getList(String name) {
        return userMapper.getListByName(name == null ? "" : name);
    }

    public boolean del(int id) {
        return userMapper.deleteById(id) > 0;
    }

}
