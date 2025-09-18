package com.seagox.lowcode.util;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ExportUtils {

    /***
     * excel导出公共方法
     *
     * @param templateUrl 模板地址
     * @param fileName    导出文件名
     * @param params      参数
     * @param request     request
     * @param response    response
     */
    public static void exportExcel(String templateUrl, String fileName, Map<String, Object> params,
                                   HttpServletRequest request, HttpServletResponse response) {
        OutputStream os = null;
        InputStream in = null;
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((fileName + ".xlsx").getBytes("GBK"), "ISO-8859-1"));
            response.setContentType("application/octet-stream;charset=UTF-8");
            Context context = new Context();
            if (params != null) {
                for (String key : params.keySet()) {
                    context.putVar(key, params.get(key));
                }
            }
            in = new URL(templateUrl).openStream();
            os = response.getOutputStream();
            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            Transformer transformer = jxlsHelper.createTransformer(in, os);
            // 获得配置
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig()
                    .getExpressionEvaluator();
            // 函数强制，自定义功能
            Map<String, Object> funcs = new HashMap<String, Object>();
            funcs.put("utils", new JxlsUtils()); // 添加自定义功能
            JexlBuilder jb = new JexlBuilder();
            jb.namespaces(funcs);
            jb.silent(true); // 设置静默模式，不报警告
            JexlEngine je = jb.create();
            evaluator.setJexlEngine(je);
            transformer.setEvaluateFormulas(true);
            // 必须要这个，否者表格函数统计会错乱
            jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * excel导出公共方法
     *
     * @param templateUrl 模板地址
     * @param params      参数
     * @param out         输出流
     *
     */
    public static void exportExcel(String templateUrl, Map<String, Object> params, OutputStream out) {
        InputStream in = null;
        try {
            Context context = new Context();
            if (params != null) {
                for (String key : params.keySet()) {
                    context.putVar(key, params.get(key));
                }
            }
            in = new URL(templateUrl).openStream();
            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            Transformer transformer = jxlsHelper.createTransformer(in, out);
            // 获得配置
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig()
                    .getExpressionEvaluator();
            // 函数强制，自定义功能
            Map<String, Object> funcs = new HashMap<String, Object>();
            funcs.put("utils", new JxlsUtils()); // 添加自定义功能
            JexlBuilder jb = new JexlBuilder();
            jb.namespaces(funcs);
            jb.silent(true); // 设置静默模式，不报警告
            JexlEngine je = jb.create();
            evaluator.setJexlEngine(je);
            // 必须要这个，否者表格函数统计会错乱
            jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * excel导出公共方法(模板在项目里)
     *
     * @param templateName 模板名称
     * @param fileName    导出文件名
     * @param params      参数
     * @param request     request
     * @param response    response
     */
    public static void exportExcelTemplate(String templateName, String fileName, Map<String, Object> params,
                                           HttpServletRequest request, HttpServletResponse response) {
        OutputStream os = null;
        InputStream in = null;
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((fileName + ".xlsx").getBytes("GBK"), "ISO-8859-1"));
            response.setContentType("application/octet-stream;charset=UTF-8");
            Context context = new Context();
            if (params != null) {
                for (String key : params.keySet()) {
                    context.putVar(key, params.get(key));
                }
            }
            Resource resource = new ClassPathResource("\\template\\" + templateName);
            in = resource.getInputStream();
            os = response.getOutputStream();
            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            Transformer transformer = jxlsHelper.createTransformer(in, os);
            // 获得配置
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig()
                    .getExpressionEvaluator();
            // 函数强制，自定义功能
            Map<String, Object> funcs = new HashMap<String, Object>();
            funcs.put("utils", new JxlsUtils()); // 添加自定义功能
            JexlBuilder jb = new JexlBuilder();
            jb.namespaces(funcs);
            jb.silent(true); // 设置静默模式，不报警告
            JexlEngine je = jb.create();
            evaluator.setJexlEngine(je);
            // 必须要这个，否者表格函数统计会错乱
            jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
