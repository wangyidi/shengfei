package com.shengfei.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shengfei.entity.SysToken;
import org.apache.ibatis.annotations.Select;

public interface TokenMapper extends BaseMapper<SysToken> {

    @Select("select * from sys_token where user_id=#{userId}")
    SysToken  findByUserId(Integer userId);
}
