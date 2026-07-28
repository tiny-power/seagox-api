package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.ProjectChangeOrder;
import com.seagox.lowcode.business.service.IProjectChangeOrderService;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 签证单
 */
@RestController
@RequestMapping("/projectChangeOrder")
public class ProjectChangeOrderController {

    /**
     * 签证单服务
     */
    @Autowired
    private IProjectChangeOrderService projectChangeOrderService;

    /**
     * 分页查询签证单
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
        return projectChangeOrderService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 查询签证单详情
     *
     * @param id 签证单ID
     * @return 查询结果
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id, @RequestParam Map<String, Object> params) {
        return projectChangeOrderService.queryById(id, params);
    }

    /**
     * 新增签证单
     *
     * @param projectChangeOrder 签证单
     * @return 操作结果
     */
    @PostMapping("/insert")
    @LogPoint("新增签证单")
    public ResultData insert(@Valid ProjectChangeOrder projectChangeOrder) {
        return projectChangeOrderService.insert(projectChangeOrder);
    }

    /**
     * 修改签证单
     *
     * @param projectChangeOrder 签证单
     * @return 操作结果
     */
    @PostMapping("/update")
    @LogPoint("修改签证单")
    public ResultData update(@Valid ProjectChangeOrder projectChangeOrder) {
        return projectChangeOrderService.update(projectChangeOrder);
    }

    /**
     * 删除签证单
     *
     * @param id 签证单ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除签证单")
    public ResultData delete(@PathVariable Long id) {
        return projectChangeOrderService.delete(id);
    }

    /**
     * 提交签证单
     *
     * @param id 签证单ID
     * @return 操作结果
     */
    @PostMapping("/submit/{id}")
    @LogPoint("提交签证单")
    public ResultData submit(@PathVariable Long id) {
        return projectChangeOrderService.submit(id);
    }

    /**
     * 撤销签证单
     *
     * @param id 签证单ID
     * @return 操作结果
     */
    @PostMapping("/cancel/{id}")
    @LogPoint("撤销签证单")
    public ResultData cancel(@PathVariable Long id) {
        return projectChangeOrderService.cancel(id);
    }
}
