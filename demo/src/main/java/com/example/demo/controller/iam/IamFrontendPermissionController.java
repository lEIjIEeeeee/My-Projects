package com.example.demo.controller.iam;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.diboot.core.controller.BaseCrudRestController;
import com.diboot.core.util.BeanUtils;
import com.diboot.core.util.V;
import com.diboot.core.vo.*;
import com.diboot.iam.annotation.BindPermission;
import com.diboot.iam.annotation.Operation;
import com.diboot.iam.annotation.process.ApiPermissionCache;
import com.diboot.iam.config.Cons;
import com.diboot.iam.dto.IamFrontendPermissionDTO;
import com.diboot.iam.entity.IamFrontendPermission;
import com.diboot.iam.service.IamFrontendPermissionService;
import com.diboot.iam.vo.IamFrontendPermissionListVO;
import com.diboot.iam.vo.IamFrontendPermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
* 前端资源权限相关Controller
* @author MyName
* @version 1.0
* @date 2020-07-24
* Copyright © MyCompany
*/
@RestController
@RequestMapping("/iam/frontendPermission")
@Slf4j
@BindPermission(name = "前端资源权限")
public class IamFrontendPermissionController extends BaseCrudRestController<IamFrontendPermission> {

    @Autowired
    private IamFrontendPermissionService iamFrontendPermissionService;

    /***
    * 查询ViewObject的分页数据
    * <p>
    * url请求参数示例: /list?field=abc&pageSize=20&pageIndex=1&orderBy=id
    * </p>
    * @return
    * @throws Exception
    */
    @BindPermission(name = "查看列表", code = Operation.LIST)
    @GetMapping("/list")
    public JsonResult getViewObjectListMapping(IamFrontendPermission entity) throws Exception{
        QueryWrapper<IamFrontendPermission> queryWrapper = super.buildQueryWrapper(entity);
        queryWrapper.lambda().orderByDesc(IamFrontendPermission::getSortId, IamFrontendPermission::getId);
        List<IamFrontendPermissionListVO> voList = iamFrontendPermissionService.getViewObjectList(queryWrapper, null, IamFrontendPermissionListVO.class);
        voList = BeanUtils.buildTree(voList);
        return JsonResult.OK(voList);
    }

    /***
    * 根据资源id查询ViewObject
    * @param id ID
    * @return
    * @throws Exception
    */
    @BindPermission(name = "查看详情", code = Operation.DETAIL)
    @GetMapping("/{id}")
    public JsonResult getViewObjectMapping(@PathVariable("id")Long id) throws Exception{
        return super.getViewObject(id, IamFrontendPermissionVO.class);
    }

    /***
    * 新建菜单项、按钮/权限列表
    * @param iamFrontendPermissionDTO
    * @return
    * @throws Exception
    */
    @BindPermission(name = "新建", code = Operation.CREATE)
    @PostMapping("/")
    public JsonResult createEntityMapping(@Valid @RequestBody IamFrontendPermissionDTO iamFrontendPermissionDTO) throws Exception {
        iamFrontendPermissionService.createMenuAndPermissions(iamFrontendPermissionDTO);
        return JsonResult.OK();
    }

    /***
    * 更新用户、账号和用户角色关联列表
    * @param iamFrontendPermissionDTO
    * @return JsonResult
    * @throws Exception
    */
    @PutMapping("/{id}")
    @BindPermission(name = "更新", code = Operation.UPDATE)
    public JsonResult updateEntityMapping(@PathVariable("id") Long id, @Valid @RequestBody IamFrontendPermissionDTO iamFrontendPermissionDTO) throws Exception {
        iamFrontendPermissionService.updateMenuAndPermissions(iamFrontendPermissionDTO);
        return JsonResult.OK();
    }

    /***
    * 删除用户、账号和用户角色关联列表
    * @param id
    * @return
    * @throws Exception
    */
    @DeleteMapping("/{id}")
    @BindPermission(name = "删除", code = Operation.DELETE)
    public JsonResult deleteEntityMapping(@PathVariable("id")Long id) throws Exception {
        iamFrontendPermissionService.deleteMenuAndPermissions(id);
        return JsonResult.OK();
    }

    /***
    * 加载更多数据
    * @return
    * @throws Exception
    */
    @GetMapping("/attachMore")
    public JsonResult attachMore(ModelMap modelMap) throws Exception{
        // 获取关联表数据IamFrontendPermission的树状列表
        List<IamFrontendPermissionListVO> menuList = iamFrontendPermissionService.getViewObjectList(
            Wrappers.<IamFrontendPermission>lambdaQuery().eq(IamFrontendPermission::getDisplayType, Cons.FRONTEND_PERMISSION_DISPLAY_TYPE.MENU.name()),
            null,
            IamFrontendPermissionListVO.class
        );
        menuList = BeanUtils.buildTree(menuList);
        modelMap.put("menuList", menuList);
        // 获取关联数据字典USER_STATUS的KV
        List<KeyValue> frontendPermissionCodeKvList = dictionaryService.getKeyValueList(Cons.DICTTYPE.FRONTEND_PERMISSION_CODE.name());
        modelMap.put("frontendPermissionCodeKvList", frontendPermissionCodeKvList);
        return JsonResult.OK(modelMap);
    }

    /**
    * 列表排序
    * @param permissionList
    * @return
    * @throws Exception
    */
    @PostMapping("/sortList")
    @BindPermission(name="列表排序", code = Operation.UPDATE)
    public JsonResult sortList(@RequestBody List<IamFrontendPermission> permissionList) throws Exception {
        iamFrontendPermissionService.sortList(permissionList);
        return JsonResult.OK().msg("更新成功");
    }

    /***
    * api接口列表（供前端选择）
    * @return
    * @throws Exception
    */
    @GetMapping("/apiList")
    public JsonResult apiList() throws Exception{
        return JsonResult.OK(ApiPermissionCache.getApiPermissionVoList());
    }

    /***
    * 检查菜单编码是否重复
    * @param id
    * @param code
    * @return
    */
    @GetMapping("/checkCodeDuplicate")
    public JsonResult checkCodeDuplicate(@RequestParam(required = false) Long id, @RequestParam String code) {
        if (V.notEmpty(code)) {
            LambdaQueryWrapper<IamFrontendPermission> wrapper = Wrappers.<IamFrontendPermission>lambdaQuery()
                .select(IamFrontendPermission::getId)
                .eq(IamFrontendPermission::getFrontendCode, code)
                .eq(IamFrontendPermission::getDisplayType, Cons.FRONTEND_PERMISSION_DISPLAY_TYPE.MENU.name());
            if (V.notEmpty(id)) {
                wrapper.ne(IamFrontendPermission::getId, id);
            }
            boolean exists = iamFrontendPermissionService.exists(wrapper);
            if (exists) {
                return JsonResult.FAIL_VALIDATION("编码已存在: "+code);
            }
        }
        return JsonResult.OK();
    }

}