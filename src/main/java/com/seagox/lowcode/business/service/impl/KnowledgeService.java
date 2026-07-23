package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.Knowledge;
import com.seagox.lowcode.business.mapper.KnowledgeMapper;
import com.seagox.lowcode.business.service.IKnowledgeService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 科普内容服务实现
 */
@Service
public class KnowledgeService implements IKnowledgeService {

    /**
     * 科普内容数据访问对象
     */
    @Autowired
    private KnowledgeMapper knowledgeMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = knowledgeMapper.queryKnowledgeList(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryList(Map<String, Object> params) {
        params.put("status", 1);
        List<Map<String, Object>> list = knowledgeMapper.queryKnowledgeList(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = knowledgeMapper.queryKnowledgeById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "科普内容不存在");
        }
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData insert(Knowledge knowledge) {
        ResultData verifyResult = verify(knowledge);
        if (verifyResult != null) {
            return verifyResult;
        }
        Date now = new Date();
        knowledge.setCreateTime(now);
        knowledge.setUpdateTime(now);
        if (knowledge.getStatus() == null) {
            knowledge.setStatus(1);
        }
        if (knowledge.getSort() == null) {
            knowledge.setSort(0);
        }
        knowledgeMapper.insert(knowledge);
        return ResultData.success(knowledge.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData update(Knowledge knowledge) {
        if (knowledge == null || knowledge.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "科普内容ID不能为空");
        }
        Knowledge original = knowledgeMapper.selectById(knowledge.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "科普内容不存在");
        }
        ResultData verifyResult = verify(knowledge);
        if (verifyResult != null) {
            return verifyResult;
        }
        knowledge.setCreateTime(original.getCreateTime());
        knowledge.setUpdateTime(new Date());
        knowledgeMapper.updateById(knowledge);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData delete(Long id) {
        if (knowledgeMapper.selectById(id) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "科普内容不存在");
        }
        knowledgeMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 校验科普内容
     */
    private ResultData verify(Knowledge knowledge) {
        if (knowledge == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "科普内容不能为空");
        }
        if (StringUtils.isEmpty(knowledge.getCategory())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "分类名称不能为空");
        }
        if (StringUtils.isEmpty(knowledge.getTitle())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "标题不能为空");
        }
        if (StringUtils.isEmpty(knowledge.getCoverUrl())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "封面图片不能为空");
        }
        if (StringUtils.isEmpty(knowledge.getVideoId())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "视频号视频ID不能为空");
        }
        if (StringUtils.isEmpty(knowledge.getVideoAccountId())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "视频号账号ID不能为空");
        }
        return null;
    }
}
