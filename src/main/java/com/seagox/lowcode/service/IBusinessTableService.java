package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.BusinessTable;

public interface IBusinessTableService {

	/**
	 * 查询全部
	 */
	public ResultData queryAll(Long companyId, String name, String remark);

	/**
	 * 添加
	 */
	public ResultData insert(BusinessTable businessTable);

	/**
	 * 修改
	 */
	public ResultData update(BusinessTable businessTable);

	/**
	 * 删除
	 */
	public ResultData delete(Long id);

	/**
	 * 查询通过id
	 */
	public ResultData queryById(Long id);

	/**
	 * 根据级联数据
	 */
	public ResultData queryCascader(Long id, String rule);

}
