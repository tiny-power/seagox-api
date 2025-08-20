package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息通知
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private IMessageService messageService;
    
    /**
     * 分页查询
     *
     * @param pageNo    起始页
     * @param pageSize  每页大小
     * @param companyId 公司id
     * @param userId    用户id
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId, Long userId, Integer status, String title) {
        return messageService.queryByPage(pageNo, pageSize, companyId, userId, status, title);
    }

    /**
     * 是否有未读消息
     */
    @GetMapping("/queryUnRead")
    public ResultData queryUnRead(Long companyId, Long userId) {
        return messageService.queryUnRead(companyId, userId);
    }

    /**
     * 处理消息
     */
    @PostMapping("/update")
    @LogPoint("处理消息")
    public ResultData update(Long userId, Long id) {
        return messageService.update(userId, id);
    }

    /**
     * 处理消息(所有未读)
     */
    @PostMapping("/updateAll")
    @LogPoint("处理未读消息")
    public ResultData updateAll(Long userId) {
        return messageService.updateAll(userId);
    }

    /**
     * 查询未读通知
     */
    @GetMapping("/getUnReadMessage")
    public ResultData getUnReadMessage(Long companyId, Long userId){
        return messageService.getUnReadMessage(companyId, userId);
    }

}
