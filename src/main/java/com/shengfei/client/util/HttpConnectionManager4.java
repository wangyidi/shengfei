package com.shengfei.client.util;

import com.shengfei.client.base.Assist;
import com.shengfei.client.config.ConfigurationManager;
import com.shengfei.client.exception.ServerException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpConnectionManager4 {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnectionManager4.class);
    private static ConfigurationManager cm = ConfigurationManager.getInstance();
    private static final Object syncLock = new Object();
    private static PoolingHttpClientConnectionManager connectionManager;
    private static volatile CloseableHttpClient httpClient;

    public HttpConnectionManager4() {
    }

    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized(syncLock) {
                if (httpClient == null) {
                    httpClient = createHttpClient();
                }
            }
        }

        return httpClient;
    }

    public static CloseableHttpClient createHttpClient() {
        int connectimeOut = (Integer)cm.getConfig("connectimeOut");
        int readtimeOut = (Integer)cm.getConfig("readtimeOut");
        int pooltimeOut = (Integer)cm.getConfig("pooltimeOut");
        boolean isProxy = (Boolean)cm.getConfig("isProxy");
        RequestConfig requestConfig = null;
        if (isProxy) {
            String proxyHost = (String)cm.getConfig("proxyHost");
            int proxyPort = (Integer)cm.getConfig("proxyPort");
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            requestConfig = RequestConfig.custom().setConnectTimeout(connectimeOut * 1000).setConnectionRequestTimeout(pooltimeOut * 1000).setSocketTimeout(readtimeOut * 1000).setProxy(proxy).setCookieSpec("best-match").build();
        } else {
            requestConfig = RequestConfig.custom().setConnectTimeout(connectimeOut * 1000).setConnectionRequestTimeout(pooltimeOut * 1000).setSocketTimeout(readtimeOut * 1000).setCookieSpec("best-match").build();
        }

        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {
                    return false;
                } else if (exception instanceof NoHttpResponseException) {
                    return true;
                } else if (exception instanceof SSLHandshakeException) {
                    return false;
                } else if (exception instanceof InterruptedIOException) {
                    return false;
                } else if (exception instanceof UnknownHostException) {
                    return false;
                } else if (exception instanceof ConnectTimeoutException) {
                    return false;
                } else if (exception instanceof SSLException) {
                    return false;
                } else {
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    return !(request instanceof HttpEntityEnclosingRequest);
                }
            }
        };
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
        httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).setRedirectStrategy(redirectStrategy).setRetryHandler(httpRequestRetryHandler).evictExpiredConnections().evictIdleConnections(30L, TimeUnit.SECONDS).build();
        return httpClient;
    }

    public static Map<String, String> remoteCall(String fullUrl, Map<String, Object> params) {
        Map<String, String> re = new HashMap();
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;

        try {
            httpPost = new HttpPost(fullUrl);
            Assist.setPostParams(httpPost, params);
            response = getHttpClient().execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 500) {
                throw new ServerException("响应状态码为" + statusCode + "异常");
            }

            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            re.put("servStr", result);
        } catch (Exception var16) {
            LOGGER.error("调用百融api服务发生异常,异常原因:", var16);
            re.put("cliJson", Assist.dealException(var16));
        } finally {
            try {
                if (response != null) {
                    response.close();
                }

                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
            } catch (IOException var15) {
                LOGGER.error("关闭连接IO异常：", var15);
            }

        }

        return re;
    }

    public static Map<String, String> downLoadFile(String fullUrl, Map<String, Object> paramMap, String downloadFilePath) {
        Map<String, String> re = new HashMap();
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        FileOutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            httpPost = new HttpPost(fullUrl);
            Assist.setPostParams(httpPost, paramMap);
            response = getHttpClient().execute(httpPost);
            HttpEntity entity = response.getEntity();
            Header contentType = entity.getContentType();
            if (!contentType.getValue().contains("download") && !contentType.getValue().contains("audio")) {
                String result = EntityUtils.toString(entity, "UTF-8");
                re.put("servStr", result);
            } else {
                inputStream = entity.getContent();
                File file = new File(downloadFilePath);
                if (!file.exists()) {
                    file.createNewFile();
                }

                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[2048];
                boolean var12 = false;

                int ch;
                while((ch = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, ch);
                }

                outputStream.flush();
                JSONObject servStr = new JSONObject();
                servStr.put("code", "000");
                servStr.put("message", "下载成功");
                re.put("servStr", servStr.toString());
            }

            EntityUtils.consume(entity);
        } catch (Exception var22) {
            LOGGER.error("调用百融api服务发生异常,异常原因:", var22);
            re.put("cliJson", Assist.dealException(var22));
        } finally {
            try {
                if (response != null) {
                    response.close();
                }

                if (httpPost != null) {
                    httpPost.releaseConnection();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException var21) {
                LOGGER.error("关闭连接IO异常：", var21);
            }

        }

        return re;
    }

    static {
        String keytoolPwd = (String)cm.getConfig("keyToolPwd");
        String keystorePath = (String)cm.getConfig("keyStorePath");
        String trustStorePath = (String)cm.getConfig("trustStorePath");
        boolean isIgnoreCertificate = (Boolean)cm.getConfig("isIgnoreCertificate");
        int maxTotal = (Integer)cm.getConfig("maxTotal");
        int maxPerRoute = (Integer)cm.getConfig("maxPerRoute");
        String tlsVersion = (String)cm.getConfig("tlsVersion");
        SSLContext sslcontext = null;

        try {
            if (isIgnoreCertificate) {
                sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
            } else {
                sslcontext = HttpsPoster.getSSLContext(keytoolPwd, keystorePath, trustStorePath);
            }

            SSLConnectionSocketFactory sslsf = isIgnoreCertificate ? new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) : (StringUtils.isBlank(tlsVersion) ? new SSLConnectionSocketFactory(sslcontext, HttpsPoster.hnv) : new SSLConnectionSocketFactory(sslcontext, new String[]{tlsVersion}, (String[])null, HttpsPoster.hnv));
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();
//            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
//            // by wangyidi
//            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
//                    sslContextBuilder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//                    .register("https", sslConnectionSocketFactory)
//                    .build();
            connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(maxTotal);
            connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        } catch (Exception var10) {
            LOGGER.error("初始化sslContext出错", var10);
        }

    }
}
