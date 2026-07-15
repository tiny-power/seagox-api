package com.seagox.lowcode.system.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.Disk;
import com.seagox.lowcode.system.service.IDiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 网盘
 */
@RestController
@RequestMapping("/disk")
public class DiskController {

    @Autowired
    private IDiskService diskService;

    /**
     * 查询当前目录内容
     */
    @GetMapping("/queryChildren")
    public ResultData queryChildren(Long companyId, Long parentId, String keyword) {
        return diskService.queryChildren(companyId, parentId, keyword);
    }

    /**
     * 预览压缩包目录
     */
    @GetMapping("/previewArchive")
    public ResultData previewArchive(Long companyId, Long id) {
        return diskService.previewArchive(companyId, id);
    }

    /**
     * 新增文件夹
     */
    @PostMapping("/insertFolder")
    @LogPoint("新增文件夹")
    public ResultData insertFolder(Long companyId, Long userId, Disk disk) {
        return diskService.insertFolder(companyId, userId, disk);
    }

    /**
     * 新增文件
     */
    @PostMapping("/insertFile")
    @LogPoint("新增文件")
    public ResultData insertFile(Long companyId, Long userId, Disk disk) {
        return diskService.insertFile(companyId, userId, disk);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @LogPoint("更新网盘")
    public ResultData update(Long companyId, Long userId, Disk disk) {
        return diskService.update(companyId, userId, disk);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除网盘")
    public ResultData delete(@PathVariable Long id, Long companyId) {
        return diskService.delete(companyId, id);
    }

}
