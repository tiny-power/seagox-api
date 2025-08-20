package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;

import java.util.Map;

public interface IRuntimeService {

    /**
     * @param variables 参数
     * @param resources 流程数据
     * @param tenantId 租户
     * @param creator 发起者
     * @param businessType 业务类型
     * @param businessKey 业务key
     * @param title 标题
     * 
     * 启动流程
     */
    public int startProcessInstance(Map<String, Object> variables);

    /**
     * 流程终止
     */
    public ResultData deleteProcessInstance(Long processInstanceId, String reason);

    /**
     * 暂停
     */
    public ResultData suspendProcessInstanceById(Long processInstanceId);

    /**
     * 恢复
     */
    public ResultData resumeProcessInstanceById(Long processInstanceId);

    /**
     * 完成　
     */
    public int completeTask(Map<String, Object> variables);
    
    /**
     * 撤回
     */
    public ResultData revokeTask(Long processInstanceId);
    
}
