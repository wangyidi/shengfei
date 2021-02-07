package com.shengfei.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shengfei.entity.Permission;
import com.shengfei.mapper.PermissionMapper;
import com.shengfei.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper,Permission> implements PermissionService {

}
