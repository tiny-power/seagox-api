package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.ConstructionLog;
import com.seagox.lowcode.business.service.IConstructionLogService;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 施工日志
 */
@RestController
@RequestMapping("/constructionLog")
public class ConstructionLogController {

    /**
     * 施工日志服务
     */
    @Autowired
    private IConstructionLogService constructionLogService;

    /**
     * 分页查询施工日志
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam Map<String, Object> params) {
        return constructionLogService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 查询施工日志详情
     *
     * @param id 施工日志ID
     * @return 查询结果
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return constructionLogService.queryById(id);
    }

    /**
     * 新增施工日志
     *
     * @param constructionLog 施工日志
     * @param userId 当前用户ID
     * @return 操作结果
     */
    @PostMapping("/insert")
    @LogPoint("新增施工日志")
    public ResultData insert(ConstructionLog constructionLog, Long userId) {
        return constructionLogService.insert(constructionLog, userId);
    }

    /**
     * 修改施工日志
     *
     * @param constructionLog 施工日志
     * @param userId 当前用户ID
     * @return 操作结果
     */
    @PostMapping("/update")
    @LogPoint("修改施工日志")
    public ResultData update(ConstructionLog constructionLog, Long userId) {
        return constructionLogService.update(constructionLog, userId);
    }

    /**
     * 删除施工日志
     *
     * @param id 施工日志ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除施工日志")
    public ResultData delete(@PathVariable Long id) {
        return constructionLogService.delete(id);
    }
}
