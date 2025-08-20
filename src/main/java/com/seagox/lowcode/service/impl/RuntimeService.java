package com.seagox.lowcode.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SeaInstance;
import com.seagox.lowcode.entity.SeaNode;
import com.seagox.lowcode.entity.SeaNodeDetail;
import com.seagox.lowcode.entity.SysMessage;
import com.seagox.lowcode.mapper.MessageMapper;
import com.seagox.lowcode.mapper.SeaInstanceMapper;
import com.seagox.lowcode.mapper.SeaNodeDetailMapper;
import com.seagox.lowcode.mapper.SeaNodeMapper;
import com.seagox.lowcode.service.IRuntimeService;
import com.seagox.lowcode.strategy.flow.FlowHandler;
import com.seagox.lowcode.strategy.flow.FlowHandlerFactory;

@Service
public class RuntimeService extends ServiceImpl<SeaNodeDetailMapper, SeaNodeDetail> implements IRuntimeService {
	
    @Autowired
    private SeaInstanceMapper seaInstanceMapper;

    @Autowired
    private SeaNodeMapper seaNodeMapper;
    
    @Autowired
    private SeaNodeDetailMapper seaNodeDetailMapper;
    
    @Autowired
    private MessageMapper  messageMapper;
    
    @Transactional
	@Override
	public int startProcessInstance(Map<String, Object> variables) {
		FlowUtils.checkStartArgument(variables);
		JSONObject resources = JSONObject.parseObject(variables.get("resources").toString());
		JSONArray nodes = resources.getJSONArray("nodes");
		JSONArray edges = resources.getJSONArray("edges");
		JSONObject edgesObj = new JSONObject();
		JSONObject nodeObj = new JSONObject();
		for(int i=0;i<edges.size();i++) {
			JSONObject edge = edges.getJSONObject(i);
			if(edgesObj.containsKey(edge.getString("source"))) {
				JSONArray target = edgesObj.getJSONArray(edge.getString("source"));
				target.add(edge);
				edgesObj.put(edge.getString("source"), target);
			} else {
				JSONArray target = new JSONArray();
				target.add(edge);
				edgesObj.put(edge.getString("source"), target);
			}
		}
		for(int i=0;i<nodes.size();i++) {
			JSONObject node = nodes.getJSONObject(i);
			nodeObj.put(node.getString("id"), node);
		}
		LambdaQueryWrapper<SeaInstance> qw = new LambdaQueryWrapper<>();
		qw.eq(SeaInstance::getBusinessKey, variables.get("businessKey").toString())
		.eq(SeaInstance::getBusinessType, variables.get("businessType").toString());
		SeaInstance seaInstance = seaInstanceMapper.selectOne(qw);
		if(seaInstance == null) {
			seaInstance = new SeaInstance();
			seaInstance.setCompanyId(Long.valueOf(variables.get("companyId").toString()));
			seaInstance.setUserId(Long.valueOf(variables.get("creator").toString()));
			seaInstance.setName(variables.get("title").toString());
			seaInstance.setResources(resources.toJSONString());
			seaInstance.setBusinessType(variables.get("businessType").toString());
			seaInstance.setBusinessKey(variables.get("businessKey").toString());
			seaInstance.setStatus(0);
			seaInstance.setStartTime(new Date());
			seaInstanceMapper.insert(seaInstance);
		} else {
			seaInstance.setStatus(0);
			seaInstance.setName(variables.get("title").toString());
		}
		String curNodeKey = "start";
		String path = "";
		String precede = "";
		JSONObject result = execute(seaInstance.getId(), curNodeKey, path, precede, nodeObj, edgesObj, variables);
		resources.put("nodes", result.getJSONArray("nodes"));
		seaInstance.setResources(resources.toJSONString());
		if(result.containsKey("finish")) {
			seaInstance.setStatus(1);
			seaInstance.setEndTime(new Date());
		}
		seaInstanceMapper.updateById(seaInstance);
		return seaInstance.getStatus();
	}
	
