package com.seagox.lowcode.business.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.ConstructionDrawing;
import com.seagox.lowcode.business.entity.ConstructionDrawingDetail;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.mapper.ConstructionDrawingMapper;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.service.IConstructionDrawingService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 施工图出图
 */
@Service
public class ConstructionDrawingService implements IConstructionDrawingService {

    public static final int STATUS_DRAFT = 1;
    public static final int STATUS_PENDING = 2;
    public static final int STATUS_ARCHIVED = 3;

    @Autowired
    private ConstructionDrawingMapper constructionDrawingMapper;

    @Autowired
    private ProjectMapper projectMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = constructionDrawingMapper.queryConstructionDrawings(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = constructionDrawingMapper.queryConstructionDrawingById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工图出图不存在");
        }
        List<Map<String, Object>> details = constructionDrawingMapper.queryConstructionDrawingDetails(id);
        MapDateFormatUtils.formatDateValues(details);
        data.put("details", details);
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryByProjectId(Long projectId) {
        if (projectId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择项目");
        }
        ConstructionDrawing constructionDrawing = findByProjectId(projectId);
        if (constructionDrawing == null) {
            return ResultData.success(null);
        }
        return queryById(constructionDrawing.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ResultData save(ConstructionDrawing constructionDrawing, Long userId) {
        ResultData verifyResult = verify(constructionDrawing);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        Project project = projectMapper.selectById(constructionDrawing.getProjectId());
        if (project == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        ConstructionDrawing original = null;
        if (constructionDrawing.getId() != null) {
            original = constructionDrawingMapper.selectById(constructionDrawing.getId());
        }
        if (original == null) {
            original = findByProjectId(constructionDrawing.getProjectId());
        }

        if (original == null) {
            constructionDrawing.setStatus(constructionDrawing.getStatus() == null ? STATUS_DRAFT : constructionDrawing.getStatus());
            constructionDrawing.setCreatedBy(userId);
            constructionDrawing.setUpdatedBy(userId);
            constructionDrawing.setCreatedAt(now);
            constructionDrawing.setUpdatedAt(now);
            constructionDrawingMapper.insert(constructionDrawing);
            constructionDrawingMapper.insertDetail(buildDetail(constructionDrawing, constructionDrawing.getId(), userId, now));
            return ResultData.success(constructionDrawing.getId());
        }
        if (Integer.valueOf(STATUS_ARCHIVED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已归档的施工图不可以修改");
        }

        ConstructionDrawingDetail latestDetail = constructionDrawingMapper.queryLatestDetail(original.getId());
        ConstructionDrawingDetail detail = buildDetail(constructionDrawing, original.getId(), userId, now);
        if (latestDetail != null && shouldUpdateLatestDetail(latestDetail, detail)) {
            detail.setId(latestDetail.getId());
            detail.setCreatedBy(latestDetail.getCreatedBy());
            detail.setCreatedAt(latestDetail.getCreatedAt());
            constructionDrawingMapper.updateDetailById(detail);
        } else {
            constructionDrawingMapper.insertDetail(detail);
        }
        original.setStatus(constructionDrawing.getStatus() == null ? original.getStatus() : constructionDrawing.getStatus());
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        constructionDrawingMapper.updateById(original);
        return ResultData.success(original.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData submit(Long id, Long userId) {
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        ConstructionDrawing constructionDrawing = constructionDrawingMapper.selectById(id);
        if (constructionDrawing == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工图出图不存在");
        }
        if (Integer.valueOf(STATUS_ARCHIVED).equals(constructionDrawing.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已归档的施工图不可以提交");
        }
        ConstructionDrawingDetail detail = constructionDrawingMapper.queryLatestDetail(id);
        if (detail == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工图出图详情不存在");
        }
        Date now = new Date();
        detail.setConfirmMembers(resetConfirmMembers(detail.getConfirmMembers()));
        detail.setUpdatedBy(userId);
        detail.setUpdatedAt(now);
        constructionDrawingMapper.updateDetailById(detail);
        constructionDrawing.setStatus(STATUS_PENDING);
        constructionDrawing.setUpdatedBy(userId);
        constructionDrawing.setUpdatedAt(now);
        constructionDrawingMapper.updateById(constructionDrawing);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData confirmRead(Long id, String roleKey, Long userId) {
        if (!"owner".equals(roleKey) && !"builder".equals(roleKey)) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "确认角色不正确");
        }
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        ConstructionDrawing constructionDrawing = constructionDrawingMapper.selectById(id);
        if (constructionDrawing == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工图出图不存在");
        }
        if (!Integer.valueOf(STATUS_PENDING).equals(constructionDrawing.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有待确认的施工图可以确认阅读");
        }
        ConstructionDrawingDetail detail = constructionDrawingMapper.queryLatestDetail(id);
        if (detail == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工图出图详情不存在");
        }

        Date now = new Date();
        JSONObject confirmMembers = parseConfirmMembers(detail.getConfirmMembers());
        String confirmRoleKey = resolveConfirmRoleKey(confirmMembers, roleKey, userId);
        JSONObject confirmation = confirmMembers.getJSONObject(confirmRoleKey);
        confirmation.put("confirmed", true);
        confirmation.put("confirmedAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
        confirmMembers.put(confirmRoleKey, confirmation);

        detail.setConfirmMembers(confirmMembers.toJSONString());
        detail.setUpdatedBy(userId);
        detail.setUpdatedAt(now);
        constructionDrawingMapper.updateDetailById(detail);
        if (isAllSelectedConfirmed(confirmMembers)) {
            constructionDrawing.setStatus(STATUS_ARCHIVED);
        }
        constructionDrawing.setUpdatedBy(userId);
        constructionDrawing.setUpdatedAt(now);
        constructionDrawingMapper.updateById(constructionDrawing);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData cancelArchive(Long id, Long userId) {
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        ConstructionDrawing constructionDrawing = constructionDrawingMapper.selectById(id);
        if (constructionDrawing == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工图出图不存在");
        }
        if (!Integer.valueOf(STATUS_ARCHIVED).equals(constructionDrawing.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有已归档的施工图可以取消归档");
        }
        Date now = new Date();
        ConstructionDrawingDetail detail = constructionDrawingMapper.queryLatestDetail(id);
        if (detail != null) {
            detail.setConfirmMembers(resetConfirmMembers(detail.getConfirmMembers()));
            detail.setUpdatedBy(userId);
            detail.setUpdatedAt(now);
            constructionDrawingMapper.updateDetailById(detail);
        }
        constructionDrawing.setStatus(STATUS_DRAFT);
        constructionDrawing.setUpdatedBy(userId);
        constructionDrawing.setUpdatedAt(now);
        constructionDrawingMapper.updateById(constructionDrawing);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData delete(Long id) {
        ConstructionDrawing constructionDrawing = constructionDrawingMapper.selectById(id);
        if (constructionDrawing == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工图出图不存在");
        }
        if (Integer.valueOf(STATUS_ARCHIVED).equals(constructionDrawing.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已归档的施工图不可以删除");
        }
        constructionDrawingMapper.deleteDetailsByConstructionDrawingId(id);
        constructionDrawingMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 通过项目ID查询最新施工图出图记录
     */
    private ConstructionDrawing findByProjectId(Long projectId) {
        LambdaQueryWrapper<ConstructionDrawing> qw = new LambdaQueryWrapper<>();
        qw.eq(ConstructionDrawing::getProjectId, projectId).orderByDesc(ConstructionDrawing::getUpdatedAt);
        List<ConstructionDrawing> list = constructionDrawingMapper.selectList(qw);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 校验当前用户ID
     */
    private ResultData checkUserId(Long userId) {
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        return null;
    }

    /**
     * 校验施工图出图保存参数
     */
    private ResultData verify(ConstructionDrawing constructionDrawing) {
        if (constructionDrawing == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工图出图不能为空");
        }
        if (constructionDrawing.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择项目");
        }
        if (StringUtils.isEmpty(constructionDrawing.getVersion())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写版本");
        }
        if (isBlankAttachment(constructionDrawing.getArchitectureAttachments())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请上传建筑设计附件");
        }
        if (isBlankAttachment(constructionDrawing.getStructureAttachments())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请上传结构设计附件");
        }
        if (isBlankAttachment(constructionDrawing.getDecorationAttachments())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请上传精装图纸附件");
        }
        if (isBlankAttachment(constructionDrawing.getProcurementAttachments())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请上传主材及软装采购方案附件");
        }
        return null;
    }

    /**
     * 构建施工图出图版本明细
     */
    private ConstructionDrawingDetail buildDetail(ConstructionDrawing constructionDrawing, Long constructionDrawingId, Long userId, Date now) {
        ConstructionDrawingDetail detail = new ConstructionDrawingDetail();
        detail.setConstructionDrawingId(constructionDrawingId);
        detail.setVersion(parseVersion(constructionDrawing.getVersion()));
        detail.setArchitectureAttachments(constructionDrawing.getArchitectureAttachments());
        detail.setStructureAttachments(constructionDrawing.getStructureAttachments());
        detail.setDecorationAttachments(constructionDrawing.getDecorationAttachments());
        detail.setProcurementAttachments(constructionDrawing.getProcurementAttachments());
        detail.setSolutionExplanation(constructionDrawing.getSolutionExplanation());
        detail.setConfirmMembers(StringUtils.isEmpty(constructionDrawing.getConfirmMembers()) ? defaultConfirmMembers() : constructionDrawing.getConfirmMembers());
        detail.setCreatedBy(userId);
        detail.setUpdatedBy(userId);
        detail.setCreatedAt(now);
        detail.setUpdatedAt(now);
        return detail;
    }

    /**
     * 判断是否更新最新版本明细
     */
    private boolean shouldUpdateLatestDetail(ConstructionDrawingDetail latestDetail, ConstructionDrawingDetail detail) {
        return latestDetail.getVersion() != null
                && latestDetail.getVersion().equals(detail.getVersion());
    }

    /**
     * 解析版本号
     */
    private Integer parseVersion(String version) {
        if (StringUtils.isEmpty(version)) {
            return 1;
        }
        String value = version.trim().replaceAll("^[vV]", "");
        int dotIndex = value.indexOf(".");
        if (dotIndex > -1) {
            value = value.substring(0, dotIndex);
        }
        value = value.replaceAll("[^0-9]", "");
        if (StringUtils.isEmpty(value)) {
            return 1;
        }
        return Integer.valueOf(value);
    }

    /**
     * 判断附件内容是否为空
     */
    private boolean isBlankAttachment(String value) {
        return StringUtils.isEmpty(value) || "[]".equals(value.trim());
    }

    /**
     * 构建默认确认成员信息
     */
    private String defaultConfirmMembers() {
        JSONObject data = new JSONObject();
        data.put("owner", confirmation(false, ""));
        data.put("builder", confirmation(false, ""));
        return data.toJSONString();
    }

    /**
     * 解析确认成员信息
     */
    private JSONObject parseConfirmMembers(String value) {
        if (StringUtils.isEmpty(value)) {
            return JSON.parseObject(defaultConfirmMembers());
        }
        try {
            JSONObject data = JSON.parseObject(value);
            if (data == null) {
                return JSON.parseObject(defaultConfirmMembers());
            }
            if (data.getJSONObject("owner") == null) {
                data.put("owner", confirmation(false, ""));
            }
            if (data.getJSONObject("builder") == null) {
                data.put("builder", confirmation(false, ""));
            }
            return data;
        } catch (Exception e) {
            return JSON.parseObject(defaultConfirmMembers());
        }
    }

    /**
     * 重置确认成员状态
     */
    private String resetConfirmMembers(String value) {
        JSONObject data = parseConfirmMembers(value);
        resetConfirmation(data, "owner");
        resetConfirmation(data, "builder");
        return data.toJSONString();
    }

    /**
     * 重置指定角色确认状态
     */
    private void resetConfirmation(JSONObject data, String roleKey) {
        JSONObject item = data.getJSONObject(roleKey);
        if (item == null) {
            item = confirmation(false, "");
        }
        item.put("confirmed", false);
        item.put("confirmedAt", "");
        data.put(roleKey, item);
    }

    /**
     * 构建确认状态对象
     */
    private JSONObject confirmation(boolean confirmed, String confirmedAt) {
        JSONObject item = new JSONObject();
        item.put("confirmed", confirmed);
        item.put("confirmedAt", confirmedAt);
        return item;
    }

    /**
     * 判断指定角色是否已确认
     */
    private boolean isConfirmed(JSONObject confirmMembers, String roleKey) {
        JSONObject confirmation = confirmMembers.getJSONObject(roleKey);
        return confirmation != null && Boolean.TRUE.equals(confirmation.getBoolean("confirmed"));
    }

    /**
     * 判断已选择的确认成员是否都已确认
     */
    private boolean isAllSelectedConfirmed(JSONObject confirmMembers) {
        boolean hasSelectedMember = false;
        for (String roleKey : new String[]{"owner", "builder"}) {
            JSONObject confirmation = confirmMembers.getJSONObject(roleKey);
            if (confirmation == null || !hasSelectedMember(confirmation)) {
                continue;
            }
            hasSelectedMember = true;
            if (!Boolean.TRUE.equals(confirmation.getBoolean("confirmed"))) {
                return false;
            }
        }
        return hasSelectedMember;
    }

    /**
     * 判断确认成员是否已选择
     */
    private boolean hasSelectedMember(JSONObject confirmation) {
        return StringUtils.hasText(confirmation.getString("memberId"))
                || StringUtils.hasText(confirmation.getString("userId"))
                || StringUtils.hasText(confirmation.getString("name"));
    }

    /**
     * 兼容确认成员槽位与当前角色不一致的历史数据
     */
    private String resolveConfirmRoleKey(JSONObject confirmMembers, String roleKey, Long userId) {
        JSONObject roleConfirmation = confirmMembers.getJSONObject(roleKey);
        if (roleConfirmation != null && hasSelectedMember(roleConfirmation)) {
            return roleKey;
        }

        String userIdText = String.valueOf(userId);
        for (String itemRoleKey : new String[]{"owner", "builder"}) {
            JSONObject confirmation = confirmMembers.getJSONObject(itemRoleKey);
            if (confirmation != null
                    && hasSelectedMember(confirmation)
                    && userIdText.equals(String.valueOf(confirmation.get("userId")))) {
                return itemRoleKey;
            }
        }
        return roleKey;
    }
}
