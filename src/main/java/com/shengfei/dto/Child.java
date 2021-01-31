package com.shengfei.dto;

import lombok.Data;

import java.util.List;


@Data
public class Child {
    private Integer id;
    private String title;
    private String href;
    private String icon;
    private String target;
    private List<Child> child;
}