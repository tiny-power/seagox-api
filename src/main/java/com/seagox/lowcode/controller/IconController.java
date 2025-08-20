package com.seagox.lowcode.controller;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.service.IIconService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * icon
 */
@RestController
@RequestMapping("icon")
public class IconController {
    
    @Autowired
    private IIconService iconService;

    /**
     * 分页查询
     *
     * @param pageNo   起始页
     * @param pageSize 每页大小
     * @param name     名称
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, String name) {
        return iconService.queryByPage(pageNo, pageSize, name);
    }
    
    /**
	 * 生成图标
	 */
	@GetMapping("/generate")
	public ResultData generate() {
		return iconService.generate();
	}
    
}