    /**
     * @param processInstanceId 流程实例id
     * @param curNodeKey 节点key
     * @param path 节点路径
     * @param nodeObj 节点对象
     * @param edgesObj 连线对象
     * @param variables 参数
     * 
     * 流程执行
     */
	public JSONObject execute(Long processInstanceId, String curNodeKey, String path, String precede, JSONObject nodeObj, JSONObject edgesObj, Map<String, Object> variables) {
		JSONObject result = new JSONObject();
		JSONObject curNode = nodeObj.getJSONObject(curNodeKey);
		String type = curNode.getString("type");
		FlowHandler eventService = FlowHandlerFactory.getHandler(type);
		if(eventService != null) {
			result = eventService.execute(processInstanceId, curNodeKey, path, precede, nodeObj, edgesObj, variables);
			JSONArray nodes = new JSONArray();
			for(String key : nodeObj.keySet()) {
				nodes.add(nodeObj.getJSONObject(key));
			}
			result.put("nodes", nodes);
			return result;
		} else {
			throw new IllegalArgumentException("节点类型:" + type + "不存在");
		}
	}
	
	@Transactional
	@Override
	public int completeTask(Map<String, Object> variables) {
		FlowUtils.checkCompleteArgument(variables);
		String businessType = variables.get("businessType").toString();
		String businessKey = variables.get("businessKey").toString();
		String userId = variables.get("userId").toString();
		LambdaQueryWrapper<SeaInstance> qw = new LambdaQueryWrapper<>();
	    	qw.eq(SeaInstance::getBusinessKey, businessKey)
	        .eq(SeaInstance::getBusinessType, businessType);
	    SeaInstance seaInstance = seaInstanceMapper.selectOne(qw);
		List<Map<String ,Object>> currentNodeList = seaNodeMapper.queryCurrentNodeDetail(seaInstance.getId(), userId);
		if(currentNodeList.size() == 0) {
			throw new IllegalArgumentException("当前没有审批节点,请检查数据");
		} else {
			SeaNodeDetail seaNodeDetail = new SeaNodeDetail();
			seaNodeDetail.setId(Long.valueOf(currentNodeList.get(0).get("nodeDetailId").toString()));
			seaNodeDetail.setEndTime(new Date());
			if(variables.get("comment") != null) {
				seaNodeDetail.setRemark(variables.get("comment").toString());
			}
			boolean approved = Boolean.valueOf(variables.get("approved").toString());
			if(approved) {
				// 同意
				seaNodeDetail.setStatus(1);
			} else {
				// 拒绝
				seaNodeDetail.setStatus(2);
			}
			seaNodeDetailMapper.updateById(seaNodeDetail);
			SeaNode seaNode = new SeaNode();
			seaNode.setId(Long.valueOf(currentNodeList.get(0).get("id").toString()));
			if(approved) {
				// TODO 会签、或签
				// 同意
				LambdaQueryWrapper<SeaNodeDetail> seaNodeDetailQw = new LambdaQueryWrapper<>();
				seaNodeDetailQw.eq(SeaNodeDetail::getNodeId, currentNodeList.get(0).get("id"))
		    	.eq(SeaNodeDetail::getStatus, 0);
				List<SeaNodeDetail> seaNodeDetailList = seaNodeDetailMapper.selectList(seaNodeDetailQw);
				if(seaNodeDetailList.size() == 0) {
					seaNode.setStatus(1);
					seaNode.setEndTime(new Date());
					seaNodeMapper.updateById(seaNode);
					JSONObject resources = JSONObject.parseObject(seaInstance.getResources());
					JSONArray nodes = resources.getJSONArray("nodes");
					JSONArray edges = resources.getJSONArray("edges");
					JSONObject edgesObj = new JSONObject();
					JSONObject nodeObj = new JSONObject();
					for(int i=0;i<edges.size();i++) {
						JSONObject edge = edges.getJSONObject(i);
						if(edgesObj.containsKey(edge.getString("source"))) {
							JSONArray target = edgesObj.getJSONArray(edge.getString("source"));
							target.add(edge);
							edgesObj.put(edge.getString("source"), target);
						} else {
							JSONArray target = new JSONArray();
							target.add(edge);
							edgesObj.put(edge.getString("source"), target);
						}
					}
					for(int i=0;i<nodes.size();i++) {
						JSONObject node = nodes.getJSONObject(i);
						if(node.getString("id").equals(currentNodeList.get(0).get("mark").toString())) {
							node.put("status", 1);
						}
						nodeObj.put(node.getString("id"), node);
					}
					// 获取下个节点
					JSONArray target = edgesObj.getJSONArray(currentNodeList.get(0).get("mark").toString());
					String precede = "";
					if(!StringUtils.isEmpty(currentNodeList.get(0).get("precede"))) {
						precede = currentNodeList.get(0).get("precede").toString();
					}
					for(int i=0;i<target.size();i++) {
						JSONObject result = execute(seaInstance.getId(), target.getJSONObject(i).getString("target"), currentNodeList.get(0).get("path").toString(), precede, nodeObj, edgesObj, variables);
						resources.put("nodes", result.getJSONArray("nodes"));
						seaInstance.setResources(resources.toJSONString());
						if(result.containsKey("finish")) {
							seaInstance.setStatus(1);
							seaInstance.setEndTime(new Date());
						}
						seaInstanceMapper.updateById(seaInstance);
					}
				}
			} else {
				// 拒绝
				seaNode.setStatus(2);
				seaNode.setEndTime(new Date());
				seaNodeMapper.updateById(seaNode);
				JSONObject resources = JSONObject.parseObject(seaInstance.getResources());
				JSONArray nodes = resources.getJSONArray("nodes");
				JSONObject nodeObj = new JSONObject();
				for(int i=0;i<nodes.size();i++) {
					JSONObject node = nodes.getJSONObject(i);
					if(node.getString("id").equals(currentNodeList.get(0).get("mark").toString())) {
						node.put("status", 0);
					}
					nodeObj.put(node.getString("id"), node);
				}
				seaInstance.setResources(resources.toJSONString());
				seaInstanceMapper.updateById(seaInstance);
				// 驳回指定节点
				String rejectNode = variables.get("rejectNode").toString();
				LambdaQueryWrapper<SeaNode> rejectNodeQw = new LambdaQueryWrapper<>();
				rejectNodeQw.eq(SeaNode::getDefId, seaInstance.getId())
				.eq(SeaNode::getMark, rejectNode);
				SeaNode rejectSeaNode = seaNodeMapper.selectList(rejectNodeQw).get(0);
				String rejectPath = rejectSeaNode.getPath();
				// 处理要处理的其他的节点
				LambdaQueryWrapper<SeaNode> otherQw = new LambdaQueryWrapper<>();
				otherQw.eq(SeaNode::getDefId, seaInstance.getId())
		    	.eq(SeaNode::getStatus, 0);
				List<SeaNode> seaNodeList = seaNodeMapper.selectList(otherQw);
				for(int i=0;i<seaNodeList.size();i++) {
					SeaNode handelNode = seaNodeList.get(i);
					if(handelNode.getPath().contains(rejectPath)) {
						handelNode.setStatus(3);
						seaNodeMapper.updateById(handelNode);
					}
				}
				if(rejectNode.equals("start")) {
					for(int i=0;i<nodes.size();i++) {
						JSONObject node = nodes.getJSONObject(i);
						node.remove("status");
					}
					resources.put("nodes", nodes);
					seaInstance.setResources(resources.toJSONString());
					seaInstance.setStatus(3);
					seaInstanceMapper.updateById(seaInstance);
					
					SysMessage message = new SysMessage();
					message.setBusinessKey(Long.valueOf(seaInstance.getBusinessKey()));
					message.setBusinessType(Long.valueOf(seaInstance.getBusinessType()));
					message.setCompanyId(seaInstance.getCompanyId());
					message.setFromUserId(seaInstance.getUserId());
					message.setTitle(seaInstance.getName());
					message.setToUserId(seaInstance.getUserId());
					message.setType(1);
					messageMapper.insert(message);
				} else {
					JSONArray edges = resources.getJSONArray("edges");
					JSONObject edgesObj = new JSONObject();
					for(int i=0;i<edges.size();i++) {
						JSONObject edge = edges.getJSONObject(i);
						if(edgesObj.containsKey(edge.getString("source"))) {
							JSONArray target = edgesObj.getJSONArray(edge.getString("source"));
							target.add(edge);
							edgesObj.put(edge.getString("source"), target);
						} else {
							JSONArray target = new JSONArray();
							target.add(edge);
							edgesObj.put(edge.getString("source"), target);
						}
					}
					execute(seaInstance.getId(), rejectNode, rejectSeaNode.getPath(), rejectSeaNode.getPrecede(), nodeObj, edgesObj, variables);
				}
			}
		}
		return seaInstance.getStatus();
	}
	
