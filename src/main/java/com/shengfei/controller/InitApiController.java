package com.shengfei.controller;


import com.shengfei.dto.Child;
import com.shengfei.dto.Init;
import com.shengfei.dto.Menu;
import com.shengfei.dto.MenuInfo;
import com.shengfei.entity.Permission;
import com.shengfei.entity.Role;
import com.shengfei.entity.SysToken;
import com.shengfei.service.PermissionService;
import com.shengfei.service.RoleService;
import com.shengfei.service.ShiroService;
import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.utils.TokenUtil;
import com.shengfei.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Api(tags = "初始化接口")
@RestController
@RequestMapping("/api")
public class InitApiController {

    @Autowired
    ShiroService shiroService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    RoleService roleService;

    @ApiOperation(value = "获取当前用户的菜单")
    @GetMapping("/init")
    public ResultVO init(HttpServletRequest request) {
        String token = TokenUtil.getRequestToken(request);
        Init init = new Init();
        // 菜单加载
        getMenuInfo(token, init);

//      JSONUtil.toJsonStr(init);
        return ResultVO.success(init,"成功");
    }


    private void getMenuInfo(String token, Init init) {
        SysToken accessToken = shiroService.findByToken(token);
        List<Role> roles = roleService.getUserRoles(accessToken.getUserId().toString());
        if(ValidatorUtils.empty(roles)){
            init.setMenuInfo(null);
            return;
        }
        List<Permission> userResource = permissionService.getUserPermission(roles.get(0).getId().toString());
        MenuInfo menuInfo = new MenuInfo();
        List<Menu> list = set(userResource, 0);
        if (list.size() > 0) {
            menuInfo.setMenu1(list.get(0));
        }
        if (list.size() > 1) {
            menuInfo.setMenu2(list.get(1));
        }
        if (list.size() > 2) {
            menuInfo.setMenu3(list.get(2));
        }
        if (list.size() > 3) {
            menuInfo.setMenu4(list.get(3));
        }
        if (list.size() > 4) {
            menuInfo.setMenu5(list.get(4));
        }
        init.setMenuInfo(menuInfo);
    }

    private List<Menu> set(List<Permission> userResource, Integer superId) {
        // 一级菜单
        List<Menu> list = new ArrayList<>();
        for (Permission sysResource : userResource) {
            if (sysResource.getPid() == superId) {
                // 权限菜单
                Menu menu = new Menu();
                menu.setId(Long.parseLong(sysResource.getId().toString()));
                menu.setTitle(sysResource.getName());
                menu.setIcon(sysResource.getIcon());

                List<Child> childList = setChild(userResource, sysResource.getId());
                if (childList.size() > 0) {
                    menu.setChild(childList);
                }
                list.add(menu);
            }
        }
        return list;
    }

    private List<Child> setChild(List<Permission> userResource, Integer superId) {
        // 2级菜单
        List<Child> list = new ArrayList<>();
        for (Permission sysResource : userResource) {
            if (superId == sysResource.getPid()) {
                Child child = new Child();
                child.setId(sysResource.getId());
                child.setTitle(sysResource.getName());
                child.setIcon(sysResource.getIcon());
                child.setHref(sysResource.getUrl());
                child.setTarget("_self");

                List<Child> childList = setChild(userResource, sysResource.getId());
                if (childList.size() > 0) {
                    child.setChild(childList);
                }
                list.add(child);
            }
        }
        return list;
    }
}
