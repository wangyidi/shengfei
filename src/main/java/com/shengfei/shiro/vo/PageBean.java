package com.shengfei.shiro.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class PageBean implements Serializable {
    @ApiModelProperty(value = "每页页数")
    private int pageNum = 1;
    @ApiModelProperty(value = "每页条数")
    private int pageSize = 10;
}
