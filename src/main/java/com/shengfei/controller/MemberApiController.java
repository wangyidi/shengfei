package com.shengfei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shengfei.dto.MemberSearchDTO;
import com.shengfei.entity.Member;
import com.shengfei.entity.SysToken;
import com.shengfei.mapper.TokenMapper;
import com.shengfei.service.MemberService;
import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.utils.TokenUtil;
import com.shengfei.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@Slf4j
@Api(tags = "客户")
@RestController
@RequestMapping("/api/admin/member")
public class MemberApiController {

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private MemberService memberService;

    /**
     * 添加客户资料
     * @param member
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加客户资料")
    @PostMapping("/create")
    public ResultVO create(HttpServletRequest httpServletRequest, @Validated @RequestBody Member member, BindingResult bindingResult) {
        try {
            // 获取用户信息
            String token = TokenUtil.getRequestToken(httpServletRequest);
            QueryWrapper<SysToken> queryWrapper = new QueryWrapper();
            queryWrapper.eq("token",token);
            SysToken sysToken =  tokenMapper.selectOne(queryWrapper);
            if (sysToken == null ){
                return ResultVO.systemError("请登陆");
            }

            Integer userId = sysToken.getUserId();

            if (!ValidatorUtils.validate(MemberApiController.class,bindingResult)) {
                return ResultVO.systemError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }

            member.setSysUserId(userId);
            memberService.createMember(member);
            log.info("客户添加成功");
            return ResultVO.success("添加完成");
        }catch (Exception e){
            log.error("客户添加失败：{}",e.getMessage(),e);
            return ResultVO.systemError(e.getMessage());
        }

    }

    /**
     *  查询客户详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询客户详情")
    @GetMapping("/member/{id}")
    public ResultVO getMember(@PathVariable Integer id) {
        try {
           Member member = memberService.getMember(id);
            log.info("查询客户详情 成功");
            return ResultVO.success(member,"添加完成");
        }catch (Exception e){
            log.error("查询客户详情失败：{}",e.getMessage(),e);
            return ResultVO.systemError(e.getMessage());
        }

    }



    /**
     *  查询客户列表
     *
     *
     * @return
     */
    @ApiOperation(value = "查询客户列表")
    @PostMapping("/members")
    public ResultVO getMemberList(@RequestBody MemberSearchDTO memberSearchDTO) {
        try {
            return ResultVO.success(memberService.getMemberList(memberSearchDTO),"查询客户列表");
        }catch (Exception e){
            log.error("查询客户列表失败：{}",e.getMessage(),e);
            return ResultVO.systemError(e.getMessage());
        }

    }

    /**
     * 修改客户资料
     * @param member
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "修改客户资料")
    @PutMapping("/update")
    public ResultVO update(@Validated @RequestBody Member member, BindingResult bindingResult) {
        try {
            if (ValidatorUtils.empty(member.getId())){
                return ResultVO.parameterError("用户id为空");
            }
            if (!ValidatorUtils.validate(MemberApiController.class,bindingResult)) {
                return ResultVO.parameterError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }

            memberService.updateMember(member);
            log.info("修改客户资料成功");
            return ResultVO.success("修改客户资料成功");
        }catch (Exception e){
            log.error("修改客户资料失败：{}",e.getMessage(),e);
            return ResultVO.systemError(e.getMessage());
        }
    }
}
