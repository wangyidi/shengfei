package com.shengfei.service;


import com.github.pagehelper.PageInfo;
import com.shengfei.entity.SysLog;
import com.shengfei.shiro.vo.PageBean;

public interface SysLogService {

    PageInfo<SysLog> page(PageBean pageBean);
}
