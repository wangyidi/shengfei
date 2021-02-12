package com.shengfei.controller;

import com.shengfei.constant.MemberStatusEnum;
import com.shengfei.dto.MemberFinalSearchDTO;
import com.shengfei.dto.MemberPreliminarySearchDTO;
import com.shengfei.dto.MemberSearchDTO;
import com.shengfei.dto.MemberWaiteSearchDTO;
import com.shengfei.entity.Member;
import com.shengfei.entity.User;
import com.shengfei.service.MemberService;
import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;


@Slf4j
@Api(tags = "客户")
@RestController
@RequestMapping("/api/admin/member")
public class MemberApiController {

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
    public ResultVO create( @Validated @RequestBody Member member, BindingResult bindingResult) {
        try {
            // 获取用户信息
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            Integer userId = user.getId();

            if (!ValidatorUtils.validate(MemberApiController.class,bindingResult)) {
                return ResultVO.parameterError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
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
     * 查询客户列表 --全部
     * @param memberSearchDTO
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
     * 查询客户列表 --待审列表
     * @param memberSearchDTO
     * @return
     */
    @ApiOperation(value = "查询客户 待审列表")
    @PostMapping("/preliminary/members")
    public ResultVO getPreliminaryMemberList(@RequestBody MemberPreliminarySearchDTO memberSearchDTO) {
        try {
            // 获取用户信息
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            Integer userId = user.getId();

            return ResultVO.success(memberService.getPreliminaryMemberList(memberSearchDTO,userId),"查询客户待审列表");
        }catch (Exception e){
            log.error("查询客户待审列表失败：{}",e.getMessage(),e);
            return ResultVO.systemError(e.getMessage());
        }
    }

    /**
     * 查询客户列表 --初审
     * @param memberSearchDTO
     * @return
     */
    @ApiOperation(value = "查询客户 初审列表")
    @PostMapping("/check/members")
    public ResultVO getCheckMemberList(@RequestBody MemberWaiteSearchDTO memberSearchDTO) {
        try {
            return ResultVO.success(memberService.getWaiteCheckMemberList(memberSearchDTO),"查询初审列表");
        }catch (Exception e){
            log.error("查询客户初审列表失败：{}",e.getMessage(),e);
            return ResultVO.systemError(e.getMessage());
        }
    }


    /**
     * 查询客户列表 --终审列表
     * @param memberSearchDTO
     * @return
     */
    @ApiOperation(value = "查询客户 终审列表")
    @PostMapping("/final/members")
    public ResultVO getFinalMemberList(@RequestBody MemberFinalSearchDTO memberSearchDTO) {
        try {
            return ResultVO.success(memberService.getFinalCheckMemberList(memberSearchDTO),"查询客户终审列表");
        }catch (Exception e){
            log.error("查询客户终审列表失败：{}",e.getMessage(),e);
            return ResultVO.systemError(e.getMessage());
        }
    }

    /**
     *  初审通过
     * @param memberId
     * @return
     */
    @ApiOperation(value = " 初审通过")
    @GetMapping("/check/{memberId}")
    public ResultVO checkPass(@PathVariable Integer memberId) {
        try {
            // check 改状态是否能被修改
            Member member = memberService.getById(memberId);
            if(member ==null || !member.getStatus().equals(MemberStatusEnum.WAITE_CHECK.getId())){
                return ResultVO.parameterError("该用户不能被初审通过");
            }
            memberService.updateStatus(memberId, MemberStatusEnum.FINAL_CHECK.getId());
            return ResultVO.success();
        }catch (Exception e){
            log.error("查询客户终审列表失败：{}",e.getMessage(),e);
            return ResultVO.systemError(e.getMessage());
        }
    }


    /**
     *  --终审通过
     * @param memberId
     * @return
     */
    @ApiOperation(value = " 终审通过")
    @GetMapping("/final/{memberId}")
    public ResultVO update(@PathVariable Integer memberId) {
        try {
            // check 改状态是否能被修改
            Member member = memberService.getById(memberId);
            if(member ==null || !member.getStatus().equals(MemberStatusEnum.FINAL_CHECK.getId())){
                return ResultVO.parameterError("该用户不能被终身完成");
            }
            memberService.updateStatus(memberId, MemberStatusEnum.SUCCESS.getId());
            return ResultVO.success();
        }catch (Exception e){
            log.error("查询客户终审列表失败：{}",e.getMessage(),e);
            return ResultVO.systemError(e.getMessage());
        }
    }

    /**
     *  退回到待审
     * @param memberId
     * @return
     */
    @ApiOperation(value = "退回到待审")
    @GetMapping("/return/{memberId}")
    public ResultVO returnCheck(@PathVariable Integer memberId) {
        try {
            memberService.updateStatus(memberId, MemberStatusEnum.WAITE_CHECK.getId());
            return ResultVO.success();
        }catch (Exception e){
            log.error("退回到待审失败：{}",e.getMessage(),e);
            return ResultVO.systemError(e.getMessage());
        }
    }

    /**
     *  终审拒绝
     * @param memberId
     * @return
     */
    @ApiOperation(value = "终审拒绝")
    @GetMapping("/reject/{memberId}")
    public ResultVO reject(@PathVariable Integer memberId) {
        try {
            Member member = memberService.getById(memberId);
            if(member ==null || !member.getStatus().equals(MemberStatusEnum.FINAL_CHECK.getId())){
                return ResultVO.parameterError("该用户不能终审拒绝");
            }
            memberService.updateStatus(memberId, MemberStatusEnum.REJECT.getId());
            return ResultVO.success();
        }catch (Exception e){
            log.error("终审拒绝失败：{}",e.getMessage(),e);
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
