package com.shengfei.controller;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shengfei.constant.HouseUrlConstant;
import com.shengfei.dto.EvaluationDTO;
import com.shengfei.entity.Permission;
import com.shengfei.service.PermissionService;
import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.third.HouseRestTemplate;
import com.shengfei.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Api(tags = "第三方房产")
@RestController
@RequestMapping("/api/house")
public class HouseApiController {

    @Resource
    private HouseRestTemplate houseRestTemplate;

    @ApiOperation(value = "获取省列表")
    @GetMapping("/provinces")
    public Object provinces() {
        try {
            return houseRestTemplate.getRequest(HouseUrlConstant.HOUSE_PROVINCES,null);
        }catch (Exception e){
            log.error("菜单列表查询错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

    @ApiOperation(value = "获取市列表")
    @GetMapping("/citys/{provinceId}")
    public Object citys(@PathVariable Integer provinceId) {
        try {
            Map map = new HashMap<>();
            map.put("provinceId",provinceId);
            return houseRestTemplate.getRequest(HouseUrlConstant.HOUSE_CITYS,map);
        }catch (Exception e){
            log.error("获取市列表错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

    @ApiOperation(value = "获取区列表")
    @GetMapping("/areas/{cityId}")
    public Object areas(@PathVariable Integer cityId) {
        try {
            Map map = new HashMap<>();
            map.put("cityId",cityId);
            return houseRestTemplate.getRequest(HouseUrlConstant.HOUSE_AREAS,map);
        }catch (Exception e){
            log.error("区列表查询错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

    @ApiOperation(value = "获取楼盘列表")
    @GetMapping("/construction/{cityId}/{buildName}")
    public Object construction(@PathVariable Integer cityId, @PathVariable String buildName) {
        try {
            Map map = new HashMap<>();
            map.put("cityId",cityId);
            map.put("buildName",buildName);
            return houseRestTemplate.getRequest(HouseUrlConstant.HOUSE_CONSTRUCTION,map);
        }catch (Exception e){
            log.error("楼盘列表查询错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

    @ApiOperation(value = "获取楼盘详情")
    @GetMapping("/construction/{constructId}")
    public Object constructionView(@PathVariable Integer constructId) {
        try {
            Map map = new HashMap<>();
            map.put("constructId",constructId);
            return houseRestTemplate.getRequest(HouseUrlConstant.HOUSE_CONSTRUTIONVIEW,map);
        }catch (Exception e){
            log.error("获取楼盘详情错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

    @ApiOperation(value = "根据楼盘获取楼栋列表")
    @GetMapping("/build/{constructId}")
    public Object build(@PathVariable Integer constructId) {
        try {
            Map map = new HashMap<>();
            map.put("constructId",constructId);
            return houseRestTemplate.getRequest(HouseUrlConstant.HOUSE_BUILD,map);
        }catch (Exception e){
            log.error("获取楼盘详情错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

    @ApiOperation(value = "根据楼栋获取房间列表")
    @GetMapping("/houses/{buildId}")
    public Object houses(@PathVariable Integer buildId) {
        try {
            Map map = new HashMap<>();
            map.put("buildId",buildId);
            return houseRestTemplate.getRequest(HouseUrlConstant.HOUSE_HOUSE,map);
        }catch (Exception e){
            log.error("根据楼栋获取房间列表错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

    @ApiOperation(value = "估价")
    @PostMapping("/estateevaluation")
    public Object estateevaluation(@Validated @RequestBody EvaluationDTO evaluationDTO, BindingResult bindingResult) {
        try {
            if (!ValidatorUtils.validate(HouseApiController.class,bindingResult)) {
                return ResultVO.parameterError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            evaluationDTO.setCaseId(String.valueOf(new DateTime().getTime()));
            return houseRestTemplate.postRequest(HouseUrlConstant.HOUSE_ESTATEEVALUATION,evaluationDTO);
        }catch (Exception e){
            log.error("根据楼栋获取房间列表错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

    @ApiOperation(value = "估价单下载")
    @GetMapping("/PrintInfo/{caseId}")
    public Object houses(@PathVariable String caseId) {
        try {
            Map map = new HashMap<>();
            map.put("caseId",caseId);
            return houseRestTemplate.getFileRequest(HouseUrlConstant.HOUSE_PRINTINFO,map);
        }catch (Exception e){
            log.error("根据楼栋获取房间列表错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询错误"+e.getMessage());
        }
    }

}
