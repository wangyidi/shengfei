package com.shengfei.third;


import lombok.extern.slf4j.Slf4j;

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
