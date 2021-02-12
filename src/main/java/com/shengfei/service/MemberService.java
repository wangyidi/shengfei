package com.shengfei.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.shengfei.dto.MemberFinalSearchDTO;
import com.shengfei.dto.MemberPreliminarySearchDTO;
import com.shengfei.dto.MemberSearchDTO;
import com.shengfei.dto.MemberWaiteSearchDTO;
import com.shengfei.entity.Member;

public interface MemberService extends IService<Member> {

    /**
     * 创建用户
     * @param member
     */
    void createMember(Member member);

    /**
     * 查询客户详情
     * @param memberId
     * @return Member
     */
    Member getMember(Integer memberId);

    /**
     * 修改
     * @param member
     * @return Boolean
     */
    Boolean updateMember(Member member);

    /**
     * 查询全部客户列表
     * @param memberSearchDTO
     * @return
     */
    PageInfo<Member> getMemberList(MemberSearchDTO memberSearchDTO);


    /**
     * 查询待审客户列表
     * @param memberSearchDTO
     * @return PageInfo<Member>
     */
    PageInfo<Member> getPreliminaryMemberList(MemberPreliminarySearchDTO memberSearchDTO,Integer userId);

    /**
     * 获取终审列表
     * @param memberSearchDTO
     * @return PageInfo<Member>
     */
    PageInfo<Member> getFinalCheckMemberList(MemberFinalSearchDTO memberSearchDTO);

    /**
     * 获取初审列表
     * @param memberSearchDTO
     * @return
     */
    PageInfo<Member> getWaiteCheckMemberList(MemberWaiteSearchDTO memberSearchDTO);

    /**
     * 更改状态
     * @param memberId
     * @param status
     */
    void updateStatus(Integer memberId,Integer status);
}
