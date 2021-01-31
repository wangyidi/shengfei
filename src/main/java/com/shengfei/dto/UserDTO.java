package com.shengfei.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


@Data
public class UserDTO implements Serializable {

    @NotBlank(message = "用户名称不能为空")
    @ApiModelProperty(value = "用户名称",required = true)
    @Length(min = 1, max = 10, message = "用户名长度必须是1~10位")
    private String name;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码",required = true)
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$",message = "密码格式不正确")
    private String password;


    @NotBlank(message = "机构名称不能为空")
    @Length(max = 50)
    @ApiModelProperty(value = "机构名称",required = true)
    private String companyName;

    @NotBlank(message = "身份证号不能为空")
    @ApiModelProperty(value = "身份证号",required = true)
    @Length(max = 20)
    private String idCard;

    @NotBlank(message = "电话号不能为空")
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty(value = "电话号",required = true)
    private String mobile;

    @NotBlank(message = "岗位不能为空")
    @Length(max = 50)
    @ApiModelProperty(value = "岗位",required = true)
    private String station;

}