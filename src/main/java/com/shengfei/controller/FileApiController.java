package com.shengfei.controller;

import cn.hutool.core.date.DateUtil;
import com.shengfei.shiro.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@Api(tags = "文件上传")
@Controller
@RequestMapping("/api/entry/file")
public class FileApiController {

    Logger logger = LoggerFactory.getLogger(FileApiController.class);
    /**
     * 上传文件并返回对应url
     * @param request
     * @return
     */
    @ApiOperation(value = "上传文件并返回对应url")
    @PostMapping("/upload")
    @ResponseBody
    public ResultVO upload(HttpServletRequest request) {
        // 将request变成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 获取multiRequest 中所有的文件名
        Iterator iter = multiRequest.getFileNames();
        List<String> urls = new ArrayList<>();
        while (iter.hasNext()) {
            //一次遍历所有文件
            MultipartFile file = multiRequest.getFile(iter.next().toString());
            if (file != null) {
                try {
                    String format = DateUtil.format(new Date(), "yyyyMMddHHmmss");
                    String year = DateUtil.format(new Date(), "yyyyMM");
                    // 获取源文件名称
                    String originalFilename = file.getOriginalFilename();
                    // 文件名如果包含以下内容的话:法院接收会出错导致上传法院失败
                    String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                    String fileName = year + "/" + fileType + "/" + format + "." + fileType;
                    File filepath = new File( request.getServletContext().getRealPath("//upload//equipViews")+ fileName);
                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    }
                    file.transferTo(filepath);
                    urls.add(fileName);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return ResultVO.success(urls,"上传完成");
    }

}
