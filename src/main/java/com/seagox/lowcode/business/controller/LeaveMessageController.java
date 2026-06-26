package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.LeaveMessage;
import com.seagox.lowcode.business.service.ILeaveMessageService;
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
 * 留言表
 */
@RestController
@RequestMapping("/leaveMessage")
public class LeaveMessageController {

    @Autowired
    private ILeaveMessageService leaveMessageService;

    /**
     * 分页查询留言
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "20") Integer pageSize,
                                  @RequestParam Map<String, Object> params) {
        return leaveMessageService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 新增留言
     */
    @PostMapping("/insert")
    @LogPoint("新增留言")
    public ResultData insert(LeaveMessage leaveMessage, Long userId) {
        return leaveMessageService.insert(leaveMessage, userId);
    }

    /**
     * 删除留言
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除留言")
    public ResultData delete(@PathVariable Long id) {
        return leaveMessageService.delete(id);
    }
}
