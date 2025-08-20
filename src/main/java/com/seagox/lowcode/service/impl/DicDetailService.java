package com.seagox.lowcode.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.DicDetail;
import com.seagox.lowcode.entity.Form;
import com.seagox.lowcode.mapper.DicDetailMapper;
import com.seagox.lowcode.mapper.FormMapper;
import com.seagox.lowcode.service.IDicDetailService;
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
	private FormMapper formMapper;

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
		updateFormDicData("add", companyId, dicDetail);
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
			updateFormDicData("edit", companyId, dicDetail);
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
				updateFormDicData("edit", companyId, dicDetail);
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
		updateFormDicData("delete", companyId, dicDetail);
		return ResultData.success(null);
	}

	@Override
	public ResultData queryByClassifyId(Long classifyId) {
		LambdaQueryWrapper<DicDetail> qw = new LambdaQueryWrapper<>();
		qw.eq(DicDetail::getClassifyId, classifyId).orderByAsc(DicDetail::getSort);
		List<DicDetail> list = dicDetailMapper.selectList(qw);
		return ResultData.success(list);
	}

	public void updateFormDicData(String type, Long companyId, DicDetail dicDetail) {
		LambdaQueryWrapper<Form> qw = new LambdaQueryWrapper<>();
		qw.eq(Form::getCompanyId, companyId);
		List<Form> formList = formMapper.selectList(qw);
		for (Form form : formList) {
			boolean isUpdate = false;
			JSONObject options = JSON.parseObject(form.getOptions());
			JSONArray searchColumn = options.getJSONArray("searchColumn");
			for (int i = 0; i < searchColumn.size(); i++) {
				JSONObject searchColumnDetail = searchColumn.getJSONObject(i);
				Long source = searchColumnDetail.getLong("source");
				if (!StringUtils.isEmpty(source)) {
					if (source.equals(dicDetail.getClassifyId())) {
						isUpdate = true;
						JSONArray detailOptions = searchColumnDetail.getJSONArray("options");
						if (type.equals("add")) {
							detailOptions.add(dicDetail);
						} else if (type.equals("edit")) {
							for (int j = 0; j < detailOptions.size(); j++) {
								JSONObject detail = detailOptions.getJSONObject(j);
								if (detail.getLong("id").equals(dicDetail.getId())) {
									detail.put("name", dicDetail.getName());
									detail.put("code", dicDetail.getCode());
									searchColumnDetail.put("options", detailOptions);
									break;
								}
							}
						} else if (type.equals("delete")) {
							for (int j = 0; j < detailOptions.size(); j++) {
								JSONObject detail = detailOptions.getJSONObject(j);
								if (detail.getLong("id").equals(dicDetail.getId())) {
									detailOptions.remove(j);
									break;
								}
							}
						}
					}
				}
			}
			JSONArray tableColumn = options.getJSONArray("tableColumn");
			for (int i = 0; i < tableColumn.size(); i++) {
				JSONObject tableColumnDetail = tableColumn.getJSONObject(i);
				Long formatter = tableColumnDetail.getLong("formatter");
				if (!StringUtils.isEmpty(formatter)) {
					if (formatter.equals(dicDetail.getClassifyId())) {
						isUpdate = true;
						JSONArray detailOptions = tableColumnDetail.getJSONArray("options");
						if (type.equals("add")) {
							detailOptions.add(dicDetail);
						} else if (type.equals("edit")) {
							for (int j = 0; j < detailOptions.size(); j++) {
								JSONObject detail = detailOptions.getJSONObject(j);
								if (detail.getLong("id").equals(dicDetail.getId())) {
									detail.put("name", dicDetail.getName());
									detail.put("code", dicDetail.getCode());
									tableColumnDetail.put("options", detailOptions.toString());
									break;
								}
							}
						} else if (type.equals("delete")) {
							for (int j = 0; j < detailOptions.size(); j++) {
								JSONObject detail = detailOptions.getJSONObject(j);
								if (detail.getLong("id").equals(dicDetail.getId())) {
									detailOptions.remove(j);
									break;
								}
							}
						}
					}
				}
			}
			if (isUpdate) {
				form.setOptions(options.toString());
				formMapper.updateById(form);
			}
		}
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
			updateFormDicData("add", companyId, dicDetail);
		}
		return ResultData.success(null);
	}

}
