package com.seagox.lowcode.system.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 流程相关查询
 */
public interface FlowMapper {
	
	/**
     * 待办事项
     */
    List<Map<String, Object>> queryTodoItem(@Param("companyId") Long companyId,
                                            @Param("userId") String userId,
                                            @Param("formId") Long formId,
                                            @Param("name") String name,
                                            @Param("statusStr") String statusStr,
                                            @Param("businessTypeStr") String businessTypeStr);
    
    /**
     * 已办事项
     */
    List<Map<String, Object>> queryDoneItem(@Param("companyId") Long companyId,
                                            @Param("userId") String userId,
                                            @Param("name") String name,
                                            @Param("startTime") String startTime,
                                            @Param("endTime") String endTime,
                                            @Param("todoPerson") String todoPerson,
                                            @Param("businessTypeStr") String businessTypeStr);
    
    /**
     * 抄送事项
     */
    List<Map<String, Object>> queryCopyItem(@Param("companyId") Long companyId,
                                            @Param("userId") String userId,
                                            @Param("name") String name,
                                            @Param("businessTypeStr") String businessTypeStr);
    
    /**
     * 我发起的
     */
    List<Map<String, Object>> querySelfItem(@Param("companyId") Long companyId,
                                            @Param("userId") Long userId,
                                            @Param("name") String name,
                                            @Param("businessTypeStr") String businessTypeStr);
    
    /**
     * 待发事项
     */
    List<Map<String, Object>> queryReadyItem(@Param("companyId") Long companyId,
                                             @Param("userId") Long userId,
                                             @Param("name") String name,
                                             @Param("businessTypeStr") String businessTypeStr);

}
