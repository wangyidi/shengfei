package com.shengfei.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@TableName("sys_token")
@Data
@Getter
@Setter
public class SysToken implements Serializable {

    /**
     * 用户ID
     */
    @TableId
    private Integer userId;

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
