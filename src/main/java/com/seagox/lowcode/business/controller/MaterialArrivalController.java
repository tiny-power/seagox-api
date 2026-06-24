package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.MaterialArrival;
import com.seagox.lowcode.business.service.IMaterialArrivalService;
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
 * 材料到场记录
 */
@RestController
@RequestMapping("/materialArrival")
public class MaterialArrivalController {

    /**
     * 材料到场记录服务
     */
    @Autowired
    private IMaterialArrivalService materialArrivalService;

    /**
     * 分页查询材料到场记录
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
        return materialArrivalService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 查询材料到场记录详情
     *
     * @param id 材料到场记录ID
     * @return 查询结果
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return materialArrivalService.queryById(id);
    }

    /**
     * 新增材料到场记录
     *
     * @param materialArrival 材料到场记录
     * @param userId 当前用户ID
     * @return 保存结果
     */
    @PostMapping("/insert")
    @LogPoint("新增材料到场记录")
    public ResultData insert(MaterialArrival materialArrival, Long userId) {
        return materialArrivalService.insert(materialArrival, userId);
    }

    /**
     * 删除材料到场记录
     *
     * @param id 材料到场记录ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除材料到场记录")
    public ResultData delete(@PathVariable Long id) {
        return materialArrivalService.delete(id);
    }
}
