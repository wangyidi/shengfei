package com.shengfei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shengfei.entity.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select p.* from sys_role_permission rp , sys_permission p where rp.PERMISSION_ID = p.id AND rp.ROLE_ID = #{roleId}")
    public List<Permission> getUserPermission(String roleId);

}
