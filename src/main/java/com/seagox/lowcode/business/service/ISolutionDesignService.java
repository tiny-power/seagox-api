package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.SolutionDesign;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 方案设计
 */
public interface ISolutionDesignService {

    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    ResultData queryById(Long id);

    ResultData queryByProjectId(Long projectId);

    ResultData save(SolutionDesign solutionDesign, Long userId);

    ResultData submit(Long id, Long userId);

    ResultData confirm(Long id, Long userId);

    ResultData freeze(Long id, String signatureUrl, Long userId);

    ResultData applyDefrost(Long id, String defrostExplanation, Long userId);

    ResultData approveDefrost(Long id, Long userId);

    ResultData rejectDefrost(Long id, Long userId);

    ResultData complete(Long id, Long userId);

    ResultData delete(Long id);
}
