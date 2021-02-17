package com.shengfei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shengfei.entity.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

public interface MemberMapper extends BaseMapper<Member> {

    @Insert({"insert into member(create_date,name,sex,age,id_card,id_card_station,birthday" +
            ",address_detail,marriage,phone,healthy,current_province,current_city,current_area" +
            ",current_detail,family_num,live_status,telephone_area_code,zip_code,family_salary,company_name" +
            ",company_province,company_city,company_area,company_detail,work_age,salary,company_tel" +
            ",major_source_revenue,social_security_flag,assets_flag,company_code,company_nature,profession" +
            ",position,company_tel_code,onboard_time,house_source,house_type,emergency_contact,emergency_contact_phone,emergency_contact_relation" +
            ",emergency_contact_bk,emergency_contact_phone_bk,emergency_contact_relation_bk,education,sys_user_id,status,lending_institutions,sequence，apply_amount,business_type) " +
            "values(#{createDate}, #{name}, #{sex}, #{age}, #{idCard}, #{idCardStation}, #{birthday}, #{addressDetail}" +
            ",#{marriage}, #{phone}, #{healthy}, #{currentProvince}, #{currentCity}, #{currentArea}, #{currentDetail}" +
            ",#{familyNum}, #{liveStatus}, #{telephoneAreaCode}, #{zipCode}, #{familySalary}, #{companyName} " +
            ",#{companyProvince}, #{companyCity}, #{companyArea}, #{companyDetail}, #{workAge}, #{salary}, #{companyTel}" +
            ",#{majorSourceRevenue}, #{socialSecurityFlag}, #{assetsFlag}, #{companyCode}, #{companyNature}, #{profession}" +
            ",#{position}, #{companyTelCode}, #{onboardTime}, #{houseSource}, #{houseType}, #{emergencyContact},#{emergencyContactPhone}, #{emergencyContactRelation}" +
            ",#{emergencyContactBK},#{emergencyContactPhoneBK}, #{emergencyContactRelationBK}, #{education},#{sysUserId},#{status},#{lendingInstitutions},NEXTVAL('member'),#{applyAmount},#{businessType})" })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createMember(Member member);


    @Update(value = {"update member set status=#{status} where id=#{memberId}"})
    int updateStatus(Map map);

}
