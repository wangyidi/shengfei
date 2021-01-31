package com.shengfei.service;


import com.github.pagehelper.PageInfo;
import com.shengfei.entity.Role;
import com.shengfei.shiro.vo.PageBean;

import java.util.List;

public interface RoleService {

	List<Role> getRoles();

	List<Role> getUserRoles(String userid);

	PageInfo<Role> page(PageBean pageBean);

	Role getRoleById(Integer id);
}
