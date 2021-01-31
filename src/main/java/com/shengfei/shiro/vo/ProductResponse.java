package com.shengfei.shiro.vo;

import lombok.Data;

@Data
public class ProductResponse {

    private Integer id;

    private Integer productCateId;

    private String productName;

    private String sku;

    private String imgUrl;

    private Integer stock;

    private Integer sales;

    private String catName;
}
