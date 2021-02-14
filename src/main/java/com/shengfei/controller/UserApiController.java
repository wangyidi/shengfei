package com.shengfei.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.shengfei.dto.UserMenuAdd;
import com.shengfei.dto.UserSearchDTO;
import com.shengfei.dto.UserUpdatePasswordDTO;
import com.shengfei.entity.User;
import com.shengfei.entity.UserPermission;
import com.shengfei.mapper.TokenMapper;
import com.shengfei.mapper.UserMapper;
import com.shengfei.service.ShiroService;
import com.shengfei.service.UserPermissionService;
import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Api(tags = "用户信息")
@RestController
@RequestMapping("/api/admin/users")
public class UserApiController {

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private ShiroService shiroService;

    @Resource
    private UserPermissionService userPermissionService;

    @Resource
    private UserMapper userMapper;

    @ApiOperation("用户列表")
    @PostMapping("/list")
    public Object list(@RequestBody UserSearchDTO userSearchDTO) {
        try {
            PageInfo<User> userPage = shiroService.page(userSearchDTO);
            return ResultVO.success(userPage,"查询成功");
        }catch (Exception e){
            log.error("用户列表查询错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }


    @ApiOperation("查看用户详细信息")
    @GetMapping("/user/{userId}")
    public Object view(@PathVariable Integer userId) {

        Map<String,Object> maps = new HashMap<>();
        maps.put("user",shiroService.findByUserId(userId));

        QueryWrapper<UserPermission> query = new QueryWrapper<>();
        query.eq("user_id",userId);
        List<UserPermission>list =  userPermissionService.list(query);
        maps.put("menuList",list);

        return ResultVO.success(maps);
    }


    @ApiOperation("查看当前登陆人的权限列表根据token")
    @PostMapping("/current/menus")
    public Object currentMenus() {
        try {
            // 获取用户信息
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            Integer userId = user.getId();

            QueryWrapper<UserPermission> query = new QueryWrapper<>();
            query.eq("user_id",userId);
            List<UserPermission>list =  userPermissionService.list(query);

            Map<String,Object> map = new HashMap<>();
            map.put("userPermissionList",list);
            map.put("user",user);

            return ResultVO.success(map);
        }catch (Exception e){
            log.error("查看当前登陆人的权限列表根据token错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查看当前登陆人的权限列表根据token查询错误"+e.getMessage());
        }
    }


    @ApiOperation("为用户添加权限")
    @PostMapping("/menu/user")
    public Object addMenuForUser(@RequestBody UserMenuAdd userMenuAdd, BindingResult bindingResult) {
        try {
            if (!ValidatorUtils.validate(MemberApiController.class,bindingResult)) {
                return ResultVO.parameterError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            userPermissionService.addPermission(userMenuAdd);
            return ResultVO.success();
        }catch (Exception e){
            log.error("用户列表查询错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }



    @ApiOperation("修改密码")
    @PostMapping("/user/password")
    public Object updatePassword(@RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO, BindingResult bindingResult) {
        try {
            if (!ValidatorUtils.validate(MemberApiController.class,bindingResult)) {
                return ResultVO.systemError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            if (!Objects.equals(userUpdatePasswordDTO.getPassword(),userUpdatePasswordDTO.getPasswordConfirm())) {
                return ResultVO.systemError("密码不相符");
            }
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",userUpdatePasswordDTO.getUserId());
            if (userMapper.selectCount(queryWrapper)!=1) {
                return ResultVO.systemError("用户不存在");
            }

            User user = new User();
            user.setId(userUpdatePasswordDTO.getUserId());
            user.setPassword(userUpdatePasswordDTO.getPassword());
            userMapper.updateById(user);

            return ResultVO.success("修改密码成功");
        }catch (Exception e){
            log.error("修改密码错误：{}",e.getMessage(),e);
            return ResultVO.systemError("修改密码错误"+e.getMessage());
        }
    }
}
