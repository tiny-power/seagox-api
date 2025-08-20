package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.TableColumnConfig;
import com.seagox.lowcode.mapper.TableColumnConfigMapper;
import com.seagox.lowcode.service.ITableColumnConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableColumnConfigService implements ITableColumnConfigService {

    @Autowired
    private TableColumnConfigMapper tableColumnConfigMapper;

	@Override
	public ResultData insertOrUpdate(TableColumnConfig tableColumnConfig) {
		LambdaQueryWrapper<TableColumnConfig> qw = new LambdaQueryWrapper<>();
		qw.eq(TableColumnConfig::getUserId, tableColumnConfig.getUserId())
		.eq(TableColumnConfig::getFormId, tableColumnConfig.getFormId());
		TableColumnConfig selectTableColumnConfig = tableColumnConfigMapper.selectOne(qw);
		if(selectTableColumnConfig != null) {
			tableColumnConfig.setId(selectTableColumnConfig.getId());
			tableColumnConfigMapper.updateById(tableColumnConfig);
		} else {
			tableColumnConfigMapper.insert(tableColumnConfig);
		}
		return ResultData.success(null);
	}
}
