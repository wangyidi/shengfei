package com.shengfei.dto;

import lombok.Data;

import java.util.List;


@Data
public class Menu {
    private Long id;
    private String title;
    private String icon;
    private List<Child> child;
}