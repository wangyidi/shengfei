package com.shengfei.controller;


import com.github.pagehelper.PageInfo;
import com.shengfei.entity.SysLog;
import com.shengfei.service.SysLogService;
import com.shengfei.shiro.vo.PageBean;
import com.shengfei.shiro.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


//@Api(tags = "日志管理")
@RestController
@RequestMapping("/api/admin/setting/logLogin")
public class LogApiController {

    @Resource
    SysLogService sysLogService;

    @ApiOperation(value = "日志列表")
    @RequiresRoles(value = {"admin","guest","superadmin"},logical = Logical.OR)
    @GetMapping("/list")
    public Object list(PageBean pageBean) {
        try {
            PageInfo<SysLog> logLoginPage = sysLogService.page(pageBean);
            return ResultVO.success(logLoginPage,"查询成功");
        }catch (Exception e){
            return ResultVO.systemError("日志查询失败 :"+e.getMessage());
        }
    }

}
