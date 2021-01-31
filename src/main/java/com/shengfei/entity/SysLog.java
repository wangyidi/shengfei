package com.shengfei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("sys_log")
@Data
public class SysLog {

    @ApiModelProperty(value = "自增id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "登录账号")
    private String mobile;

    @ApiModelProperty(value = "访问IP")
    private String ip;

    @ApiModelProperty(value = "登录日期")
    private Date loginTime;

    @ApiModelProperty(value = "所在省")
    private String province;

    private String city;

    @ApiModelProperty(value = "访客操作系统")
    private String os;

    @ApiModelProperty(value = "访客浏览器")
    private String browser;

    @ApiModelProperty(value = "经度")
    private BigDecimal pointX;

    @ApiModelProperty(value = "纬度")
    private BigDecimal pointY;
}
