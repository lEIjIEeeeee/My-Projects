package com.accp.caro2o.controller;

import com.accp.caro2o.entity.Store;
import com.accp.caro2o.entity.StoreItem;
import com.accp.caro2o.entity.User;
import com.accp.caro2o.service.CommentService;
import com.accp.caro2o.service.StoreItemService;
import com.accp.caro2o.service.StoreService;
import com.accp.caro2o.service.UserService;
import com.accp.caro2o.vo.StoreVo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/store")   //店铺
public class StoreController {

    @Resource
    private StoreService storeService;

    @Resource
    private StoreItemService storeItemService;

    @Resource
    private UserService userService;

    @Resource
    private CommentService commentService;

    @RequestMapping("")     //默认店铺列表
    public String index(Model model, String name, Integer type, HttpSession session) {
        model.addAttribute("list", storeService.listAll(name, type == null ? 1 : type, session));
        model.addAttribute("type", type == null ? 1 : type);        //默认按热门排序
        model.addAttribute("name", name == null ? "" : name);       //其他方式排序
        return "store/store";
    }

    @RequestMapping("/myStore")    //显示我的店铺  列表
    public String myStore(HttpSession session, Model model, String name) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/user/goLogin";
        }
        User user = (User) session.getAttribute("USER");
        List<StoreVo> list = storeService.list(name, user.getId());
        model.addAttribute("list", list);
        return "store/myStore";
    }

    @RequestMapping("/gotoStoreAdd")  //添加我的店铺
    public String gotoStoreAdd(HttpSession session, Model model) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/user/goLogin";
        }
        model.addAttribute("userList", userService.getList(null));
        return "store/storeAdd";
    }

    @RequestMapping("/storeAdd")
    public String add(Store store, @RequestParam("file") MultipartFile[] multipartFiles, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/user/goLogin";
        }
        User user = (User) session.getAttribute("USER");
        if (!user.getUsername().equals("admin")) {
            store.setUserId(user.getId());
        }

        //创建输入输出流
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            int first = 0;
            StringBuilder imgs = new StringBuilder();
            for (MultipartFile multipartFile : multipartFiles) {
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
                if (first++ == 0) {
                    store.setPicUrl(name);
                } else {
                    imgs.append(name + ",");
                }
            }
            store.setImages(imgs.substring(0, imgs.length() - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        store.setVisitNum(0);
        store.setCreateData(new Date());
        storeService.add(store);
        return "redirect:/store/myStore";
    }

    @RequestMapping("/gotoStoreItemAdd")
    public String gotoStoreItemAdd(int id, Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/user/goLogin";
        }
        model.addAttribute("id", id);
        model.addAttribute("list", storeItemService.getList(id));
        return "store/storeAddItem";
    }

    @RequestMapping("/details")
    public String details(int id, Model model) {
        storeService.addVisitNum(id);
        Store storeVo = storeService.getDetails(id);
        model.addAttribute("store", storeVo);
        model.addAttribute("CommentList", commentService.getListByStore(id));
        model.addAttribute("imgList", storeVo.getImages() != null && !storeVo.getImages().isEmpty() ? storeVo.getImages().split(",") : new ArrayList<>());
        return "store/storeDetails";
    }

    @RequestMapping("/storeList")
    public String storeList(Model model, String name) {
        List<StoreVo> list = storeService.list(name, 1);
        model.addAttribute("size", list.size());
        model.addAttribute("list", list);
        return "/store/storeList";
    }

    @RequestMapping("/saveItem")
    public String save(StoreItem storeItem) {
        storeItemService.save(storeItem);
        return "redirect:/store/gotoStoreItemAdd?id=" + storeItem.getStoreId();
    }

    @RequestMapping("/del")
    public String del(int id) {
        storeService.del(id);
        return "redirect:/store/myStore";
    }

    @RequestMapping("/delItem")
    public String delItem(int id, int storeId) {
        storeItemService.del(id);
        return "redirect:/store/gotoStoreItemAdd?id=" + storeId;
    }

    @RequestMapping("/storeEdit")
    public String storeEdit(Model model, int id, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/user/goLogin";
        }
        model.addAttribute("userList", userService.getList(null));
        model.addAttribute("store", storeService.getById(id));
        return "store/storeEdit";
    }

    @RequestMapping("/updateStore")
    public String updateStore(Store store, @RequestParam("file") MultipartFile[] multipartFiles, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/user/goLogin";
        }
        User user = (User) session.getAttribute("USER");
        if (store.getUserId() == 0) {
            store.setUserId(user.getId());
        }
        //创建输入输出流
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Store store1 = storeService.getById(store.getId());
        try {
            if (multipartFiles.length != 0) {
                int first = 0;
                StringBuilder imgs = new StringBuilder();
                for (MultipartFile multipartFile : multipartFiles) {
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
                    if (first++ == 0) {
                        store.setPicUrl(name);
                    } else {
                        imgs.append(name + ",");
                    }
                }
                store.setImages(imgs.substring(0, imgs.length() - 1));
            } else {
                store.setPicUrl(store1.getPicUrl());
                store.setImages(store1.getImages());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        store.setVisitNum(store1.getVisitNum());
        store.setCreateData(store1.getCreateData());

        storeService.update(store);
        return "redirect:/store/myStore";
    }

    @RequestMapping("/getTree")
    @ResponseBody
    public List<StoreItem> getTree(int storeId) {
        return storeItemService.getTree(0, storeId);
    }

}
