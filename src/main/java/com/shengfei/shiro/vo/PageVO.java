package com.shengfei.shiro.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class PageVO<T> implements Serializable {
    @ApiModelProperty(value = "状态码")
    private Long code;
    @ApiModelProperty(value = "返回消息")
    private String msg;
    @ApiModelProperty(value = "总条数")
    private Long count;
    @ApiModelProperty(value = "Json参数")
    private List<T> data;


    private PageVO(List<T> list, Long count) {
        this.code = 0L;
        this.msg = "";
        this.count = count;
        this.data = list;
    }

    public static <T> PageVO<T> pageVO(List<T> list, Long count) {
        return new PageVO<>(list, count);
    }

}