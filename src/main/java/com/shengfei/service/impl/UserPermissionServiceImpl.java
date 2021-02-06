package com.shengfei.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shengfei.entity.UserPermission;
import com.shengfei.mapper.UserPermissionMapper;
import com.shengfei.service.UserPermissionService;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionServiceImpl extends ServiceImpl<UserPermissionMapper, UserPermission> implements UserPermissionService {

}
