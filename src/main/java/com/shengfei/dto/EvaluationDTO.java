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

    private String NearRoad;

    private String LandSpace;

    private String PropertyType;

    private String HouseType;

    private String Forward;

    private String Remark;
}
