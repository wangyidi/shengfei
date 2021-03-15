package com.shengfei.mapper;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shengfei.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Map;

public interface MemberMapper extends BaseMapper<Member> {

    @Insert({"insert into member(create_date,name,sex,age,id_card,birthday" +
            ",address_detail,marriage,phone,healthy,current_province,current_city,current_area" +
            ",current_detail,family_num,live_status,family_salary,company_name" +
            ",company_province,company_city,company_area,company_detail,work_age,salary,company_tel" +
            ",major_source_revenue,social_security_flag,assets_flag,company_nature,profession" +
            ",position,onboard_time,house_source,house_type,emergency_contact,emergency_contact_phone,emergency_contact_relation" +
            ",education,sys_user_id,status,lending_institutions,sequence,apply_amount,property_land,house_years,loan_product,loan_time,repayment_method,assure_means" +
            ",social_security_deposit_place,social_security_fund_amount,business_area,turnover,annual_run_amount" +
            ",housing_assessment,housing_assessment_amount,residual_loan,preliminary_amount) " +
            "values(#{createDate}, #{name}, #{sex}, #{age}, #{idCard}, #{birthday}" +
            ", #{addressDetail},#{marriage}, #{phone}, #{healthy}, #{currentProvince}, #{currentCity}, #{currentArea} " +
            ", #{currentDetail} ,#{familyNum}, #{liveStatus}, #{familySalary}, #{companyName} " +
            ", #{companyProvince}, #{companyCity}, #{companyArea}, #{companyDetail}, #{workAge}, #{salary}, #{companyTel}" +
            ", #{majorSourceRevenue}, #{socialSecurityFlag}, #{assetsFlag}, #{companyNature}, #{profession}" +
            ", #{position}, #{onboardTime}, #{houseSource}, #{houseType}, #{emergencyContact},#{emergencyContactPhone}, #{emergencyContactRelation}" +
            ", #{education},#{sysUserId},#{status},#{lendingInstitutions},NEXTVAL('member'),#{applyAmount},#{propertyLand},#{houseYears},#{loanProduct},#{loanTime},#{repaymentMethod},#{assureMeans}" +
            ", #{socialSecurityDepositPlace},#{socialSecurityFundAmount},#{businessArea},#{turnover},#{annualRunAmount}" +
            ",#{housingAssessment},#{housingAssessmentAmount},#{residualLoan},#{preliminaryAmount})" })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createMember(Member member);


    @Update(value = {"update member set status=#{status} where id=#{memberId}"})
    int updateStatus(Map map);


}
