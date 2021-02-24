package com.shengfei.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel
public class EvaluationDTO implements Serializable {

    @NotNull
    private Integer DivisionId;

    private Integer ConstructionId;

    private Integer BuildingId;

    private Integer HouseId;

    @Length(max = 32)
    private String BuildingName;

    @Length(max = 24)
    private String HouseName;

    @NotNull
    private BigDecimal BuildArea;

    private String CaseId;

    @Size(max = 4)
    private String NearRoad;

    @Size(max = 4)
    private String LandSpace;
    @Size(max = 4,message = "PropertyType max 12")
    private String PropertyType;

    @Size(max = 12,message = "PropertyType max 12")
    private String HouseType;

    @Size(max = 4,message = "Forward max 12")
    private String Forward;

    @Size(max = 64,message = "Remark max 12")
    private String Remark;
}
