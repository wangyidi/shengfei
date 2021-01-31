package com.shengfei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shengfei.entity.SysLog;
import com.shengfei.mapper.LogMapper;
import com.shengfei.service.SysLogService;
import com.shengfei.shiro.vo.PageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Resource
    LogMapper logMapper;

    @Override
    public PageInfo<SysLog> page(PageBean pageBean) {
        QueryWrapper<SysLog> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("login_time");
        PageHelper.startPage(pageBean.getPageNum(),pageBean.getPageSize());
        List<SysLog> logList =  logMapper.selectList(wrapper);
        return new PageInfo<>(logList);
    }
}
