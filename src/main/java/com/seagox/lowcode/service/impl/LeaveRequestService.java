package com.seagox.lowcode.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.LeaveRequest;
import com.seagox.lowcode.mapper.LeaveRequestMapper;
import com.seagox.lowcode.service.ILeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LeaveRequestService implements ILeaveRequestService {

    @Autowired
    private LeaveRequestMapper leaveRequestMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, Long applicantId,
                                  String applicantName, Integer leaveType, Integer status, String startTime,
                                  String endTime) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = leaveRequestMapper.queryByPage(companyId, applicantId, applicantName,
                leaveType, status, startTime, endTime);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }

    @Override
    public ResultData insert(LeaveRequest leaveRequest) {
        ResultData verifyResult = verify(leaveRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        leaveRequest.setStatus(0);
        leaveRequestMapper.insert(leaveRequest);
        return ResultData.success(null);
    }

    @Override
    public ResultData update(LeaveRequest leaveRequest) {
        LeaveRequest original = leaveRequestMapper.selectById(leaveRequest.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (Integer.valueOf(1).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已提交的请假单不可以修改");
        }
        ResultData verifyResult = verify(leaveRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        leaveRequest.setStatus(original.getStatus());
        leaveRequestMapper.updateById(leaveRequest);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData delete(Long id) {
        LeaveRequest leaveRequest = leaveRequestMapper.selectById(id);
        if (leaveRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (Integer.valueOf(1).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已提交的请假单不可以删除");
        }
        leaveRequestMapper.deleteById(id);
        return ResultData.success(null);
    }

    @Override
    public ResultData submit(Long id) {
        LeaveRequest leaveRequest = leaveRequestMapper.selectById(id);
        if (leaveRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (Integer.valueOf(1).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单已经提交");
        }
        ResultData verifyResult = verify(leaveRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        leaveRequest.setStatus(1);
        leaveRequest.setSubmitTime(new Date());
        leaveRequestMapper.updateById(leaveRequest);
        return ResultData.success(null);
    }

    @Override
    public ResultData cancel(Long id) {
        LeaveRequest leaveRequest = leaveRequestMapper.selectById(id);
        if (leaveRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (!Integer.valueOf(1).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有已提交的请假单可以撤销");
        }
        leaveRequest.setStatus(2);
        leaveRequestMapper.updateById(leaveRequest);
        return ResultData.success(null);
    }

    private ResultData verify(LeaveRequest leaveRequest) {
        if (leaveRequest.getCompanyId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择公司");
        }
        if (leaveRequest.getApplicantId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择申请人");
        }
        if (leaveRequest.getLeaveType() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择请假类型");
        }
        if (leaveRequest.getStartTime() == null || leaveRequest.getEndTime() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择请假时间");
        }
        if (leaveRequest.getEndTime().before(leaveRequest.getStartTime())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "结束时间不能早于开始时间");
        }
        if (leaveRequest.getDuration() == null || leaveRequest.getDuration().compareTo(BigDecimal.ZERO) <= 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写请假时长");
        }
        if (StringUtils.isEmpty(leaveRequest.getReason())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写请假事由");
        }
        return null;
    }

}
