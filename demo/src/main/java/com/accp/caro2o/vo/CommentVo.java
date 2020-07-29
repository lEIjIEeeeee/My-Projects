package com.accp.caro2o.vo;

import com.accp.caro2o.entity.Comment;
import lombok.Data;

@Data
public class CommentVo extends Comment {
    private String userPic;
    private String userName;
}
