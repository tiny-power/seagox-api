package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.DicClassify;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字典分类
 */
public interface DicClassifyMapper extends BaseMapper<DicClassify> {
    
    /**
     * 查询列表通过名称
     */
   public List<Map<String, Object>> queryByName(@Param("companyId") Long companyId, @Param("name") String name);
   
   /**
    * 查询末级列表通过名称
    */
  public List<String> queryLastStageByName(@Param("companyId") Long companyId, @Param("name") String name);
   
   /**
    * 查询列表通过名称
    */
  public List<DicClassify> queryByNameList(@Param("nameList") List<String> nameList);

}
