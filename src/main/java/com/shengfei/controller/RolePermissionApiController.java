package com.shengfei.controller;


import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageInfo;
import com.shengfei.entity.Permission;
import com.shengfei.entity.Role;
import com.shengfei.service.PermissionService;
import com.shengfei.service.RoleService;
import com.shengfei.shiro.vo.PageBean;
import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.shiro.vo.RolePermissionParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "角色模块")
@RestController
@RequestMapping("/api/admin/setting/rolePermission")
public class RolePermissionApiController {

    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;

    /**
     * 不分页 - 给用户分配角色的提供
     * @return
     */
    @ApiOperation("不分页-给用户分配角色的提供")
    @PostMapping("/roles")
    public ResultVO<List<Role>> roles() {
        return ResultVO.success(roleService.getRoles());
    }

    /**
     * 角色列表分页
     * @return
     */
    @ApiOperation("角色列表分页")
    @GetMapping("/rolesByPage")
    public Object list(PageBean pageBean) {
        PageInfo<Role> rolePage = roleService.page(pageBean);
        return ResultVO.success(rolePage,"查询成功");
    }


    /**
     * 菜单展开图
     * @return
     */
    @ApiOperation(value = "获取菜单层级列表给角色分配菜单")
    @GetMapping("/menuList")
    public List<RolePermissionParam> addPermission() {
        return set(permissionService.list(), "0", false, null);
    }

    /**
     * 查询单个角色信息和菜单信息 并匹配
     * @param roleId
     * @return
     */
    @ApiOperation("查询单个角色信息和菜单信息 并匹配")
    @PostMapping("/role/{roleId}")
    public List<RolePermissionParam> editPermission(@PathVariable Integer roleId) {
        val sysPermissionList = permissionService.getUserPermission(roleId.toString());
        List<Integer> menuIdList = new ArrayList<>();
        for (Permission sysPermission : sysPermissionList) {
            menuIdList.add(sysPermission.getId());
        }
        return set(permissionService.list(), "0", true, menuIdList);
    }

    /**
     * 轮训获取对应的权限
     *
     * @param resourceList 权限列表
     * @param superId      父级
     * @param spread       是否展开
     * @param menuIdList   拥有的权限id集合
     * @return 权限列表
     */
    private List<RolePermissionParam> set(List<Permission> resourceList, String superId, Boolean spread, List<Integer> menuIdList) {
        val list = new ArrayList<RolePermissionParam>();
        for (Permission sysResource : resourceList) {
            if (superId.equals(sysResource.getPid().toString())) {
                val param = new RolePermissionParam();
                param.setTitle(sysResource.getName());
                param.setId(sysResource.getId());
                param.setSpread(spread);
                param.setDisabled(false);
                val rolePermissionParams = set(resourceList, sysResource.getId().toString(), spread, menuIdList);
                if (CollUtil.isNotEmpty(rolePermissionParams)) {
                    param.setChildren(rolePermissionParams);
                } else {
                    param.setChildren(new ArrayList<>());
                    // 判断是否属于已经存在
                    param.setChecked(CollUtil.isNotEmpty(menuIdList) && menuIdList.contains(sysResource.getId()));
                }
                list.add(param);
            }
        }
        return list;
    }

}
