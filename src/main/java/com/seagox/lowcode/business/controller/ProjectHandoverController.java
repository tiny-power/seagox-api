package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.business.service.IProjectHandoverService;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交接单
 */
@RestController
@RequestMapping("/projectHandover")
public class ProjectHandoverController {

    /**
     * 交接单服务
     */
    @Autowired
    private IProjectHandoverService projectHandoverService;

    /**
     * 分页查询交接单
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
        return projectHandoverService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 查询交接单详情
     *
     * @param id 交接单ID
     * @return 查询结果
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return projectHandoverService.queryById(id);
    }
}
