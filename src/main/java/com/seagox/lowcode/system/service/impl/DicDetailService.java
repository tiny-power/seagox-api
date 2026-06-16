package com.seagox.lowcode.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.DicDetail;
import com.seagox.lowcode.system.mapper.DicDetailMapper;
import com.seagox.lowcode.system.service.IDicDetailService;
import com.seagox.lowcode.util.TreeUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class DicDetailService implements IDicDetailService {

	@Autowired
	private DicDetailMapper dicDetailMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public ResultData queryDisplay(Long classifyId) {
		List<Map<String, Object>> list = dicDetailMapper.queryMaps(classifyId);
		return ResultData.success(TreeUtils.categoryTreeHandle(list, "parentId", 0L));
	}

	@Override
	public ResultData queryDisplayNoTree(Long classifyId) {
		LambdaQueryWrapper<DicDetail> qw = new LambdaQueryWrapper<>();
		qw.eq(DicDetail::getClassifyId, classifyId).orderByAsc(DicDetail::getSort);
		List<Map<String, Object>> list = dicDetailMapper.selectMaps(qw);
		return ResultData.success(list);
	}

	@Transactional
	@Override
	public ResultData insert(Long companyId, DicDetail dicDetail) {
		LambdaQueryWrapper<DicDetail> codeQw = new LambdaQueryWrapper<>();
		codeQw.eq(DicDetail::getClassifyId, dicDetail.getClassifyId()).eq(DicDetail::getCode, dicDetail.getCode());
		Long codeCount = dicDetailMapper.selectCount(codeQw);
		if (codeCount > 0) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "字典值已经存在");
		} else {
			LambdaQueryWrapper<DicDetail> nameQw = new LambdaQueryWrapper<>();
			nameQw.eq(DicDetail::getClassifyId, dicDetail.getClassifyId()).eq(DicDetail::getName, dicDetail.getName());
			if (StringUtils.isEmpty(dicDetail.getParentId())) {
				nameQw.isNull(DicDetail::getParentId);
			} else {
				nameQw.eq(DicDetail::getParentId, dicDetail.getParentId());
			}
			Long nameCount = dicDetailMapper.selectCount(nameQw);
			if (nameCount > 0) {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "字典名称已经存在");
			}
		}
		if (dicDetail.getParentId() != null) {
			String sql = "update dic_detail set last_stage = 0 where id = " + dicDetail.getParentId();
			jdbcTemplate.update(sql);
		}
		dicDetail.setLastStage(1);
		dicDetailMapper.insert(dicDetail);
		return ResultData.success(null);
	}

	@Transactional
	@Override
	public ResultData update(Long companyId, DicDetail dicDetail) {
		DicDetail originalDicDetail = dicDetailMapper.selectById(dicDetail.getId());
		if (originalDicDetail.getCode().equals(dicDetail.getCode())) {
			if (!dicDetail.getName().equals(originalDicDetail.getName())) {
				LambdaQueryWrapper<DicDetail> nameQw = new LambdaQueryWrapper<>();
				nameQw.eq(DicDetail::getClassifyId, dicDetail.getClassifyId()).eq(DicDetail::getName,
						dicDetail.getName());
				if (StringUtils.isEmpty(dicDetail.getParentId())) {
					nameQw.isNull(DicDetail::getParentId);
				} else {
					nameQw.eq(DicDetail::getParentId, dicDetail.getParentId());
				}
				Long nameCount = dicDetailMapper.selectCount(nameQw);
				if (nameCount > 0) {
					return ResultData.warn(ResultCode.PARAMETER_ERROR, "字典名称已经存在");
				}
			}
			dicDetailMapper.updateById(dicDetail);
			dicDetail.setClassifyId(originalDicDetail.getClassifyId());
			return ResultData.success(null);
		} else {
			LambdaQueryWrapper<DicDetail> qw = new LambdaQueryWrapper<>();
			qw.eq(DicDetail::getClassifyId, dicDetail.getClassifyId()).eq(DicDetail::getCode, dicDetail.getCode());
			Long count = dicDetailMapper.selectCount(qw);
			if (count == 0) {
				if (!dicDetail.getName().equals(originalDicDetail.getName())) {
					LambdaQueryWrapper<DicDetail> nameQw = new LambdaQueryWrapper<>();
					nameQw.eq(DicDetail::getClassifyId, dicDetail.getClassifyId()).eq(DicDetail::getName,
							dicDetail.getName());
					if (StringUtils.isEmpty(dicDetail.getParentId())) {
						nameQw.isNull(DicDetail::getParentId);
					} else {
						nameQw.eq(DicDetail::getParentId, dicDetail.getParentId());
					}
					Long nameCount = dicDetailMapper.selectCount(nameQw);
					if (nameCount > 0) {
						return ResultData.warn(ResultCode.PARAMETER_ERROR, "字典名称已经存在");
					}
				}
				dicDetailMapper.updateById(dicDetail);
				dicDetail.setClassifyId(originalDicDetail.getClassifyId());
				return ResultData.success(null);
			} else {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "字典值已经存在");
			}
		}
	}

	@Transactional
	@Override
	public ResultData delete(Long companyId, Long id) {
		LambdaQueryWrapper<DicDetail> qw = new LambdaQueryWrapper<>();
		qw.eq(DicDetail::getParentId, id);
		Long count = dicDetailMapper.selectCount(qw);
		if (count != 0) {
			return ResultData.warn(ResultCode.OTHER_ERROR, "存在下级，不可删除");
		}
		DicDetail dicDetail = dicDetailMapper.selectById(id);
		dicDetailMapper.deleteById(id);
		if (dicDetail.getParentId() != null) {
			LambdaQueryWrapper<DicDetail> childQw = new LambdaQueryWrapper<>();
			childQw.eq(DicDetail::getParentId, dicDetail.getParentId());
			Long childCount = dicDetailMapper.selectCount(childQw);
			if (childCount == 0) {
				String sql = "update dic_detail set last_stage = 0 where id = " + dicDetail.getParentId();
				jdbcTemplate.update(sql);
			}
		}
		return ResultData.success(null);
	}

	@Override
	public ResultData queryByClassifyId(Long classifyId) {
		LambdaQueryWrapper<DicDetail> qw = new LambdaQueryWrapper<>();
		qw.eq(DicDetail::getClassifyId, classifyId).orderByAsc(DicDetail::getSort);
		List<DicDetail> list = dicDetailMapper.selectList(qw);
		return ResultData.success(list);
	}

	@Transactional
	@Override
	public ResultData batch(Long companyId, Long classifyId, String remark) {
		String[] lines = remark.split("\n");
		for (int i = 0; i < lines.length; i++) {
			DicDetail dicDetail = new DicDetail();
			dicDetail.setClassifyId(classifyId);
			dicDetail.setCode(String.valueOf(i + 1));
			dicDetail.setName(lines[i]);
			dicDetail.setSort(i + 1);
			dicDetail.setStatus(1);
			dicDetail.setLastStage(1);
			dicDetailMapper.insert(dicDetail);
		}
		return ResultData.success(null);
	}

}
