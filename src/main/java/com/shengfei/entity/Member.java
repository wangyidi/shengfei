package com.shengfei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import com.shengfei.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
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

    @TableField("status")
    private Integer status;

    @TableField("sequence")
    private String sequence;

    @Length(min = 1,max = 10,message = "名字长度在1～10之间")
    private String name;

    @ApiModelProperty(value = "性别")
    @NotNull(message = "sex is nou null")
    private Integer sex;

    @NotNull
    @Max(value = 150,message = "年龄不能大于150")
    @Min(value = 1,message = "年龄不能小于1")
    private Integer age;

    @Length(min = 1,max = 20,message = "idCard 应在1-20")
    @ApiModelProperty(value = "身份证号")
    @TableField("id_card")
    private String idCard;

    @ApiModelProperty(value = "生日")
    @NotBlank
    private String birthday;

    @ApiModelProperty(value = "身份证地址")
    @Length(min = 1,max = 200,message = "身份证应在1-200")
    @TableField("address_detail")
    private String addressDetail;

    @ApiModelProperty(value = "婚姻状况")
    @NotNull(message = "marriage is no null")
    private Integer marriage;

    @NotBlank(message = "电话号不能为空")
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty(value = "健康状况")
    @NotNull(message = "健康状况 不能为空")
    private Integer healthy;

    @NotBlank(message = "currentProvince is not blank")
    @ApiModelProperty(value = "现住址省")
    @TableField("current_province")
    private String currentProvince;

    @NotBlank(message = "currentCity is not null")
    @ApiModelProperty(value = "现住址市")
    @TableField("current_city")
    private String currentCity;

    @NotBlank(message = "currentArea is not null")
    @ApiModelProperty(value = "现住址区")
    @TableField("current_area")
    private String currentArea;

    @NotBlank(message = "currentDetail is not null")
    @ApiModelProperty(value = "现居住详细地址")
    @TableField("current_detail")
    private String currentDetail;

    @ApiModelProperty(value = "家庭成员数")
    @TableField("family_num")
    private Integer familyNum;

    @NotBlank(message = "liveStatus is not null")
    @ApiModelProperty(value = "居住地状况")
    @TableField("live_status")
    private String liveStatus;


    @ApiModelProperty(value = "家庭收入")
    @TableField("family_salary")
    private Integer familySalary;

    @ApiModelProperty(value = "公司名称")
    @NotBlank(message = "companyName is not null")
    @TableField("company_name")
    private String companyName;

    @ApiModelProperty(value = "公司-省")
    @NotBlank(message = "companyProvince is not null")
    @TableField("company_province")
    private String companyProvince;

    @ApiModelProperty(value = "公司-市")
    @NotBlank(message = "companyCity is not null")
    @TableField("company_city")
    private String companyCity;

    @ApiModelProperty(value = "公司-区")
    @NotBlank(message = "companyArea is not null")
    @TableField("company_area")
    private String companyArea;

    @ApiModelProperty(value = "公司-详细地址")
    @NotBlank(message = "companyDetail is not null")
    @TableField("company_detail")
    private String companyDetail;

    @NotNull(message = "workAge is not null")
    @ApiModelProperty(value = "工龄")
    @TableField("work_age")
    private Integer workAge;

    @NotNull(message = "salary is not null")
    @ApiModelProperty(value = "月收入工资")
    private Integer salary;

    @NotBlank(message = "companyTel is not null")
    @ApiModelProperty(value = "公司电话")
    @TableField("company_tel")
    private String companyTel;

    @NotBlank(message = "majorSourceRevenue is not null")
    @ApiModelProperty(value = "主要收入来源")
    @TableField("major_source_revenue")
    private String majorSourceRevenue;

    @NotNull(message = "socialSecurityFlag is not null")
    @TableField("social_security_flag")
    private Integer socialSecurityFlag;

    @NotNull(message = "assetsFlag is not null")
    @ApiModelProperty(value = "资产证明")
    @TableField("assets_flag")
    private Integer assetsFlag;

    @ApiModelProperty(value = "公司邮编")
    @TableField("company_code")
    private String companyCode;

    @NotBlank(message = "companyNature is not null")
    @ApiModelProperty(value = "公司性质")
    @TableField("company_nature")
    private String companyNature;

    @NotBlank(message = "profession is not null")
    @ApiModelProperty(value = "职业")
    private String profession;

    @NotBlank(message = "position is not null")
    @ApiModelProperty(value = "职务")
    private String position;


    @ApiModelProperty(value = "入职时间")
    @TableField("onboard_time")
    private String onboardTime;

    @NotBlank(message = "houseSource is not null")
    @ApiModelProperty(value = "住房性质")
    @TableField("house_source")
    private String houseSource;

    @NotBlank(message = "houseType is not null")
    @ApiModelProperty(value = "住房类型")
    @TableField("house_type")
    private String houseType;

    @ApiModelProperty(value = "紧急联系人")
    @TableField("emergency_contact")
    private String emergencyContact;

    @ApiModelProperty(value = "紧急手机")
    @TableField("emergency_contact_phone")
    private String emergencyContactPhone;

    @ApiModelProperty(value = "紧急关系")
    @TableField("emergency_contact_relation")
    private String emergencyContactRelation;

    @ApiModelProperty(value = "学历")
    @TableField("education")
    private Integer education;


    @ApiModelProperty(value = "申请金额")
    @TableField("apply_amount")
    private BigDecimal applyAmount;

    @ApiModelProperty(value = "贷款机构")
    @TableField("lending_institutions")
    private String lendingInstitutions;

    @TableField("sys_user_id")
    private Integer sysUserId;

    @TableField(value="create_date")
    private Date createDate;

    @ApiModelProperty(value ="房产土地性质（国有出让 国有划拨 国有租赁 工业用地 集体土地 其他）")
    @TableField(value="property_land")
    private Integer propertyLand;

    @ApiModelProperty(value ="住房年限")
    @TableField(value="house_years")
    private Integer houseYears;

    @NotNull(message = "loanProduct is not null")
    @ApiModelProperty(value ="产品:永安快贷，永安快贷_顺位贷，个人抵押贷款，个人房屋按揭贷款，个人信用贷款")
    @TableField(value="loan_product")
    private Integer loanProduct;

    @NotNull(message = "loanProduct is not null")
    @ApiModelProperty(value ="贷款期限 （月)")
    @TableField(value="loan_time")
    private Integer loanTime;

    @NotNull(message = "repaymentMethod is not null")
    @ApiModelProperty(value ="还款方式")
    @TableField(value="repayment_method")
    private Integer repaymentMethod;

    @ApiModelProperty(value ="担保方式")
    @TableField(value="assure_means")
    private Integer assureMeans;


    @TableField(exist = false)
    private List<MemberImage> imageList;

    @TableField(exist = false)
    private UserVO sysUserBean;


    public String getSequence() {
        return StringUtils.leftPad(sequence,8,"0");
    }
}
