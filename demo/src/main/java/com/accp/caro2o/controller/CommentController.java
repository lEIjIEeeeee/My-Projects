package com.accp.caro2o.controller;

import com.accp.caro2o.entity.Comment;
import com.accp.caro2o.entity.User;
import com.accp.caro2o.service.CommentService;
import com.accp.caro2o.service.OrderService;
import com.accp.caro2o.vo.CommentVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/comment")      //评论
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private OrderService orderService;

    @RequestMapping("")
    public String index(Model model, String name) {    //评论页面 默认显示 评论列表
        List<CommentVo> list = commentService.getListByName(name == null ? "" : name);  //列表根据用户名显示
        model.addAttribute("list", list);
        model.addAttribute("size", list.size());
        return "comment/CommentList";
    }

    @RequestMapping("/gotoAdd")      //去评论
    public String gotoAdd(int storeId, int orderId, Model model) { //商品编号，订单号
        model.addAttribute("storeId", storeId);
        model.addAttribute("orderId", orderId);
        return "comment/AddComment";
    }

    @RequestMapping("/add")   //评论添加，上传更新
    public String add(Comment comment, @RequestParam("file") MultipartFile multipartFile, HttpSession session) { //获取上传文件的名称
        if (session.getAttribute("USER") == null) {
            return "redirect:/user/goLogin";
        }
        User user = (User) session.getAttribute("USER");
        comment.setUserId(user.getId());
        //创建输入输出流
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static";
            String name = "/img/" + System.currentTimeMillis() + multipartFile.getOriginalFilename();
            inputStream = multipartFile.getInputStream();
            File file = new File(path + name);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            outputStream = new FileOutputStream(file);
            //最后使用资源访问器FileCopyUtils的copy方法拷贝文件
            FileCopyUtils.copy(inputStream, outputStream);
            comment.setImages(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        comment.setCreateTime(new Date());
        commentService.add(comment);
        orderService.updateStatus(comment.getOrderId(), 3);
        return "redirect:/user";
    }

    @RequestMapping("/del")
    @ResponseBody
    public void del(int id) {
        commentService.del(id);
    }
}
