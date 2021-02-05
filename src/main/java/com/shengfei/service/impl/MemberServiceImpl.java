package com.shengfei.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shengfei.entity.Member;
import com.shengfei.mapper.MemberMapper;
import com.shengfei.service.MemberService;
import com.shengfei.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

}
