package com.seagox.lowcode.assets.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 公共
 */
public interface AssetsMapper {
	
	/**
	 * 查询处置单明细
	 *
	 * @param disposeId    处置单id
	 */
	public List<Map<String, Object>> queryDisposeDetail(@Param("disposeId") String disposeId);
	
	/**
	 * 查询借用单明细
	 *
	 * @param borrowId    借用单id
	 */
	public List<Map<String, Object>> queryBorrowDetail(@Param("borrowId") String borrowId);
	
	/**
	 * 查询退库单明细
	 *
	 * @param borrowId    退库单id
	 */
	public List<Map<String, Object>> queryReturnStockDetail(@Param("returnStockId") String returnStockId);
	
	/**
	 * 查询归还单明细
	 *
	 * @param returnId    归还单id
	 */
	public List<Map<String, Object>> queryReturnDetail(@Param("returnId") String returnId);
	
	/**
	 * 查询调拨单明细
	 *
	 * @param allocateId    调拨单id
	 */
	public List<Map<String, Object>> queryAllocateDetail(@Param("allocateId") String allocateId);
	
	/**
	 * 查询移交单明细
	 *
	 * @param transferId    移交单id
	 */
	public List<Map<String, Object>> queryTransferDetail(@Param("transferId") String transferId);
	
	/**
	 * 查询变更单明细
	 *
	 * @param changeId  变更单id
	 */
	public List<Map<String, Object>> queryChangeDetail(@Param("changeId") String changeId);
	
	/**
	 * 查询维修单明细
	 *
	 * @param maintenanceId  维修单id
	 */
	public List<Map<String, Object>> queryMaintenanceDetail(@Param("maintenanceId") String maintenanceId);
	
	/**
	 * 查询领用单明细
	 *
	 * @param useId  领用单id
	 */
	public List<Map<String, Object>> queryUseDetail(@Param("useId") String useId);
	
	/**
	 * 查询盘点资产明细
	 */
	public List<Map<String, Object>> queryInventoryAssetsDetail(@Param("companyId") String companyId,
			@Param("zcmlList") List<String> zcmlList, @Param("statusList") List<String> statusList, @Param("sybmList") List<String> sybmList, @Param("zcwzList") List<String> zcwzList);
	
	/**
	 * 查询盘点资产明细
	 */
	public List<Map<String, Object>> queryInventoryDetail(@Param("planId") String planId);
	
	/**
	 * 更新盘点计划
	 */
	public int updateInventoryPlan(@Param("id") Long id, @Param("status") Integer status);
	
	/**
	 * 查询采购单明细
	 *
	 * @param purchaseId    采购单id
	 */
	public List<Map<String, Object>> queryPurchaseDetail(@Param("purchaseId") String purchaseId);
	
}
