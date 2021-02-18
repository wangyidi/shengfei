package com.shengfei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@TableName("sys_user")
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
	private Integer id;

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "密码")
    private String password;

    private Date createDate;

    private String state;

    private Integer loginCount;

    private Date lastVisit;


    @TableField(value = "company_name")
    private String companyName;

    @TableField(value = "id_card")
    private String idCard;


    @ApiModelProperty(value = "电话号")
    private String mobile;

    @ApiModelProperty(value = "岗位")
    private String station;

    @TableField(exist=false)
    private List<String> roleIds;

    @TableField(exist = false)
    private String search;

    @TableField(value = "branch_company")
    @ApiModelProperty(value = "分支机构")
    private String branchCompany;
}