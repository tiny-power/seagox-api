package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.ProjectHandover;
import com.seagox.lowcode.business.mapper.ProjectHandoverMapper;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.service.IProjectHandoverService;
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
 * 交接单
 */
@Service
public class ProjectHandoverService implements IProjectHandoverService {

    /**
     * 交接单数据访问对象
     */
    @Autowired
    private ProjectHandoverMapper projectHandoverMapper;

    /**
     * 项目数据访问对象
     */
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 分页查询交接单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = projectHandoverMapper.queryProjectHandovers(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询交接单详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = projectHandoverMapper.queryProjectHandoverById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "交接单不存在");
        }
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * 新增交接单
     */
    @Override
    public ResultData insert(ProjectHandover projectHandover, Long userId) {
        if (projectHandover != null && projectHandover.getHandoverUserId() == null) {
            projectHandover.setHandoverUserId(userId);
        }
        ResultData verifyResult = verify(projectHandover);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (projectMapper.selectById(projectHandover.getProjectId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        Long operatorId = userId == null ? projectHandover.getHandoverUserId() : userId;
        if (projectHandover.getHandoverSignedAt() == null) {
            projectHandover.setHandoverSignedAt(now);
        }
        if (projectHandover.getHandoverSignatureUrl() == null) {
            projectHandover.setHandoverSignatureUrl("");
        }
        if (projectHandover.getReceiverSignatureUrl() == null) {
            projectHandover.setReceiverSignatureUrl("");
        }
        projectHandover.setReceiverSignedAt(null);
        projectHandover.setCreatedBy(operatorId);
        projectHandover.setUpdatedBy(operatorId);
        projectHandover.setCreatedAt(now);
        projectHandover.setUpdatedAt(now);
        projectHandoverMapper.insert(projectHandover);
        return ResultData.success(projectHandover.getId());
    }

    /**
     * 确认交接单
     */
    @Override
    public ResultData confirm(Long id, String receiverSignatureUrl, Long userId) {
        ProjectHandover projectHandover = projectHandoverMapper.selectById(id);
        if (projectHandover == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "交接单不存在");
        }
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        if (!userId.equals(projectHandover.getReceiverUserId())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有接收负责人可以确认交接");
        }
        if (projectHandover.getReceiverSignedAt() != null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "交接单已确认");
        }

        Date now = new Date();
        projectHandover.setReceiverSignatureUrl(receiverSignatureUrl == null ? "" : receiverSignatureUrl);
        projectHandover.setReceiverSignedAt(now);
        projectHandover.setUpdatedBy(userId);
        projectHandover.setUpdatedAt(now);
        projectHandoverMapper.updateById(projectHandover);
        return ResultData.success(null);
    }

    /**
     * 校验交接单
     */
    private ResultData verify(ProjectHandover projectHandover) {
        if (projectHandover == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "交接单不能为空");
        }
        if (projectHandover.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "所属项目不能为空");
        }
        if (projectHandover.getHandoverType() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "交接类型不能为空");
        }
        if (projectHandover.getHandoverTime() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "交接时间不能为空");
        }
        if (StringUtils.isEmpty(projectHandover.getHandoverContent())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "交接内容不能为空");
        }
        if (projectHandover.getHandoverUserId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "移交负责人不能为空");
        }
        if (projectHandover.getReceiverUserId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "接收负责人不能为空");
        }
        return null;
    }
}
