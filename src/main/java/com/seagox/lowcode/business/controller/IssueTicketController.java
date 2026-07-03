package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.IssueTicket;
import com.seagox.lowcode.business.service.IIssueTicketService;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 问题单
 */
@RestController
@RequestMapping("/issueTicket")
public class IssueTicketController {

    /**
     * 问题单服务
     */
    @Autowired
    private IIssueTicketService issueTicketService;

    /**
     * 分页查询问题单
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam Map<String, Object> params) {
        return issueTicketService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 查询问题单详情
     *
     * @param id 问题单ID
     * @return 查询结果
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return issueTicketService.queryById(id);
    }

    /**
     * 新增问题单
     *
     * @param issueTicket 问题单
     * @param userId 当前用户ID
     * @return 操作结果
     */
    @PostMapping("/insert")
    @LogPoint("新增问题单")
    public ResultData insert(IssueTicket issueTicket, Long userId) {
        return issueTicketService.insert(issueTicket, userId);
    }

    /**
     * 修改问题单
     *
     * @param issueTicket 问题单
     * @param userId 当前用户ID
     * @return 操作结果
     */
    @PostMapping("/update")
    @LogPoint("修改问题单")
    public ResultData update(IssueTicket issueTicket, Long userId) {
        return issueTicketService.update(issueTicket, userId);
    }

    /**
     * 删除问题单
     *
     * @param id 问题单ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除问题单")
    public ResultData delete(@PathVariable Long id) {
        return issueTicketService.delete(id);
    }
}
