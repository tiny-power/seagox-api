package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.DesignCase;
import com.seagox.lowcode.business.service.IDesignCaseService;
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
 * 案例库
 */
@RestController
@RequestMapping("/designCase")
public class DesignCaseController {

    /**
     * 案例库服务
     */
    @Autowired
    private IDesignCaseService designCaseService;

    /**
     * 分页查询案例库
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
        return designCaseService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 查询案例详情
     *
     * @param id 案例ID
     * @return 查询结果
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return designCaseService.queryById(id);
    }

    /**
     * 新增案例
     *
     * @param designCase 案例
     * @param userId 当前用户ID
     * @return 保存结果
     */
    @PostMapping("/insert")
    @LogPoint("新增案例")
    public ResultData insert(DesignCase designCase, Long userId) {
        return designCaseService.insert(designCase, userId);
    }

    /**
     * 更新案例
     *
     * @param designCase 案例
     * @param userId 当前用户ID
     * @return 更新结果
     */
    @PostMapping("/update")
    @LogPoint("更新案例")
    public ResultData update(DesignCase designCase, Long userId) {
        return designCaseService.update(designCase, userId);
    }

    /**
     * 删除案例
     *
     * @param id 案例ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除案例")
    public ResultData delete(@PathVariable Long id) {
        return designCaseService.delete(id);
    }
}
