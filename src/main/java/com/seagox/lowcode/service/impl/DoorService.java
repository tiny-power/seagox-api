package com.seagox.lowcode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Company;
import com.seagox.lowcode.entity.Door;
import com.seagox.lowcode.entity.Gauge;
import com.seagox.lowcode.mapper.CompanyMapper;
import com.seagox.lowcode.mapper.DoorMapper;
import com.seagox.lowcode.mapper.GaugeMapper;
import com.seagox.lowcode.service.IDoorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoorService implements IDoorService {

	@Autowired
	private DoorMapper doorMapper;

	@Value(value = "${spring.datasource.url}")
	private String datasourceUrl;

	@Autowired
	private GaugeMapper gaugeMapper;

	@Autowired
	private CompanyMapper companyMapper;

	@Override
	public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId) {
		PageHelper.startPage(pageNo, pageSize);
		LambdaQueryWrapper<Door> qw = new LambdaQueryWrapper<>();
		qw.eq(Door::getCompanyId, companyId).orderByDesc(Door::getCreateTime);
		List<Door> list = doorMapper.selectList(qw);
		PageInfo<Door> pageInfo = new PageInfo<>(list);
		return ResultData.success(pageInfo);
	}

	@Override
	public ResultData insert(Door door) {
		doorMapper.insert(door);
		return ResultData.success(null);
	}

	@Override
	public ResultData update(Door door) {
		doorMapper.updateById(door);
		return ResultData.success(null);
	}

	@Override
	public ResultData delete(Long id) {
		doorMapper.deleteById(id);
		return ResultData.success(null);
	}

	@Override
	public ResultData queryById(Long id, Long userId) {
		Door door = doorMapper.selectById(id);
		return ResultData.success(door);
	}

	@Override
	public ResultData queryAnalysis(Long companyId, Long userId) {
		Long parentCompanyId = companyId;
		Company company = companyMapper.selectById(companyId);
		if (company.getParentId() != null) {
			LambdaQueryWrapper<Company> qw = new LambdaQueryWrapper<>();
			qw.eq(Company::getCode, company.getCode().substring(0, 4));
			parentCompanyId = companyMapper.selectOne(qw).getId();
		}
		JSONObject result = new JSONObject();
		Door door = doorMapper.queryUserData(parentCompanyId, companyId, userId);
		if (door != null) {
			result.put("id", door.getId());
			Gauge gauge = gaugeMapper.selectById(door.getPath());
			result.put("data", gauge);
		}
		return ResultData.success(result);
	}

}
