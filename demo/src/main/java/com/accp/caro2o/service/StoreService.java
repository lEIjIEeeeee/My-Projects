package com.accp.caro2o.service;

import com.accp.caro2o.entity.Store;
import com.accp.caro2o.entity.User;
import com.accp.caro2o.mapper.StoreMapper;
import com.accp.caro2o.util.DistanceUtil;
import com.accp.caro2o.vo.StoreVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class StoreService {
    @Resource
    private StoreMapper storeMapper;

    public boolean add(Store store) {
        return storeMapper.insert(store) > 0;
    }

    public List<StoreVo> list(String name, int userId) {
        if (userId == 1) {
            return storeMapper.getListByName(name == null ? "" : name);
        } else {
            return storeMapper.getList(name == null ? "" : name, userId);
        }

    }

    public List<Store> listAll() {
        return storeMapper.selectList(null);
    }

    public List<StoreVo> listAll(String name, int type, HttpSession session) {
        //更具距离排序
        if (type == 0) {   //默认显示全部
            List<StoreVo> list = storeMapper.getListByName(name == null ? "" : name);

            if (session.getAttribute("USER") == null) {
                return list;
            }
            User user = (User) session.getAttribute("USER");

            //判断是否完善了自己的信息
            if (user.getLongitude() == null || user.getLatitude() == null || user.getLongitude().isEmpty() || user.getLatitude().isEmpty()) {
                return list;
            }
            double lon = Double.parseDouble(user.getLongitude());
            double lat = Double.parseDouble(user.getLatitude());
            for (StoreVo storeVo : list) {
                //计算距离
                double addLon = Double.parseDouble(storeVo.getLongitude());    //场地 精度
                double addLat = Double.parseDouble(storeVo.getLatitude());      //场地 维度
                storeVo.setRange(DistanceUtil.getDistance(lon, lat, addLon, addLat));
            }

            Collections.sort(list);  //排序

            return list;

        } else if (type == 1) {
            //更具消费排序
            return storeMapper.getListByConsumptionNum(name == null ? "" : name);
        } else {
            //更具人气排序
            return storeMapper.getListByVisitNum(name == null ? "" : name);
        }
    }

    public void del(int id) {
        storeMapper.deleteById(id);
    }

    public void addVisitNum(int id) {
        storeMapper.addVisitNum(id);
    }

    public Store getById(int id) {
        return storeMapper.selectById(id);
    }

    public StoreVo getDetails(int id) {
        return storeMapper.getDetails(id);
    }

    public void update(Store store) {
        storeMapper.updateById(store);
    }

}
