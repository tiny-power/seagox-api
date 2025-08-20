package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.SeaInstance;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 流程实例
 */
public interface SeaInstanceMapper extends BaseMapper<SeaInstance> {

	/**
	 * 待办事项
	 */
	public List<Map<String, Object>> queryTodoItem(@Param("companyId") Long companyId, @Param("userId") String userId,
			@Param("formId") Long formId, @Param("name") String name, @Param("statusStr") String statusStr,
			@Param("businessTypeStr") String businessTypeStr);

    /**
     * 已办事项
     */
    public List<Map<String, Object>> queryDoneItem(@Param("companyId") Long companyId, @Param("userId") String userId, @Param("name") String name, @Param("startTime") String startTime,
            @Param("endTime") String endTime, @Param("todoPerson") String todoPerson, @Param("businessTypeStr") String businessTypeStr);

    /**
     * 抄送事项
     */
    public List<Map<String, Object>> queryCopyItem(@Param("companyId") Long companyId, @Param("userId") String userId, @Param("name") String name, @Param("businessTypeStr") String businessTypeStr);

    /**
     * 我发起的
     */
    public List<Map<String, Object>> querySelfItem(@Param("companyId") Long companyId, @Param("userId") Long userId, @Param("name") String name, @Param("businessTypeStr") String businessTypeStr);

    /**
     * 待发事项
     */
    public List<Map<String, Object>> queryReadyItem(@Param("companyId") Long companyId, @Param("userId") String userId, @Param("name") String name, @Param("businessTypeStr") String businessTypeStr);


    /**
     * 删除流程记录
     */
    public void deleteProcess(@Param("businessType") String businessType, @Param("businessKey") String businessKey);

    /**
     * 流程记录
     */
    public List<Map<String, Object>> queryProcessInfo(@Param("businessType") String businessType, @Param("businessKey") String businessKey);
}
