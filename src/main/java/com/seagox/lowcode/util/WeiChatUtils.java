package com.seagox.lowcode.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
	 * 服务号用户信息地址
	 */
	public static final String SERVICE_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

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
	@Value("${third-party.mini-program.appid}")
	private String appletsAppid;

	/**
	 * 小程序SERCRET
	 */
	@Value("${third-party.mini-program.sercret}")
	private String appletsSercret;

	/**
	 * 服务号APPID
	 */
	@Value("${third-party.official-account.appid}")
	private String servicesAppid;

	/**
	 * 服务号SERCRET
	 */
	@Value("${third-party.official-account.sercret}")
	private String servicesSercret;

	/**
	 * 服务号access_token缓存
	 */
	private volatile String serviceAccessToken;

	/**
	 * 获取小程序登录凭证
	 */
	public JSONObject getAppletsLoginCertificate(String code) {
		try {
			String url = MINIPROGRAM_LOGIN_CERTIFICATE.replace("APPID", appletsAppid).replace("SECRET", appletsSercret)
					.replace("CODE", code);
			RestTemplate restTemplate = new RestTemplate();
			return parseJson(restTemplate.getForObject(url, String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取小程序access_token
	 */
	public JSONObject getAccessToken() {
		try {
			String url = ACCESS_TOKEN.replace("APPID", appletsAppid).replace("SECRET", appletsSercret);
			RestTemplate restTemplate = new RestTemplate();
			return parseJson(restTemplate.getForObject(url, String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 定时刷新服务号access_token，微信有效期为7200秒。
	 */
	@Scheduled(fixedRate = 7200 * 1000, initialDelay = 1000)
	public void refreshServiceAccessToken() {
		JSONObject jsonObject = getServiceAccessToken();
		if (jsonObject != null && !StringUtils.isEmpty(jsonObject.getString("access_token"))) {
			serviceAccessToken = jsonObject.getString("access_token");
		}
	}

	/**
	 * 获取服务号access_token。
	 */
	public JSONObject getServiceAccessToken() {
		try {
			String url = ACCESS_TOKEN.replace("APPID", servicesAppid).replace("SECRET", servicesSercret);
			RestTemplate restTemplate = new RestTemplate();
			return parseJson(restTemplate.getForObject(url, String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取服务号用户信息。
	 */
	public JSONObject getServiceUserInfo(String openid) {
		if (StringUtils.isEmpty(openid)) {
			return null;
		}
		if (StringUtils.isEmpty(serviceAccessToken)) {
			refreshServiceAccessToken();
		}
		if (StringUtils.isEmpty(serviceAccessToken)) {
			return null;
		}
		try {
			String url = SERVICE_USER_INFO.replace("ACCESS_TOKEN", serviceAccessToken).replace("OPENID", openid);
			RestTemplate restTemplate = new RestTemplate();
			JSONObject result = parseJson(restTemplate.getForObject(url, String.class));
			if (result != null && Integer.valueOf(40001).equals(result.getInteger("errcode"))) {
				refreshServiceAccessToken();
				url = SERVICE_USER_INFO.replace("ACCESS_TOKEN", serviceAccessToken).replace("OPENID", openid);
				result = parseJson(restTemplate.getForObject(url, String.class));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析微信返回的JSON字符串
	 */
	private JSONObject parseJson(String response) {
		System.err.println(response);
		if (StringUtils.isEmpty(response)) {
			return null;
		}
		return JSON.parseObject(response);
	}

	/**
	 * 发送小程序模版消息
	 */
	public JSONObject sendMiniProgramMsg(String accessToken, String templateId, String page, String openid) {
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
	public JSONObject sendServiceMsg(String templateId, String openid, String pagepath, JSONObject content) {
		JSONObject result = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("touser", openid);
		body.put("template_id", templateId);
		
		JSONObject miniprogram = new JSONObject();
		miniprogram.put("appid", appletsAppid);
		miniprogram.put("pagepath", pagepath);
		body.put("miniprogram", miniprogram);

		JSONObject data = new JSONObject();
		for (Map.Entry<String, Object> entry : content.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    JSONObject item = new JSONObject();
		    item.put("value", value);
			data.put(key, item);
		}
		body.put("data", data);
		String url = SEND_SERVICE_MSG.replace("ACCESS_TOKEN", serviceAccessToken);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		httpPost.setEntity(new StringEntity(body.toJSONString(), ContentType.APPLICATION_JSON.withCharset("UTF-8")));
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(httpPost);
			String resultStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			result = JSONObject.parseObject(resultStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
