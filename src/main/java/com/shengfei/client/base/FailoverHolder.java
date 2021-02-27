package com.shengfei.client.base;

import com.shengfei.client.config.ConfigurationManager;
import com.shengfei.client.util.HttpConnectionManager4;
import com.shengfei.client.util.VersionUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FailoverHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(FailoverHolder.class);
    private static String domainListPath;
    private static String currDomain;
    private static List<String> availableDomainList;
    private static List<String> lastRefreshBadDomains = new ArrayList();
    private static int updateThreshold;
    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("failOverGroup");
    private static AtomicInteger errCnt = new AtomicInteger(0);
    private static AtomicLong firstErrTime = new AtomicLong(0L);
    private static int errCountThreshold1;
    private static int errCountThreshold2;
    private static int errCountThreshold3;
    private static int errTimeThreshold1;
    private static int errTimeThreshold2;
    private static int errTimeThreshold3;
    private static int switchFlag = 1;
    private static int riskStrategy;
    private static String apiCode;
    private static String appKey;
    private static ConfigurationManager cm = ConfigurationManager.getInstance();
    private static final Object LOCK;
    private static long lastUpdateTime;
    private static volatile int failOverExitFlag;

    public FailoverHolder() {
    }

    public static String getCurrDomain() {
        return currDomain;
    }

    public static void setUpdateThreshold(int update) {
        updateThreshold = update;
    }

    public static void errProcess(String usingDomain) {
        if (errCnt.get() == 0) {
            firstErrTime.getAndSet(System.currentTimeMillis());
        }

        if (usingDomain.equals(currDomain)) {
            errCnt.incrementAndGet();
        }

        startFailover();
    }

    public static void startFailover() {
        if (!isAlive("failoverThread")) {
            synchronized(LOCK) {
                if (!isAlive("failoverThread")) {
                    (new Thread(THREAD_GROUP, new FailoverHolder.FailoverTask(), "failoverThread")).start();
                }
            }
        }

    }

    public static boolean isAlive(String threadName) {
        boolean flag;
        try {
            flag = false;
            int noThreads = THREAD_GROUP.activeCount();
            Thread[] lstThreads = new Thread[noThreads];
            THREAD_GROUP.enumerate(lstThreads);
            if (lstThreads.length > 0) {
                Thread[] var4 = lstThreads;
                int var5 = lstThreads.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Thread thread = var4[var6];
                    if (thread != null && threadName.equals(thread.getName())) {
                        flag = true;
                        break;
                    }
                }
            }
        } catch (Exception var8) {
            flag = false;
        }

        return flag;
    }

    public static boolean dealSwitch() {
        Long currTime = System.currentTimeMillis();
        long t = currTime - firstErrTime.get();
        boolean breakThread = false;
        boolean isSwitch = false;
        if (t < (long)(errTimeThreshold1 * 1000) && errCnt.get() >= errCountThreshold1 || t < (long)(errTimeThreshold2 * 1000) && errCnt.get() >= errCountThreshold2 || t < (long)(errTimeThreshold3 * 1000) && errCnt.get() >= errCountThreshold3) {
            isSwitch = true;
        }

        if (isSwitch) {
            switchFlag = 2;
            int i = availableDomainList.indexOf(currDomain);
            if (i == availableDomainList.size() - 1) {
                currDomain = (String)availableDomainList.get(0);
            } else {
                currDomain = (String)availableDomainList.get(i + 1);
            }

            resetErrCnt();
        } else if (t > (long)(5 * errTimeThreshold3 * 1000)) {
            resetErrCnt();
            breakThread = true;
        }

        return breakThread;
    }

    public static void resetErrCnt() {
        errCnt.getAndSet(0);
        firstErrTime.getAndSet(0L);
    }

    public static boolean detectDomain(String domain, int retryTimes, int wellTimes) {
        int times = 0;

        for(int i = 0; i < retryTimes; ++i) {
            JSONObject domainList = getDomainList(Assist.getFullUrl(domain, domainListPath));

            try {
                Thread.sleep(10000L);
            } catch (InterruptedException var7) {
                LOGGER.error("error-->", var7);
            }

            if (domainList != null) {
                ++times;
            }
        }

        boolean flag = false;
        if (times >= wellTimes) {
            flag = true;
        }

        return flag;
    }

    public static void dealSuccessUpdateDomainList(JSONObject domainList) {
        if (domainList != null && "00".equals(domainList.getString("code"))) {
            String[] split = domainList.getString("domainList").split(",");
            List<String> list = new ArrayList();
            List<String> badDomainlist = new ArrayList();
            String[] var4 = split;
            int var5 = split.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String domain = var4[var6];
                if (!detectDomain(domain, 3, 2)) {
                    LOGGER.warn("=============服务端返回的域名:{}连通性有问题,请排查原因！！！================", domain);
                    badDomainlist.add(domain);
                }

                list.add(domain);
            }

            if (badDomainlist.contains(split[0])) {
                return;
            }

            lastRefreshBadDomains = badDomainlist;
            availableDomainList = list;
            if (availableDomainList.size() > 0) {
                currDomain = (String)availableDomainList.get(0);
                resetErrCnt();
                switchFlag = 1;
            }

            String failoverConfig = domainList.getString("failoverConfig");
            String[] failoverConfigs = failoverConfig.split(",");
            String threshold1 = failoverConfigs[0];
            String[] threshold1Split = threshold1.split("/");
            String errCountThreshold1Str = threshold1Split[0];
            String errTimeThreshold1Str = threshold1Split[1];
            errCountThreshold1 = Integer.parseInt(errCountThreshold1Str);
            errTimeThreshold1 = Integer.parseInt(errTimeThreshold1Str);
            String threshold2 = failoverConfigs[1];
            String[] threshold2Split = threshold2.split("/");
            String errCountThreshold2Str = threshold2Split[0];
            String errTimeThreshold2Str = threshold2Split[1];
            errCountThreshold2 = Integer.parseInt(errCountThreshold2Str);
            errTimeThreshold2 = Integer.parseInt(errTimeThreshold2Str);
            String threshold3 = failoverConfigs[2];
            String[] threshold3Split = threshold3.split("/");
            String errCountThreshold3Str = threshold3Split[0];
            String errTimeThreshold3Str = threshold3Split[1];
            errCountThreshold3 = Integer.parseInt(errCountThreshold3Str);
            errTimeThreshold3 = Integer.parseInt(errTimeThreshold3Str);
            String updateThreshold = domainList.getString("updateThreshold");
            setUpdateThreshold(Integer.parseInt(updateThreshold));
        }

    }

    public static JSONObject getDomainList(String url) {
        JSONObject jsonData = JSONObject.fromObject("{}");
        jsonData.put("ver", VersionUtil.getVersion("V2.0"));
        jsonData.put("lan", "java");
        boolean isProxy = (Boolean)cm.getConfig("isProxy");
        jsonData.put("proxy", isProxy ? "2" : "1");
        jsonData.put("strategy", riskStrategy);
        jsonData.put("curr_domain", currDomain);
        jsonData.put("updateThreshold", updateThreshold);
        jsonData.put("lastRefreshBadDomains", lastRefreshBadDomains.toString());
        jsonData.put("status", switchFlag);
        jsonData.put("failoverConfig", getFailoverConfig());
        JSONObject reqData = new JSONObject();
        reqData.put("reqData", reqData);
        JSONObject result = null;
        HttpPost httPost = null;
        CloseableHttpResponse response = null;

        try {
            Map<String, Object> params = Assist.dealParams(reqData, apiCode, appKey);
            httPost = new HttpPost(url);
            Assist.setPostParams(httPost, params);
            response = HttpConnectionManager4.getHttpClient().execute(httPost);
            HttpEntity entity = response.getEntity();
            result = JSONObject.fromObject(EntityUtils.toString(entity, "UTF-8"));
        } catch (Exception var17) {
        } finally {
            try {
                if (response != null) {
                    response.close();
                }

                if (httPost != null) {
                    httPost.releaseConnection();
                }
            } catch (IOException var16) {
                LOGGER.error("error-->", var16);
            }

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("获取远程返回的配置信息{}", result);
        }

        return result;
    }

    private static String getFailoverConfig() {
        return errCountThreshold1 + "/" + errTimeThreshold1 + "," + errCountThreshold2 + "/" + errTimeThreshold2 + "," + errCountThreshold3 + "/" + errTimeThreshold3;
    }

    static {
        riskStrategy = (Integer)cm.getConfig("riskStrategy");
        errCountThreshold1 = (Integer)cm.getConfig("errCountThreshold1");
        errCountThreshold2 = (Integer)cm.getConfig("errCountThreshold2");
        errCountThreshold3 = (Integer)cm.getConfig("errCountThreshold3");
        errTimeThreshold1 = (Integer)cm.getConfig("errTimeThreshold1");
        errTimeThreshold2 = (Integer)cm.getConfig("errTimeThreshold2");
        errTimeThreshold3 = (Integer)cm.getConfig("errTimeThreshold3");
        domainListPath = cm.getUrl("getDomainListPath");
        updateThreshold = (Integer)cm.getConfig("updateThreshold");
        availableDomainList = cm.getDomainList();
        currDomain = (String)availableDomainList.get(0);
        if (riskStrategy == 1) {
            apiCode = (String)cm.getConfig("apiCode");
            appKey = (String)cm.getConfig("appKey");
            (new Thread(THREAD_GROUP, new FailoverHolder.UpdateConfigTask(), "updataConfigThread")).start();
        }

        LOCK = new Object();
        lastUpdateTime = 0L;
        failOverExitFlag = 0;
    }

    public static class FailoverTask implements Runnable {
        public FailoverTask() {
        }

        public void run() {
            FailoverHolder.failOverExitFlag = 1;

            while(true) {
                boolean breakFlag = FailoverHolder.dealSwitch();
                if (breakFlag) {
                    break;
                }

                if (FailoverHolder.switchFlag == 2 && FailoverHolder.detectDomain((String)FailoverHolder.availableDomainList.get(0), 3, 3)) {
                    FailoverHolder.switchFlag = 1;
                    FailoverHolder.currDomain = (String)FailoverHolder.availableDomainList.get(0);
                    FailoverHolder.resetErrCnt();
                    break;
                }

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException var3) {
                    FailoverHolder.LOGGER.error("error-->", var3);
                    Thread.currentThread().interrupt();
                }
            }

            FailoverHolder.failOverExitFlag = 0;
        }
    }

    public static class UpdateConfigTask implements Runnable {
        public UpdateConfigTask() {
        }

        public void run() {
            while(true) {
                while(true) {
                    try {
                        if (!FailoverHolder.isAlive("failoverThread")) {
                            long startTime = System.currentTimeMillis();
                            if (startTime - FailoverHolder.lastUpdateTime <= (long)(FailoverHolder.updateThreshold * 60 * 1000) && FailoverHolder.lastUpdateTime != 0L) {
                                if (FailoverHolder.riskStrategy == 1 && FailoverHolder.switchFlag == 2) {
                                    if (FailoverHolder.detectDomain((String)FailoverHolder.availableDomainList.get(0), 3, 3)) {
                                        FailoverHolder.switchFlag = 1;
                                        FailoverHolder.currDomain = (String)FailoverHolder.availableDomainList.get(0);
                                        FailoverHolder.resetErrCnt();
                                    }
                                } else {
                                    Thread.sleep(60000L);
                                }
                            } else {
                                JSONObject domainList = this.updateConfig();
                                if (FailoverHolder.riskStrategy == 1) {
                                    FailoverHolder.dealSuccessUpdateDomainList(domainList);
                                }

                                if (FailoverHolder.lastRefreshBadDomains.size() > 0) {
                                    FailoverHolder.getDomainList(Assist.getFullUrl(FailoverHolder.currDomain, FailoverHolder.domainListPath));
                                }
                            }
                        }
                    } catch (Exception var4) {
                    }
                }
            }
        }

        private JSONObject updateConfig() {
            int retryTimes = 0;
            JSONObject domainList = null;
            String usingDomain = FailoverHolder.currDomain;

            while(domainList == null) {
                domainList = FailoverHolder.getDomainList(Assist.getFullUrl(usingDomain, FailoverHolder.domainListPath));
                if (FailoverHolder.availableDomainList.indexOf(usingDomain) == FailoverHolder.availableDomainList.size() - 1) {
                    usingDomain = (String)FailoverHolder.availableDomainList.get(0);
                } else {
                    usingDomain = (String)FailoverHolder.availableDomainList.get(FailoverHolder.availableDomainList.indexOf(usingDomain) + 1);
                }

                ++retryTimes;
                if (domainList == null && retryTimes == FailoverHolder.availableDomainList.size()) {
                    retryTimes = 0;

                    try {
                        Thread.sleep(120000L);
                    } catch (InterruptedException var5) {
                        FailoverHolder.LOGGER.error("error-->", var5);
                        Thread.currentThread().interrupt();
                    }
                }
            }

            FailoverHolder.lastUpdateTime = System.currentTimeMillis();
            return domainList;
        }
    }
}