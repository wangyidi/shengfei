package com.shengfei.client.facade;

import com.shengfei.client.base.Assist;
import com.shengfei.client.base.FailoverHolder;
import com.shengfei.client.config.ConfigurationManager;
import com.shengfei.client.util.HttpConnectionManager4;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MerchantServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantServer.class);
    private static ConfigurationManager cm = ConfigurationManager.getInstance();
    private static int retryTimes;

    public MerchantServer() {
    }

    public String getApiData(String jsonData) {
        return this.getApiData(jsonData, (String)cm.getConfig("apiCode"), (String)cm.getConfig("appKey"));
    }

    public String getApiData(String jsonData, String apiCode, String appKey) {
        String result = "";

        JSONObject errMeg;
        try {
            JSONObject jsonObject = JSONObject.fromObject(jsonData);
            if (!jsonObject.containsKey("reqData")) {
                errMeg = new JSONObject();
                errMeg.put("code", "C100002");
                errMeg.put("message", "输入参数格式错误！");
                result = errMeg.toString();
            } else {
                result = this.getData(jsonObject, apiCode, appKey);
            }
        } catch (Exception var7) {
            LOGGER.error("参数检查过程中发生异常,", var7);
            errMeg = new JSONObject();
            errMeg.put("code", "C100003");
            errMeg.put("message", "输入参数不是JSON格式！");
            result = errMeg.toString();
        }

        return result;
    }

    protected String getData(JSONObject inputJsonObject, String apiCode, String appKey) {
        String resStr = "";

        try {
            Map<String, Object> paramMap = Assist.dealParams(inputJsonObject, apiCode, appKey);
            Map<String, String> res = null;
            String usingDomain;
            if (1 == Assist.getRiskStrategy()) {
                for(int retryTime = 0; retryTime < retryTimes; ++retryTime) {
                    usingDomain = FailoverHolder.getCurrDomain();
                    String fullUrl = Assist.getFullUrl(usingDomain, inputJsonObject);
                    res = HttpConnectionManager4.remoteCall(fullUrl, paramMap);
                    String cliJson = (String)res.get("cliJson");
                    if (!StringUtils.isNotEmpty(cliJson)) {
                        break;
                    }

                    JSONObject re = JSONObject.fromObject(cliJson);
                    String code = re.getString("code");
                    if (!Assist.isNeedRetry(code)) {
                        break;
                    }

                    FailoverHolder.errProcess(usingDomain);
                }
            } else {
                usingDomain = FailoverHolder.getCurrDomain();
                usingDomain = Assist.getFullUrl(usingDomain, inputJsonObject);
                res = HttpConnectionManager4.remoteCall(usingDomain, paramMap);
            }

            resStr = StringUtils.isNotEmpty((String)res.get("cliJson")) ? (String)res.get("cliJson") : (String)res.get("servStr");
        } catch (Exception var13) {
            LOGGER.error("调用服务期间发生异常，具体异常信息：", var13);
            resStr = Assist.dealMerchantException(var13);
        }

        return resStr;
    }

    public String getFile(String jsonData, String downloadFilePath) {
        return this.getFile(jsonData, downloadFilePath, (String)cm.getConfig("apiCode"), (String)cm.getConfig("appKey"));
    }

    public String getFile(String jsonData, String downloadFilePath, String apiCode, String appKey) {
        String resStr = "";

        try {
            JSONObject inputJson = JSONObject.fromObject(jsonData);
            String fullUrl = Assist.getFullUrl(FailoverHolder.getCurrDomain(), inputJson);
            Map<String, Object> paramMap = Assist.dealParams(inputJson, apiCode, appKey);
            Map<String, String> res = HttpConnectionManager4.downLoadFile(fullUrl, paramMap, downloadFilePath);
            resStr = StringUtils.isNotEmpty((String)res.get("cliJson")) ? (String)res.get("cliJson") : (String)res.get("servStr");
        } catch (Exception var10) {
            LOGGER.error("调用服务期间发生异常，具体异常信息：", var10);
            resStr = Assist.dealMerchantException(var10);
        }

        return resStr;
    }

    static {
        retryTimes = (Integer)cm.getConfig("retryTimes");
    }
}
