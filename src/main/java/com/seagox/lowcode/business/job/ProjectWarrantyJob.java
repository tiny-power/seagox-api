package com.seagox.lowcode.business.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.system.job.JobHandler;
import com.seagox.lowcode.system.job.JobHandlerFactory;
import com.seagox.lowcode.util.SpringUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 项目保修到期自动完结任务
 */
@Component
public class ProjectWarrantyJob extends JobHandler {

    /**
     * 项目已交付
     */
    private static final int PROJECT_STATUS_DELIVERED = 4;

    /**
     * 项目已完结
     */
    private static final int PROJECT_STATUS_FINISHED = 5;

    /**
     * 系统用户
     */
    private static final long SYSTEM_USER_ID = 1L;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ProjectMapper projectMapper = SpringUtils.getBean(ProjectMapper.class);
        Date now = new Date();
        List<Project> projects = projectMapper.selectList(new LambdaQueryWrapper<Project>()
                .eq(Project::getStatus, PROJECT_STATUS_DELIVERED)
                .isNotNull(Project::getDeliveredAt));
        for (Project project : projects) {
            if (!isWarrantyExpired(project, now)) {
                continue;
            }
            project.setStatus(PROJECT_STATUS_FINISHED);
            project.setUpdatedBy(SYSTEM_USER_ID);
            project.setUpdatedAt(now);
            projectMapper.updateById(project);
        }
    }

    @Override
    public void afterPropertiesSet() {
        JobHandlerFactory.register(this.getClass().getName(), this);
    }

    private boolean isWarrantyExpired(Project project, Date now) {
        Integer warrantyMonths = project.getWarrantyMonths() == null ? 12 : project.getWarrantyMonths();
        Calendar expireAt = Calendar.getInstance();
        expireAt.setTime(project.getDeliveredAt());
        expireAt.add(Calendar.MONTH, warrantyMonths);
        return !expireAt.getTime().after(now);
    }
}
