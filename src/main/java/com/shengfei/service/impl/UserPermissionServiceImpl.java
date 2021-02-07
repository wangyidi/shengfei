package com.shengfei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shengfei.dto.UserMenuAdd;
import com.shengfei.entity.UserPermission;
import com.shengfei.mapper.UserPermissionMapper;
import com.shengfei.service.UserPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserPermissionServiceImpl extends ServiceImpl<UserPermissionMapper, UserPermission> implements UserPermissionService {

    @Resource
    private UserPermissionService userPermissionService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addPermission(UserMenuAdd userMenuAdd) {

        QueryWrapper<UserPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userMenuAdd.getUserId());
        queryWrapper.in("permission_id",userMenuAdd.getPermissionIds());
        userPermissionService.remove(queryWrapper);

        List<UserPermission>list = new ArrayList<>();
        userMenuAdd.getPermissionIds().forEach(e->{
            UserPermission userPermission = new UserPermission();
            userPermission.setUserId(userMenuAdd.getUserId());
            userPermission.setPermissionId(e);
            list.add(userPermission);
        });
        userPermissionService.saveBatch(list);
    }



}
