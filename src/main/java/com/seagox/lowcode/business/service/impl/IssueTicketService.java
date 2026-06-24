package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.IssueTicket;
import com.seagox.lowcode.business.mapper.IssueTicketMapper;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.service.IIssueTicketService;
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
 * 问题单
 */
@Service
public class IssueTicketService implements IIssueTicketService {

    /**
     * 待整改
     */
    public static final int STATUS_PENDING = 1;
    /**
     * 整改中
     */
    public static final int STATUS_RECTIFYING = 2;
    /**
     * 待复验
     */
    public static final int STATUS_REVIEWING = 3;
    /**
     * 已关闭
     */
    public static final int STATUS_CLOSED = 4;
    /**
     * 复验通过
     */
    public static final int REVIEW_PASS = 1;
    /**
     * 复验不通过
     */
    public static final int REVIEW_REJECT = 2;

    /**
     * 问题单数据访问对象
     */
    @Autowired
    private IssueTicketMapper issueTicketMapper;

    /**
     * 项目数据访问对象
     */
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 分页查询问题单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = issueTicketMapper.queryIssueTickets(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询问题单详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = issueTicketMapper.queryIssueTicketById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * 新增问题单
     */
    @Override
    public ResultData insert(IssueTicket issueTicket, Long userId) {
        ResultData verifyResult = verifyBase(issueTicket);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (projectMapper.selectById(issueTicket.getProjectId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        issueTicket.setStatus(STATUS_PENDING);
        issueTicket.setReportedBy(defaultUser(issueTicket.getReportedBy(), userId));
        issueTicket.setReportedAt(defaultDate(issueTicket.getReportedAt(), now));
        issueTicket.setRectificationCount(0);
        issueTicket.setCreatedBy(userId);
        issueTicket.setUpdatedBy(userId);
        issueTicket.setCreatedAt(now);
        issueTicket.setUpdatedAt(now);
        issueTicketMapper.insert(issueTicket);
        return ResultData.success(issueTicket.getId());
    }

    /**
     * 修改问题单
     */
    @Override
    public ResultData update(IssueTicket issueTicket, Long userId) {
        if (issueTicket == null || issueTicket.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单ID不能为空");
        }
        IssueTicket original = issueTicketMapper.selectById(issueTicket.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        if (Integer.valueOf(STATUS_CLOSED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已关闭的问题单不可以修改");
        }
        ResultData verifyResult = verifyBase(issueTicket);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (projectMapper.selectById(issueTicket.getProjectId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        issueTicket.setStatus(original.getStatus());
        issueTicket.setReportedBy(defaultUser(issueTicket.getReportedBy(), original.getReportedBy()));
        issueTicket.setReportedAt(defaultDate(issueTicket.getReportedAt(), original.getReportedAt()));
        issueTicket.setAssignedBy(original.getAssignedBy());
        issueTicket.setAssignedAt(original.getAssignedAt());
        issueTicket.setRectificationDeadline(original.getRectificationDeadline());
        issueTicket.setRectificationCount(original.getRectificationCount());
        issueTicket.setRectificationDescription(original.getRectificationDescription());
        issueTicket.setRectificationAttachments(original.getRectificationAttachments());
        issueTicket.setRectificationUserId(original.getRectificationUserId());
        issueTicket.setRectificationSubmittedAt(original.getRectificationSubmittedAt());
        issueTicket.setReviewUserId(original.getReviewUserId());
        issueTicket.setReviewResult(original.getReviewResult());
        issueTicket.setReviewRemark(original.getReviewRemark());
        issueTicket.setReviewAttachments(original.getReviewAttachments());
        issueTicket.setReviewedAt(original.getReviewedAt());
        issueTicket.setClosedBy(original.getClosedBy());
        issueTicket.setClosedAt(original.getClosedAt());
        issueTicket.setCreatedBy(original.getCreatedBy());
        issueTicket.setCreatedAt(original.getCreatedAt());
        issueTicket.setUpdatedBy(userId);
        issueTicket.setUpdatedAt(new Date());
        issueTicketMapper.updateById(issueTicket);
        return ResultData.success(null);
    }

    /**
     * 指派整改人
     */
    @Override
    public ResultData assign(IssueTicket issueTicket, Long userId) {
        if (issueTicket == null || issueTicket.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单ID不能为空");
        }
        IssueTicket original = issueTicketMapper.selectById(issueTicket.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        if (Integer.valueOf(STATUS_CLOSED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已关闭的问题单不可以指派");
        }
        if (issueTicket.getRectificationUserId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "整改责任人不能为空");
        }

        Date now = new Date();
        original.setRectificationUserId(issueTicket.getRectificationUserId());
        original.setRectificationDeadline(issueTicket.getRectificationDeadline());
        original.setAssignedBy(userId);
        original.setAssignedAt(now);
        original.setStatus(STATUS_RECTIFYING);
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        issueTicketMapper.updateById(original);
        return ResultData.success(null);
    }

    /**
     * 提交整改
     */
    @Override
    public ResultData rectify(IssueTicket issueTicket, Long userId) {
        if (issueTicket == null || issueTicket.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单ID不能为空");
        }
        IssueTicket original = issueTicketMapper.selectById(issueTicket.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        if (!Integer.valueOf(STATUS_RECTIFYING).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有整改中的问题单可以提交整改");
        }
        if (StringUtils.isEmpty(issueTicket.getRectificationDescription())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "整改说明不能为空");
        }

        Date now = new Date();
        original.setRectificationDescription(issueTicket.getRectificationDescription());
        original.setRectificationAttachments(issueTicket.getRectificationAttachments());
        original.setRectificationUserId(defaultUser(original.getRectificationUserId(), userId));
        original.setRectificationSubmittedAt(defaultDate(issueTicket.getRectificationSubmittedAt(), now));
        original.setRectificationCount(defaultCount(original.getRectificationCount()) + 1);
        original.setStatus(STATUS_REVIEWING);
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        issueTicketMapper.updateById(original);
        return ResultData.success(null);
    }

    /**
     * 复验问题单
     */
    @Override
    public ResultData review(IssueTicket issueTicket, Long userId) {
        if (issueTicket == null || issueTicket.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单ID不能为空");
        }
        IssueTicket original = issueTicketMapper.selectById(issueTicket.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        if (!Integer.valueOf(STATUS_REVIEWING).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有待复验的问题单可以复验");
        }
        if (!Integer.valueOf(REVIEW_PASS).equals(issueTicket.getReviewResult())
                && !Integer.valueOf(REVIEW_REJECT).equals(issueTicket.getReviewResult())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "复验结果不正确");
        }

        Date now = new Date();
        original.setReviewUserId(userId);
        original.setReviewResult(issueTicket.getReviewResult());
        original.setReviewRemark(issueTicket.getReviewRemark());
        original.setReviewAttachments(issueTicket.getReviewAttachments());
        original.setReviewedAt(defaultDate(issueTicket.getReviewedAt(), now));
        if (Integer.valueOf(REVIEW_PASS).equals(issueTicket.getReviewResult())) {
            original.setStatus(STATUS_CLOSED);
            original.setClosedBy(userId);
            original.setClosedAt(now);
        } else {
            original.setStatus(STATUS_RECTIFYING);
            original.setClosedBy(null);
            original.setClosedAt(null);
        }
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        issueTicketMapper.updateById(original);
        return ResultData.success(null);
    }

    /**
     * 删除问题单
     */
    @Override
    public ResultData delete(Long id) {
        IssueTicket issueTicket = issueTicketMapper.selectById(id);
        if (issueTicket == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        issueTicketMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 基础校验
     */
    private ResultData verifyBase(IssueTicket issueTicket) {
        if (issueTicket == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不能为空");
        }
        if (issueTicket.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "所属项目不能为空");
        }
        if (StringUtils.isEmpty(issueTicket.getTitle())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题标题不能为空");
        }
        if (StringUtils.isEmpty(issueTicket.getDescription())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题描述不能为空");
        }
        return null;
    }

    /**
     * 默认用户
     */
    private Long defaultUser(Long first, Long second) {
        return first == null ? second : first;
    }

    /**
     * 默认时间
     */
    private Date defaultDate(Date first, Date second) {
        return first == null ? second : first;
    }

    /**
     * 默认次数
     */
    private int defaultCount(Integer count) {
        return count == null ? 0 : count;
    }
}
