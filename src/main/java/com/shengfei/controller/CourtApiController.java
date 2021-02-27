package com.shengfei.controller;


import com.alibaba.fastjson.JSON;
import com.br.client.facade.MerchantServer;
import com.shengfei.config.HttpsClientRequestFactory;
import com.shengfei.dto.CourtDTO;
import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Api(tags = "第三方法院")
@RestController
@RequestMapping("/api/court")
public class CourtApiController {

    @Value("${court.strategy.id}")
    private String strategy_id;

    @Value("${court.token.name}")
    private String user;

    @Value("${court.token.pwd}")
    private String pwd;

    @Value("${court.token.url}")
    private String tokenUrl;

    @ApiOperation(value = "查询法院数据")
    @PostMapping("/court")
    public Object court(@Validated @RequestBody CourtDTO courtDTO, BindingResult bindingResult) {
        try {
            if (!ValidatorUtils.validate(CourtApiController.class,bindingResult)) {
                return ResultVO.parameterError(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            MerchantServer ms = new MerchantServer();
            JSONObject jso = new JSONObject();
            JSONObject reqData = new JSONObject();
            /**
             * apiName对应brConfig.properties中的url，根据产品类型传参。
             * 贷前：strategyApi
             * 验证：verificationApi
             */
            jso.put("apiName", "strategyApi");
            reqData.put("strategy_id",strategy_id);
            reqData.put("id",courtDTO.getIdCard());
            reqData.put("cell",courtDTO.getPhone());
            reqData.put("name",courtDTO.getName());
            jso.put("reqData", reqData);
            String result = ms.getApiData(jso.toString());
            return ResultVO.success(JSON.parse(result));
        }catch (Exception e){
            log.error("查询法院数据报错：{}",e.getMessage(),e);
            return ResultVO.systemError("查询法院数据报错"+e.getMessage());
        }
    }

    @ApiOperation(value = "获取token")
    @PostMapping("/getToken")
    public Object token() {
        try {
            RestTemplate restTemplate =  new RestTemplate(new HttpsClientRequestFactory());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
            postParameters.add("username",user);
            postParameters.add("password",pwd);

            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postParameters, headers);
            log.info("[请求地址] {} \n [请求参数]{} \n",tokenUrl, postParameters);
            ResponseEntity<String> responseEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST, httpEntity, String.class);
            log.info("[请求地址] {} \n [请求参数]{} \n [响应码] {} \n [响应数据] {}",tokenUrl,JSON.toJSONString(httpEntity),responseEntity.getStatusCode(),responseEntity.getBody());

            return ResultVO.success(JSON.parse(responseEntity.getBody()));
        }catch (Exception e){
            log.error("查询Token错误：{}",e.getMessage(),e);
            return ResultVO.systemError("查询Token错误 ："+e.getMessage());
        }
    }
}
