package com.shengfei.third;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengfei.constant.HouseUrlConstant;
import com.shengfei.dto.EvaluationDTO;
import com.shengfei.model.HouseTokenModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class HouseRestTemplate {

    @Resource
    private RestTemplate restTemplate;

    @Value("${house.name}")
    private String houseName;

    @Value("${house.pwd}")
    private String housePwd;

    private volatile HouseTokenModel houseTokenModel;

    public Object getRequest(String url, Map map) throws Exception {
        String token = getToken().getToken();
//        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Basic "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = null;
        ResponseEntity<String> responseEntity = null;
        if(map != null){
            httpEntity = new HttpEntity<>(JSON.toJSONString(map),headers);
            log.info("[请求地址] {} \n [请求参数]{} \n",url,JSON.toJSONString(httpEntity));
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class,map);
            log.info("[请求地址] {} \n [请求参数]{} \n [响应码] {} \n [响应数据] {}",url,JSON.toJSONString(httpEntity),responseEntity.getStatusCode(),responseEntity.getBody());
        }else {
            httpEntity = new HttpEntity<>(headers);
            log.info("[请求地址] {} \n [请求参数]{} \n",url,JSON.toJSONString(httpEntity));
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            log.info("[请求地址] {} \n [请求参数]{} \n [响应码] {} \n [响应数据] {}",url,JSON.toJSONString(httpEntity),responseEntity.getStatusCode(),responseEntity.getBody());
        }

        return JSON.parse(responseEntity.getBody());
    }

    public Object getFileRequest(String url, Map map) throws Exception {
        String token = getToken().getToken();
//        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Basic "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = null;
//        if(map != null){
//            httpEntity = new HttpEntity<>(JSON.toJSONString(map),headers);
//        }else {
        httpEntity = new HttpEntity<>(headers);
//        }
        log.info("[请求地址] {} \n [请求参数]{} \n",url,JSON.toJSONString(httpEntity));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class,map);
        log.info("[请求地址] {} \n [请求参数]{} \n [响应码] {} \n [响应数据] {}",url,JSON.toJSONString(httpEntity),responseEntity.getStatusCode(),responseEntity.getBody());
        return responseEntity.getBody();
    }

    public Object postRequest(String url, EvaluationDTO evaluationDTO) throws Exception {
        String token = getToken().getToken();
//        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Basic "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = null;
        httpEntity = new HttpEntity<>(JSON.toJSONString(evaluationDTO),headers);

        log.info("[请求地址] {} \n [请求参数]{} \n",url,JSON.toJSONString(httpEntity));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        log.info("[请求地址] {} \n [请求参数]{} \n [响应码] {} \n [响应数据] {}",url,JSON.toJSONString(httpEntity),responseEntity.getStatusCode(),responseEntity.getBody());
        return JSON.parse(responseEntity.getBody());
    }

    private HouseTokenModel getToken() throws Exception {
        if(checkOverTime()) {
            String url = HouseUrlConstant.HOUSE_LOGIN;
            HttpHeaders headers = new HttpHeaders();//header参数
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject obj = new JSONObject();//放入body中的json参数
            obj.put("UserName", houseName);
            obj.put("Password", housePwd);

            HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(obj),headers);
//            RestTemplate restTemplate = new RestTemplate();
            log.info("[请求地址] {} \n [请求参数]{} \n",url,JSON.toJSONString(obj));
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST,request, String.class);
            log.info("[请求地址] {} \n [请求参数]{} \n [响应码] {} \n [响应数据] {}",url,JSON.toJSONString(obj),responseEntity.getStatusCode(),responseEntity.getBody());
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                JSONObject response = JSON.parseObject(responseEntity.getBody());
                houseTokenModel = HouseTokenModel.builder()
                        .token(response.getString("Token"))
                        .overTime(response.getString("OverTime"))
                        .build();
            }else {
                throw new Exception(responseEntity.getBody());
            }
        }
        return houseTokenModel;
    }


    private Boolean checkOverTime() throws ParseException {
        if(houseTokenModel == null) {
            return true;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date currentDate = new Date();
        Date overTime = df.parse(houseTokenModel.getOverTime());

        return currentDate.after(overTime);
    }

}
