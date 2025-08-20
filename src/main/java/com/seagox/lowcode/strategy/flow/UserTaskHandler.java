package com.seagox.lowcode.strategy.flow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.entity.SeaNode;
import com.seagox.lowcode.mapper.SeaNodeMapper;
import com.seagox.lowcode.service.impl.FlowUtils;
import com.seagox.lowcode.service.impl.NodeUser;
import com.seagox.lowcode.service.impl.RuntimeService;

/**
 * 用户任务节点
 */
@Service
public class UserTaskHandler implements FlowHandler {
	
	@Autowired
    private SeaNodeMapper seaNodeMapper;
	
	@Autowired
	private FlowUtils flowUtils;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private RuntimeService runtimeService;

	@Override
	public void afterPropertiesSet() throws Exception {
		FlowHandlerFactory.register("userTask", this);
	}

	@Override
	public JSONObject execute(Long processInstanceId, String curNodeKey, String path, String precede, JSONObject nodeObj, JSONObject edgesObj, Map<String, Object> variables) {
		JSONObject result = new JSONObject();
		JSONObject curNode = nodeObj.getJSONObject(curNodeKey);
		SeaNode seaNode = new SeaNode();
		seaNode.setDefId(processInstanceId);
		seaNode.setMark(curNode.getString("id"));
		seaNode.setLabel(curNode.getString("label"));
		seaNode.setType(3);
		seaNode.setStartTime(new Date());
		seaNode.setPath(path + "_" + curNode.getString("id"));
		seaNode.setPrecede(precede);
		JSONArray assignee = curNode.getJSONArray("value");
		List<NodeUser> nodeUserList = flowUtils.getNodeApprover(assignee);
		if(nodeUserList.size() == 0) {
			seaNode.setStatus(3);
			seaNodeMapper.insert(seaNode);
			JSONArray target = edgesObj.getJSONArray(curNodeKey);
			// 多条路径
			if(target.size() > 1) {
				if(StringUtils.isEmpty(precede)) {
					precede = curNode.getString("id");
				} else {
					precede = precede + "," + curNode.getString("id");
				}
			}
			for(int i=0;i<target.size();i++) {
				result = runtimeService.execute(processInstanceId, target.getJSONObject(i).getString("target"), seaNode.getPath(), precede, nodeObj, edgesObj, variables);
			}
			curNode.put("status", 1);
		} else {
			seaNode.setStatus(0);
			seaNodeMapper.insert(seaNode);
			saveBatchNodeDetail(seaNode.getId(), nodeUserList);
			curNode.put("status", 0);
		}
		return result;
	}

	public void saveBatchNodeDetail(Long nodeId, List<NodeUser> nodeUserList) {
		List<Object[]> batchArgs = new ArrayList<>();
		for (NodeUser nodeUser : nodeUserList) {
			batchArgs.add(new Object[] { nodeUser.getCompanyId(), nodeId, nodeUser.getName(), nodeUser.getUserId(), 0,
					new Date() });
		}
		String sql = "insert into sea_node_detail(company_id, node_id, name, assignee, status, start_time) values(?, ?, ?, ?, ?, ?)";
		jdbcTemplate.batchUpdate(sql, batchArgs);
	}
	
}
