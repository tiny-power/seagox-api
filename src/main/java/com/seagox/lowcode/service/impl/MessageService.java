package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SysMessage;
import com.seagox.lowcode.mapper.MessageMapper;
import com.seagox.lowcode.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MessageService implements IMessageService {

    @Autowired
    private MessageMapper messageMapper;
    
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, Long userId, Integer status, String title) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = messageMapper.queryAll(companyId, userId, status, title);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }

    @Override
    public ResultData queryUnRead(Long companyId, Long userId) {
        return ResultData.success(messageMapper.queryCount(companyId, userId));
    }

    @Override
    public ResultData update(Long userId, Long id) {
        SysMessage message = messageMapper.selectById(id);
        message.setStatus(1);
        message.setUpdateTime(new Date());
        messageMapper.updateById(message);
        return ResultData.success(null);
    }

    @Override
    public ResultData updateAll(Long userId) {
        SysMessage message = new SysMessage();
        message.setStatus(1);
        message.setUpdateTime(new Date());
        LambdaQueryWrapper<SysMessage> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(SysMessage::getStatus, 0)
                .eq(SysMessage::getToUserId, userId);
        messageMapper.update(message, updateWrapper);
        return ResultData.success(null);
    }

    @Override
    public ResultData getUnReadMessage(Long companyId, Long userId) {
        LambdaQueryWrapper<SysMessage> qw = new LambdaQueryWrapper<>();
        qw.eq(SysMessage::getCompanyId, companyId)
                .eq(SysMessage::getToUserId, userId).eq(SysMessage::getStatus, 0)
                .orderByDesc(SysMessage::getCreateTime);
        List<SysMessage> sysMessages = messageMapper.selectList(qw);
        if (sysMessages != null && sysMessages.size() > 0){
            return ResultData.success(sysMessages.get(0));
        }
        return ResultData.success(null);
    }

}
