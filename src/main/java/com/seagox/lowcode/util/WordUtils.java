package com.seagox.lowcode.util;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class WordUtils {

    /***
     * word导出公共方法
     *
     * @param templateUrl 模板地址
     * @param params      参数
     * @param out         输出流
     * @param policyList  表格参数
     */
    public static void exportWord(String templateUrl, Map<String, Object> params, OutputStream out,
                                  List<String> policyList) {
        InputStream in = null;
        try {
            in = new URL(templateUrl).openStream();
            AbstractRenderPolicy<Object> objectAbstractRenderPolicy = new AbstractRenderPolicy<Object>() {
                @SuppressWarnings("rawtypes")
                @Override
                public void doRender(RenderContext renderContext) throws Exception {

                }
            };
            if (policyList != null) {
                ConfigureBuilder configureBuilder = Configure.builder();
                for (int i = 0; i < policyList.size(); i++) {
                    configureBuilder.bind(policyList.get(i), objectAbstractRenderPolicy);
                }
                Configure config = configureBuilder.build();
                XWPFTemplate template = XWPFTemplate.compile(templateUrl, config).render(params);
                template.write(out);
                template.close();
            } else {
                XWPFTemplate template = XWPFTemplate.compile(in).render(params);
                template.write(out);
                template.close();
            }
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
    
}
