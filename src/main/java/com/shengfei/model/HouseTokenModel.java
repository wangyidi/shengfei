package com.shengfei.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HouseTokenModel {

    // 过期时间
    private String overTime;

    // TOKEN
    private String token;

}
