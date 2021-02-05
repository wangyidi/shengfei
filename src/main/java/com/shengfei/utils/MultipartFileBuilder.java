package com.shengfei.utils;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 文件上传builder
 *
 * @author Martin
 */
@Slf4j
@Data
public class MultipartFileBuilder {

    public String uploadFile(MultipartFile file,String prefix) throws Exception{
        String filePath =  getFilePath();
        if (!StringUtils.isNotBlank(prefix)) {
            filePath = prefix+filePath;
        }
        String fileUrl = filePath + getFileName() + getSuffix(file);

        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileUrl);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            out.write(file.getBytes());
            out.flush();
            out.close();
        }

        return fileUrl;

    }

    private String getSuffix (MultipartFile file) {
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

    }

    /**
     *  获取文件名称
     *
     * @return
     */
    private String getFileName(){
        IdWorker idWorker = new IdWorker(8,8,8);
        return String.valueOf(idWorker.nextId());

    }

    /**
     * 获取文件路径
     *
     * @return
     */
    private String getFilePath() {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd/");
        return ldt.format(dtf);
    }
}
