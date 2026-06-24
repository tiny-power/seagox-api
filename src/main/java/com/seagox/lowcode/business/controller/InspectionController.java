package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.Inspection;
import com.seagox.lowcode.business.service.IInspectionService;
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
 * 验收单
 */
@RestController
@RequestMapping("/inspection")
public class InspectionController {

    /**
     * 验收单服务
     */
    @Autowired
    private IInspectionService inspectionService;

    /**
     * 分页查询验收单
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
        return inspectionService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 查询验收单详情
     *
     * @param id 验收单ID
     * @return 查询结果
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return inspectionService.queryById(id);
    }

    /**
     * 新增验收单
     *
     * @param inspection 验收单
     * @param userId 当前用户ID
     * @return 操作结果
     */
    @PostMapping("/insert")
    @LogPoint("新增验收单")
    public ResultData insert(Inspection inspection, Long userId) {
        return inspectionService.insert(inspection, userId);
    }

    /**
     * 修改验收单
     *
     * @param inspection 验收单
     * @param userId 当前用户ID
     * @return 操作结果
     */
    @PostMapping("/update")
    @LogPoint("修改验收单")
    public ResultData update(Inspection inspection, Long userId) {
        return inspectionService.update(inspection, userId);
    }

    /**
     * 完成验收单
     *
     * @param id 验收单ID
     * @param inspection 验收单
     * @param userId 当前用户ID
     * @return 操作结果
     */
    @PostMapping("/complete/{id}")
    @LogPoint("完成验收单")
    public ResultData complete(@PathVariable Long id, Inspection inspection, Long userId) {
        inspection.setId(id);
        return inspectionService.complete(inspection, userId);
    }

    /**
     * 删除验收单
     *
     * @param id 验收单ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除验收单")
    public ResultData delete(@PathVariable Long id) {
        return inspectionService.delete(id);
    }
}
