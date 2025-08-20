package com.seagox.lowcode.auth.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.auth.serivce.IUploadService;
import com.seagox.lowcode.common.OssConfig;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 文件上传下载
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private IUploadService uploadService;
    
    @Autowired
    private OssConfig ossConfig;

    /**
     * 文件上传
     */
    @LogPoint("文件上传")
    @PostMapping("/putObject/{bucketName}")
    public ResultData putObject(@RequestParam("file") MultipartFile file, @PathVariable String bucketName) throws IOException {
    	List<String> accept = ossConfig.getAccept();
    	String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    	if(accept.contains(suffix)) {
    		String address = uploadService.uploadByInputStream(bucketName, file.getOriginalFilename(),
                    file.getContentType(), file.getSize(), file.getInputStream());
            if (StringUtils.isEmpty(address)) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "文件服务器配置有误");
            } else {
                return ResultData.success(address);
            }
    	} else {
    		return ResultData.warn(ResultCode.OTHER_ERROR, "文件类型不支持");
    	}
    }

    /**
     * 下载文件
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse response, String url, String fileName) {
        uploadService.download(response, url, fileName);
    }

    /**
     * 下载模版
     */
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response, String templateName) {
        uploadService.downloadTemplate(response, templateName);
    }

}
