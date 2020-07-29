package com.example.demo.controller.iam;

import com.diboot.core.controller.BaseCrudRestController;
import com.diboot.core.vo.*;
import com.diboot.iam.annotation.BindPermission;
import com.diboot.iam.annotation.Operation;
import com.diboot.iam.config.Cons;
import com.diboot.iam.entity.IamLoginTrace;
import com.diboot.iam.vo.IamLoginTraceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
* 登录日志
* @author MyName
* @version 1.0
* @date 2020-07-24
* Copyright © MyCompany
*/
@RestController
@RequestMapping("/iam/loginTrace")
@Slf4j
@BindPermission(name = "登录日志")
public class IamLoginTraceController extends BaseCrudRestController<IamLoginTrace> {

    /***
    * 查询分页数据
    * @return
    * @throws Exception
    */
    @GetMapping("/list")
    @BindPermission(name = "查看列表", code = Operation.LIST)
    public JsonResult getViewObjectListMapping(IamLoginTrace entity, Pagination pagination) throws Exception{
        return super.getViewObjectList(entity, pagination, IamLoginTraceVO.class);
    }

    /**
    * 加载更多数据
    * @return
    * @throws Exception
    */
    @GetMapping("/attachMore")
    public JsonResult attachMore(ModelMap modelMap) throws Exception {
        // 获取关联数据字典AUTH_TYPE的KV
        List<KeyValue> authTypeKvList = dictionaryService.getKeyValueList(Cons.DICTTYPE.AUTH_TYPE.name());
        modelMap.put("authTypeKvList", authTypeKvList);
        return JsonResult.OK(modelMap);
    }

}