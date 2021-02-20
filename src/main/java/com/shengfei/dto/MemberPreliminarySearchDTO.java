package com.shengfei.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class MemberPreliminarySearchDTO {

    private int pageNum;

    private int pageSize;

    private String startTime;

    private String endTime;

    private String name;

    @ApiModelProperty(value = "分支机构")
    private String branchCompany;
}