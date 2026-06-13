package com.seagox.lowcode.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.LeaveRequest;
import com.seagox.lowcode.mapper.LeaveRequestMapper;
import com.seagox.lowcode.mapper.MessageMapper;
import com.seagox.lowcode.mapper.SeaInstanceMapper;
import com.seagox.lowcode.service.IFlowService;
import com.seagox.lowcode.service.ILeaveRequestService;

@Service
public class FlowService implements IFlowService {

	@Autowired
	private SeaInstanceMapper seaInstanceMapper;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private LeaveRequestMapper leaveRequestMapper;
	
	@Autowired
	private ILeaveRequestService leaveRequestService;
	
	@Autowired
	private MessageMapper messageMapper;

	/**
	 * 待办事项
	 */
	@Override
	public ResultData queryTodoItem(Integer pageNo, Integer pageSize, Long companyId, String userId, Long formId,
			String name, String statusStr, String businessTypeStr) {
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>> list = seaInstanceMapper.queryTodoItem(companyId, userId, formId, name, statusStr,
				businessTypeStr);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		return ResultData.success(pageInfo);
	}

	/**
	 * 已办事项
	 */
	@Override
	public ResultData queryDoneItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name,
			String startTime, String endTime, String todoPerson, String businessTypeStr) {
		PageHelper.startPage(pageNo, pageSize);
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>> list = seaInstanceMapper.queryDoneItem(companyId, String.valueOf(userId), name,
				startTime, endTime, todoPerson, businessTypeStr);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		return ResultData.success(pageInfo);
	}

	/**
	 * 抄送事项
	 */
	@Override
	public ResultData queryCopyItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name, String businessTypeStr) {
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>> list = seaInstanceMapper.queryCopyItem(companyId, String.valueOf(userId), name, businessTypeStr);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		return ResultData.success(pageInfo);
	}

	/**
	 * 我发起的
	 */
	@Override
	public ResultData querySelfItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name, String businessTypeStr) {
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>> list = seaInstanceMapper.querySelfItem(companyId, userId, name, businessTypeStr);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		return ResultData.success(pageInfo);
	}

	/**
	 * 待发事项
	 */
	@Override
	public ResultData queryReadyItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name, String businessTypeStr) {
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>> list = seaInstanceMapper.queryReadyItem(companyId, String.valueOf(userId), name, businessTypeStr);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		return ResultData.success(pageInfo);
	}

	@Override
	public ResultData queryProcessInfo(String businessType, String businessKey) {
		return ResultData.success(seaInstanceMapper.queryProcessInfo(businessType, businessKey));
	}

	@Override
	public ResultData batchSubmit(String batchData) {
		JSONObject result = new JSONObject();
		int successNum = 0;
		int errorNum = 0;
		List<String> failList = new ArrayList<>();
		JSONArray batchArray = JSONArray.parseArray(batchData);
		for (int i = 0; i < batchArray.size(); i++) {
			JSONObject item = batchArray.getJSONObject(i);
			String businessType = item.getString("businessType");
			String businessKey = item.getString("businessKey");
			try {
				if (!LeaveRequestService.BUSINESS_TYPE.equals(businessType)) {
					throw new IllegalArgumentException("暂不支持该类别提交");
				}
				ResultData submitResult = leaveRequestService.submit(Long.valueOf(businessKey));
				if (submitResult.getCode() != ResultCode.SUCCESS.getCode()) {
					throw new IllegalArgumentException(submitResult.getMessage());
				}
				if (item.getLong("id") != null) {
					messageMapper.deleteById(item.getLong("id"));
				}
				successNum ++;
			} catch (Exception e) {
				errorNum ++;
				failList.add(e.getMessage());
			}
		}
		result.put("successNum", successNum);
		result.put("errorNum", errorNum);
		result.put("failList", failList);
		return ResultData.success(result);
	}

	@Override
	public ResultData batchApprove(Long companyId, Long userId, Boolean approved, String comment, String rejectNode, String batchData) {
		JSONObject result = new JSONObject();
		int successNum = 0;
		int errorNum = 0;
		List<String> failList = new ArrayList<>();
		JSONArray batchArray = JSONArray.parseArray(batchData);
		for (int i = 0; i < batchArray.size(); i++) {
			JSONObject item = batchArray.getJSONObject(i);
			String businessType = item.getString("businessType");
			String businessKey = item.getString("businessKey");
			Map<String, Object> variables = new HashMap<>();
			variables.put("companyId", companyId);
			variables.put("creator", userId);
			variables.put("userId", userId);
			variables.put("businessType", businessType);
			variables.put("businessKey", businessKey);
			variables.put("approved", approved);
			variables.put("comment", comment);
			variables.put("rejectNode", rejectNode);
			try {
				int processStatus = runtimeService.completeTask(variables);
				syncBusinessStatus(businessType, businessKey, approved, processStatus);
				successNum ++;
			} catch (Exception e) {
				errorNum ++;
				failList.add(e.getMessage());
			}
		}
		result.put("successNum", successNum);
		result.put("errorNum", errorNum);
		result.put("failList", failList);
		return ResultData.success(result);
	}

	private void syncBusinessStatus(String businessType, String businessKey, Boolean approved, int processStatus) {
		if (LeaveRequestService.BUSINESS_TYPE.equals(businessType)) {
			LeaveRequest leaveRequest = new LeaveRequest();
			leaveRequest.setId(Long.valueOf(businessKey));
			if (processStatus == 1) {
				leaveRequest.setStatus(LeaveRequestService.STATUS_APPROVED);
			} else if (!approved && processStatus == 3) {
				leaveRequest.setStatus(LeaveRequestService.STATUS_REJECTED);
			} else {
				return;
			}
			leaveRequestMapper.updateById(leaveRequest);
		}
	}
	
}
