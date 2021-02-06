package com.shengfei.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.shengfei.dto.UserMenuAdd;
import com.shengfei.dto.UserSearchDTO;
import com.shengfei.entity.SysToken;
import com.shengfei.entity.User;
import com.shengfei.entity.UserPermission;
import com.shengfei.mapper.TokenMapper;
import com.shengfei.service.ShiroService;
import com.shengfei.service.UserPermissionService;
import com.shengfei.shiro.vo.PageBean;
import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.utils.TokenUtil;
import com.shengfei.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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


    @ApiOperation("查看用户详细信息")
    @PostMapping("/user")
    public ResultVO view(Integer userId) {

        Map<String,Object> maps = new HashMap<>();
        maps.put("user",shiroService.findByUserId(userId));

        QueryWrapper<UserPermission> query = new QueryWrapper();
        query.eq("user_id",userId);
        List<UserPermission>list =  userPermissionService.list(query);
        maps.put("menuList",list);

        return ResultVO.success(maps);
    }


    @ApiOperation("查看当前登陆人的权限列表根据token")
    @PostMapping("/current/menus")
    public ResultVO currentMenus(HttpServletRequest httpServletRequest) {
        try {
            // 获取用户信息
            String token = TokenUtil.getRequestToken(httpServletRequest);
            QueryWrapper<SysToken> queryWrapper = new QueryWrapper();
            queryWrapper.eq("token",token);
            SysToken sysToken =  tokenMapper.selectOne(queryWrapper);
            if (sysToken == null ){
                return ResultVO.systemError("请登陆");
            }
            Integer userId = sysToken.getUserId();

            QueryWrapper<UserPermission> query = new QueryWrapper();
            query.eq("user_id",userId);
            List<UserPermission>list =  userPermissionService.list(query);
            return ResultVO.success(list);
        }catch (Exception e){
            log.error("查看当前登陆人的权限列表根据token错误：{}",e.getMessage());
            return ResultVO.systemError("查看当前登陆人的权限列表根据token查询错误"+e.getMessage());
        }
    }


    @ApiOperation("为用户添加权限")
    @PostMapping("/menu/user")
    public Object addMenuForUser(@RequestBody UserMenuAdd userMenuAdd, BindingResult bindingResult) {
        try {
            if (!ValidatorUtils.validate(MemberApiController.class,bindingResult)) {
                return ResultVO.systemError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            // TODO
            return ResultVO.success();
        }catch (Exception e){
            log.error("用户列表查询错误：{}",e.getMessage());
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

}
