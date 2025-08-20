package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.TableColumnConfig;

public interface ITableColumnConfigService {

    /**
     * 新增或者更新
     */
	public ResultData insertOrUpdate(TableColumnConfig tableColumnConfig);

}
