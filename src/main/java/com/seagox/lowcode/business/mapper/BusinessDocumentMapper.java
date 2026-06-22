package com.seagox.lowcode.business.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 工程业务单据只读查询
 */
public interface BusinessDocumentMapper {

    List<Map<String, Object>> queryConstructionLogs(@Param("params") Map<String, Object> params);

    Map<String, Object> queryConstructionLogById(@Param("id") Long id);

    List<Map<String, Object>> queryInspections(@Param("params") Map<String, Object> params);

    Map<String, Object> queryInspectionById(@Param("id") Long id);

    List<Map<String, Object>> queryIssueTickets(@Param("params") Map<String, Object> params);

    Map<String, Object> queryIssueTicketById(@Param("id") Long id);

    List<Map<String, Object>> queryPaymentRequests(@Param("params") Map<String, Object> params);

    Map<String, Object> queryPaymentRequestById(@Param("id") Long id);

    List<Map<String, Object>> queryMaterialArrivals(@Param("params") Map<String, Object> params);

    Map<String, Object> queryMaterialArrivalById(@Param("id") Long id);

    List<Map<String, Object>> queryProjectHandovers(@Param("params") Map<String, Object> params);

    Map<String, Object> queryProjectHandoverById(@Param("id") Long id);
}
