package com.shengfei.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class HttpsPoster {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpsPoster.class);
    public static final HostnameVerifier hnv = new HttpsPoster.MyHostnameVerifier();

    public HttpsPoster() {
    }

    public static KeyStore getKeyStore(String password, String keyStorePath) {
        KeyStore ks = null;
        Object is = null;

        try {
            File file;
            try {
                String keyStoPath = System.getProperty("keyStorePath");
                if (keyStoPath == null || "".equals(keyStoPath)) {
                    throw new Exception("jvm参数中未配置信任文件路径，开始使用配置文件brConfig.properties中的路径。");
                }

                file = new File(keyStoPath);
                if (!file.isFile()) {
                    throw new Exception("jvm启动参数配置的信任文件路径错误");
                }

                if (!file.getName().endsWith(".keystore")) {
                    throw new Exception("jvm启动参数配置的信任文件不是keystore类型");
                }

                is = new FileInputStream(file);
                ks = KeyStore.getInstance("JKS");
                ks.load((InputStream)is, password.toCharArray());
                LOGGER.info("jvm启动参数配置的信任文件加载成功!!!");
            } catch (Exception var19) {
                try {
                    file = new File(keyStorePath);
                    is = new FileInputStream(file);
                    ks = KeyStore.getInstance("JKS");
                    ks.load((InputStream)is, password.toCharArray());
                    LOGGER.info("从配置文件brConfig.properties中指定的路径取keyStore文件成功!!!");
                } catch (Exception var18) {
                    throw new Exception("从brConfig.properties参数中获取信任文件失败");
                }
            }
        } catch (Exception var20) {
            try {
                ks = KeyStore.getInstance("JKS");
                is = HttpsPoster.class.getClassLoader().getResourceAsStream("tomcat.keystore");
                if (is == null) {
                    throw new Exception("获取jar包内部证书文件失败");
                }

                ks.load((InputStream)is, password.toCharArray());
                LOGGER.info("获取jar包内部证书文件成功!!!");
            } catch (Exception var17) {
                LOGGER.error("从jvm启动参数, brConfig.properties参数, 内置证书文件 中获取信任文件均失败-->", var17);
            }
        } finally {
            try {
                if (is != null) {
                    ((InputStream)is).close();
                }
            } catch (IOException var16) {
            }

        }

        return ks;
    }

    public static SSLContext getSSLContext(String password, String keyStorePath, String trustStorePath) throws Exception {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = getKeyStore(password, keyStorePath);
        keyManagerFactory.init(keyStore, password.toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore = getKeyStore(password, trustStorePath);
        trustManagerFactory.init(trustStore);
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), (SecureRandom)null);
        return ctx;
    }

    static class MyHostnameVerifier implements HostnameVerifier {
        MyHostnameVerifier() {
        }

        public boolean verify(String host, SSLSession session) {
            return true;
        }
    }
}