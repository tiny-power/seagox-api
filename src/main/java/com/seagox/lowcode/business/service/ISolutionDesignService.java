package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.SolutionDesign;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 方案设计
 */
public interface ISolutionDesignService {

    /**
     * 分页查询方案设计
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 通过ID查询方案设计
     */
    ResultData queryById(Long id);

    /**
     * 通过项目ID查询方案设计
     */
    ResultData queryByProjectId(Long projectId);

    /**
     * 保存方案设计
     */
    ResultData save(SolutionDesign solutionDesign, Long userId);

    /**
     * 提交方案设计
     */
    ResultData submit(Long id, Long userId, Long companyId);

    /**
     * 确认方案设计
     */
    ResultData confirm(Long id, Long userId);

    /**
     * 冻结方案设计
     */
    ResultData freeze(Long id, String signatureUrl, Long userId);

    /**
     * 申请解冻方案设计
     */
    ResultData applyDefrost(Long id, String defrostExplanation, Long userId);

    /**
     * 同意解冻方案设计
     */
    ResultData approveDefrost(Long id, Long userId);

    /**
     * 拒绝解冻方案设计
     */
    ResultData rejectDefrost(Long id, Long userId);

    /**
     * 完成方案设计
     */
    ResultData complete(Long id, Long userId);

    /**
     * 删除方案设计
     */
    ResultData delete(Long id);
}
