package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.Repair;
import com.seagox.lowcode.common.ResultData;
import java.util.Date;
import java.util.Map;

/**
 * 报修单
 */
public interface IRepairService {

    /**
     * 分页查询维修
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 通过ID查询维修
     */
    ResultData queryById(Long id);

    /**
     * 新增维修
     */
    ResultData insert(Repair repair, Long userId);

    /**
     * 更新维修
     */
    ResultData update(Repair repair, Long userId);

    /**
     * 分配维修人员
     */
    ResultData assign(Long id, Long repairMemberId, Date expectedVisitAt, Long userId);

    /**
     * 完成维修
     */
    ResultData complete(Long id, Repair repair, Long userId);

    /**
     * 确认维修完成
     */
    ResultData confirm(Long id, Long userId);

    /**
     * 重新维修
     */
    ResultData rework(Long id, Long userId);

    /**
     * 取消维修
     */
    ResultData cancel(Long id, Long userId);

    /**
     * 删除维修
     */
    ResultData delete(Long id);
}
