package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.InspectionItem;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 验收单验收项
 */
public interface InspectionItemMapper extends BaseMapper<InspectionItem> {

    /**
     * 查询验收单验收项
     */
    List<Map<String, Object>> queryByInspectionId(@Param("inspectionId") Long inspectionId);
}
