package com.shengfei.client.util;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.shengfei.client.exception.AESException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AESAlgorithmUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AESAlgorithmUtil.class);
    private static final String encode = System.getProperty("file.encoding");

    public AESAlgorithmUtil() {
    }

    public static String encrypt(String content, String pwdKey) throws Exception {
        if (!StringUtils.isBlank(content) && !StringUtils.isBlank(pwdKey)) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(pwdKey.getBytes(StandardCharsets.UTF_8));
                kgen.init(128, secureRandom);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                byte[] byteContent = content.getBytes(encode);
                cipher.init(1, key);
                byte[] result = cipher.doFinal(byteContent);
                return Base64.encodeBase64URLSafeString(result);
            } catch (Exception var10) {
                LOGGER.error("对参数进行AES加解密过程中异常：", var10);
                throw new AESException("AESException:对参数进行AES加解密过程中异常", var10);
            }
        } else {
            throw new IllegalArgumentException("参数不合法");
        }
    }
}