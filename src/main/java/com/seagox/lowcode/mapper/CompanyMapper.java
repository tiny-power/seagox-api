package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公司
 */
public interface CompanyMapper extends BaseMapper<Company> {

    /**
     * 查询全部通过编码前缀
     */
    public List<Map<String, Object>> queryByPrefix(@Param("prefix") String prefix);

    /**
     * 查询公司
     */
    public List<Map<String, Object>> queryByIds(@Param("array") String[] array, @Param("ids") String ids, @Param("topLevel") boolean topLevel);

    /**
     * 查询最大编码
     */
    public String queryMaxCode(@Param("prefix") String prefix, @Param("digit") int digit);

}
