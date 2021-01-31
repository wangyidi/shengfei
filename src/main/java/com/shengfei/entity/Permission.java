package com.shengfei.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_permission")
public class Permission {

    @ApiModelProperty(value = "自增id")
    private Integer id;

    @ApiModelProperty(value = "父id")
    private Integer pid;

    @ApiModelProperty(value = "类别(1:目录,2:菜单,3:按钮)")
    private Integer category;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型:菜单or功能")
    private String type;

    private Integer sort;

    private String url;

    private String permCode;

    private String icon;

    private String state;

    private String description;

}