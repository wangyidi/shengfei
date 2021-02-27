
package com.shengfei.client.base;

import com.shengfei.client.config.ConfigurationManager;
import com.shengfei.client.exception.AESException;
import com.shengfei.client.exception.SHAException;
import com.shengfei.client.exception.ServerException;
import com.shengfei.client.util.AESAlgorithmUtil;
import com.shengfei.client.util.MD5Utils;
import com.shengfei.client.util.Sha256;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

public class Assist {
    private static final Logger LOGGER = LoggerFactory.getLogger(Assist.class);
    private static ConfigurationManager cm = ConfigurationManager.getInstance();
    private static int encryType;
    private static int riskStrategy;
    private static int contentType;
    private static List<String> needRetryCodes = new ArrayList();
    private static Map<String, String> headers;

    public Assist() {
    }

    public static int getRiskStrategy() {
        return riskStrategy;
    }

    public static Map<String, Object> dealParams(JSONObject json, String apiCode, String appKey) throws Exception {
        String jsonData = json.getString("reqData");
        String checkCode = getCheckCode(jsonData, apiCode, appKey);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("生成的checkCode是{}", checkCode);
        }

        String encryptJsonData = AESAlgorithmUtil.encrypt(URLEncoder.encode(jsonData, StandardCharsets.UTF_8.name()), appKey);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("生成的jsonData是{}", encryptJsonData);
        }

        Map<String, Object> paramMap = new HashMap();
        String appKeyEncrpt = "";
        if (1 == encryType) {
            appKeyEncrpt = MD5Utils.genMd5(appKey);
        } else {
            appKeyEncrpt = Sha256.getSHA256(appKey);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("生成的加密后的appKey是{}", appKeyEncrpt);
        }

        paramMap.put("jsonData", encryptJsonData);
        paramMap.put("appKey", appKeyEncrpt);
        paramMap.put("apiCode", apiCode);
        paramMap.put("checkCode", checkCode);
        return paramMap;
    }

    public static String getCheckCode(String jsonData, String apiCode, String appKey) throws Exception {
        String checkCode = "";
        if (1 == encryType) {
            checkCode = MD5Utils.genMd5(jsonData + apiCode + appKey);
        } else {
            checkCode = Sha256.getSHA256(jsonData + apiCode + appKey);
        }

        return checkCode;
    }

    public static String getFullUrl(String currDomain, JSONObject inputJsonObject) {
        String apiName = inputJsonObject.getString("apiName");
        String apiUrl = cm.getUrl(apiName);
        return getFullUrl(currDomain, apiUrl);
    }

    public static String getFullUrl(String domain, String reqPath) {
        String fullUrl = "";
        if (riskStrategy == 0) {
            fullUrl = domain + reqPath;
        }

        if (riskStrategy == 1) {
            fullUrl = "https://" + domain + reqPath;
        }

        return fullUrl;
    }

    public static boolean isNeedRetry(String code) {
        return needRetryCodes.contains(code);
    }

    public static void setPostParams(HttpPost httpPost, Map<String, Object> params) {
        if (1 == contentType) {
            setKeyValueParams(httpPost, params);
        } else if (2 == contentType) {
            setJsonParams(httpPost, params);
        } else {
            setKeyValueParams(httpPost, params);
        }

        if (headers != null) {
            Set<Entry<String, String>> entrySet = headers.entrySet();
            Iterator iterator = entrySet.iterator();

            while(iterator.hasNext()) {
                Entry<String, String> entry = (Entry)iterator.next();
                httpPost.addHeader((String)entry.getKey(), (String)entry.getValue());
            }
        }

    }

    private static void setKeyValueParams(HttpPost httPost, Map<String, Object> params) {
        try {
            List<NameValuePair> nvps = new ArrayList();
            Set<Entry<String, Object>> entrySet = params.entrySet();
            Iterator iterator = entrySet.iterator();

            while(iterator.hasNext()) {
                Entry<String, Object> entry = (Entry)iterator.next();
                nvps.add(new BasicNameValuePair((String)entry.getKey(), entry.getValue().toString()));
            }

            httPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException var6) {
            LOGGER.error("设置参数失败", var6);
        }

    }

    private static void setJsonParams(HttpPost httPost, Map<String, Object> params) {
        StringEntity entity = new StringEntity(JSONObject.fromObject(params).toString(), StandardCharsets.UTF_8);
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httPost.setEntity(entity);
    }

    public static String dealException(Exception e) {
        JSONObject res = new JSONObject();
        if (e instanceof SocketTimeoutException) {
            res.put("code", "C100007");
            res.put("message", "https调用期间发生Read Time out异常: " + e.getLocalizedMessage());
        } else if (!(e instanceof ConnectTimeoutException) && !(e instanceof ConnectException)) {
            if (e instanceof UnknownHostException) {
                res.put("code", "C100009");
                res.put("message", "https调用期间Dns或hosts中配置有误异常: " + e.getLocalizedMessage());
            } else if (e instanceof ServerException) {
                res.put("code", "C100010");
                res.put("message", "https调用返回状态码为5xx异常: " + e.getLocalizedMessage());
            } else {
                res.put("code", "C100011");
                res.put("message", "https调用期间发生未知异常: " + e.getLocalizedMessage());
            }
        } else {
            res.put("code", "C100008");
            res.put("message", "https调用期间发生Connect Time Out异常: " + e.getLocalizedMessage());
        }

        return res.toString();
    }

    public static String dealMerchantException(Exception e) {
        JSONObject exMessage = new JSONObject();
        if (e instanceof AESException) {
            exMessage.put("code", "C100004");
            exMessage.put("message", "AES对请求参数进行加密过程出错！！！");
        } else if (e instanceof SHAException) {
            exMessage.put("code", "C100005");
            exMessage.put("message", "SHA对请求参数进行加密过程出错！！！");
        } else {
            exMessage.put("code", "C100006");
            exMessage.put("message", "其他未知异常！！！");
        }

        return exMessage.toString();
    }

    static {
        encryType = (Integer)cm.getConfig("encryType");
        riskStrategy = (Integer)cm.getConfig("riskStrategy");
        contentType = (Integer)cm.getConfig("contentType");
        headers = cm.getHeaders();
        needRetryCodes.add("C100007");
        needRetryCodes.add("C100008");
        needRetryCodes.add("C100009");
        needRetryCodes.add("C100010");
    }
}
