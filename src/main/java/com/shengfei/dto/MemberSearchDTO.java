package com.shengfei.dto;

import lombok.Data;


@Data
public class MemberSearchDTO {

    private int pageNum;

    private int pageSize;

    private String startTime;

    private String endTime;

    private String name;

    private String idCard;
}