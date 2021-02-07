package com.shengfei.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shengfei.dto.UserMenuAdd;
import com.shengfei.entity.UserPermission;

public interface UserPermissionService extends IService<UserPermission> {


    void addPermission(UserMenuAdd userMenuAdd);
}
