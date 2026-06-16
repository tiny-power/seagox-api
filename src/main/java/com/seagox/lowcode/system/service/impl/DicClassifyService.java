package com.seagox.lowcode.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.DicClassify;
import com.seagox.lowcode.system.entity.DicDetail;
import com.seagox.lowcode.system.mapper.DicClassifyMapper;
import com.seagox.lowcode.system.mapper.DicDetailMapper;
import com.seagox.lowcode.system.service.IDicClassifyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DicClassifyService implements IDicClassifyService {

    @Autowired
    private DicClassifyMapper dicClassifyMapper;

    @Autowired
    private DicDetailMapper dicDetailMapper;
    
    @Override
    public ResultData queryDisplay(Long companyId) {
    	LambdaQueryWrapper<DicClassify> qw = new LambdaQueryWrapper<>();
    	qw.eq(DicClassify::getCompanyId, companyId)
    	.orderByDesc(DicClassify::getCreateTime);
        List<DicClassify> list = dicClassifyMapper.selectList(qw);
        return ResultData.success(list);
    }

    @Override
    public ResultData insert(DicClassify dicClassify) {
    	LambdaQueryWrapper<DicClassify> qw = new LambdaQueryWrapper<>();
		qw.eq(DicClassify::getCompanyId, dicClassify.getCompanyId()).eq(DicClassify::getName, dicClassify.getName());
		Long count = dicClassifyMapper.selectCount(qw);
		if (count == 0) {
			dicClassifyMapper.insert(dicClassify);
			return ResultData.success(null);
		} else {
			return ResultData.warn(ResultCode.OTHER_ERROR, "名称已经存在");
		}
    }

    @Override
    public ResultData update(DicClassify dicClassify) {
    	DicClassify originalDicClassify = dicClassifyMapper.selectById(dicClassify.getId());
    	if (originalDicClassify.getName().equals(dicClassify.getName())) {
    		dicClassifyMapper.updateById(dicClassify);
    		return ResultData.success(null);
    	} else {
    		LambdaQueryWrapper<DicClassify> qw = new LambdaQueryWrapper<>();
			qw.eq(DicClassify::getCompanyId, dicClassify.getCompanyId()).eq(DicClassify::getName, dicClassify.getName());
			Long count = dicClassifyMapper.selectCount(qw);
			if (count == 0) {
				dicClassifyMapper.updateById(dicClassify);
	    		return ResultData.success(null);
			} else {
				return ResultData.warn(ResultCode.OTHER_ERROR, "名称已经存在");
			}
    	}
    }


    @Override
    public ResultData delete(Long id) {
        LambdaQueryWrapper<DicDetail> qw = new LambdaQueryWrapper<>();
        qw.eq(DicDetail::getClassifyId, id);
        Long count = dicDetailMapper.selectCount(qw);
        if (count > 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "字典分类下有数据，不可删除");
        } else {
            dicClassifyMapper.deleteById(id);
            return ResultData.success(null);
        }
    }

    @Override
    public ResultData queryById(Long id) {
        return ResultData.success(dicClassifyMapper.selectById(id));
    }

	@Override
	public ResultData queryByName(Long companyId, String name) {
		return ResultData.success(dicClassifyMapper.queryByName(companyId, name));
	}

}
