package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.Requirement;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 需求沟通表
 */
public interface IRequirementService {

    /**
     * 分页查询需求沟通
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 查询需求沟通详情
     */
    ResultData queryById(Long id);

    /**
     * 查询项目需求沟通
     */
    ResultData queryByProjectId(Long projectId);

    /**
     * 保存需求沟通
     */
    ResultData save(Requirement requirement, Long userId);

    /**
     * 提交业主审核
     */
    ResultData submit(Long id, Long userId);

    /**
     * 签字确认
     */
    ResultData sign(Long id, String signatureUrl, Long userId);

    /**
     * 删除需求沟通
     */
    ResultData delete(Long id);
}
