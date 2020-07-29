package com.accp.caro2o.service;

import com.accp.caro2o.entity.Order;
import com.accp.caro2o.entity.OrdersItem;
import com.accp.caro2o.entity.Store;
import com.accp.caro2o.entity.StoreItem;
import com.accp.caro2o.mapper.OrderMapper;
import com.accp.caro2o.mapper.OrdersItemMapper;
import com.accp.caro2o.mapper.StoreMapper;
import com.accp.caro2o.vo.OrderVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrdersItemMapper ordersItemMapper;

    @Resource
    private StoreMapper storeMapper;

    public List<OrderVo> getListByUserId(int userId, int type) {
        return orderMapper.getMyOrder(userId, type);
    }

    public List<OrderVo> getListByCode(String name) {
        return orderMapper.getListByCode(name);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean adds(List<StoreItem> list, int userId) {
        Order order = new Order();
        order.setCode(System.currentTimeMillis() + "");   //获取当前系统时间
        order.setCreateTime(new Date());   //获取日期
        order.setStoreId(list.get(0).getStoreId());   //订单状态
        order.setStoreUserId(storeMapper.selectById(list.get(0).getStoreId()).getUserId());
        order.setPrice(list.stream().mapToDouble(StoreItem::getPrice).sum());
        order.setUserId(userId);
        order.setType(0);
        orderMapper.insert(order);
        OrdersItem ordersItem = null;
        for (StoreItem storeItem : list) {
            ordersItem = new OrdersItem();
            ordersItem.setOid(order.getId());
            ordersItem.setCreateTime(new Date());
            ordersItem.setOrdersItem(storeItem.getName());
            ordersItem.setPrice(storeItem.getPrice());
            ordersItemMapper.insert(ordersItem);
        }
        return true;
    }

    public void updateStatus(int id, int type) {
        orderMapper.updateStatus(type, id);
    }

    public void del(int id) {
        ordersItemMapper.dleByPid(id);
        orderMapper.deleteById(id);
    }
}
