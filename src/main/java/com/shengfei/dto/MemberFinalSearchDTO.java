package com.shengfei.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class MemberFinalSearchDTO {

    private int pageNum;

    private int pageSize;

    private String startTime;

    private String endTime;

    private String name;

    private String idCard;

    @ApiModelProperty(value = "分支机构")
    private String branchCompany;
}