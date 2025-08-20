package com.seagox.lowcode.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.entity.BusinessField;
import com.seagox.lowcode.entity.BusinessTable;
import com.seagox.lowcode.entity.Form;
import com.seagox.lowcode.entity.Job;
import com.seagox.lowcode.mapper.FormMapper;
import com.seagox.lowcode.mapper.JobMapper;
import com.seagox.lowcode.service.IBusinessFieldService;
import com.seagox.lowcode.service.IBusinessTableService;
import com.seagox.lowcode.util.SchedulerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private SchedulerUtils schedulerUtils;
    
    @Autowired
    private IBusinessTableService businessTableService;
    
    @Autowired
    private IBusinessFieldService businessFieldService;
    
    @Autowired
    private FormMapper formMapper;

    @Override
    public void run(String... args) throws Exception {
        //任务调度
        LambdaQueryWrapper<Job> qw = new LambdaQueryWrapper<>();
        qw.eq(Job::getStatus, 1);
        List<Job> jobList = jobMapper.selectList(qw);
        for (int i = 0; i < jobList.size(); i++) {
            Job job = jobList.get(i);
            schedulerUtils.start(String.valueOf(job.getId()), "seagox", job.getCron(), job.getMark());
        }
        matter();
    }
    
    public void matter() {
    	LambdaQueryWrapper<Form> qw = new LambdaQueryWrapper<>();
        qw.eq(Form::getMark, "matter");
    	Long count = formMapper.selectCount(qw);
    	if(count.equals(0L)) {
    		BusinessTable businessTable = new BusinessTable();
    		businessTable.setId(0L);
    		businessTable.setCompanyId(0L);
    		businessTable.setName("matter");
    		businessTable.setRemark("新建事项");
    		businessTable.setInitialize("company_id,user_id");
    		businessTableService.insert(businessTable);
    		
    		BusinessField titleField = new BusinessField();
    		titleField.setBusinessTableId(businessTable.getId());
    		titleField.setDecimals(0);
    		titleField.setDefaultValue(null);
    		titleField.setKind("text");
    		titleField.setLength(255);
    		titleField.setName("title");
    		titleField.setNotNull(1);
    		titleField.setRemark("标题");
    		titleField.setTargetTableId(null);
    		titleField.setType("varchar");
    		businessFieldService.insert(titleField);
    		
    		BusinessField attachmentField = new BusinessField();
    		attachmentField.setBusinessTableId(businessTable.getId());
    		attachmentField.setDecimals(0);
    		attachmentField.setDefaultValue(null);
    		attachmentField.setKind("upload");
    		attachmentField.setLength(30);
    		attachmentField.setName("attachment");
    		attachmentField.setNotNull(0);
    		attachmentField.setRemark("附件");
    		attachmentField.setTargetTableId(null);
    		attachmentField.setType("text");
    		businessFieldService.insert(attachmentField);
    		
    		BusinessField editorField = new BusinessField();
    		editorField.setBusinessTableId(businessTable.getId());
    		editorField.setDecimals(0);
    		editorField.setDefaultValue(null);
    		editorField.setKind("editor");
    		editorField.setLength(30);
    		editorField.setName("editor");
    		editorField.setNotNull(0);
    		editorField.setRemark("正文");
    		editorField.setTargetTableId(null);
    		editorField.setType("text");
    		businessFieldService.insert(editorField);
    		
    		BusinessField resourcesField = new BusinessField();
    		resourcesField.setBusinessTableId(businessTable.getId());
    		resourcesField.setDecimals(0);
    		resourcesField.setDefaultValue(null);
    		resourcesField.setKind("editor");
    		resourcesField.setLength(30);
    		resourcesField.setName("resources");
    		resourcesField.setNotNull(0);
    		resourcesField.setRemark("流程数据 ");
    		resourcesField.setTargetTableId(null);
    		resourcesField.setType("text");
    		businessFieldService.insert(resourcesField);
    		
    		Form form = new Form();
    		form.setCompanyId(0L);
    		form.setDataSource(businessTable.getId());
    		form.setMark("matter");
    		form.setName("新建事项");
    		form.setOptions("{\"import\":13,\"tableColumn\":[{\"summary\":\"0\",\"formatter\":\"\",\"total\":\"0\",\"prop\":\"title\",\"locking\":3,\"width\":0,\"id\":\"behf01\",\"label\":\"标题\",\"align\":\"left\",\"parentId\":\"\"}],\"searchColumn\":[],\"title\":\"title + '(' + user_id + ' ' + createTime + ')'\",\"freeProcess\":true,\"sql\":\"<select>\\n    select * from matter a  \\n    <where>\\n        <if test=\\\"companyStr != null and companyStr !=''\\\"> AND a.company_id IN (${companyStr})</if>\\n        <if test=\\\"userStr != null and userStr !=''\\\"> AND a.user_id IN (${userStr})</if>\\n        <if test=\\\"userId != null and userId !=''\\\"> AND a.user_id = #{userId}</if>\\n    </where>\\n</select>\"}");
    		form.setWorkbook("{\"row\":3,\"col\":6,\"data\":{\"name\":\"sheet2\",\"freeze\":\"A1\",\"styles\":[{\"align\":\"center\"},{\"align\":\"center\",\"bgcolor\":\"#a1a3a6\"},{\"align\":\"center\",\"font\":{\"bold\":true}}],\"merges\":[\"B1:F1\",\"B3:F3\",\"B2:F2\"],\"rows\":{\"0\":{\"cells\":{\"0\":{\"text\":\"标题 *\",\"style\":2},\"1\":{\"merge\":[0,4],\"style\":1,\"key\":\"wb22sw\",\"text\":\"输入框\"},\"2\":{\"style\":1,\"key\":\"wb22sw\"},\"3\":{\"style\":1,\"key\":\"wb22sw\"},\"4\":{\"style\":1,\"key\":\"wb22sw\"},\"5\":{\"style\":1,\"key\":\"wb22sw\"}}},\"1\":{\"cells\":{\"0\":{\"text\":\"附件\",\"style\":2},\"1\":{\"merge\":[0,4],\"style\":1,\"key\":\"oxqdd6\",\"text\":\"附件上传\"},\"2\":{\"style\":1,\"key\":\"oxqdd6\"},\"3\":{\"style\":1,\"key\":\"oxqdd6\"},\"4\":{\"style\":1,\"key\":\"oxqdd6\"},\"5\":{\"style\":1,\"key\":\"oxqdd6\"}}},\"2\":{\"cells\":{\"0\":{\"text\":\"正文\",\"style\":2},\"1\":{\"merge\":[0,4],\"style\":1,\"key\":\"178uks\",\"text\":\"富文本\"},\"2\":{\"style\":1,\"key\":\"178uks\"},\"3\":{\"style\":1,\"key\":\"178uks\"},\"4\":{\"style\":1,\"key\":\"178uks\"},\"5\":{\"style\":1,\"key\":\"178uks\"}},\"height\":218},\"len\":3},\"cols\":{\"len\":6},\"validations\":[],\"autofilter\":{}},\"module\":{\"wb22sw\":{\"type\":\"text\",\"field\":\"title\",\"label\":\"标题\",\"placeholder\":\"请输入标题\",\"required\":true,\"inputType\":\"text\",\"maxlength\":\"255\"},\"oxqdd6\":{\"type\":\"upload\",\"field\":\"attachment\",\"label\":\"附件\",\"placeholder\":\"请选择附件\",\"required\":false,\"accept\":[]},\"178uks\":{\"type\":\"editor\",\"field\":\"editor\",\"label\":\"正文\",\"placeholder\":\"请输入正文\",\"required\":false}}}");
    		formMapper.insert(form);
    	}
    	formMapper.updateZero();
	}
}
