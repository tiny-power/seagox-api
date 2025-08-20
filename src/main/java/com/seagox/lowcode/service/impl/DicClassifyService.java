package com.seagox.lowcode.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.DicClassify;
import com.seagox.lowcode.entity.DicDetail;
import com.seagox.lowcode.mapper.DicClassifyMapper;
import com.seagox.lowcode.mapper.DicDetailMapper;
import com.seagox.lowcode.service.IDicClassifyService;
import com.seagox.lowcode.util.ExcelUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DicClassifyService implements IDicClassifyService {

    @Autowired
    private DicClassifyMapper dicClassifyMapper;

    @Autowired
    private DicDetailMapper dicDetailMapper;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
	
	@Transactional
	@Override
	public ResultData importHandle(MultipartFile file, Long companyId) {
		try {
			JSONObject result = ExcelUtils.readDicData(file.getInputStream());
			for (String key : result.keySet()) {
				DicClassify dicClassify = new DicClassify();
				dicClassify.setCompanyId(companyId);
				dicClassify.setName(key);
				LambdaQueryWrapper<DicClassify> qw = new LambdaQueryWrapper<>();
				qw.eq(DicClassify::getCompanyId, dicClassify.getCompanyId()).eq(DicClassify::getName, dicClassify.getName());
				DicClassify isExist = dicClassifyMapper.selectOne(qw);
				if(isExist != null) {
					LambdaQueryWrapper<DicDetail> qwDetail = new LambdaQueryWrapper<>();
					qwDetail.eq(DicDetail::getClassifyId, isExist.getId());
					dicClassify.setId(isExist.getId());
					dicDetailMapper.delete(qwDetail);
				} else {
					dicClassifyMapper.insert(dicClassify);
				}
				JSONArray data = result.getJSONArray(key);
				JSONObject parentObject = new JSONObject();
				JSONObject sortObject = new JSONObject();
				for (int i = 0; i < data.size(); i++) {
					JSONObject item = data.getJSONObject(i);
					int level = item.getIntValue("level");
					DicDetail dicDetail = new DicDetail();
					dicDetail.setClassifyId(dicClassify.getId());
					dicDetail.setCode(item.getString("code"));
					dicDetail.setName(item.getString("value"));
					dicDetail.setLastStage(1);
					if(!sortObject.containsKey(String.valueOf(level))) {
						dicDetail.setSort(1);
						sortObject.put(String.valueOf(level), 1);
					} else {
						int sort = sortObject.getIntValue(String.valueOf(level));
						dicDetail.setSort(sort + 1);
						sortObject.put(String.valueOf(level), sort + 1);
					}
					if(level != 1) {
						dicDetail.setParentId(parentObject.getLong(String.valueOf(level-1)));
					}
					if(dicDetail.getParentId() != null) {
						String sql = "update dic_detail set last_stage = 0 where id = " + dicDetail.getParentId();
						jdbcTemplate.update(sql);
			        }
					dicDetailMapper.insert(dicDetail);
					parentObject.put(String.valueOf(level), dicDetail.getId());
				}
			}
			return ResultData.success(null);
		} catch (IOException e) {
			e.printStackTrace();
			return ResultData.error(ResultCode.INTERNAL_SERVER_ERROR);
		}
	}

}
