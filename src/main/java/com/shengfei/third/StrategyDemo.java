package com.shengfei.third;


import com.alibaba.fastjson.JSON;
import com.br.client.util.AESAlgorithmUtil;
import com.br.client.util.MD5Utils;
import com.shengfei.config.HttpsClientRequestFactory;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class StrategyDemo {
//
//	private static String url = "https://apiservice.100credit.com/api/auth-service/auth/getToken";
//
//	public static void main(String[] args) throws Exception {
//		RestTemplate restTemplate =  new RestTemplate(new HttpsClientRequestFactory());
//		HttpHeaders headers = new HttpHeaders();
//
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		String user = "sfeiStr";
//		String pwd = "sfeiStr123";
//
//		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
//		postParameters.add("username",user);
//		postParameters.add("password",pwd);
//
//
//		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postParameters, headers);
//		log.info("[请求地址] {} \n [请求参数]{} \n",url, postParameters);
//		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
//		log.info("[请求地址] {} \n [请求参数]{} \n [响应码] {} \n [响应数据] {}",url,JSON.toJSONString(httpEntity),responseEntity.getStatusCode(),responseEntity.getBody());
//		System.out.println(responseEntity.toString());
//
//
//
//
//	}




}
