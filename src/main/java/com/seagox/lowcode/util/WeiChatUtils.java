package com.seagox.lowcode.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 微信工具类
 */
@Component
public class WeiChatUtils {

    /**
     * 小程序登录凭证
     */
    public static final String MINIPROGRAM_LOGIN_CERTIFICATE = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";

    /**
     * 获取小程序及服务号access_token地址
     */
    public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";

    /**
     * 小程序发送模板消息地址
     */
    public static final String SEND_MINIPROGRAM_MSG = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=ACCESS_TOKEN";
    
    /**
     * 服务号发送模板消息地址
     */
    public static final String SEND_SERVICE_MSG = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**
     * 小程序APPID
     */
    @Value("${third-party.applets.appid}")
    private String appletsAppid;

    /**
     * 小程序SERCRET
     */
    @Value("${third-party.applets.sercret}")
    private String appletsSercret;

    /**
     * 获取小程序登录凭证
     */
    public JSONObject getAppletsLoginCertificate(String code) {
        try {
            String url = MINIPROGRAM_LOGIN_CERTIFICATE.replace("APPID", appletsAppid).replace("SECRET", appletsSercret)
                    .replace("CODE", code);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取access_token
     */
    public JSONObject getAccessToken() {
        try {
            String url = ACCESS_TOKEN.replace("APPID", appletsAppid).replace("SECRET", appletsSercret);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送小程序模版消息
     */
    public  JSONObject sendMiniProgramMsg(String accessToken, String templateId, String page, String openid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("template_id", templateId);
        jsonObject.put("page", page);
        jsonObject.put("touser", openid);

        JSONObject data = new JSONObject();
        JSONObject name = new JSONObject();
        name.put("value", "hello");
        data.put("name2", name);
        
        JSONObject thing = new JSONObject();
        thing.put("value", "hello");
        data.put("thing11", thing);
        
        JSONObject time = new JSONObject();
        time.put("value", "2023-08-09 16:10:00");
        data.put("time26", time);

        jsonObject.put("data", data);

//        String url = SEND_MINIPROGRAM_MSG.replace("ACCESS_TOKEN", accessToken);
//        String result = HttpClientUtils.sendPostJsonStr(url, jsonObject.toJSONString(), "text/plain");
//        if (!StringUtils.isEmpty(result)) {
//            return JSON.parseObject(result);
//        }
        return null;
    }
    
    /**
     * 发送服务号模版消息
     */
    public  JSONObject sendServiceMsg(String accessToken, String templateId, String page, String openid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("template_id", templateId);
        jsonObject.put("page", page);
        jsonObject.put("touser", openid);

        JSONObject data = new JSONObject();
        JSONObject name = new JSONObject();
        name.put("value", "hello");
        data.put("name2", name);
        
        JSONObject thing = new JSONObject();
        thing.put("value", "hello");
        data.put("thing11", thing);
        
        JSONObject time = new JSONObject();
        time.put("value", "2023-08-09 16:10:00");
        data.put("time26", time);

        jsonObject.put("data", data);

//        String url = SEND_SERVICE_MSG.replace("ACCESS_TOKEN", accessToken);
//        String result = HttpClientUtils.sendPostJsonStr(url, jsonObject.toJSONString(), "text/plain");
//        if (!StringUtils.isEmpty(result)) {
//            return JSON.parseObject(result);
//        }
        return null;
    }

}
