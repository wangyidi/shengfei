package com.shengfei.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class UserSearchDTO implements Serializable {

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "机构名称")
    private String companyName;

    @ApiModelProperty(value = "电话号")
    private String mobile;

    @ApiModelProperty(value = "岗位")
    private String station;

    @ApiModelProperty(value = "每页页数")
    private int pageNum;

    @ApiModelProperty(value = "每页条数")
    private int pageSize;
}