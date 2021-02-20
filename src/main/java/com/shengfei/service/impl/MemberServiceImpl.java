package com.shengfei.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shengfei.constant.MemberStatusEnum;
import com.shengfei.dto.MemberFinalSearchDTO;
import com.shengfei.dto.MemberPreliminarySearchDTO;
import com.shengfei.dto.MemberSearchDTO;
import com.shengfei.dto.MemberWaiteSearchDTO;
import com.shengfei.entity.Member;
import com.shengfei.entity.MemberImage;
import com.shengfei.entity.User;
import com.shengfei.mapper.MemberMapper;
import com.shengfei.mapper.UserMapper;
import com.shengfei.service.MemberImageService;
import com.shengfei.service.MemberService;
import com.shengfei.utils.ValidatorUtils;
import com.shengfei.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private MemberImageService memberImageService;

    @Resource
    private UserMapper userMapper;

    /**
     * 待审创建
     * @param member
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createMember(Member member) {

        // 待审上传
        member.setStatus(MemberStatusEnum.WAITE_CHECK.getId());
        member.setCreateDate(new Date());
        memberMapper.createMember(member);
        Integer id = member.getId();
        List<MemberImage> imageList = member.getImageList();
        if (!ValidatorUtils.empty(imageList)) {
            imageList.forEach(e->{
                e.setCreateTime(new Date());
                e.setMemberId(id);
            });
            memberImageService.saveBatch(imageList);
        }
        return id;
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

        // 获取登记用户信息
        User user =  userMapper.selectById(member.getSysUserId());
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user,userVO);
        member.setSysUserBean(userVO);

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
        member.setStatus(MemberStatusEnum.WAITE_CHECK.getId());
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

    /**
     * 获取全部列表
     * @param memberSearchDTO
     * @return
     */
    @Override
    public PageInfo<Member> getMemberList(MemberSearchDTO memberSearchDTO) {

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(memberSearchDTO.getStartTime())) {
            queryWrapper.gt("create_date",memberSearchDTO.getStartTime());
        }
        if (StringUtils.isNotEmpty(memberSearchDTO.getEndTime())) {
            queryWrapper.lt("create_date",memberSearchDTO.getEndTime());
        }
        if (StringUtils.isNotEmpty(memberSearchDTO.getName())) {
            queryWrapper.like("name",memberSearchDTO.getName());
        }
        if (StringUtils.isNotEmpty(memberSearchDTO.getIdCard())) {
            queryWrapper.like("id_card",memberSearchDTO.getIdCard());
        }
        if (StringUtils.isNotBlank(memberSearchDTO.getBranchCompany())) {
            List<User>userList = userMapper.selectList(new QueryWrapper<User>().eq("branch_company",memberSearchDTO.getBranchCompany()));
            if (ValidatorUtils.empty(userList)) {
                return new PageInfo<> ();
            }
            List<Integer>ids = userList.stream().map(User::getId).collect(Collectors.toList());
            queryWrapper.in("sys_user_id",ids);
        }
        return getMemberPageInfo(queryWrapper,memberSearchDTO.getPageNum(),memberSearchDTO.getPageSize());
    }

    /**
     * 获取待审列表
     * @param memberSearchDTO
     * @param userId
     * @return
     */
    @Override
    public PageInfo<Member> getPreliminaryMemberList(MemberPreliminarySearchDTO memberSearchDTO,Integer userId) {

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sys_user_id",userId);

        if (StringUtils.isNotEmpty(memberSearchDTO.getStartTime())) {
            queryWrapper.gt("create_date",memberSearchDTO.getStartTime());
        }
        if (StringUtils.isNotEmpty(memberSearchDTO.getEndTime())) {
            queryWrapper.lt("create_date",memberSearchDTO.getEndTime());
        }
        if (StringUtils.isNotEmpty(memberSearchDTO.getName())) {
            queryWrapper.like("name",memberSearchDTO.getName());
        }
        PageInfo<Member> list = getMemberPageInfo(queryWrapper,memberSearchDTO.getPageNum(),memberSearchDTO.getPageSize());
        List<Member> memberList = list.getList();
        // phone 脱敏
        memberList.forEach(e->
            e.setIdCard(desensitizedIdNumber(e.getIdCard()))
        );
        return list;
    }

    /**
     * 获取初审列表
     * @param memberSearchDTO
     * @return
     */
    @Override
    public PageInfo<Member> getWaiteCheckMemberList(MemberWaiteSearchDTO memberSearchDTO) {

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",MemberStatusEnum.WAITE_CHECK.getId());

        if (StringUtils.isNotEmpty(memberSearchDTO.getStartTime())) {
            queryWrapper.gt("create_date",memberSearchDTO.getStartTime());
        }
        if (StringUtils.isNotEmpty(memberSearchDTO.getEndTime())) {
            queryWrapper.lt("create_date",memberSearchDTO.getEndTime());
        }
        if (StringUtils.isNotEmpty(memberSearchDTO.getName())) {
            queryWrapper.like("name",memberSearchDTO.getName());
        }
        if (StringUtils.isNotEmpty(memberSearchDTO.getIdCard())) {
            queryWrapper.like("id_card",memberSearchDTO.getIdCard());
        }
        if (StringUtils.isNotBlank(memberSearchDTO.getBranchCompany())) {
            List<User>userList = userMapper.selectList(new QueryWrapper<User>().eq("branch_company",memberSearchDTO.getBranchCompany()));
            if (ValidatorUtils.empty(userList)) {
                return new PageInfo<> ();
            }
            List<Integer>ids = userList.stream().map(User::getId).collect(Collectors.toList());
            queryWrapper.in("sys_user_id",ids);
        }
        return getMemberPageInfo(queryWrapper,memberSearchDTO.getPageNum(),memberSearchDTO.getPageSize());
    }

    @Override
    public void updateStatus(Integer memberId, Integer status) {
        Map<String,Object> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("status",status);
        memberMapper.updateStatus(map);
    }


    /**
     * 获取终审列表
     * @param memberSearchDTO
     * @return
     */
    @Override
    public PageInfo<Member> getFinalCheckMemberList(MemberFinalSearchDTO memberSearchDTO) {

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("status");
        queryWrapper.in("status",MemberStatusEnum.FINAL_CHECK.getId(),MemberStatusEnum.REJECT.getId(),MemberStatusEnum.SUCCESS.getId());

        if (StringUtils.isNotBlank(memberSearchDTO.getStartTime())) {
            queryWrapper.gt("create_date",memberSearchDTO.getStartTime());
        }
        if (StringUtils.isNotBlank(memberSearchDTO.getEndTime())) {
            queryWrapper.lt("create_date",memberSearchDTO.getEndTime());
        }
        if (StringUtils.isNotBlank(memberSearchDTO.getName())) {
            queryWrapper.like("name",memberSearchDTO.getName());
        }
        if (StringUtils.isNotBlank(memberSearchDTO.getIdCard())) {
            queryWrapper.like("id_card",memberSearchDTO.getIdCard());
        }
        if (StringUtils.isNotBlank(memberSearchDTO.getBranchCompany())) {
            List<User>userList = userMapper.selectList(new QueryWrapper<User>().eq("branch_company",memberSearchDTO.getBranchCompany()));
            if (ValidatorUtils.empty(userList)) {
                return new PageInfo<> ();
            }
            List<Integer>ids = userList.stream().map(User::getId).collect(Collectors.toList());
            queryWrapper.in("sys_user_id",ids);
        }


        return getMemberPageInfo(queryWrapper,memberSearchDTO.getPageNum(),memberSearchDTO.getPageSize());
    }





    private PageInfo<Member> getMemberPageInfo(QueryWrapper<Member> queryWrapper,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        queryWrapper.orderByDesc("create_date");
        List<Member> memberList = memberMapper.selectList(queryWrapper);

        memberList.forEach(e->{
            Integer id = e.getId();
            QueryWrapper<MemberImage> query = new QueryWrapper<>();
            query.eq("member_id",id);
            List<MemberImage> list = memberImageService.list(query);
            e.setImageList(list);
            User user =  userMapper.selectById(e.getSysUserId());
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user,userVO);
            e.setSysUserBean(userVO);
        });



        return new PageInfo<>(memberList);
    }





    private  String desensitizedIdNumber(String idNumber){
        if (!StringUtils.isEmpty(idNumber)) {
            if (idNumber.length() == 15){
                idNumber = idNumber.replaceAll("(\\w{6})\\w*(\\w{3})", "$1******$2");
            }
            if (idNumber.length() == 18){
                idNumber = idNumber.replaceAll("(\\w{6})\\w*(\\w{3})", "$1*********$2");
            }
        }
        return idNumber;
    }
}
