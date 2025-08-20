package com.seagox.lowcode.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.mapper.AccountMapper;
import com.seagox.lowcode.mapper.UserRoleMapper;

@Component
public class FlowUtils {
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired
    private AccountMapper accountMapper;
    
	/**
     * 获取节点审批人
     *
     * @param nextNodeInfo         节点信息
     */
    public List<NodeUser> getNodeApprover(JSONArray assigneeArray) {
    	List<NodeUser> assigneeSet = new ArrayList<>();
    	if(assigneeArray != null) {
    		for (int i = 0; i < assigneeArray.size(); i++) {
                JSONObject assigneeObject = assigneeArray.getJSONObject(i);
                String type = assigneeObject.getString("type");
                String value = assigneeObject.getString("value");
                if(type.equals("company")) {
                	List<Map<String, Object>> userList = accountMapper.queryByCompanyId(Long.valueOf(value));
                	for (Map<String, Object> user : userList) {
                		NodeUser nodeUser = new NodeUser();
                		nodeUser.setCompanyId(Long.valueOf(user.get("companyId").toString()));
                		nodeUser.setUserId(user.get("id").toString());
                		nodeUser.setName(user.get("name").toString());
                		assigneeSet.add(nodeUser);
                	}
                } else if (type.equals("department")) {
                	List<Map<String, Object>> userList = accountMapper.queryByDeptId(assigneeObject.getLong("value"));
                	for (Map<String, Object> user : userList) {
                		NodeUser nodeUser = new NodeUser();
                		nodeUser.setCompanyId(Long.valueOf(user.get("companyId").toString()));
                		nodeUser.setUserId(user.get("id").toString());
                		nodeUser.setName(user.get("name").toString());
                		assigneeSet.add(nodeUser);
                	}
                } else if (type.equals("user")) {
                	NodeUser nodeUser = new NodeUser();
                	nodeUser.setCompanyId(assigneeObject.getLong("companyId"));
                	nodeUser.setUserId(value);
                	nodeUser.setName(assigneeObject.getString("name"));
            		assigneeSet.add(nodeUser);
                } else if (type.equals("role")) {
                	List<Map<String, Object>> userList = userRoleMapper.queryByRoleId(assigneeObject.getLong("value"));
                	for (Map<String, Object> user : userList) {
                		NodeUser nodeUser = new NodeUser();
                		nodeUser.setCompanyId(Long.valueOf(user.get("companyId").toString()));
                		nodeUser.setUserId(user.get("id").toString());
                		nodeUser.setName(user.get("name").toString());
                		assigneeSet.add(nodeUser);
                	}
                }
            }
    	}
        return assigneeSet;
    }
    
	public static void checkStartArgument(Map<String, Object> variables) {
		if (Objects.isNull(variables.get("companyId"))) {
            throw new IllegalArgumentException("companyId 参数不能为 null");
        }
		if (Objects.isNull(variables.get("creator"))) {
            throw new IllegalArgumentException("creator 参数不能为 null");
        }
		if (Objects.isNull(variables.get("title"))) {
            throw new IllegalArgumentException("title 参数不能为 null");
        }
		if (Objects.isNull(variables.get("businessType"))) {
            throw new IllegalArgumentException("businessType 参数不能为 null");
        }
		if (Objects.isNull(variables.get("businessKey"))) {
            throw new IllegalArgumentException("businessKey 参数不能为 null");
        }
		if (Objects.isNull(variables.get("resources"))) {
            throw new IllegalArgumentException("resources 参数不能为 null");
        }
	}
	
	public static void checkCompleteArgument(Map<String, Object> variables) {
		if (Objects.isNull(variables.get("companyId"))) {
            throw new IllegalArgumentException("companyId 参数不能为 null");
        }
		if (Objects.isNull(variables.get("creator"))) {
            throw new IllegalArgumentException("creator 参数不能为 null");
        }
		if (Objects.isNull(variables.get("businessType"))) {
            throw new IllegalArgumentException("businessType 参数不能为 null");
        }
		if (Objects.isNull(variables.get("businessKey"))) {
            throw new IllegalArgumentException("businessKey 参数不能为 null");
        }
		if (Objects.isNull(variables.get("approved"))) {
            throw new IllegalArgumentException("approved 参数不能为 null");
        }
		if (Objects.isNull(variables.get("rejectNode"))) {
            throw new IllegalArgumentException("rejectNode 参数不能为 null");
        }
	}
}
