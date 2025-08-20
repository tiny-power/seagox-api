package com.seagox.lowcode.assets.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seagox.lowcode.assets.service.IProvisionDepreciationService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.assets.entity.AssetsDepreciation;
import com.seagox.lowcode.assets.entity.ProvisionDepreciation;
import com.seagox.lowcode.assets.mapper.AssetsDepreciationMapper;
import com.seagox.lowcode.assets.mapper.ProvisionDepreciationMapper;

/**
 * 计提折旧
 */
@Service
public class ProvisionDepreciationService implements IProvisionDepreciationService {

	@Autowired
	private ProvisionDepreciationMapper provisionDepreciationMapper;

	@Autowired
	private AssetsDepreciationMapper assetsDepreciationMapper;

	@Override
	public ResultData accountInfo(Long companyId) {
		LambdaQueryWrapper<ProvisionDepreciation> qw = new LambdaQueryWrapper<>();
		qw.eq(ProvisionDepreciation::getCompanyId, companyId).orderByAsc(ProvisionDepreciation::getPeriod);
		List<ProvisionDepreciation> list = provisionDepreciationMapper.selectList(qw);
		if (list.size() > 0) {
			return ResultData.success(list.get(0));
		} else {
			return ResultData.success(null);
		}
	}
	
	@Override
	public ResultData queryById(Long id) {
		ProvisionDepreciation provisionDepreciation = provisionDepreciationMapper.selectById(id);
		provisionDepreciation.setAssetsStr(null);
		return ResultData.success(provisionDepreciation);
	}

	@Transactional
	@Override
	public ResultData insert(ProvisionDepreciation provisionDepreciation) {
		provisionDepreciationMapper.insert(provisionDepreciation);
		JSONArray assetsDepreciationList = JSONArray.parseArray(provisionDepreciation.getAssetsStr());
		for (int i = 0; i < assetsDepreciationList.size(); i++) {
			JSONObject assetsDepreciationObject = assetsDepreciationList.getJSONObject(i);
			AssetsDepreciation assetsDepreciation = new AssetsDepreciation();
			assetsDepreciation.setCompanyId(provisionDepreciation.getCompanyId());
			assetsDepreciation.setPeriod(provisionDepreciation.getPeriod());
			assetsDepreciation.setAssetsId(assetsDepreciationObject.getLong("assetsId"));
			assetsDepreciation.setAmount(assetsDepreciationObject.getDoubleValue("amount"));
			assetsDepreciationMapper.insert(assetsDepreciation);
		}
		return ResultData.success(null);
	}
	
	@Override
	public ResultData updateStatus(Long companyId, Long id, int status) {
		ProvisionDepreciation provisionDepreciation = provisionDepreciationMapper.selectById(id);
		if(status == 0) {
			LambdaQueryWrapper<ProvisionDepreciation> qw = new LambdaQueryWrapper<>();
			qw.eq(ProvisionDepreciation::getCompanyId, companyId).orderByDesc(ProvisionDepreciation::getPeriod);
			List<ProvisionDepreciation> list = provisionDepreciationMapper.selectList(qw);
			if (!list.get(0).getId().equals(id)) {
				if(list.size() == 1) {
					return ResultData.warn(ResultCode.OTHER_ERROR, "折旧最后一个月不可取消，可以反建账");
				} else {
					return ResultData.warn(ResultCode.OTHER_ERROR, "不可跨月取消确认，需从最新计提折旧月份开始");
				}
			}
		}
		provisionDepreciation.setStatus(status);
		provisionDepreciationMapper.updateById(provisionDepreciation);
		return ResultData.success(null);
	}

	@Transactional
	@Override
	public ResultData delete(Long companyId, Long id) {
		LambdaQueryWrapper<ProvisionDepreciation> qw = new LambdaQueryWrapper<>();
		qw.eq(ProvisionDepreciation::getCompanyId, companyId).orderByDesc(ProvisionDepreciation::getPeriod);
		List<ProvisionDepreciation> list = provisionDepreciationMapper.selectList(qw);
		if (list.get(0).getId().equals(id)) {
			provisionDepreciationMapper.deleteById(id);
			LambdaQueryWrapper<AssetsDepreciation> qwAssetsDepreciation = new LambdaQueryWrapper<>();
			qwAssetsDepreciation.eq(AssetsDepreciation::getCompanyId, companyId).eq(AssetsDepreciation::getPeriod,
					list.get(0).getPeriod());
			assetsDepreciationMapper.delete(qwAssetsDepreciation);
			return ResultData.success(null);
		} else {
			return ResultData.warn(ResultCode.OTHER_ERROR, "不可跨月取消确认，需从最新计提折旧月份开始");
		}
	}

}