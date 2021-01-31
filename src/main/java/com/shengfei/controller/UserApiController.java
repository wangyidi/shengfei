package com.shengfei.controller;


import com.github.pagehelper.PageInfo;
import com.shengfei.dto.UserSearchDTO;
import com.shengfei.entity.Role;
import com.shengfei.entity.User;
import com.shengfei.service.RoleService;
import com.shengfei.service.ShiroService;
import com.shengfei.shiro.vo.PageBean;
import com.shengfei.shiro.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "用户信息")
@RestController
@RequestMapping("/api/admin/users")
public class UserApiController {

    @Resource
    private ShiroService shiroService;

    @Resource
    private RoleService roleService;

    @ApiOperation("用户列表")
    @GetMapping("/list")
    public Object list(UserSearchDTO userSearchDTO, PageBean pageBean) {
        try {
            PageInfo<User> userPage = shiroService.page(pageBean,userSearchDTO);
            return ResultVO.success(userPage,"查询成功");
        }catch (Exception e){
            log.error("用户列表查询错误：{}",e.getMessage());
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

//
//    @PostMapping("/confirm/add")
//    public ResultVO confirmAdd(User sysAccount) {
//        shiroService.createUser(sysAccount);
//        return ResultVO.success("添加成功");
//    }

    @ApiOperation("查看用户详细信息")
    @PostMapping("/user")
    public ResultVO<Map<String,Object>> view(Integer userId) {

        Map<String,Object> maps = new HashMap<>();
        Map<Integer,String> userRoles = roleService.getUserRoles(userId.toString()).
                    stream().collect(Collectors.toMap(Role::getId,Role::getName));
        List<Role> roles = roleService.getRoles();
        for (Role r: roles) {
            String role =  userRoles.get(r.getId());
            if(StringUtils.isNotBlank(role)) {
                r.setChecked(true);
            }
        }
        maps.put("role", roles);
        maps.put("user",shiroService.findByUserId(userId));
        return ResultVO.success(maps);
    }

}
