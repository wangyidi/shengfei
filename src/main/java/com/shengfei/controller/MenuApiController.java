package com.shengfei.controller;



import com.shengfei.entity.Permission;
import com.shengfei.service.PermissionService;
import com.shengfei.shiro.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/api/admin/setting/menu")
public class MenuApiController {

    @Autowired
    PermissionService permissionService;

    @ApiOperation(value = "菜单列表")
    @GetMapping("/list")
    public PageVO<Permission> list() {
        List<Permission> list = permissionService.list();
        return PageVO.pageVO(list, (long) list.size());
    }

}
