package com.shengfei.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role")
public class Role {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Short sort;

    private String delFlag;

    private String description;

    @TableField(exist=false)
    private boolean checked = false;

}