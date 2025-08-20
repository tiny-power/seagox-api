package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;

public interface IFlowService {

	/**
	 * 待办事项
	 */
	public ResultData queryTodoItem(Integer pageNo, Integer pageSize, Long companyId, String userId, Long formId,
			String name, String statusStr, String businessTypeStr);

	/**
	 * 已办事项
	 */
	public ResultData queryDoneItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name,
			String startTime, String endTime, String todoPerson, String businessTypeStr);

	/**
	 * 抄送事项
	 */
	public ResultData queryCopyItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name,
			String businessTypeStr);

	/**
	 * 我发起的
	 */
	public ResultData querySelfItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name,
			String businessTypeStr);

	/**
	 * 待发事项
	 */
	public ResultData queryReadyItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name,
			String businessTypeStr);

	/**
	 * 流程记录
	 * 
	 * @param businessType 业务类型
	 * @param businessKey  业务主键
	 * @return
	 */
	public ResultData queryProcessInfo(String businessType, String businessKey);

	/**
	 * 批量审批
	 */
	public ResultData batchApprove(Long companyId, Long userId, Boolean approved, String comment, String rejectNode,
			String batchData);
	
	/**
	 * 批量提交
	 */
	public ResultData batchSubmit(Long companyId, Long userId, String batchData);

}
