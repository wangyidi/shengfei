package com.shengfei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shengfei.entity.Permission;
import com.shengfei.mapper.PermissionMapper;
import com.shengfei.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public List<Permission> getUserPermission(String roleId) {

		return permissionMapper.getUserPermission(roleId);
	}

	@Override
	public List<Permission> list() {
		return permissionMapper.selectList(new QueryWrapper<>());
	}


}
