package com.shengfei.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("member_image")
public class MemberImage implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "img_name")
    private String imgName;

    @TableField(value = "img_path")
    private String imgPath;

    @TableField(value = "member_id")
    private Integer memberId;

    @TableField(value = "create_time")
    private Date createTime;
}