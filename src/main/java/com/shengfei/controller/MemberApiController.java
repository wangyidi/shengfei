package com.shengfei.controller;

import com.shengfei.entity.Member;
import com.shengfei.service.MemberService;
import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
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
    public ResultVO create(@Validated @RequestBody Member member, BindingResult bindingResult) {
        try {
            if (!ValidatorUtils.validate(MemberApiController.class,bindingResult)) {
                return ResultVO.systemError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            memberService.save(member);
            log.info("客户添加成功");
            return ResultVO.success("添加完成");
        }catch (Exception e){
            log.error("客户添加失败：{}",e.getMessage());
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
    public ResultVO update(@PathVariable Integer id) {
        try {

            log.info("客户添加成功");
            return ResultVO.success("添加完成");
        }catch (Exception e){
            log.error("客户添加失败：{}",e.getMessage());
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
                return ResultVO.systemError("用户id为空");
            }
            if (!ValidatorUtils.validate(MemberApiController.class,bindingResult)) {
                return ResultVO.systemError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            log.info("修改客户资料成功");
            return ResultVO.success("修改客户资料成功");
        }catch (Exception e){
            log.error("修改客户资料失败：{}",e.getMessage());
            return ResultVO.systemError(e.getMessage());
        }

    }
}
