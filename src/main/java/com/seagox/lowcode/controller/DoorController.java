package com.seagox.lowcode.controller;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Door;
import com.seagox.lowcode.service.IDoorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 门户
 */
@RestController
@RequestMapping("/door")
public class DoorController {

    @Autowired
    private IDoorService doorService;

    /**
     * 分页查询
     *
     * @param pageNo    起始页
     * @param pageSize  每页大小
     * @param companyId 公司id
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId) {
        return doorService.queryByPage(pageNo, pageSize, companyId);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    public ResultData insert(@Valid Door door) {
        return doorService.insert(door);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public ResultData update(@Valid Door door) {
        return doorService.update(door);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    public ResultData delete(@PathVariable Long id) {
        return doorService.delete(id);
    }

    /**
     * 信息
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id, Long userId) {
        return doorService.queryById(id, userId);
    }

    /**
     * 统计分析
     */
    @GetMapping("/queryAnalysis")
    public ResultData queryAnalysis(Long companyId, Long userId) {
        return doorService.queryAnalysis(companyId, userId);
    }
    
}
