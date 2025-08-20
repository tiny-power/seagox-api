package com.seagox.lowcode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SeaInstance;
import com.seagox.lowcode.mapper.SeaInstanceMapper;
import com.seagox.lowcode.service.IFlowService;
import com.seagox.lowcode.service.IRuntimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 工作流
 */
@RestController
@RequestMapping("/flow")
public class FlowController {

    @Autowired
    private IFlowService flowService;
    
    @Autowired
    private SeaInstanceMapper seaInstanceMapper;
    
    @Autowired
    private IRuntimeService runtimeService;

    /**
     * 待办事项
     */
    @GetMapping("/queryTodoItem")
    public ResultData queryTodoItem(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId, String userId, Long formId, String name, String statusStr, String businessTypeStr) {
        return flowService.queryTodoItem(pageNo, pageSize, companyId, userId, formId, name, statusStr, businessTypeStr);
    }

    /**
     * 已办事项
     */
    @GetMapping("/queryDoneItem")
    public ResultData queryDoneItem(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId, Long userId, String name, String startTime, String endTime, String todoPerson, String businessTypeStr) {
        return flowService.queryDoneItem(pageNo, pageSize, companyId, userId, name, startTime, endTime, todoPerson, businessTypeStr);
    }

    /**
     * 抄送事项
     */
    @GetMapping("/queryCopyItem")
    public ResultData queryCopyItem(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId, Long userId, String name, String businessTypeStr) {
        return flowService.queryCopyItem(pageNo, pageSize, companyId, userId, name, businessTypeStr);
    }

    /**
     * 我发起的
     */
    @GetMapping("/querySelfItem")
    public ResultData querySelfItem(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId, Long userId, String name, String businessTypeStr) {
        return flowService.querySelfItem(pageNo, pageSize, companyId, userId, name, businessTypeStr);
    }

    /**
     * 待发事项
     */
    @GetMapping("/queryReadyItem")
    public ResultData queryReadyItem(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId, Long userId, String name, String businessTypeStr) {
        return flowService.queryReadyItem(pageNo, pageSize, companyId, userId, name, businessTypeStr);
    }
    
    /**
     * 流程信息
     */
    @PostMapping("/flowInfo")
    public ResultData flowInfo(String businessType, String businessKey) {
        LambdaQueryWrapper<SeaInstance> qw = new LambdaQueryWrapper<>();
        qw.eq(SeaInstance::getBusinessKey, businessKey)
        .eq(SeaInstance::getBusinessType, businessType);
        SeaInstance seaInstance = seaInstanceMapper.selectOne(qw);
        if(seaInstance != null) {
        	return ResultData.success(seaInstance);
        } else {
        	return ResultData.warn(ResultCode.OTHER_ERROR, "无流程信息");
        }		
    }
    
    /**
     * 流程记录
     */
    @PostMapping("/processInfo")
    public ResultData processInfo(String businessType, String businessKey) {
        return flowService.queryProcessInfo(businessType, businessKey);
    }
    
    /**
     * 撤回
     */
    @GetMapping("/revokeTask/{processInstanceId}")
    public ResultData revokeTask(@PathVariable Long processInstanceId) {
        return runtimeService.revokeTask(processInstanceId);
    }
    
    /**
     * 批量审批
     */
    @PostMapping("/batchApprove")
    public ResultData batchApprove(Long companyId, Long userId, Boolean approved, String comment, String rejectNode, String batchData) {
        return flowService.batchApprove(companyId, userId, approved, comment, rejectNode, batchData);
    }
    
    /**
     * 批量提交
     */
    @PostMapping("/batchSubmit")
    public ResultData batchSubmit(Long companyId, Long userId, String batchData) {
        return flowService.batchSubmit(companyId, userId, batchData);
    }

}
