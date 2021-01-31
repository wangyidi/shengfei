package com.shengfei.service;


import com.github.pagehelper.PageInfo;
import com.shengfei.dto.UserSearchDTO;
import com.shengfei.entity.SysToken;
import com.shengfei.entity.User;
import com.shengfei.shiro.vo.PageBean;

import java.util.Map;

public interface ShiroService {

    /**
     * find token by token
     * @param accessToken
     * @return
     */
    SysToken findByToken(String accessToken);

    /**
     * find user by userId
     * @param userId
     * @return
     */
    User findByUserId(Integer userId);

    /**
     * Find user by username
     * @param username
     * @return
     */
    User findByUserMobile(String username);

    /**
     * create token by userId
     * @param userId
     * @return
     */
    Map<String,Object> createToken(Integer userId);

    /**
     * logout
     * @param token
     */
    void logout(String token);

    PageInfo<User> page(PageBean page, UserSearchDTO user);

    Boolean createUser(User user);
}
