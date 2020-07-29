package com.accp.caro2o.service;

import com.accp.caro2o.entity.StoreItem;
import com.accp.caro2o.mapper.StoreItemMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StoreItemService {
    @Resource
    private StoreItemMapper storeItemMapper;

    public List<StoreItem> getList(int id) {
        return storeItemMapper.getListByStoreId(id);
    }

    public List<StoreItem> getList() {
        return storeItemMapper.selectList(null);
    }

    public boolean save(StoreItem storeItem) {
        if (storeItem.getId() == 0) {
            return storeItemMapper.insert(storeItem) > 0;
        } else {
            return storeItemMapper.updateById(storeItem) > 0;
        }
    }

    public boolean del(int id) {
        return storeItemMapper.deleteById(id) > 0;
    }

    public List<StoreItem> getTree(int pid, int storeId) {
        return storeItemMapper.queryByPidAndStoreId(storeId);
    }

}
