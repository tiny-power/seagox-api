package com.seagox.lowcode.system.service;

import app.tinybrief.weave.api.command.DeployDefinitionCommand;
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
	 * 流程信息
	 */
	public ResultData flowInfo(String businessType, String businessKey);

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
	public ResultData batchSubmit(String batchData);

	/**
	 * 撤回
	 */
	public ResultData revokeTask(Long processInstanceId);

	/**
	 * 流程定义分页查询
	 */
	public ResultData queryByPage(Integer pageNo, Integer pageSize, String businessType, String name);

	/**
	 * 新增流程定义
	 */
	public ResultData insert(DeployDefinitionCommand command);

	/**
	 * 修改流程定义
	 */
	public ResultData update(DeployDefinitionCommand command);

	/**
	 * 删除流程定义
	 */
	public ResultData delete(Long id);

	/**
	 * 流程定义详情
	 */
	public ResultData queryById(Long id, String dataSource);

	/**
	 * 通过业务类型查询流程定义
	 */
	public ResultData queryByBusinessType(String businessType);

}
