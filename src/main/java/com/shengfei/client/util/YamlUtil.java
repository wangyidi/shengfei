package com.shengfei.client.util;

import com.shengfei.config.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YamlUtil {
    private static Map<String, Object> ymlMap = new ConcurrentHashMap();
    private static final String CONFIG_FILE_NAME = "bootstrap.yml";
    private static final Logger LOGGER = LoggerFactory.getLogger(YamlUtil.class);

    public YamlUtil() {
    }

    private static Map<String, Object> loadYaml() {
        try {
            Yaml yaml = new Yaml();
            Resource resource = new ClassPathResource("bootstrap.yml");
//            InputStream inputStream = ClassLoader.getSystemResourceAsStream("classpath:bootstrap.yml");
            if (resource != null) {
                Map<String, Object> map = (Map)yaml.loadAs(resource.getInputStream(), Map.class);
                switchToMap((String)null, map);
            }
        } catch (Exception var3) {
            LOGGER.error("加载配置文件失败", var3);
        }

        return ymlMap;
    }

    private static void switchToMap(String myKey, Map<String, Object> map) {
        Iterator<String> it = map.keySet().iterator();
        myKey = myKey == null ? "" : myKey;
        String tmpkey = myKey;

        while(it.hasNext()) {
            String key = (String)it.next();
            myKey = tmpkey + key;
            Object value = map.get(key);
            if (value instanceof Map) {
                switchToMap(myKey.concat("."), (Map)value);
            } else if (null != value) {
                ymlMap.put(myKey, value);
            }
        }

    }

    public static <T> T get(String key) {
        return (T) ymlMap.get(key);
    }

    public static String getStr(String key) {
        return String.valueOf(ymlMap.get(key));
    }

    static {
        loadYaml();
    }
}