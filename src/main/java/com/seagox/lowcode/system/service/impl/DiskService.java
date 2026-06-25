package com.seagox.lowcode.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.Disk;
import com.seagox.lowcode.system.mapper.DiskMapper;
import com.seagox.lowcode.system.service.IDiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 网盘
 */
@Service
public class DiskService implements IDiskService {

    @Autowired
    private DiskMapper diskMapper;

    @Override
    public ResultData queryChildren(Long companyId, Long parentId, String keyword) {
        LambdaQueryWrapper<Disk> qw = new LambdaQueryWrapper<>();
        qw.eq(Disk::getCompanyId, companyId);
        if (!StringUtils.isEmpty(keyword)) {
            qw.like(Disk::getName, keyword);
        } else if (parentId == null) {
            qw.isNull(Disk::getParentId);
        } else {
            qw.eq(Disk::getParentId, parentId);
        }
        qw.orderByAsc(Disk::getType).orderByDesc(Disk::getUpdateTime);
        List<Disk> list = diskMapper.selectList(qw);
        return ResultData.success(list);
    }

    @Transactional
    @Override
    public ResultData insertFolder(Long companyId, Disk disk) {
        disk.setCompanyId(companyId);
        disk.setType(1);
        disk.setUrl("");
        disk.setSize(0L);
        ResultData checkResult = checkName(companyId, disk.getParentId(), disk.getName(), null);
        if (checkResult != null) {
            return checkResult;
        }
        Disk parent = getParent(companyId, disk.getParentId());
        if (disk.getParentId() != null && parent == null) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "上级目录不存在");
        }
        disk.setLevel(parent == null ? 1 : parent.getLevel() + 1);
        disk.setPath("/");
        diskMapper.insert(disk);
        disk.setPath(buildPath(parent, disk.getId()));
        diskMapper.updateById(disk);
        return ResultData.success(disk);
    }

    @Transactional
    @Override
    public ResultData insertFile(Long companyId, Disk disk) {
        disk.setCompanyId(companyId);
        ResultData checkResult = checkName(companyId, disk.getParentId(), disk.getName(), null);
        if (checkResult != null) {
            return checkResult;
        }
        Disk parent = getParent(companyId, disk.getParentId());
        if (disk.getParentId() != null && parent == null) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "上级目录不存在");
        }
        if (StringUtils.isEmpty(disk.getUrl())) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "文件地址不能为空");
        }
        if (disk.getType() == null || disk.getType() == 1) {
            disk.setType(getFileType(disk.getName()));
        }
        if (disk.getSize() == null) {
            disk.setSize(0L);
        }
        disk.setLevel(parent == null ? 1 : parent.getLevel() + 1);
        disk.setPath("/");
        diskMapper.insert(disk);
        disk.setPath(buildPath(parent, disk.getId()));
        diskMapper.updateById(disk);
        return ResultData.success(disk);
    }

    @Transactional
    @Override
    public ResultData update(Long companyId, Disk disk) {
        Disk original = diskMapper.selectById(disk.getId());
        if (original == null || !companyId.equals(original.getCompanyId())) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "文件不存在");
        }
        ResultData checkResult = checkName(companyId, original.getParentId(), disk.getName(), disk.getId());
        if (checkResult != null) {
            return checkResult;
        }
        original.setName(disk.getName());
        diskMapper.updateById(original);
        return ResultData.success(original);
    }

    @Transactional
    @Override
    public ResultData delete(Long companyId, Long id) {
        Disk disk = diskMapper.selectById(id);
        if (disk == null || !companyId.equals(disk.getCompanyId())) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "文件不存在");
        }
        if (Integer.valueOf(1).equals(disk.getType())) {
            LambdaQueryWrapper<Disk> qw = new LambdaQueryWrapper<>();
            qw.eq(Disk::getCompanyId, companyId).likeRight(Disk::getPath, disk.getPath());
            diskMapper.delete(qw);
            return ResultData.success(null);
        }
        diskMapper.deleteById(id);
        return ResultData.success(null);
    }

    private Disk getParent(Long companyId, Long parentId) {
        if (parentId == null) {
            return null;
        }
        Disk parent = diskMapper.selectById(parentId);
        if (parent == null || !companyId.equals(parent.getCompanyId()) || !Integer.valueOf(1).equals(parent.getType())) {
            return null;
        }
        return parent;
    }

    private ResultData checkName(Long companyId, Long parentId, String name, Long id) {
        if (StringUtils.isEmpty(name)) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "名称不能为空");
        }
        LambdaQueryWrapper<Disk> qw = new LambdaQueryWrapper<>();
        qw.eq(Disk::getCompanyId, companyId).eq(Disk::getName, name);
        if (parentId == null) {
            qw.isNull(Disk::getParentId);
        } else {
            qw.eq(Disk::getParentId, parentId);
        }
        if (id != null) {
            qw.ne(Disk::getId, id);
        }
        Long count = diskMapper.selectCount(qw);
        if (count > 0) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "同级目录下名称已经存在");
        }
        return null;
    }

    private String buildPath(Disk parent, Long id) {
        if (parent == null) {
            return "/" + id + "/";
        }
        return parent.getPath() + id + "/";
    }

    private Integer getFileType(String filename) {
        if (StringUtils.isEmpty(filename) || filename.lastIndexOf(".") == -1) {
            return 11;
        }
        String suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        if (Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp").contains(suffix)) {
            return 2;
        }
        if (Arrays.asList("doc", "docx").contains(suffix)) {
            return 3;
        }
        if (Arrays.asList("xls", "xlsx").contains(suffix)) {
            return 4;
        }
        if (Arrays.asList("ppt", "pptx").contains(suffix)) {
            return 5;
        }
        if ("pdf".equals(suffix)) {
            return 6;
        }
        if (Arrays.asList("zip", "rar", "7z", "tar", "gz").contains(suffix)) {
            return 7;
        }
        if ("txt".equals(suffix)) {
            return 8;
        }
        if (Arrays.asList("md", "rtf", "ofd").contains(suffix)) {
            return 9;
        }
        if (Arrays.asList("mp4", "avi", "mov", "wmv", "mkv").contains(suffix)) {
            return 10;
        }
        return 11;
    }

}
