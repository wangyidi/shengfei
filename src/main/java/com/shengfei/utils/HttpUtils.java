package com.shengfei.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {

    static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static JSONObject requestGet(String url) {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader reader = null;
        InputStreamReader isr = null;
        try {
            URL URL = new URL(url);
            URLConnection conn = URL.openConnection();
            isr = new InputStreamReader(conn.getInputStream(), "utf-8");
            reader = new BufferedReader(isr);// 转码
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if(reader !=null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            try {
                if(isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return JSONObject.parseObject(stringBuffer.toString());
    }

    //java 通过url下载图片保存到本地
    public static void download(String urlString, String i) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream is =con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        String filename = "D:\\images\\" + i + ".jpg";  //下载路径及下载图片名称
        File file = new File(filename);
        FileOutputStream os = new FileOutputStream(file, true);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        System.out.println(i);
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }

}
