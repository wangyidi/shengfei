package com.shengfei.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shengfei.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface UserMapper extends BaseMapper<User> {

    @Select("select * from sys_user where mobile=#{mobile}")
    User findByUserMobile(@Param("mobile") String mobile);


}
