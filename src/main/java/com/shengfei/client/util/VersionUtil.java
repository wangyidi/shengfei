package com.shengfei.client.util;

import org.apache.commons.lang.StringUtils;

import java.util.Properties;

public class VersionUtil {
    private static Properties properties = new Properties();

    public VersionUtil() {
    }

    public static String getVersion(String defaultVersion) {
        String version = "";

        try {
            version = properties.getProperty("version");
            String jarFile;
            if (StringUtils.isEmpty(version)) {
                jarFile = "/META-INF/maven/com.br.client/brCommonApi/pom.properties";
                properties.load(VersionUtil.class.getResourceAsStream(jarFile));
                version = properties.getProperty("version");
            }

            if (StringUtils.isEmpty(version)) {
                jarFile = VersionUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
                if (StringUtils.isNotEmpty(jarFile) && jarFile.endsWith(".jar")) {
                    int index = jarFile.lastIndexOf(47);
                    version = jarFile.substring(index + 1, jarFile.length() - 4);
                }
            }
        } catch (Exception var4) {
            version = defaultVersion;
        }

        return version;
    }
}