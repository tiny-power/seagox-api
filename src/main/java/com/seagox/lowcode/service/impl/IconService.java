package com.seagox.lowcode.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SysIcon;
import com.seagox.lowcode.mapper.IconMapper;
import com.seagox.lowcode.service.IIconService;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class IconService extends ServiceImpl<IconMapper, SysIcon> implements IIconService {

    @Autowired
    private IconMapper iconMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, String name) {
        PageHelper.startPage(pageNo, pageSize);
        LambdaQueryWrapper<SysIcon> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotBlank(name), SysIcon::getName, name);
        List<SysIcon> list = iconMapper.selectList(qw);
        PageInfo<SysIcon> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }
    
    @Transactional
	@Override
	public ResultData generate() {
    	iconMapper.delete(null);
		InputStream inputStream = null;
		try {
			Resource resource = new ClassPathResource("\\template\\iconfont.json");
			inputStream = resource.getInputStream();
			JSONObject result = JSONObject.parseObject(IOUtils.toString(inputStream, "utf-8"));
			String fontFamily = result.getString("font_family");
			String cssPrefixText = result.getString("css_prefix_text");
			JSONArray glyphs = result.getJSONArray("glyphs");
			List<SysIcon> list = new ArrayList<SysIcon>();
			for(int i=0;i<glyphs.size();i++) {
				JSONObject glyph = glyphs.getJSONObject(i);
				SysIcon icon = new SysIcon();
				icon.setName(glyph.getString("name"));
				icon.setFont(fontFamily + " " + cssPrefixText + glyph.getString("font_class"));
				list.add(icon);
			}
			this.saveBatch(list);
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

}
