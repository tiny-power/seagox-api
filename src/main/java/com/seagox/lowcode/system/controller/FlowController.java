package com.seagox.lowcode.system.controller;

import app.tinybrief.weave.api.command.DeployDefinitionCommand;
import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.service.IFlowService;

import javax.validation.Valid;

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
        return flowService.flowInfo(businessType, businessKey);
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
        return flowService.revokeTask(processInstanceId);
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
    public ResultData batchSubmit(String batchData) {
        return flowService.batchSubmit(batchData);
    }

    /**
     * 分页查询
     *
     * @param pageNo 起始页
     * @param pageSize 每页大小
     * @param businessType 业务类型
     * @param name 名称
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                  String businessType, String name) {
        return flowService.queryByPage(pageNo, pageSize, businessType, name);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增流程定义")
    public ResultData insert(@Valid DeployDefinitionCommand command) {
        return flowService.insert(command);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @LogPoint("修改流程定义")
    public ResultData update(@Valid DeployDefinitionCommand command) {
        return flowService.update(command);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除流程定义")
    public ResultData delete(@PathVariable Long id) {
        return flowService.delete(id);
    }

    /**
     * 详情
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id, String dataSource) {
        return flowService.queryById(id, dataSource);
    }

    /**
     * 通过业务类型查询流程
     */
    @GetMapping("/queryByBusinessType/{businessType}")
    public ResultData queryByBusinessType(@PathVariable String businessType) {
        return flowService.queryByBusinessType(businessType);
    }

}
