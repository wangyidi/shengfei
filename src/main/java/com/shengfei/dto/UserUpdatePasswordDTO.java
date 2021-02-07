package com.shengfei.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class UserUpdatePasswordDTO implements Serializable {

    @NotNull
    private Integer userId;

    @NotBlank
    @Length(max = 20,min = 8)
    private String password;

    @NotBlank
    @Length(max = 20,min = 8)
    private String passwordConfirm;


}