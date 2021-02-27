package com.shengfei.client.config;

import org.springframework.context.annotation.PropertySource;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Configuration {
    private String keyToolPwd;
    private String keyStorePath;
    private String trustStorePath;
    private String apiCode;
    private String appKey;
    private List<String> domainList;
    private Integer updateThreshold;
    private Integer retryTimes;
    private Integer encryType;
    private Integer errCountThreshold1;
    private Integer errCountThreshold2;
    private Integer errCountThreshold3;
    private Integer errTimeThreshold1;
    private Integer errTimeThreshold2;
    private Integer errTimeThreshold3;
    private Integer connectimeOut;
    private Integer readtimeOut;
    private Integer pooltimeOut;
    private Integer maxTotal;
    private Integer maxPerRoute;
    private Integer riskStrategy;
    private String tlsVersion;
    private Boolean isProxy;
    private String proxyHost;
    private Integer proxyPort;
    private Boolean isIgnoreCertificate;
    private Integer contentType;
    private Map<String, String> urls;
    private String version;
    private Map<String, String> headers;

    public Configuration() {
    }

    public String getKeyToolPwd() {
        return this.keyToolPwd;
    }

    public void setKeyToolPwd(String keyToolPwd) {
        this.keyToolPwd = keyToolPwd;
    }

    public String getKeyStorePath() {
        return this.keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getTrustStorePath() {
        return this.trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getApiCode() {
        return this.apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public List<String> getDomainList() {
        return Collections.unmodifiableList(this.domainList);
    }

    public void setDomainList(List<String> domainList) {
        this.domainList = Collections.unmodifiableList(domainList);
    }

    public Integer getUpdateThreshold() {
        return this.updateThreshold;
    }

    public void setUpdateThreshold(Integer updateThreshold) {
        this.updateThreshold = updateThreshold;
    }

    public Integer getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getEncryType() {
        return this.encryType;
    }

    public void setEncryType(Integer encryType) {
        this.encryType = encryType;
    }

    public Integer getErrCountThreshold1() {
        return this.errCountThreshold1;
    }

    public void setErrCountThreshold1(Integer errCountThreshold1) {
        this.errCountThreshold1 = errCountThreshold1;
    }

    public Integer getErrCountThreshold2() {
        return this.errCountThreshold2;
    }

    public void setErrCountThreshold2(Integer errCountThreshold2) {
        this.errCountThreshold2 = errCountThreshold2;
    }

    public Integer getErrCountThreshold3() {
        return this.errCountThreshold3;
    }

    public void setErrCountThreshold3(Integer errCountThreshold3) {
        this.errCountThreshold3 = errCountThreshold3;
    }

    public Integer getErrTimeThreshold1() {
        return this.errTimeThreshold1;
    }

    public void setErrTimeThreshold1(Integer errTimeThreshold1) {
        this.errTimeThreshold1 = errTimeThreshold1;
    }

    public Integer getErrTimeThreshold2() {
        return this.errTimeThreshold2;
    }

    public void setErrTimeThreshold2(Integer errTimeThreshold2) {
        this.errTimeThreshold2 = errTimeThreshold2;
    }

    public Integer getErrTimeThreshold3() {
        return this.errTimeThreshold3;
    }

    public void setErrTimeThreshold3(Integer errTimeThreshold3) {
        this.errTimeThreshold3 = errTimeThreshold3;
    }

    public Integer getConnectimeOut() {
        return this.connectimeOut;
    }

    public void setConnectimeOut(Integer connectimeOut) {
        this.connectimeOut = connectimeOut;
    }

    public Integer getReadtimeOut() {
        return this.readtimeOut;
    }

    public void setReadtimeOut(Integer readtimeOut) {
        this.readtimeOut = readtimeOut;
    }

    public Integer getPooltimeOut() {
        return this.pooltimeOut;
    }

    public void setPooltimeOut(Integer pooltimeOut) {
        this.pooltimeOut = pooltimeOut;
    }

    public Integer getMaxTotal() {
        return this.maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxPerRoute() {
        return this.maxPerRoute;
    }

    public void setMaxPerRoute(Integer maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public Integer getRiskStrategy() {
        return this.riskStrategy;
    }

    public void setRiskStrategy(Integer riskStrategy) {
        this.riskStrategy = riskStrategy;
    }

    public String getTlsVersion() {
        return this.tlsVersion;
    }

    public void setTlsVersion(String tlsVersion) {
        this.tlsVersion = tlsVersion;
    }

    public Boolean isProxy() {
        return this.isProxy;
    }

    public void setProxy(boolean proxy) {
        this.isProxy = proxy;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return this.proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public Boolean isIgnoreCertificate() {
        return this.isIgnoreCertificate;
    }

    public void setIgnoreCertificate(boolean ignoreCertificate) {
        this.isIgnoreCertificate = ignoreCertificate;
    }

    public Integer getContentType() {
        return this.contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getUrls() {
        return this.urls;
    }

    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}