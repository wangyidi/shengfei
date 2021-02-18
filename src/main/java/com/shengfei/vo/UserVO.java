package com.shengfei.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserVO {

    @ApiModelProperty(value = "用户名称")
    private String name;

    private String companyName;

    private String idCard;

    @ApiModelProperty(value = "电话号")
    private String mobile;

    @ApiModelProperty(value = "分支机构")
    private String branchCompany;
}
