package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Job;

public interface IJobService {

    /**
     * 启动任务
     *
     * @param id
     */
    public void startJob(Long id);

    /**
     * 暂停任务
     *
     * @param id
     */
    public void stopJob(Long id);


    /**
     * 分页查询
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, String name);

    /**
     * 添加
     */
    public ResultData insert(Job job);

    /**
     * 修改
     */
    public ResultData update(Job job);

    /**
     * 删除
     */
    public ResultData delete(Long id);
    
    /**
     * 验证cron
     */
    public ResultData valid(String cron);

}
