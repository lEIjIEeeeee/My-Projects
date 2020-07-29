package com.accp.caro2o.controller;

import com.accp.caro2o.entity.User;
import com.accp.caro2o.service.OrderService;
import com.accp.caro2o.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("/user")   //用户主界面
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private OrderService orderService;

    @RequestMapping("")       //页面返回
    public String goUser(HttpSession session, Model model) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/user/goLogin";
        }
        User user = (User) session.getAttribute("USER");
        model.addAttribute("list", orderService.getListByUserId(user.getId(), user.getType()));
        return "user/user";
    }

    @RequestMapping("/goLogin")   //登录
    public String goLogin() {
        return "login";
    }

    @RequestMapping(value = "/toRegister")   //注册
    public String gotoRegister() {
        return "register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody             //返回登录用户数据
    public boolean login(User user, HttpServletRequest request) {
        user = userService.login(user);
        if (user != null && user.getId() != 0) {
            request.getSession().setAttribute("USER", user);
        }
        return user != null && user.getId() != 0;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody   //返回注册用户数据
    public boolean register(User user) {
        return userService.register(user);
    }

    @RequestMapping("/del")
    @ResponseBody    //删除用户数据
    public boolean del(int id) {
        return userService.del(id);
    }

    @RequestMapping(value = "/userInfo")   //个人资料
    public String userInfo() {
        return "user/information";
    }

    @RequestMapping(value = "/userList")   //用户管理
    public String userList(Model model, String name) {
        List<User> list = userService.getList(name);
        model.addAttribute("list", list);
        model.addAttribute("size", list.size());
        return "admin/admin-list";
    }

    @RequestMapping("/userEdit")      //用户信息编辑
    public String userEdit(int id, Model model) {
        model.addAttribute("user", userService.queryById(id));
        return "admin/admin-edit";
    }

    @RequestMapping("/update")    //编辑后更新
    @ResponseBody
    public boolean edit(User user) {
        return userService.updateUserByAdmin(user) > 0;
    }

    @RequestMapping(value = "/userAdd")     //添加用户
    public String userAdd(Model model) {
        return "admin/admin-add";
    }

    @RequestMapping("/logout")   //用户退出
    public String logout(HttpSession session) {
        if (session.getAttribute("USER") != null) {
            session.removeAttribute("USER");
        }
        return "redirect:/user/goLogin";
    }

    @RequestMapping("/updateUser")
    public String updateUser(User user, @RequestParam("file") MultipartFile multipartFile, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/user/goLogin";
        }
        User user1 = (User) session.getAttribute("USER");
        //创建输入输出流
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            if (multipartFile.isEmpty()) {
                user.setPicUrl(user1.getPicUrl());
            } else {
                String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static";

                String name = "/img/" + System.currentTimeMillis() + multipartFile.getOriginalFilename();

                user.setPicUrl(name);

                inputStream = multipartFile.getInputStream();

                File file = new File(path + name);

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }

                outputStream = new FileOutputStream(file);

                //最后使用资源访问器FileCopyUtils的copy方法拷贝文件
                FileCopyUtils.copy(inputStream, outputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        user.setId(user1.getId());
        user.setType(user1.getType());

        if (userService.update(user) > 0) {
            session.setAttribute("USER", user);
        }

        return "redirect:/user/userInfo";
    }
}
