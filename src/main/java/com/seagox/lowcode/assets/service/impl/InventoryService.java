package com.seagox.lowcode.assets.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.assets.entity.InventoryPlanDetail;
import com.seagox.lowcode.assets.mapper.AssetsMapper;
import com.seagox.lowcode.assets.mapper.InventoryPlanDetailMapper;
import com.seagox.lowcode.assets.service.IInventoryService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class InventoryService implements IInventoryService {

	@Autowired
	private InventoryPlanDetailMapper inventoryPlanDetailMapper;

	@Autowired
	private AssetsMapper commonMapper;

	@Override
	public ResultData updateInventoryPlanDetail(InventoryPlanDetail inventoryPlanDetail) {
		inventoryPlanDetailMapper.updateById(inventoryPlanDetail);
		return ResultData.success(null);
	}

	@Override
	public ResultData updateInventoryPlan(Long id, Integer status, String checkStr) {
		if (status.equals(2)) {
			LambdaQueryWrapper<InventoryPlanDetail> qw = new LambdaQueryWrapper<>();
			qw.eq(InventoryPlanDetail::getPlanId, id).eq(InventoryPlanDetail::getStatus, 1);
			Long count = inventoryPlanDetailMapper.selectCount(qw);
			if (count > 0) {
				return ResultData.warn(ResultCode.OTHER_ERROR, "还有" + count + "条资产未盘点");
			} else {
				commonMapper.updateInventoryPlan(id, status);
			}
		} else if (status.equals(3)) {
			if (!StringUtils.isEmpty(checkStr)) {
				List<String> checkList = Arrays.asList(checkStr.split(","));
				for (int i = 0; i < checkList.size(); i++) {
					String item = checkList.get(i);
					if(item.equals("1")) {
						// 无盈亏
					} else if(item.equals("2")) {
						// 盘亏
					} else if(item.equals("3")) {
						// 盘盈
					}
				}
			}
			commonMapper.updateInventoryPlan(id, status);
		}
		return ResultData.success(null);
	}

}
