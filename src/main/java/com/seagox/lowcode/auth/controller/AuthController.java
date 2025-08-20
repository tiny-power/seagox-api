package com.seagox.lowcode.auth.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.auth.serivce.IAuthService;
import com.seagox.lowcode.auth.serivce.IUploadService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.DicDetail;
import com.seagox.lowcode.mapper.DicDetailMapper;
import com.seagox.lowcode.service.IGenerateService;
import com.seagox.lowcode.util.WeiChatUtils;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

/**
 * 认证
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private IAuthService authService;

	@Autowired
	private WeiChatUtils weiChatUtils;

	@Autowired
    private IUploadService uploadService;
	
	@Autowired
    private DicDetailMapper dicDetailMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private IGenerateService generateService;
	
	@GetMapping("/unit")
	public ResultData unit() {
		InputStream inputStream = null;
		try {
			Resource resource = new ClassPathResource("\\template\\sojson.json");
			inputStream = resource.getInputStream();
			JSONArray result = JSONArray.parseArray(IOUtils.toString(inputStream, "utf-8"));
			Long classifyId = 3L;
			LambdaQueryWrapper<DicDetail> qwDetail = new LambdaQueryWrapper<>();
			qwDetail.eq(DicDetail::getClassifyId, classifyId);
			dicDetailMapper.delete(qwDetail);
			JSONArray data = new JSONArray();
			data = ad(result, 1, data);
			Set<String> unitSet = new HashSet<String>();
			for (int j = 0; j < data.size(); j++) {
				JSONObject item = data.getJSONObject(j);
				if(item.containsKey("unit")) {
					unitSet.add(item.getString("unit"));
				}
			}
			List<String> untList = unitSet.stream().collect(Collectors.toList());
			for (int j = 0; j < untList.size(); j++) {
				DicDetail dicDetail = new DicDetail();
				dicDetail.setClassifyId(classifyId);
				dicDetail.setCode(String.valueOf(j));
				dicDetail.setLastStage(1);
				dicDetail.setName(untList.get(j));
				dicDetail.setSort(j);
				dicDetail.setStatus(1);
				if(!StringUtils.isEmpty(dicDetail.getName())) {
					dicDetailMapper.insert(dicDetail);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ResultData.success(null);
	}
	
	@GetMapping("/classify")
	public ResultData classify() {
		InputStream inputStream = null;
		try {
			Resource resource = new ClassPathResource("\\template\\sojson.json");
			inputStream = resource.getInputStream();
			JSONArray result = JSONArray.parseArray(IOUtils.toString(inputStream, "utf-8"));
			Long classifyId = 17L;
			LambdaQueryWrapper<DicDetail> qwDetail = new LambdaQueryWrapper<>();
			qwDetail.eq(DicDetail::getClassifyId, classifyId);
			dicDetailMapper.delete(qwDetail);
			JSONArray data = new JSONArray();
			data = ad(result, 1, data);
			JSONObject parentObject = new JSONObject();
			JSONObject sortObject = new JSONObject();
			for (int j = 0; j < data.size(); j++) {
				JSONObject item = data.getJSONObject(j);
				int level = item.getIntValue("level");
				DicDetail dicDetail = new DicDetail();
				dicDetail.setClassifyId(classifyId);
				dicDetail.setCode(item.getString("value"));
				dicDetail.setName(item.getString("label"));
				dicDetail.setLastStage(1);
				if (!sortObject.containsKey(String.valueOf(level))) {
					dicDetail.setSort(1);
					sortObject.put(String.valueOf(level), 1);
				} else {
					int sort = sortObject.getIntValue(String.valueOf(level));
					dicDetail.setSort(sort + 1);
					sortObject.put(String.valueOf(level), sort + 1);
				}
				if (level != 1) {
					dicDetail.setParentId(parentObject.getLong(item.getString("parentNode")));
				}
				if(dicDetail.getParentId() != null) {
					String sql = "update dic_detail set last_stage = 0 where id = " + dicDetail.getParentId();
					jdbcTemplate.update(sql);
		        }
				dicDetailMapper.insert(dicDetail);
				parentObject.put(item.getString("value"), dicDetail.getId());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ResultData.success(null);
	}
	
	@GetMapping("/category")
	public ResultData category() {
		InputStream inputStream = null;
		try {
			Resource resource = new ClassPathResource("\\template\\sojson.json");
			inputStream = resource.getInputStream();
			JSONArray result = JSONArray.parseArray(IOUtils.toString(inputStream, "utf-8"));
			for(int i=0;i<result.size();i++) {
				JSONObject json = result.getJSONObject(i);
				Long classifyId = 0L;
				if(json.getString("label").equals("房屋和构筑物")) {
					classifyId = 44L;
				} else if(json.getString("label").equals("设备")) {
					classifyId = 35L;
				} else if(json.getString("label").equals("文物和陈列品")) {
					classifyId = 36L;
				} else if(json.getString("label").equals("图书和档案")) {
					classifyId = 12L;
				} else if(json.getString("label").equals("家具和用具")) {
					classifyId = 39L;
				} else if(json.getString("label").equals("特种动植物")) {
					classifyId = 43L;
				} else if(json.getString("label").equals("物资")) {
					classifyId = 15L;
				} else if(json.getString("label").equals("无形资产")) {
					classifyId = 37L;
				}
				JSONArray data = new JSONArray();
				data = ad(json.getJSONArray("children"), 1, data);
				LambdaQueryWrapper<DicDetail> qwDetail = new LambdaQueryWrapper<>();
				qwDetail.eq(DicDetail::getClassifyId, classifyId);
				dicDetailMapper.delete(qwDetail);
				JSONObject parentObject = new JSONObject();
				JSONObject sortObject = new JSONObject();
				for (int j = 0; j < data.size(); j++) {
					JSONObject item = data.getJSONObject(j);
					int level = item.getIntValue("level");
					String lable = item.getString("label");
					String value = item.getString("value");
					DicDetail dicDetail = new DicDetail();
					dicDetail.setClassifyId(classifyId);
					dicDetail.setCode(value);
					dicDetail.setName(lable);
					dicDetail.setLastStage(1);
					if(!sortObject.containsKey(String.valueOf(level))) {
						dicDetail.setSort(1);
						sortObject.put(String.valueOf(level), 1);
					} else {
						int sort = sortObject.getIntValue(String.valueOf(level));
						dicDetail.setSort(sort + 1);
						sortObject.put(String.valueOf(level), sort + 1);
					}
					if(level != 1) {
						Long parentId = parentObject.getLong(item.getString("parentNode"));
						dicDetail.setParentId(parentId);
					}
					if(dicDetail.getParentId() != null) {
			        	String sql = "update dic_detail set last_stage = 0 where id = " + dicDetail.getParentId();
						jdbcTemplate.update(sql);
			        }
					dicDetailMapper.insert(dicDetail);
					parentObject.put(value, dicDetail.getId());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ResultData.success(null);
	}
	
	public static JSONArray ad(JSONArray data, int level, JSONArray result) {
		for(int i=0;i<data.size();i++) {
			JSONObject item = data.getJSONObject(i);
			JSONObject itemNew = new JSONObject();
			itemNew.put("label", item.getString("label"));
			itemNew.put("value", item.getString("value"));
			itemNew.put("parentNode", item.getString("parentNode"));
			itemNew.put("level", level);
			//if(item.containsKey("unit")) {
				itemNew.put("unit", item.getString("unit"));
			//}
			result.add(itemNew);
			if(item.containsKey("children")) {
				ad(item.getJSONArray("children"), level + 1, result);
			}
		}
		return result;
	}
	
	/**
	 * 登陆
	 *
	 * @param account  用户名
	 * @param password 密码
	 * @param openid   openid
	 * @param avatar   头像地址
	 */
	@PostMapping("/login")
	@LogPoint("登陆")
	public ResultData login(String account, String password, String openid, String avatar) {
		return authService.login(account, password, openid, avatar);
	}

	/**
	 * 控制台
	 *
	 * @param account  用户名
	 * @param password 密码
	 */
	@PostMapping("/console")
	@LogPoint("控制台")
	public ResultData loginConsole(String account, String password) {
		return authService.loginConsole(account, password);
	}

	/**
	 * 验证登陆
	 *
	 * @param org          组织
	 * @param account      用户名
	 * @param noncestr     随机数
	 * @param timestampStr 时间戳
	 * @param sign         签名
	 */
	@PostMapping("/verifyLogin")
	public ResultData verifyLogin(String org, String account, String noncestr, String timestampStr, String sign) {
		return authService.verifyLogin(org, account, noncestr, timestampStr, sign);
	}

	/**
	 * 小程序登录
	 *
	 * @param code 编码
	 */
	@GetMapping("/loginByCode/{code}")
	public ResultData loginByCode(@PathVariable String code) {
		JSONObject jsonObject = weiChatUtils.getAppletsLoginCertificate(code);
		if (jsonObject != null) {
			if (jsonObject.containsKey("openid")) {
				return authService.verifyByOpenid(jsonObject.getString("openid"));
			} else {
				return ResultData.warn(ResultCode.INVALID_CODE);
			}
		} else {
			return ResultData.warn(ResultCode.INVALID_CODE);
		}
	}

	/**
	 * 验证小程序登录
	 *
	 * @param openid openid
	 */
	@GetMapping("/loginByOpenid/{openid}")
	public ResultData loginByOpenid(@PathVariable String openid) {
		return authService.loginByOpenid(openid);
	}

	/**
     * 在线预览
     */
    @GetMapping("/preview")
    public void preview(String url, String fileName, HttpServletResponse response) {
        uploadService.preview(url, fileName, response);
    }
    
    /**
	 * 代码生成
	 *
	 * @param mark 标识
	 * @param tableName 表名
	 */
	@GetMapping("/generateCode")
	public ResultData generateCode(String mark, String tableName) {
		return generateService.generateCode(mark, tableName);
	}

}
