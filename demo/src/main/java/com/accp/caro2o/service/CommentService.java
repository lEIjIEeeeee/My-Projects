package com.accp.caro2o.service;

import com.accp.caro2o.entity.Comment;
import com.accp.caro2o.mapper.CommentMapper;
import com.accp.caro2o.vo.CommentVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    public void add(Comment comment) {
        commentMapper.insert(comment);
    }

    public List<CommentVo> getListByStore(int storeId) {
        return  commentMapper.getListByStore(storeId);
    }

    public List<CommentVo> getListByName(String name) {
        return  commentMapper.getListByName(name);
    }

    public void del(int id) {
        commentMapper.deleteById(id);
    }
}
