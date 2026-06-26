package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.LeaveMessage;
import com.seagox.lowcode.business.mapper.LeaveMessageMapper;
import com.seagox.lowcode.business.service.ILeaveMessageService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 留言表
 */
@Service
public class LeaveMessageService implements ILeaveMessageService {

    @Autowired
    private LeaveMessageMapper leaveMessageMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = leaveMessageMapper.queryLeaveMessages(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData insert(LeaveMessage leaveMessage, Long userId) {
        if (leaveMessage == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "留言不能为空");
        }
        if (leaveMessage.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择项目");
        }
        if (leaveMessage.getProjectMemberId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目角色不能为空");
        }
        if (leaveMessage.getType() == null) {
            leaveMessage.setType(1);
        }
        if (StringUtils.isEmpty(leaveMessage.getRemark())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "留言内容不能为空");
        }
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        Date now = new Date();
        leaveMessage.setCreatedBy(userId);
        leaveMessage.setUpdatedBy(userId);
        leaveMessage.setCreatedAt(now);
        leaveMessage.setUpdatedAt(now);
        leaveMessageMapper.insert(leaveMessage);
        return ResultData.success(leaveMessage.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData delete(Long id) {
        LeaveMessage leaveMessage = leaveMessageMapper.selectById(id);
        if (leaveMessage == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "留言不存在");
        }
        leaveMessageMapper.deleteById(id);
        return ResultData.success(null);
    }
}
