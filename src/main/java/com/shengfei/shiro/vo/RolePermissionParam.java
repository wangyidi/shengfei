package com.shengfei.shiro.vo;

import lombok.Data;

import java.util.List;


@Data
public class RolePermissionParam {
  //  @ApiModelProperty(value = "节点标题")
    private String title;
  //  @ApiModelProperty(value = "节点唯一索引，用于对指定节点进行各类操作")
    private Integer id;
  //  @ApiModelProperty(value = "子节点。支持设定选项同父节点")
    private List<RolePermissionParam> children;
  //  @ApiModelProperty(value = "点击节点弹出新窗口对应的 url。需开启 isJump 参数")
    private String href;
  //  @ApiModelProperty(value = "节点是否初始展开，默认 false")
    private Boolean spread;
  //  @ApiModelProperty(value = "节点是否初始为选中状态（如果开启复选框的话），默认 false")
    private Boolean checked;
  //  @ApiModelProperty(value = "节点是否为禁用状态。默认 false")
    private Boolean disabled;
}

