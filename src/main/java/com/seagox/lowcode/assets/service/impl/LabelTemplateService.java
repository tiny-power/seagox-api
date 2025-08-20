package com.seagox.lowcode.assets.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.assets.entity.LabelTemplate;
import com.seagox.lowcode.assets.mapper.LabelTemplateMapper;
import com.seagox.lowcode.assets.service.ILabelTemplateService;
import com.seagox.lowcode.assets.util.ExcelPrint;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;

import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;;

@Service
public class LabelTemplateService implements ILabelTemplateService {

    @Autowired
    private LabelTemplateMapper labelTemplateMapper;

	@Override
	public ResultData queryForList(Long companyId) {
		LambdaQueryWrapper<LabelTemplate> qw = new LambdaQueryWrapper<>();
		qw.eq(LabelTemplate::getCompanyId, companyId);
		return ResultData.success(labelTemplateMapper.selectList(qw));
	}

	@Override
	public ResultData queryById(Long id) {
		InputStream inputStream = null;
		LabelTemplate labelTemplate = labelTemplateMapper.selectById(id);
		String url = JSONArray.parseArray(labelTemplate.getAttachment()).getJSONObject(0).getString("url");
		try {
			inputStream = new URL(url).openStream();
			JSONObject result = new JSONObject();
			result.put("width", labelTemplate.getWidth());
			result.put("height", labelTemplate.getHeight());
			result.put("margin", labelTemplate.getMargin());
			JSONObject excelJson = ExcelPrint.readSheet(inputStream);
			result.put("excelJson", excelJson);
			return ResultData.success(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultData.warn(ResultCode.OTHER_ERROR, "获取标签模版失败");
		}
	}
	
}
