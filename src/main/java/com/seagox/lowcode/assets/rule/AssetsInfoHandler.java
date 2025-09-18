package com.seagox.lowcode.assets.rule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.seagox.lowcode.entity.Serial;
import com.seagox.lowcode.entity.SysMessage;
import com.seagox.lowcode.mapper.MessageMapper;
import com.seagox.lowcode.mapper.SerialMapper;
import com.seagox.lowcode.strategy.rule.RuleHandler;
import com.seagox.lowcode.strategy.rule.RuleHandlerFactory;
import com.seagox.lowcode.util.JdbcTemplateUtils;
import com.seagox.lowcode.util.VerifyHandlerResult;

@Service
public class AssetsInfoHandler implements RuleHandler {

	@Autowired
	private JdbcTemplateUtils jdbcTemplateUtils;

	@Autowired
	private SerialMapper serialMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MessageMapper messageMapper;

	@Override
	public void afterPropertiesSet() throws Exception {
		RuleHandlerFactory.register("assets_info", this);
	}

	@Override
	public void insertBefore(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void insertAfter(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateBefore(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateAfter(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteBefore(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteAfter(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public VerifyHandlerResult importVerify(HttpServletRequest request, JSONObject row) {
		VerifyHandlerResult result = new VerifyHandlerResult(true);
		List<String> msg = new ArrayList<>();
		result.setMsg(msg);
		result.setSuccess(true);
		return result;
	}

	@Override
	public void importHandle(HttpServletRequest request, List<Map<String, Object>> result, Map<String, Object> params) {
		String tableName = params.get("tableName").toString();
		String field = "code";
		if (result.size() != 0) {
			JSONObject codeObject = getCode(tableName, field);
			String prefix = codeObject.getString("prefix");
			Long max = codeObject.getLong("max");
			int suffixLength = codeObject.getIntValue("suffixLength");
			for (int i = 0; i < result.size(); i++) {
				if (lpad(suffixLength, max).length() != suffixLength) {
					throw new IllegalArgumentException("顺序码超过" + suffixLength + "位数,请联系管理员!");
				}
				Map<String, Object> item = result.get(i);
				item.put(field, prefix + lpad(suffixLength, max));
				String businessKey = jdbcTemplateUtils.insert(tableName, item);
				SysMessage message = new SysMessage();
				message.setCompanyId(Long.valueOf(item.get("company_id").toString()));
				message.setFromUserId(Long.valueOf(item.get("user_id").toString()));
				message.setToUserId(Long.valueOf(item.get("user_id").toString()));
				message.setType(1);
				message.setBusinessKey(Long.valueOf(businessKey.toString()));
				message.setBusinessType(Long.valueOf(item.get("category").toString()));
				message.setStatus(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				message.setTitle("资产编码:" + item.get("code") + "(" + sdf.format(new Date()) + ")");
				messageMapper.insert(message);
				max = max + 1;
			}
		}
	}

	@Override
	public Map<String, Object> detailData(Map<String, Object> params) {

		return null;
	}

	@Override
	public void flowFinish(Map<String, Object> params) {

	}

	public JSONObject getCode(String tableName, String field) {
		JSONObject result = new JSONObject();
		Serial serialData = serialMapper.selectById(1);
		if (serialData == null) {
			throw new IllegalArgumentException("编号设置不存在");
		}
		JSONArray options = JSONArray.parseArray(serialData.getOptions());
		String prefix = "";
		int suffixLength = 0;
		for (int i = 0; i < options.size(); i++) {
			JSONObject option = options.getJSONObject(i);
			if (option.getInteger("type").equals(1)) {
				// 固定码
				String value = option.getString("value");
				prefix = prefix + value;
			} else if (option.getInteger("type").equals(2)) {
				// 年月日
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat(option.getString("format"));
				String value = sdf.format(date);
				prefix = prefix + value;
			} else if (option.getInteger("type").equals(3)) {
				// 顺序码
				suffixLength = option.getIntValue("length");
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT code FROM ");
		sql.append(tableName);
		sql.append(" WHERE ");
		sql.append(field);
		sql.append(" like ");
		sql.append("'");
		sql.append(prefix);
		sql.append("%'");
		sql.append(" and ");
		sql.append("LENGTH(");
		sql.append(field);
		sql.append(")");
		sql.append(" = ");
		sql.append(prefix.length() + suffixLength);
		sql.append(" ORDER BY create_time desc limit 1 ");
		result.put("prefix", prefix);
		result.put("suffixLength", suffixLength);
		try {
			String code = jdbcTemplate.queryForObject(sql.toString(), String.class);
			Long number = 0L;
			if (!StringUtils.isEmpty(code)) {
				number = Long.valueOf(code.substring(code.length() - suffixLength, code.length()));
			}
			Long max = number + 1;
			result.put("max", max);
		} catch (Exception e) {
			result.put("max", 1L);
		}
		return result;
	}

	/**
	 * 补齐不足长度
	 *
	 * @param length 长度
	 * @param number 数字
	 * @return
	 */
	private String lpad(int length, Long number) {
		String f = "%0" + length + "d";
		return String.format(f, number);
	}

	@Override
	public void printData(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

}
