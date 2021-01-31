package com.shengfei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shengfei.entity.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {

    @Select("select r.* from sys_user_role ur,sys_role r where ur.role_id = r.id and ur.user_id=#{userId}")
    public List<Role> getUserRoles(String userId);
}
