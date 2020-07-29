package com.accp.caro2o.controller;

import com.accp.caro2o.entity.User;
import com.accp.caro2o.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller                      //定义控制器类
@RequestMapping("/admin")        //处理请求的处理器，将请求映射到MVC和REST控制器的处理方法上
public class AdminController {

    @Resource                    //实现依赖注入，不用显示的new，直接创建私有新对象
    private UserService userService;

    @RequestMapping("")          //返回页面
    public String index(HttpSession session) {
        if (session.getAttribute("ADMIN") == null) {
            return "redirect:/admin/toLogin";
        }
        return "admin/index";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "admin/login";
    }

    @RequestMapping("/login")
    @ResponseBody
    public boolean login(User user, HttpSession session) {
        user = userService.login(user);
        if (user != null && user.getId() != 0 && user.getUsername().equals("admin")) {
            session.setAttribute("ADMIN", user);
        }
        return user != null && user.getId() != 0 && user.getUsername().equals("admin");
    }
}
