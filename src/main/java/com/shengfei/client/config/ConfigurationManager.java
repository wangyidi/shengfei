package com.shengfei.client.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.shengfei.client.util.VersionUtil;
import com.shengfei.client.util.YamlUtil;
import org.apache.commons.lang.StringUtils;

public class ConfigurationManager {
    private static boolean isLoaded = false;
    private static Configuration configuration;
    private static ConfigurationManager cm = new ConfigurationManager();

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        return cm;
    }

    private Object readResolve() {
        return getInstance();
    }

    private static void loadConfig() {
        configuration = new Configuration();
        configuration.setKeyToolPwd((String)YamlUtil.get("client-config.keytoolPwd"));
        configuration.setKeyStorePath((String) YamlUtil.get("client-config.keyStorePath"));
        configuration.setTrustStorePath((String)YamlUtil.get("client-config.trustStorePath"));
        configuration.setUpdateThreshold((Integer)YamlUtil.get("client-config.updateThreshold"));
        configuration.setRetryTimes((Integer)YamlUtil.get("client-config.retryTimes"));
        configuration.setEncryType((Integer)YamlUtil.get("client-config.encryType"));
        configuration.setErrCountThreshold1((Integer)YamlUtil.get("client-config.errCountThreshold1"));
        configuration.setErrCountThreshold2((Integer)YamlUtil.get("client-config.errCountThreshold2"));
        configuration.setErrCountThreshold3((Integer)YamlUtil.get("client-config.errCountThreshold3"));
        configuration.setErrTimeThreshold1((Integer)YamlUtil.get("client-config.errTimeThreshold1"));
        configuration.setErrTimeThreshold2((Integer)YamlUtil.get("client-config.errTimeThreshold2"));
        configuration.setErrTimeThreshold3((Integer)YamlUtil.get("client-config.errTimeThreshold3"));
        configuration.setConnectimeOut((Integer)YamlUtil.get("client-config.connectimeOut"));
        configuration.setReadtimeOut((Integer)YamlUtil.get("client-config.readtimeOut"));
        configuration.setPooltimeOut((Integer)YamlUtil.get("client-config.pooltimeOut"));
        configuration.setMaxTotal((Integer)YamlUtil.get("client-config.maxTotal"));
        configuration.setMaxPerRoute((Integer)YamlUtil.get("client-config.maxPerRoute"));
        configuration.setRiskStrategy((Integer)YamlUtil.get("client-config.riskStrategy"));
        configuration.setTlsVersion((String)YamlUtil.get("client-config.tlsVersion"));
        configuration.setProxy((Boolean)YamlUtil.get("client-config.isProxy"));
        configuration.setProxyHost((String)YamlUtil.get("client-config.proxyHost"));
        configuration.setProxyPort((Integer)YamlUtil.get("client-config.proxyPort"));
        configuration.setIgnoreCertificate((Boolean)YamlUtil.get("client-config.isIgnoreCertificate"));
        configuration.setContentType((Integer)YamlUtil.get("client-config.contentType"));
        configuration.setApiCode(YamlUtil.getStr("client-config.apiCode"));
        configuration.setAppKey(YamlUtil.getStr("client-config.appKey"));
        Map<String, String> urls = new HashMap();
        List<Map<String, String>> remoteUrls = (List)YamlUtil.get("remote-urls");
        Iterator var2 = remoteUrls.iterator();

        while(var2.hasNext()) {
            Map<String, String> ele = (Map)var2.next();
            Set<Entry<String, String>> entrySet = ele.entrySet();
            Iterator iterator = entrySet.iterator();

            while(iterator.hasNext()) {
                Entry<String, String> entry = (Entry)iterator.next();
                urls.put(entry.getKey(), entry.getValue());
            }
        }

        configuration.setUrls(urls);
        Map<String, String> headers = new HashMap();
        List<Map<String, String>> configHeaders = (List)YamlUtil.get("client-config.headers");
        if (configHeaders != null) {
            Iterator var13 = configHeaders.iterator();

            while(var13.hasNext()) {
                Map<String, String> header = (Map)var13.next();
                Set<Entry<String, String>> entrySet = header.entrySet();
                Iterator iterator = entrySet.iterator();

                while(iterator.hasNext()) {
                    Entry<String, String> entry = (Entry)iterator.next();
                    headers.put(entry.getKey(), entry.getValue());
                }
            }

            configuration.setHeaders(headers);
        }

        List<String> domainList = new ArrayList();
        String defaultDomain = (String)YamlUtil.get("client-config.defaultDomain");
        String[] domainSplit = defaultDomain.split(",");
        String[] var19 = domainSplit;
        int var20 = domainSplit.length;

        for(int var9 = 0; var9 < var20; ++var9) {
            String domain = var19[var9];
            domainList.add(domain);
        }

        configuration.setDomainList(domainList);
        configuration.setVersion(VersionUtil.getVersion("V2.0"));
        isLoaded = true;
    }

    public <T> T getConfig(String configKey) {
        if ("keyToolPwd".equals(configKey)) {
            return (T) configuration.getKeyToolPwd();
        } else if ("keyStorePath".equals(configKey)) {
            return (T) configuration.getKeyStorePath();
        } else if ("trustStorePath".equals(configKey)) {
            return (T) configuration.getTrustStorePath();
        } else if ("apiCode".equals(configKey)) {
            return (T) configuration.getApiCode();
        } else if ("appKey".equals(configKey)) {
            return (T) configuration.getAppKey();
        } else if ("domainList".equals(configKey)) {
            return (T) configuration.getDomainList();
        } else if ("updateThreshold".equals(configKey)) {
            return (T) configuration.getUpdateThreshold();
        } else if ("retryTimes".equals(configKey)) {
            return (T) configuration.getRetryTimes();
        } else if ("encryType".equals(configKey)) {
            return (T) configuration.getEncryType();
        } else if ("errCountThreshold1".equals(configKey)) {
            return (T) configuration.getErrCountThreshold1();
        } else if ("errCountThreshold2".equals(configKey)) {
            return (T) configuration.getErrCountThreshold2();
        } else if ("errCountThreshold3".equals(configKey)) {
            return (T) configuration.getErrCountThreshold3();
        } else if ("errTimeThreshold1".equals(configKey)) {
            return (T) configuration.getErrTimeThreshold1();
        } else if ("errTimeThreshold2".equals(configKey)) {
            return (T) configuration.getErrTimeThreshold2();
        } else if ("errTimeThreshold3".equals(configKey)) {
            return (T) configuration.getErrTimeThreshold3();
        } else if ("connectimeOut".equals(configKey)) {
            return(T)  configuration.getConnectimeOut();
        } else if ("readtimeOut".equals(configKey)) {
            return(T)  configuration.getReadtimeOut();
        } else if ("pooltimeOut".equals(configKey)) {
            return (T) configuration.getPooltimeOut();
        } else if ("maxTotal".equals(configKey)) {
            return (T) configuration.getMaxTotal();
        } else if ("maxPerRoute".equals(configKey)) {
            return (T) configuration.getMaxPerRoute();
        } else if ("riskStrategy".equals(configKey)) {
            return (T) configuration.getRiskStrategy();
        } else if ("tlsVersion".equals(configKey)) {
            return (T) configuration.getTlsVersion();
        } else if ("isProxy".equals(configKey)) {
            return (T) configuration.isProxy();
        } else if ("proxyHost".equals(configKey)) {
            return (T) configuration.getProxyHost();
        } else if ("proxyPort".equals(configKey)) {
            return (T) configuration.getProxyPort();
        } else if ("isIgnoreCertificate".equals(configKey)) {
            return (T) configuration.isIgnoreCertificate();
        } else if ("contentType".equals(configKey)) {
            return (T) configuration.getContentType();
        } else {
            return "version".equals(configKey) ? (T) configuration.getVersion() : null;
        }
    }

    public String getUrl(String apiName) {
        Map<String, String> urls = configuration.getUrls();
        String url = (String)urls.get(apiName);
        if (StringUtils.isEmpty(url)) {
            url = apiName;
        }

        return url;
    }

    public List<String> getDomainList() {
        return configuration.getDomainList();
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = configuration.getHeaders();
        return headers != null && headers.size() != 0 ? headers : null;
    }

    static {
        if (configuration == null && !isLoaded) {
            loadConfig();
        }

    }
}