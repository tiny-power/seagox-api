package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Form;
import com.seagox.lowcode.mapper.BusinessFieldMapper;
import com.seagox.lowcode.mapper.FormMapper;
import com.seagox.lowcode.entity.SeaDefinition;
import com.seagox.lowcode.mapper.SeaDefinitionMapper;
import com.seagox.lowcode.service.ISeaDefinitionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SeaDefinitionService implements ISeaDefinitionService {

    @Autowired
    private SeaDefinitionMapper seaDefinitionMapper;

    @Autowired
    private BusinessFieldMapper businessFieldMapper;

    @Autowired
    private FormMapper formMapper;

    @Override
    public ResultData insert(SeaDefinition seaDefinition) {
        seaDefinitionMapper.insert(seaDefinition);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData update(SeaDefinition seaDefinition) {
        seaDefinitionMapper.updateById(seaDefinition);
        return ResultData.success(null);
    }

    @Override
    public ResultData delete(Long id) {
    	seaDefinitionMapper.deleteById(id);
        return ResultData.success(null);
    }

    @Override
    public ResultData queryById(Long id, String dataSource) {
        SeaDefinition seaDefinition = seaDefinitionMapper.selectById(id);
        Form form = formMapper.selectById(seaDefinition.getFormId());
        if(form != null) {
        	seaDefinition.setOperationAuthority(queryOperationAuthority(String.valueOf(form.getDataSource())));
        } else {
        	seaDefinition.setOperationAuthority(queryOperationAuthority(dataSource));
        }
        return ResultData.success(seaDefinition);
    }

    /**
     * 获取表单操作权限集合
     */
    public List<Map<String, Object>> queryOperationAuthority(String dataSource) {
        List<Map<String, Object>> operationAuthority = new ArrayList<>();
        List<Map<String, Object>> businessFieldList = businessFieldMapper.queryByTableIds(dataSource.split(","));
        for (int j = 0; j < businessFieldList.size(); j++) {
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", businessFieldList.get(j).get("tableName"));
            map.put("tableComment", businessFieldList.get(j).get("tableComment"));
            map.put("behavior", "DISABLED");
            map.put("field", businessFieldList.get(j).get("name"));
            map.put("comment", businessFieldList.get(j).get("remark"));
            operationAuthority.add(map);
        }
        return operationAuthority;
    }

	@Override
	public ResultData queryByFormId(Long formId) {
		LambdaQueryWrapper<SeaDefinition> qw = new LambdaQueryWrapper<>();
		qw.eq(SeaDefinition::getFormId, formId);
		return ResultData.success(seaDefinitionMapper.selectList(qw));
	}

}
