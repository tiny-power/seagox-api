package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.Form;

import java.util.List;
import java.util.Map;

/**
 * 表单管理
 */
public interface FormMapper extends BaseMapper<Form> {

    /**
     * 查询公共数据
     *
     * @param sql sql语句
     */
    public List<Map<String, Object>> queryPublicList(String sql);
    
    /**
     * 更新id为0
     */
    public int updateZero();

}
