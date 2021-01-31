package com.shengfei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shengfei.entity.Role;
import com.shengfei.exception.ServiceException;
import com.shengfei.mapper.RoleMapper;
import com.shengfei.service.RoleService;
import com.shengfei.shiro.vo.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<Role> getRoles() {
		return roleMapper.selectList(new QueryWrapper<>());
	}

	@Override
	public List<Role> getUserRoles(String userId) {
		return roleMapper.getUserRoles(userId);
	}

	@Override
	public PageInfo<Role> page(PageBean pageBean) {
		PageHelper.startPage(pageBean.getPageNum(),pageBean.getPageSize());
		List<Role> roleList = roleMapper.selectList(new QueryWrapper<>());
		PageInfo<Role>pageInfo = new PageInfo<>(roleList);
		return pageInfo;
	}

	@Override
	public Role getRoleById(Integer id) {
		Role role = roleMapper.selectById(id);
		if(role == null) {
			throw new ServiceException("角色不存在");
		}
		return role;
	}


}
