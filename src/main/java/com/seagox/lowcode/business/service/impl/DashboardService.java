package com.seagox.lowcode.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.business.entity.ConstructionLog;
import com.seagox.lowcode.business.entity.Inspection;
import com.seagox.lowcode.business.entity.IssueTicket;
import com.seagox.lowcode.business.entity.PaymentRequest;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.ProjectMember;
import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.mapper.ConstructionLogMapper;
import com.seagox.lowcode.business.mapper.InspectionMapper;
import com.seagox.lowcode.business.mapper.IssueTicketMapper;
import com.seagox.lowcode.business.mapper.PaymentRequestMapper;
import com.seagox.lowcode.business.mapper.ProjectMemberMapper;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectStageMapper;
import com.seagox.lowcode.business.service.IDashboardService;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.SysMessage;
import com.seagox.lowcode.system.entity.SysAccount;
import com.seagox.lowcode.system.mapper.AccountMapper;
import com.seagox.lowcode.system.mapper.FlowMapper;
import com.seagox.lowcode.system.mapper.MessageMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 首页看板服务
 */
@Service
public class DashboardService implements IDashboardService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectStageMapper stageMapper;

    @Autowired
    private ProjectMemberMapper memberMapper;

    @Autowired
    private ConstructionLogMapper constructionLogMapper;

    @Autowired
    private InspectionMapper inspectionMapper;

    @Autowired
    private IssueTicketMapper issueTicketMapper;

    @Autowired
    private PaymentRequestMapper paymentRequestMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private FlowMapper flowMapper;

    @Override
    public ResultData home(Long companyId, Long userId) {
        List<Project> projects = projectMapper.selectList(new LambdaQueryWrapper<Project>()
                .orderByDesc(Project::getCreatedAt));
        List<ProjectStage> stages = stageMapper.selectList(new LambdaQueryWrapper<ProjectStage>());
        List<Inspection> inspections = inspectionMapper.selectList(new LambdaQueryWrapper<Inspection>());

        Map<Long, Project> projectMap = new HashMap<>();
        for (Project project : projects) {
            projectMap.put(project.getId(), project);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("metrics", buildMetrics(projects, companyId, userId));
        data.put("health", buildHealth(projects));
        data.put("overdueWarnings", buildOverdueWarnings(inspections, projectMap));
        data.put("constructionLogReport", buildConstructionLogReport(projects, stages));
        data.put("reminders", buildReminders(projects, companyId, userId));
        return ResultData.success(data);
    }

    private List<Map<String, Object>> buildMetrics(List<Project> projects, Long companyId, Long userId) {
        Date monthStart = startOfMonth(0);
        Date lastMonthStart = startOfMonth(-1);
        Date lastMonthEnd = addDays(monthStart, -1);
        int active = 0;
        int activeLastMonth = 0;
        int monthNew = 0;
        int lastMonthNew = 0;
        int warning = 0;
        int warningLastMonth = 0;

        for (Project project : projects) {
            if (Integer.valueOf(2).equals(project.getStatus())) {
                active++;
            }
            if (Integer.valueOf(2).equals(project.getStatus()) && before(project.getCreatedAt(), monthStart)) {
                activeLastMonth++;
            }
            if (between(project.getCreatedAt(), monthStart, null)) {
                monthNew++;
            }
            if (between(project.getCreatedAt(), lastMonthStart, lastMonthEnd)) {
                lastMonthNew++;
            }
            if (isWarningProject(project)) {
                warning++;
            }
            if (isWarningProject(project) && before(project.getCreatedAt(), monthStart)) {
                warningLastMonth++;
            }
        }

        int approval = queryApprovalCount(companyId, userId);
        return listOf(
                metric("在建项目", active, "个", active - activeLastMonth, "building"),
                metric("本月新增", monthNew, "个", monthNew - lastMonthNew, "new"),
                metric("预警项目", warning, "个", warning - warningLastMonth, "warning"),
                metric("待审批事项", approval, "条", approval, "approval"));
    }

    private Map<String, Object> buildHealth(List<Project> projects) {
        int total = projects.size();
        int normal = 0;
        int warning = 0;
        int risk = 0;

        for (Project project : projects) {
            Integer healthStatus = project.getHealthStatus();
            if (Integer.valueOf(1).equals(healthStatus)) {
                normal++;
            } else if (Integer.valueOf(2).equals(healthStatus)) {
                warning++;
            } else {
                risk++;
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", listOf(
                healthItem("正常", normal, total, "#00b96b"),
                healthItem("预警", warning, total, "#f5a400"),
                healthItem("风险", risk, total, "#1264f6")));
        return data;
    }

    private List<Map<String, Object>> buildOverdueWarnings(List<Inspection> inspections, Map<Long, Project> projectMap) {
        Date now = new Date();
        Date sevenDaysAgo = addDays(now, -7);
        List<Inspection> rows = new ArrayList<>();
        for (Inspection inspection : inspections) {
            if (inspection.getPlanInspectionTime() != null
                    && !Integer.valueOf(3).equals(inspection.getStatus())
                    && !inspection.getPlanInspectionTime().after(now)
                    && !inspection.getPlanInspectionTime().before(sevenDaysAgo)) {
                rows.add(inspection);
            }
        }
        rows.sort(Comparator.comparing(Inspection::getPlanInspectionTime));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Inspection inspection : rows) {
            Project project = projectMap.get(inspection.getProjectId());
            Map<String, Object> item = new HashMap<>();
            item.put("projectName", projectName(project));
            item.put("nodeName", stageName(inspection.getStageId(), "待验收节点"));
            item.put("overdueDays", Math.max(daysBetween(inspection.getPlanInspectionTime(), now), 1));
            result.add(item);
            if (result.size() >= 3) {
                break;
            }
        }
        return result;
    }

    private Map<String, Object> buildConstructionLogReport(List<Project> projects, List<ProjectStage> stages) {
        Date start = startOfDay(addDays(new Date(), -6));
        Date end = startOfDay(new Date());
        Map<String, LogReporter> reporters = buildLogReporters(projects, stages);
        Map<String, Set<String>> timelyLogMap = buildTimelyLogMap(start, end);
        Set<String> logReporterKeys = buildLogReporterKeys(start, end);

        int expected = reporters.size() * 7;
        int submitted = 0;
        List<Map<String, Object>> lateItems = new ArrayList<>();
        for (LogReporter reporter : reporters.values()) {
            int lateDays = 0;
            String lastLateDate = "";
            for (int i = 0; i < 7; i++) {
                Date day = addDays(start, i);
                String dayKey = formatLogDay(day);
                Set<String> logDays = timelyLogMap.get(reporter.projectId + ":" + reporter.userId);
                if (logDays != null && logDays.contains(dayKey)) {
                    submitted++;
                } else {
                    lateDays++;
                    lastLateDate = dayKey;
                }
            }
            if (lateDays > 0) {
                Map<String, Object> item = new HashMap<>();
                item.put("projectName", reporter.projectName);
                item.put("userName", reporter.userName);
                item.put("roleName", reporter.roleName);
                item.put("lateDays", lateDays);
                item.put("lastLateDate", lastLateDate);
                item.put("submitted", 7 - lateDays);
                item.put("expected", 7);
                item.put("rate", percent(7 - lateDays, 7));
                item.put("statusText", logReporterKeys.contains(reporter.projectId + ":" + reporter.userId)
                        ? "填报不及时" : "未填报");
                lateItems.add(item);
            }
        }
        lateItems.sort((a, b) -> Integer.compare((Integer) b.get("lateDays"), (Integer) a.get("lateDays")));

        Map<String, Object> data = new HashMap<>();
        data.put("rate", expected == 0 ? 0 : percent(submitted, expected));
        data.put("submitted", submitted);
        data.put("expected", expected);
        data.put("lateItems", lateItems);
        return data;
    }

    private List<Map<String, Object>> buildReminders(List<Project> projects, Long companyId, Long userId) {
        int progressCount = 0;
        Date today = startOfDay(new Date());
        for (Project project : projects) {
            if (!Integer.valueOf(6).equals(project.getStatus()) && !Integer.valueOf(7).equals(project.getStatus())
                    && project.getPlannedEndDate() != null && !project.getPlannedEndDate().before(today)) {
                progressCount++;
            }
        }

        int issueCount = issueTicketMapper.selectCount(new LambdaQueryWrapper<IssueTicket>()
                .ne(IssueTicket::getStatus, 3)).intValue();
        int paymentApproval = paymentRequestMapper.selectCount(new LambdaQueryWrapper<PaymentRequest>()
                .eq(PaymentRequest::getStatus, 1)).intValue();
        int messageApproval = queryApprovalCount(companyId, userId);
        int todoCount = queryTodoCount(companyId, userId);

        return listOf(
                reminder("red", progressCount + " 个项目阶段验收"),
                reminder("orange", paymentApproval + " 条请款单提醒"),
                reminder("blue", messageApproval + " 条消息待处理"),
                reminder("gray", todoCount + " 条审批事项待处理"),
                reminder("green", issueCount + " 个问题单待闭环"));
    }

    private int queryTodoCount(Long companyId, Long userId) {
        if (companyId == null || userId == null) {
            return 0;
        }
        return flowMapper.queryTodoItem(companyId, String.valueOf(userId), null, null, null, null).size();
    }

    private Map<String, LogReporter> buildLogReporters(List<Project> projects, List<ProjectStage> stages) {
        Map<Long, Project> projectMap = new HashMap<>();
        Set<Long> activeProjectIds = new HashSet<>();
        for (Project project : projects) {
            projectMap.put(project.getId(), project);
            if (!Integer.valueOf(6).equals(project.getStatus()) && !Integer.valueOf(7).equals(project.getStatus())) {
                activeProjectIds.add(project.getId());
            }
        }

        Map<String, LogReporter> reporters = new LinkedHashMap<>();
        for (ProjectStage stage : stages) {
            if (stage.getProjectId() != null && activeProjectIds.contains(stage.getProjectId())
                    && stage.getManagerUserId() != null) {
                addLogReporter(reporters, projectMap.get(stage.getProjectId()), stage.getManagerUserId(), "项目经理");
            }
        }

        List<ProjectMember> members = memberMapper.selectList(new LambdaQueryWrapper<ProjectMember>()
                .in(ProjectMember::getRoleCode, 3, 4, 5)
                .eq(ProjectMember::getStatus, 1));
        for (ProjectMember member : members) {
            if (member.getProjectId() != null && activeProjectIds.contains(member.getProjectId())) {
                String roleName = Integer.valueOf(5).equals(member.getRoleCode()) ? "施工员" : "项目经理";
                addLogReporter(reporters, projectMap.get(member.getProjectId()), member.getUserId(), roleName);
            }
        }
        return reporters;
    }

    private void addLogReporter(Map<String, LogReporter> reporters, Project project, Long userId, String roleName) {
        if (project == null || userId == null) {
            return;
        }
        String key = project.getId() + ":" + userId + ":" + roleName;
        if (!reporters.containsKey(key)) {
            reporters.put(key, new LogReporter(project.getId(), projectName(project), userId, accountName(userId), roleName));
        }
    }

    private Map<String, Set<String>> buildTimelyLogMap(Date start, Date end) {
        Date nextDay = addDays(end, 1);
        List<ConstructionLog> logs = constructionLogMapper.selectList(new LambdaQueryWrapper<ConstructionLog>()
                .eq(ConstructionLog::getStatus, 1)
                .ge(ConstructionLog::getLogDate, start)
                .lt(ConstructionLog::getLogDate, nextDay));
        Map<String, Set<String>> timelyLogMap = new HashMap<>();
        for (ConstructionLog log : logs) {
            if (log.getProjectId() == null || log.getFilledBy() == null || log.getLogDate() == null
                    || log.getSubmittedAt() == null) {
                continue;
            }
            Date logDay = startOfDay(log.getLogDate());
            Date deadline = addDays(logDay, 1);
            if (log.getSubmittedAt().before(deadline)) {
                String key = log.getProjectId() + ":" + log.getFilledBy();
                if (!timelyLogMap.containsKey(key)) {
                    timelyLogMap.put(key, new HashSet<>());
                }
                timelyLogMap.get(key).add(formatLogDay(logDay));
            }
        }
        return timelyLogMap;
    }

    private Set<String> buildLogReporterKeys(Date start, Date end) {
        Date nextDay = addDays(end, 1);
        List<ConstructionLog> logs = constructionLogMapper.selectList(new LambdaQueryWrapper<ConstructionLog>()
                .eq(ConstructionLog::getStatus, 1)
                .ge(ConstructionLog::getLogDate, start)
                .lt(ConstructionLog::getLogDate, nextDay));
        Set<String> reporterKeys = new HashSet<>();
        for (ConstructionLog log : logs) {
            if (log.getProjectId() != null && log.getFilledBy() != null) {
                reporterKeys.add(log.getProjectId() + ":" + log.getFilledBy());
            }
        }
        return reporterKeys;
    }

    private String accountName(Long userId) {
        SysAccount account = accountMapper.selectById(userId);
        if (account == null) {
            return "未关联人员";
        }
        return account.getName() == null || account.getName().isEmpty() ? account.getAccount() : account.getName();
    }

    private int queryApprovalCount(Long companyId, Long userId) {
        if (companyId == null || userId == null) {
            return 0;
        }
        return messageMapper.selectCount(new LambdaQueryWrapper<SysMessage>()
                .eq(SysMessage::getCompanyId, companyId)
                .eq(SysMessage::getToUserId, userId)
                .eq(SysMessage::getStatus, 0)).intValue();
    }

    private boolean isWarningProject(Project project) {
        return project.getHealthStatus() != null && project.getHealthStatus() > 1;
    }

    private Map<String, Object> metric(String title, int value, String unit, int compare, String type) {
        Map<String, Object> item = new HashMap<>();
        item.put("title", title);
        item.put("value", value);
        item.put("unit", unit);
        item.put("compare", compare);
        item.put("type", type);
        return item;
    }

    private Map<String, Object> healthItem(String name, int value, int total, String color) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("value", value);
        item.put("percent", total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(value * 100L)
                .divide(BigDecimal.valueOf(total), 1, RoundingMode.HALF_UP));
        item.put("color", color);
        return item;
    }

    private Map<String, Object> reminder(String color, String text) {
        Map<String, Object> item = new HashMap<>();
        item.put("color", color);
        item.put("text", text);
        return item;
    }

    @SafeVarargs
    private final <T> List<T> listOf(T... items) {
        List<T> list = new ArrayList<>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    private String projectName(Project project) {
        return project == null ? "未关联项目" : project.getName();
    }

    private String stageName(Long stageId, String fallback) {
        if (stageId == null) {
            return fallback;
        }
        ProjectStage stage = stageMapper.selectById(stageId);
        return stage == null ? fallback : stage.getStageName();
    }

    private int percent(int numerator, int denominator) {
        if (denominator == 0) {
            return 0;
        }
        return BigDecimal.valueOf(numerator * 100L)
                .divide(BigDecimal.valueOf(denominator), 0, RoundingMode.HALF_UP)
                .intValue();
    }

    private Date startOfMonth(int monthOffset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthOffset);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date startOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    private boolean before(Date value, Date target) {
        return value != null && value.before(target);
    }

    private boolean between(Date value, Date start, Date end) {
        if (value == null) {
            return false;
        }
        return (start == null || !value.before(start)) && (end == null || !value.after(end));
    }

    private int daysBetween(Date start, Date end) {
        long diff = startOfDay(end).getTime() - startOfDay(start).getTime();
        return (int) (diff / (24L * 60L * 60L * 1000L));
    }

    private String formatLogDay(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private static class LogReporter {
        private final Long projectId;
        private final String projectName;
        private final Long userId;
        private final String userName;
        private final String roleName;

        private LogReporter(Long projectId, String projectName, Long userId, String userName, String roleName) {
            this.projectId = projectId;
            this.projectName = projectName;
            this.userId = userId;
            this.userName = userName;
            this.roleName = roleName;
        }
    }
}
