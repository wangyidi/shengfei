package com.shengfei.controller;

import com.shengfei.shiro.vo.ResultVO;
import com.shengfei.utils.MultipartFileBuilder;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Api(tags = "文件上传")
@Controller
@RequestMapping("/api/entry/file")
public class FileApiController {

    @Value("${file.prefix}")
    private String prefix;

    @PostMapping("/upload")
    @ResponseBody
    public ResultVO upload(@RequestParam("file") MultipartFile file) {
        try {
            MultipartFileBuilder multipartFileBuilder = new MultipartFileBuilder();
            String filePath = multipartFileBuilder.uploadFile(file,prefix);
            log.info("上传成功 file path: {}",filePath );
            return ResultVO.success(filePath,"上传成功");
        } catch (Exception e) {
            log.error(e.toString(), e);
            return ResultVO.systemError("上传失败 :"+ e.getMessage());
        }

    }

}
