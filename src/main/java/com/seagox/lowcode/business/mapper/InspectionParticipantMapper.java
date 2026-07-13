package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.InspectionParticipant;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 验收单参与者
 */
public interface InspectionParticipantMapper extends BaseMapper<InspectionParticipant> {

    /**
     * 查询验收单参与者
     */
    List<Map<String, Object>> queryByInspectionId(@Param("inspectionId") Long inspectionId);
}
