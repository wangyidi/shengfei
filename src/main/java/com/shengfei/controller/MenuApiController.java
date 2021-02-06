package com.shengfei.controller;


import com.github.pagehelper.PageInfo;
import com.shengfei.entity.Permission;
import com.shengfei.entity.User;
import com.shengfei.service.PermissionService;
import com.shengfei.shiro.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/api/admin/setting/menu")
public class MenuApiController {

    @Resource
    PermissionService permissionService;

    @ApiOperation(value = "全部菜单列表")
    @GetMapping("/menus")
    public ResultVO list() {
        try {
            List<Permission>  menuList = permissionService.list();
            return ResultVO.success(menuList,"菜单列表查询成功");
        }catch (Exception e){
            log.error("菜单列表查询错误：{}",e.getMessage());
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }


}
