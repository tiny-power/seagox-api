package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.Knowledge;
import com.seagox.lowcode.business.service.IKnowledgeService;
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
 * 科普内容
 */
@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    /**
     * 科普内容服务
     */
    @Autowired
    private IKnowledgeService knowledgeService;

    /**
     * 分页查询科普内容
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
        return knowledgeService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 查询上架科普内容
     *
     * @param params 查询条件
     * @return 查询结果
     */
    @GetMapping("/queryList")
    public ResultData queryList(@RequestParam Map<String, Object> params) {
        return knowledgeService.queryList(params);
    }

    /**
     * 查询科普内容详情
     *
     * @param id 科普内容ID
     * @return 查询结果
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return knowledgeService.queryById(id);
    }

    /**
     * 新增科普内容
     *
     * @param knowledge 科普内容
     * @return 保存结果
     */
    @PostMapping("/insert")
    @LogPoint("新增科普内容")
    public ResultData insert(Knowledge knowledge) {
        return knowledgeService.insert(knowledge);
    }

    /**
     * 更新科普内容
     *
     * @param knowledge 科普内容
     * @return 更新结果
     */
    @PostMapping("/update")
    @LogPoint("更新科普内容")
    public ResultData update(Knowledge knowledge) {
        return knowledgeService.update(knowledge);
    }

    /**
     * 删除科普内容
     *
     * @param id 科普内容ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除科普内容")
    public ResultData delete(@PathVariable Long id) {
        return knowledgeService.delete(id);
    }
}
