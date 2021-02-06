package com.shengfei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shengfei.dto.MemberSearchDTO;
import com.shengfei.entity.Member;
import com.shengfei.entity.MemberImage;
import com.shengfei.mapper.MemberMapper;
import com.shengfei.service.MemberImageService;
import com.shengfei.service.MemberService;
import com.shengfei.utils.ValidatorUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private MemberImageService memberImageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createMember(Member member) {
        Integer id = memberMapper.createMember(member);

        List<MemberImage> imageList = member.getImageList();
        if (!ValidatorUtils.empty(imageList)) {
            imageList.forEach(e->{
                e.setCreateTime(new Date());
                e.setMemberId(id);
            });
        }
        memberImageService.saveBatch(imageList);
        return true;
    }

    /**
     * 获取用户详情
     * @param memberId
     * @return
     */
    @Override
    public Member getMember(Integer memberId) {

        Member member = memberMapper.selectById(memberId);

        QueryWrapper<MemberImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id",member.getId());
        // 获取imageList
        List<MemberImage> memberImageList = memberImageService.list(queryWrapper);
        member.setImageList(memberImageList);

        return member;
    }

    /**
     * 更新用户
     * @param member
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateMember(Member member) {
        Integer id = member.getId();

        memberMapper.updateById(member);

        List<MemberImage> imageList = member.getImageList();
        if (!ValidatorUtils.empty(imageList)) {
            imageList.forEach(e->{
                e.setCreateTime(new Date());
                e.setMemberId(id);
            });
        }
        memberImageService.saveOrUpdateBatch(imageList);
        return true;
    }

    @Override
    public PageInfo<Member> getMemberList(MemberSearchDTO memberSearchDTO) {

        PageHelper.startPage(memberSearchDTO.getPageNum(),memberSearchDTO.getPageSize());
        List<Member> memberList = list();

        memberList.forEach(e->{
           Integer id = e.getId();
           QueryWrapper queryWrapper = new QueryWrapper();
           queryWrapper.eq("member_id",id);
           List<MemberImage> list = memberImageService.list(queryWrapper);
            e.setImageList(list);
        });

        return new PageInfo<Member>(memberList);
    }
}
