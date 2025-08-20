package com.seagox.lowcode.auth.serivce;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public interface IUploadService {

    /**
     *
     * @param bucketName 桶名称
     * @param originalFilename 原文件名称
     * @param contentType 文本类型
     * @param size 文件大小
     * @param inputStream 文件输入流
     * @return
     */
    public String uploadByInputStream(String bucketName, String originalFilename, String contentType, long size, InputStream inputStream);

    /**
     *
     * @param response response
     * @param url 文件路径
     * @param fileName 文件名称
     */
    public void download(HttpServletResponse response, String url, String fileName);

    /**
     *
     * @param response response
     * @param templateName 模板名称
     */
    public void downloadTemplate(HttpServletResponse response, String templateName);

    /**
     *
     * @param url 文件路径
     * @param fileName 文件名称
     * @param response response
     */
    public void preview(String url, String fileName, HttpServletResponse response);
}
