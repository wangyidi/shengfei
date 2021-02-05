package com.shengfei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;


/**
 * <p>
 * 
 * </p>
 *
 * @author Martin
 * @since 2021-02-02
 */
@Data
@TableName("member")
@ApiModel(value="Member对象", description="")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("create_date")
    private Date createDate;

    @Length(min = 1,max = 10,message = "名字长度在1～10之间")
    private String name;

    @ApiModelProperty(value = "性别")
    @NotNull
    private Integer sex;

    @NotNull
    @Max(value = 150,message = "年龄不能大于150")
    @Min(value = 1,message = "年龄不能小于1")
    private Integer age;

    @Length(min = 1,max = 20)
    @ApiModelProperty(value = "身份证号")
    @TableField("id_card")
    private String idCard;

    @Length(min = 1,max = 50)
    @ApiModelProperty(value = "身份证归属地")
    @TableField("id_card_station")
    private String idCardStation;

    @ApiModelProperty(value = "生日")
    @NotBlank
    @Length(min = 1,max = 20)
    private String birthday;

    @ApiModelProperty(value = "身份证地址")
    @Length(min = 1,max = 200)
    @TableField("address_detail")
    private String addressDetail;

    @ApiModelProperty(value = "婚姻状况")
    @NotNull
    private Integer marriage;

    @NotBlank(message = "电话号不能为空")
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty(value = "健康状况")
    @NotNull
    private Integer healthy;

    @NotBlank
    @ApiModelProperty(value = "现住址省")
    @TableField("current_province")
    private String currentProvince;

    @NotBlank
    @ApiModelProperty(value = "现住址市")
    @TableField("current_city")
    private String currentCity;

    @NotBlank
    @ApiModelProperty(value = "现住址区")
    @TableField("current_area")
    private String currentArea;

    @NotBlank
    @ApiModelProperty(value = "现居住详细地址")
    @TableField("current_detail")
    private String currentDetail;

    @ApiModelProperty(value = "家庭成员数")
    @Min(value = 1)
    @TableField("family_num")
    private Integer familyNum;

    @NotBlank
    @ApiModelProperty(value = "居住地状况")
    @TableField("live_status")
    private String liveStatus;

    @ApiModelProperty(value = "电话区号")
    @NotBlank
    @TableField("telephone_area_code")
    private String telephoneAreaCode;

    @NotBlank
    @ApiModelProperty(value = "邮编")
    @TableField("zip_code")
    private String zipCode;

    @ApiModelProperty(value = "家庭收入")
    @NotNull
    @TableField("family_salary")
    private Integer familySalary;

    @ApiModelProperty(value = "公司名称")
    @NotBlank
    @TableField("company_name")
    private String companyName;

    @ApiModelProperty(value = "公司-省")
    @NotBlank
    @TableField("company_province")
    private String companyProvince;

    @ApiModelProperty(value = "公司-市")
    @NotBlank
    @TableField("company_city")
    private String companyCity;

    @ApiModelProperty(value = "公司-区")
    @NotBlank
    @TableField("company_area")
    private String companyArea;

    @ApiModelProperty(value = "公司-详细地址")
    @NotBlank
    @TableField("company_detail")
    private String companyDetail;

    @NotNull
    @ApiModelProperty(value = "工龄")
    @TableField("work_age")
    private Integer workAge;

    @NotNull
    @ApiModelProperty(value = "月收入工资")
    private Integer salary;

    @NotBlank
    @ApiModelProperty(value = "公司电话")
    @TableField("company_tel")
    private String companyTel;

    @NotBlank
    @ApiModelProperty(value = "主要收入来源")
    @TableField("major_source_revenue")
    private String majorSourceRevenue;

    @NotNull
    @TableField("social_security_flag")
    private Integer socialSecurityFlag;

    @NotNull
    @ApiModelProperty(value = "资产证明")
    @TableField("assets_flag")
    private Integer assetsFlag;

    @NotBlank
    @ApiModelProperty(value = "公司邮编")
    @TableField("company_code")
    private String companyCode;

    @NotBlank
    @ApiModelProperty(value = "公司性质")
    @TableField("company_nature")
    private String companyNature;

    @NotBlank
    @ApiModelProperty(value = "职业")
    private String profession;

    @NotBlank
    @ApiModelProperty(value = "职务")
    private String position;

    @ApiModelProperty(value = "公司-电话区号")
    @NotBlank
    @TableField("company_tel_code")
    private String companyTelCode;

    @NotBlank
    @ApiModelProperty(value = "入职时间")
    @TableField("onboard_time")
    private String onboardTime;

    @NotBlank
    @ApiModelProperty(value = "房屋来源")
    @TableField("house_source")
    private String houseSource;

    @NotBlank
    @ApiModelProperty(value = "住房类型")
    @TableField("house_type")
    private String houseType;

    @NotBlank
    @ApiModelProperty(value = "紧急联系人")
    @TableField("emergency_contact")
    private String emergencyContact;

    @NotBlank
    @ApiModelProperty(value = "紧急电话")
    @TableField("emergency_contact_tel")
    private String emergencyContactTel;

    @NotBlank
    @ApiModelProperty(value = "紧急手机")
    @TableField("emergency_contact_phone")
    private String emergencyContactPhone;

    @NotBlank
    @ApiModelProperty(value = "紧急关系")
    @TableField("emergency_contact_relation")
    private String emergencyContactRelation;

    @NotNull
    @ApiModelProperty(value = "学历")
    @TableField("education")
    private Integer education;

}
