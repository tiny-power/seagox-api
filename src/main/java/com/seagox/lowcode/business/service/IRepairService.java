package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.Repair;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 报修单
 */
public interface IRepairService {

    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    ResultData queryById(Long id);

    ResultData insert(Repair repair, Long userId);

    ResultData update(Repair repair, Long userId);

    ResultData assign(Long id, Long repairMemberId, Long userId);

    ResultData complete(Long id, Repair repair, Long userId);

    ResultData confirm(Long id, Long userId);

    ResultData delete(Long id);
}
