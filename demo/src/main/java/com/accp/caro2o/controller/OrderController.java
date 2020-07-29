package com.accp.caro2o.controller;

import com.accp.caro2o.entity.StoreItem;
import com.accp.caro2o.entity.User;
import com.accp.caro2o.service.OrderService;
import com.accp.caro2o.service.StoreItemService;
import com.accp.caro2o.vo.OrderVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")    //订单
public class OrderController {

    @Resource
    private StoreItemService storeItemService;

    @Resource
    private OrderService orderService;

    @RequestMapping("")
    public String orderList(String name, Model model) {   //显示的默认订单列表
        List<OrderVo> list = orderService.getListByCode(name == null ? "" : name);  //根据订单编号查询
        model.addAttribute("list", list);
        model.addAttribute("size", list.size());
        return "/order/orderlist";
    }

    @RequestMapping("/toAddOrder")
    public String toAddOrder(String ids, Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/user/goLogin";
        }
        String[] id = ids.substring(0, ids.length() - 1).split(",");
        List<StoreItem> list = new ArrayList<>();   //new一个动态数组
        double price = 0l;    //价格初始化0
        for (StoreItem storeItem : storeItemService.getList()) {
            for (String i : id) {       //遍历表
                if (storeItem.getId() == Integer.parseInt(i)) {
                    if (storeItem.getPrice() == 0l) {
                        break;       //价格为0，跳出
                    }
                    price += storeItem.getPrice();   //价格递归相加
                    list.add(storeItem);
                }
            }
        }
        model.addAttribute("price", price);
        model.addAttribute("list", list);
        return "order/order";
    }

    @RequestMapping("/addOrder")
    @ResponseBody
    public String addOrder(@RequestBody List<StoreItem> list, HttpSession session) {

        if (session.getAttribute("USER") == null) {
            return "/user/goLogin";
        }

        User user = (User) session.getAttribute("USER");

        try {
            orderService.adds(list, user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return "#";
        }

        return "/user";
    }

    @RequestMapping("/updateStatus")  //更新订单状态
    public String updateStatus(HttpSession session, int id, int type) {
        if (session.getAttribute("USER") == null) {
            return "/user/goLogin";
        }
        orderService.updateStatus(id, type);
        return "redirect:/user";
    }

    @RequestMapping("/del")
    @ResponseBody
    public void del(int id) {
        orderService.del(id);
    }
}
