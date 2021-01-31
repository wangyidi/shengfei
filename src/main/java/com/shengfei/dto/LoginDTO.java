package com.shengfei.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录传输类
 */
@Data
public class LoginDTO {


    @NotBlank(message = "电话号不能为空")
    private String mobile;

    @NotBlank(message = "密码不能为空")
    private String password;


}