	@Transactional
	@Override
	public ResultData deleteProcessInstance(Long processInstanceId, String reason) {
		SeaInstance seaInstance = new SeaInstance();
		seaInstance.setId(processInstanceId);
		seaInstance.setStatus(3);
    	seaInstanceMapper.updateById(null);
		return ResultData.success(null);
	}
	
	@Transactional
	@Override
	public ResultData suspendProcessInstanceById(Long processInstanceId) {
		SeaInstance seaInstance = new SeaInstance();
		seaInstance.setId(processInstanceId);
		seaInstance.setStatus(2);
    	seaInstanceMapper.updateById(null);
		return ResultData.success(null);
	}

	@Transactional
	@Override
	public ResultData resumeProcessInstanceById(Long processInstanceId) {
		SeaInstance seaInstance = new SeaInstance();
		seaInstance.setId(processInstanceId);
		seaInstance.setStatus(0);
    	seaInstanceMapper.updateById(null);
		return ResultData.success(null);
	}
	
	@Transactional
	@Override
	public ResultData revokeTask(Long processInstanceId) {
		SeaInstance seaInstance = seaInstanceMapper.selectById(processInstanceId);
		int status = seaInstance.getStatus();
		if(status == 1) {
			return ResultData.warn(ResultCode.OTHER_ERROR, "当前流程已经结束,不可以撤回");
		} else if (status == 2) {
			return ResultData.warn(ResultCode.OTHER_ERROR, "当前流程已挂起,不可以撤回");
		} else if (status == 3) {
			return ResultData.warn(ResultCode.OTHER_ERROR, "当前流程已终止,不可以撤回");
		} else {
			seaInstance.setStatus(3);
			JSONObject resources = JSONObject.parseObject(seaInstance.getResources());
			JSONArray nodes = resources.getJSONArray("nodes");
			for(int i=0;i<nodes.size();i++) {
				JSONObject node = nodes.getJSONObject(i);
				node.remove("status");
			}
			resources.put("nodes", nodes);
			seaInstance.setResources(resources.toJSONString());
			seaInstanceMapper.updateById(seaInstance);
			SysMessage message = new SysMessage();
			message.setBusinessKey(Long.valueOf(seaInstance.getBusinessKey()));
			message.setBusinessType(Long.valueOf(seaInstance.getBusinessType()));
			message.setCompanyId(seaInstance.getCompanyId());
			message.setFromUserId(seaInstance.getUserId());
			message.setTitle(seaInstance.getName());
			message.setToUserId(seaInstance.getUserId());
			message.setType(1);
			messageMapper.insert(message);
			SeaNode seaNode = new SeaNode();
			seaNode.setStatus(3);
			LambdaQueryWrapper<SeaNode> uw = new LambdaQueryWrapper<>();
			uw.eq(SeaNode::getDefId, seaInstance.getId())
			.eq(SeaNode::getStatus, 0);
			seaNodeMapper.update(seaNode, null);
		}
		return ResultData.success(null);
	}
	
	/**
     * 加签
     */
	public void addSignature() {
		
	}
	
	/**
     * 减签
     */
	public void deleteSignature() {
		
	}
    
}
