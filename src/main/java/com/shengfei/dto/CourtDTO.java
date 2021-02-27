package com.shengfei.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel
public class CourtDTO implements Serializable {

    @NotBlank(message = "idCard is not null ")
    private String idCard;

    @NotBlank(message = "phone is not null ")
    private String phone;

    @NotBlank(message = "name is not null ")
    private String name;


}
