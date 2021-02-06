package com.shengfei.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.shengfei.dto.MemberSearchDTO;
import com.shengfei.entity.Member;
import com.shengfei.entity.MemberImage;
import com.shengfei.mapper.MemberMapper;

import javax.annotation.Resource;
import java.util.List;

public interface MemberService extends IService<Member> {

    /**
     * 创建用户
     * @param member
     * @return
     */
    Boolean createMember(Member member);

    /**
     * 查询客户详情
     * @param memberId
     * @return
     */
    Member getMember(Integer memberId);

    /**
     * 修改
     * @param member
     * @return
     */
    Boolean updateMember(Member member);

    /**
     * 查询全部客户列表
     * @param memberSearchDTO
     * @return
     */
    PageInfo<Member> getMemberList(MemberSearchDTO memberSearchDTO);
}
