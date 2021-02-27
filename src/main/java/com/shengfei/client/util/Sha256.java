package com.shengfei.client.util;

import com.shengfei.client.exception.SHAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Sha256 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sha256.class);

    public Sha256() {
    }

    public static String getSHA256(String str) throws Exception {
        String encodeStr = "";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8.name()));
            encodeStr = byte2Hex(messageDigest.digest());
            return encodeStr;
        } catch (Exception var4) {
            LOGGER.error("SHA加密过程中出现异常：" + var4);
            throw new SHAException("SHAException:SHA加密过程中出现异常", var4);
        }
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        String temp = null;

        for(int i = 0; i < bytes.length; ++i) {
            temp = Integer.toHexString(bytes[i] & 255);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }

            stringBuffer.append(temp);
        }

        return stringBuffer.toString();
    }
}
