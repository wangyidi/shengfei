package com.shengfei.service;


import com.shengfei.entity.Permission;

import java.util.List;

public interface  PermissionService {

	 List<Permission> getUserPermission(String roleId);

	 List<Permission> list();

}
