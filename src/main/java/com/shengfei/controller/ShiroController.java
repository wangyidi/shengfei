package com.shengfei.controller;


import cn.hutool.core.bean.BeanUtil;
import com.shengfei.dto.LoginDTO;
import com.shengfei.dto.UserDTO;
import com.shengfei.entity.User;
import com.shengfei.service.ShiroService;
import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Api(value = "登陆注册",tags ="登陆注册" )
@RestController
public class ShiroController {

    @Resource
    private ShiroService shiroService;

    /**
     * 登录
     */
    @ApiOperation("登陆")
    @PostMapping("/sys/login")
    public ResultVO login(@RequestBody @Validated LoginDTO loginDTO, BindingResult bindingResult) {
        try {
            if (!ValidatorUtils.validate(ShiroController.class, bindingResult)) {
                return ResultVO.parameterError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            log.info("开始登陆 {}",loginDTO.getMobile());
            User user = shiroService.findByUserMobile(loginDTO.getMobile());
            if(user == null) {
                return ResultVO.parameterError("用户不存在！！！");
            }
            if (!user.getPassword().equals(loginDTO.getPassword())) {
                return ResultVO.parameterError("账号或密码有误");
            }
            Map<String,Object> token = shiroService.createToken(user.getId());
            return ResultVO.success(token,"登录成功");
        }catch (Exception e){
            log.error("登陆异常：{}",e.getMessage(),e);
            return ResultVO.systemError("登陆异常 :"+e.getMessage());

        }
    }

    /**
     * 退出
     */
    @ApiOperation("退出登陆")
    @PostMapping("/sys/logout")
    public ResultVO logout(@RequestHeader("token")String token) {
        shiroService.logout(token);
        return ResultVO.success("您已安全退出系统");
    }

//    /**
//     * 验证是否登陆过-实现 不能异步登陆逻辑
//     */
//    @ApiOperation("验证是否登陆过-实现 不能异步登陆逻辑")
//    @PostMapping("/sys/checkLogin")
//    public ResultVO checkLogin() {
//        Subject subject = SecurityUtils.getSubject();
//        // 判断是否认证成功
//        if (!subject.isAuthenticated()) {
//            return ResultVO.systemError("登录凭证已失效，请重新登录");
//        }
//        return ResultVO.success("校验通过");
//    }

    /**
     * 注册
     *
     * @return
     */
    @ApiOperation("注册")
    @PostMapping("/sys/register")
    public ResultVO register(@RequestBody @Validated UserDTO userDTO, BindingResult bindingResult) {
        try {
            if (!ValidatorUtils.validate(ShiroController.class, bindingResult)) {
                return ResultVO.parameterError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            User user = new User();
            BeanUtil.copyProperties(userDTO,user);
            shiroService.createUser(user);
            return ResultVO.success("注册成功");
        } catch (Exception e) {
            log.error("注册异常 {}",e.getMessage(),e);
            return ResultVO.systemError("系统异常：" + e.getMessage());
        }
    }
}


