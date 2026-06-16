package com.seagox.lowcode.business.verify;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.business.template.LeaveRequestModel;
import com.seagox.lowcode.system.entity.Company;
import com.seagox.lowcode.system.entity.DeptUser;
import com.seagox.lowcode.system.entity.SysAccount;
import com.seagox.lowcode.system.mapper.AccountMapper;
import com.seagox.lowcode.system.mapper.CompanyMapper;
import com.seagox.lowcode.system.mapper.DeptUserMapper;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

/**
 * 请假单导入验证
 */
@Component
public class LeaveRequestExcelVerifyHandler implements IExcelVerifyHandler<LeaveRequestModel> {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private DeptUserMapper deptUserMapper;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(LeaveRequestModel obj) {
        if (StringUtils.isEmpty(obj.getCompanyName())) {
            return error("单位名称不能为空");
        }
        LambdaQueryWrapper<Company> companyQw = new LambdaQueryWrapper<>();
        companyQw.eq(Company::getName, obj.getCompanyName());
        Company company = companyMapper.selectOne(companyQw);
        if (company == null) {
            return error("所在单位不存在");
        }

        if (StringUtils.isEmpty(obj.getApplicantAccount())) {
            return error("申请人账号不能为空");
        }
        LambdaQueryWrapper<SysAccount> accountQw = new LambdaQueryWrapper<>();
        accountQw.eq(SysAccount::getAccount, obj.getApplicantAccount());
        SysAccount applicant = accountMapper.selectOne(accountQw);
        if (applicant == null) {
            return error("申请人账号不存在");
        }
        LambdaQueryWrapper<DeptUser> deptUserQw = new LambdaQueryWrapper<>();
        deptUserQw.eq(DeptUser::getCompanyId, company.getId()).eq(DeptUser::getUserId, applicant.getId());
        Long deptUserCount = deptUserMapper.selectCount(deptUserQw);
        if (deptUserCount == 0) {
            return error("申请人不属于该单位");
        }
        if (!StringUtils.isEmpty(obj.getApplicantName()) && !obj.getApplicantName().equals(applicant.getName())) {
            return error("申请人姓名与账号不匹配");
        }

        Date startTime = parseDate(obj.getStartTime());
        if (startTime == null) {
            return error("开始时间格式应为" + DATE_PATTERN);
        }
        Date endTime = parseDate(obj.getEndTime());
        if (endTime == null) {
            return error("结束时间格式应为" + DATE_PATTERN);
        }
        if (endTime.before(startTime)) {
            return error("结束时间不能早于开始时间");
        }
        if (obj.getDuration() == null || obj.getDuration().compareTo(BigDecimal.ZERO) <= 0) {
            return error("请假时长必须大于0");
        }
        if (StringUtils.isEmpty(obj.getReason())) {
            return error("请假事由不能为空");
        }
        return new ExcelVerifyHandlerResult(true);
    }

    private Date parseDate(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        simpleDateFormat.setLenient(false);
        try {
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    private ExcelVerifyHandlerResult error(String message) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(false);
        result.setMsg(message);
        return result;
    }

}
