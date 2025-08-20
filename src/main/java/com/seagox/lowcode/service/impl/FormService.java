package com.seagox.lowcode.service.impl;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seagox.lowcode.entity.*;
import com.seagox.lowcode.mapper.*;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.service.IFormService;
import com.seagox.lowcode.entity.SeaDefinition;
import com.seagox.lowcode.entity.SeaInstance;
import com.seagox.lowcode.mapper.SeaDefinitionMapper;
import com.seagox.lowcode.mapper.SeaInstanceMapper;
import com.seagox.lowcode.mapper.SeaNodeMapper;
import com.seagox.lowcode.service.IRuntimeService;
import com.seagox.lowcode.strategy.rule.RuleHandler;
import com.seagox.lowcode.strategy.rule.RuleHandlerFactory;
import com.seagox.lowcode.entity.SysAccount;
import com.seagox.lowcode.entity.Company;
import com.seagox.lowcode.entity.DeptUser;
import com.seagox.lowcode.entity.SysMessage;
import com.seagox.lowcode.mapper.CompanyMapper;
import com.seagox.lowcode.mapper.DepartmentMapper;
import com.seagox.lowcode.mapper.DeptUserMapper;
import com.seagox.lowcode.mapper.AccountMapper;
import com.seagox.lowcode.mapper.MessageMapper;
import com.seagox.lowcode.mapper.UserRoleMapper;
import com.seagox.lowcode.service.ICompanyService;
import com.seagox.lowcode.util.AviatorUtils;
import com.seagox.lowcode.util.ExcelStyleUtils;
import com.seagox.lowcode.util.ExcelUtils;
import com.seagox.lowcode.util.ExportUtils;
import com.seagox.lowcode.util.ImportResult;
import com.seagox.lowcode.util.JdbcTemplateUtils;
import com.seagox.lowcode.util.JsqlparserUtils;
import com.seagox.lowcode.util.TreeUtils;
import com.seagox.lowcode.util.XmlUtils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;

@Service
public class FormService implements IFormService {

	@Value(value = "${spring.datasource.url}")
	private String datasourceUrl;

	@Autowired
	private FormMapper formMapper;

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private AccountMapper userMapper;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private SeaDefinitionMapper seaDefinitionMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TableColumnConfigMapper tableColumnConfigMapper;

	@Autowired
	private ImportRuleMapper importRuleMapper;

	@Autowired
	private BusinessTableMapper businessTableMapper;

	@Autowired
	private BusinessFieldMapper businessFieldMapper;

	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private SeaInstanceMapper seaInstanceMapper;

	@Autowired
	private IRuntimeService runtimeService;

	@Autowired
	private JdbcTemplateUtils jdbcTemplateUtils;

	@Autowired
	private FormAthorityMapper formAthorityMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private SeaNodeMapper seaNodeMapper;

	@Autowired
	private DeptUserMapper deptUserMapper;

	@Autowired
	private SerialMapper serialMapper;

	@Value(value = "${jodconverter.working-dir}")
	private String workingDir;

	@Autowired
	private DicClassifyMapper dicClassifyMapper;

	@Override
	public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, String name) {
		PageHelper.startPage(pageNo, pageSize);
		LambdaQueryWrapper<Form> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Form::getCompanyId, companyId).like(!StringUtils.isEmpty(name), Form::getName, name)
				.orderByAsc(Form::getMark);
		List<Form> list = formMapper.selectList(queryWrapper);
		PageInfo<Form> pageInfo = new PageInfo<Form>(list);
		return ResultData.success(pageInfo);
	}

	@Override
	public ResultData queryByCompanyId(Long companyId) {
		LambdaQueryWrapper<Form> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Form::getCompanyId, companyId);
		List<Form> list = formMapper.selectList(queryWrapper);
		return ResultData.success(list);
	}

	@Transactional
	@Override
	public ResultData insert(Form form) {
		LambdaQueryWrapper<Form> qw = new LambdaQueryWrapper<>();
		qw.eq(Form::getCompanyId, form.getCompanyId()).eq(Form::getName, form.getName());
		Long count = formMapper.selectCount(qw);
		if (count == 0) {
			JSONObject options = JSONObject.parseObject(form.getOptions());
			String sql = options.getString("sql");
			JSONArray searchColumn = options.getJSONArray("searchColumn");
			try {
				options.put("sql", XmlUtils.asXML(sql, searchColumn, true));
			} catch (Exception e) {
				return ResultData.warn(ResultCode.OTHER_ERROR, "SQL自动生成错误");
			}
			form.setOptions(options.toJSONString());
			formMapper.insert(form);
			return ResultData.success(null);
		} else {
			return ResultData.warn(ResultCode.OTHER_ERROR, "名称已经存在");
		}
	}

	@Transactional
	@Override
	public ResultData update(Form form) {
		JSONObject options = JSON.parseObject(form.getOptions());
		String sql = options.getString("sql");
		Form originalForm = formMapper.selectById(form.getId());
		JSONObject originalOptions = JSON.parseObject(originalForm.getOptions());
		if (!options.getString("tableColumn").equals(originalOptions.getString("tableColumn"))) {
			LambdaQueryWrapper<TableColumnConfig> qw = new LambdaQueryWrapper<>();
			qw.eq(TableColumnConfig::getFormId, form.getId());
			tableColumnConfigMapper.delete(qw);
		}
		if (originalForm.getName().equals(form.getName())) {
			if (originalOptions.getString("sql").equals(sql)) {
				JSONArray searchColumn = options.getJSONArray("searchColumn");
				try {
					options.put("sql", XmlUtils.asXML(sql, searchColumn, false));
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return ResultData.warn(ResultCode.OTHER_ERROR, "SQL自动生成错误");
				}
				form.setOptions(options.toJSONString());
			}
			formMapper.updateById(form);
			return ResultData.success(null);
		} else {
			LambdaQueryWrapper<Form> qw = new LambdaQueryWrapper<>();
			qw.eq(Form::getCompanyId, form.getCompanyId()).eq(Form::getName, form.getName());
			Long count = formMapper.selectCount(qw);
			if (count == 0) {
				if (originalOptions.getString("sql").equals(sql)) {
					JSONArray searchColumn = options.getJSONArray("searchColumn");
					try {
						options.put("sql", XmlUtils.asXML(sql, searchColumn, false));
					} catch (Exception e) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return ResultData.warn(ResultCode.OTHER_ERROR, "SQL自动生成错误");
					}
					form.setOptions(options.toJSONString());
				}
				formMapper.updateById(form);
				return ResultData.success(null);
			} else {
				return ResultData.warn(ResultCode.OTHER_ERROR, "名称已经存在");
			}
		}
	}

	@Transactional
	@Override
	public ResultData delete(Long id) {
		formMapper.deleteById(id);
		LambdaQueryWrapper<SeaDefinition> qwSeaDefinition = new LambdaQueryWrapper<>();
		qwSeaDefinition.eq(SeaDefinition::getFormId, id);
		seaDefinitionMapper.delete(qwSeaDefinition);

		LambdaQueryWrapper<ImportRule> importRuleQw = new LambdaQueryWrapper<>();
		importRuleQw.eq(ImportRule::getFormId, id);
		importRuleMapper.delete(importRuleQw);

		return ResultData.success(null);
	}

	@Override
	public ResultData queryById(Long userId, Long id) {
		Form form = formMapper.selectById(id);
		return ResultData.success(form);
	}

	@Transactional
	@Override
	public ResultData insertCustom(Map<String, Object> request) {
		Form form = formMapper.selectById(request.get("_formId").toString());
		JSONObject options = JSON.parseObject(form.getOptions());
		JSONObject workbook = JSON.parseObject(form.getWorkbook());
		String type = request.get("_type").toString();
		if (type.equals("submit")) {
			List<String> failList = validFormData(workbook.getJSONObject("module"), request);
			if (failList.size() > 0) {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "参数验证不通过", failList);
			}
		}
		// 新增前规则
		RuleHandler eventService = RuleHandlerFactory.getHandler(form.getMark());
		if (eventService != null) {
			try {
				eventService.insertBefore(request);
			} catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
			}
		}
		// 逻辑处理
		String businessKey = insertLogic(request, form);
		request.put("businessKey", businessKey);
		// 判断是否有流程
		LambdaQueryWrapper<SeaDefinition> qw = new LambdaQueryWrapper<>();
		qw.eq(SeaDefinition::getFormId, form.getId());
		List<SeaDefinition> definitionList = seaDefinitionMapper.selectList(qw);
		if (definitionList.size() != 0) {
			// stage:暂存;submit:提交;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Map<String, Object> env = new HashMap<>(request);
			env.put("formName", form.getName());
			env.put("createTime", sdf.format(new Date()));

			LambdaQueryWrapper<BusinessField> businessFieldQw = new LambdaQueryWrapper<>();
			businessFieldQw.eq(BusinessField::getBusinessTableId, form.getDataSource()).in(BusinessField::getKind,
					Arrays.asList("company", "department", "member"));
			List<BusinessField> businessFieldList = businessFieldMapper.selectList(businessFieldQw);
			for (int i = 0; i < businessFieldList.size(); i++) {
				BusinessField businessField = businessFieldList.get(i);
				String kind = businessField.getKind();
				String field = businessField.getName();
				if (field.equals("user_id")) {
					SysAccount user = userMapper.selectById(env.get("userId").toString());
					if (user != null) {
						env.put(field, user.getName());
					}
				}
				if (env.containsKey(field)) {
					if (!StringUtils.isEmpty(env.get(field))) {
						if (kind.equals("company")) {
							Company company = companyMapper.selectById(env.get(field).toString());
							if (company != null) {
								env.put(field, company.getName());
							}
						} else if (kind.equals("department")) {
							Department department = departmentMapper.selectById(env.get(field).toString());
							if (department != null) {
								env.put(field, department.getName());
							}
						} else if (kind.equals("member")) {
							SysAccount user = userMapper.selectById(env.get(field).toString());
							if (user != null) {
								env.put(field, user.getName());
							}
						}
					}
				}
			}
			String title = AviatorUtils.execute(options.getString("title"), env).toString();
			if (type.equals("stage")) {
				SysMessage message = new SysMessage();
				message.setCompanyId(Long.valueOf(request.get("companyId").toString()));
				message.setType(1);
				message.setFromUserId(Long.valueOf(request.get("userId").toString()));
				message.setToUserId(Long.valueOf(request.get("userId").toString()));
				message.setTitle(title);
				message.setBusinessType(Long.valueOf(request.get("_formId").toString()));
				message.setBusinessKey(Long.valueOf(businessKey));
				messageMapper.insert(message);
			} else if (type.equals("submit")) {
				// 获取具体流程
				String resources = getResources(definitionList, request.get("companyId").toString(),
						request.get("userId").toString());
				Map<String, Object> variables = request;
				variables.put("companyId", request.get("companyId"));
				variables.put("creator", request.get("userId"));
				variables.put("title", title);
				variables.put("resources", resources);
				variables.put("businessType", request.get("_formId"));
				variables.put("businessKey", businessKey);
				int status = runtimeService.startProcessInstance(variables);
				if (status == 1) {
					// 流程结束后规则
					if (eventService != null) {
						try {
							eventService.flowFinish(request);
						} catch (Exception e) {
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
						}
					}
				}
			}
		} else if (options.getBooleanValue("freeProcess")) {
			// 自己设计流程
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Map<String, Object> env = new HashMap<>(request);
			env.put("formName", form.getName());
			env.put("createTime", sdf.format(new Date()));
			LambdaQueryWrapper<BusinessField> businessFieldQw = new LambdaQueryWrapper<>();
			businessFieldQw.eq(BusinessField::getBusinessTableId, form.getDataSource()).in(BusinessField::getKind,
					Arrays.asList("company", "department", "member"));
			List<BusinessField> businessFieldList = businessFieldMapper.selectList(businessFieldQw);
			for (int i = 0; i < businessFieldList.size(); i++) {
				BusinessField businessField = businessFieldList.get(i);
				String kind = businessField.getKind();
				String field = businessField.getName();
				if (field.equals("user_id")) {
					SysAccount user = userMapper.selectById(env.get("userId").toString());
					if (user != null) {
						env.put(field, user.getName());
					}
				}
				if (env.containsKey(field)) {
					if (!StringUtils.isEmpty(env.get(field))) {
						if (kind.equals("company")) {
							Company company = companyMapper.selectById(env.get(field).toString());
							if (company != null) {
								env.put(field, company.getName());
							}
						} else if (kind.equals("department")) {
							Department department = departmentMapper.selectById(env.get(field).toString());
							if (department != null) {
								env.put(field, department.getName());
							}
						} else if (kind.equals("member")) {
							SysAccount user = userMapper.selectById(env.get(field).toString());
							if (user != null) {
								env.put(field, user.getName());
							}
						}
					}
				}
			}
			String title = AviatorUtils.execute(options.getString("title"), env).toString();
			if (type.equals("stage")) {
				SysMessage message = new SysMessage();
				message.setCompanyId(Long.valueOf(request.get("companyId").toString()));
				message.setType(1);
				message.setFromUserId(Long.valueOf(request.get("userId").toString()));
				message.setToUserId(Long.valueOf(request.get("userId").toString()));
				message.setTitle(title);
				message.setBusinessType(Long.valueOf(request.get("_formId").toString()));
				message.setBusinessKey(Long.valueOf(businessKey));
				messageMapper.insert(message);
			} else if (type.equals("submit")) {
				Map<String, Object> variables = request;
				variables.put("companyId", request.get("companyId"));
				variables.put("creator", request.get("userId"));
				variables.put("title", title);
				variables.put("resources", request.get("resources").toString());
				variables.put("businessType", request.get("_formId"));
				variables.put("businessKey", businessKey);
				runtimeService.startProcessInstance(variables);
				;
			}
		}

		// 新增后规则
		if (eventService != null) {
			try {
				eventService.insertAfter(request);
			} catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
			}
		}
		return ResultData.success(null);
	}

	public String getResources(List<SeaDefinition> definitionList, String companyId, String userId) {
		String resource = "";
		for (int i = 0; i < definitionList.size(); i++) {
			SeaDefinition seaDefinition = definitionList.get(i);
			JSONArray empowerArray = JSONArray.parseArray(seaDefinition.getEmpower());
			if (empowerArray.isEmpty()) {
				if (StringUtils.isEmpty(resource)) {
					resource = seaDefinition.getResources();
				}
			} else {
				for (int j = 0; j < empowerArray.size(); j++) {
					JSONObject empowerObject = empowerArray.getJSONObject(j);
					String type = empowerObject.getString("type");
					String value = empowerObject.getString("value");
					if (type.equals("company")) {
						if (value.equals(companyId)) {
							resource = seaDefinition.getResources();
						}
					} else if (type.equals("department")) {
						LambdaQueryWrapper<DeptUser> qw = new LambdaQueryWrapper<>();
						qw.eq(DeptUser::getCompanyId, companyId).eq(DeptUser::getUserId, userId)
								.eq(DeptUser::getDepartmentId, value);
						Long count = deptUserMapper.selectCount(qw);
						if (count > 0) {
							resource = seaDefinition.getResources();
						}
					} else if (type.equals("role")) {
						List<String> userList = userRoleMapper.queryUserIdByRoleId(Long.valueOf(value));
						if (userList.contains(userId)) {
							resource = seaDefinition.getResources();
						}
					} else if (type.equals("user")) {
						if (value.equals(userId)) {
							resource = seaDefinition.getResources();
						}
					}
				}
			}
		}
		return resource;
	}

	public String insertLogic(Map<String, Object> request, Form form) {
		String businessKey = "";
		BusinessTable businessTable = businessTableMapper.selectById(form.getDataSource());
		List<Map<String, Object>> businessFieldList = businessFieldMapper.queryByTableName(businessTable.getName());
		// 主表数据处理
		Map<String, Object> params = new HashMap<String, Object>();
		for (int i = 0; i < businessFieldList.size(); i++) {
			Map<String, Object> businessField = businessFieldList.get(i);
			if (businessField.get("name").equals("user_id")) {
				params.put("user_id", request.get("userId"));
			} else if (businessField.get("name").equals("company_id")) {
				params.put("company_id", request.get("companyId"));
			} else {
				Object value = request.get(businessField.get("name").toString());
				if (!StringUtils.isEmpty(value)) {
					if (datasourceUrl.contains("oracle")) {
						if ("date".equals(businessField.get("type"))) {
							params.put(businessField.get("name").toString(), "TO_DATE('" + value + "', 'yyyy-MM-dd'),");
						} else if ("timestamp".equals(businessField.get("type"))) {
							params.put(businessField.get("name").toString(),
									"TO_DATE('" + value + "', 'yyyy-MM-dd hh24:mi:ss'),");
						} else {
							params.put(businessField.get("name").toString(), value);
						}
					} else {
						params.put(businessField.get("name").toString(), value);
					}
				}
			}
		}
		businessKey = jdbcTemplateUtils.insert(businessTable.getName(), params);
		return businessKey;
	}

	@Transactional
	@Override
	public ResultData updateCustom(Map<String, Object> request) {
		Form form = formMapper.selectById(request.get("_formId").toString());
		JSONObject options = JSON.parseObject(form.getOptions());
		JSONObject workbook = JSON.parseObject(form.getWorkbook());
		String type = request.get("_type").toString();
		if (type.equals("submit")) {
			List<String> failList = validFormData(workbook.getJSONObject("module"), request);
			if (failList.size() > 0) {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "参数验证不通过", failList);
			}
		}
		// 更新前规则
		RuleHandler eventService = RuleHandlerFactory.getHandler(form.getMark());
		if (eventService != null) {
			try {
				eventService.updateBefore(request);
			} catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
			}
		}
		// 逻辑处理
		updateLogic(request, form);
		// 判断是否有流程
		LambdaQueryWrapper<SeaDefinition> qw = new LambdaQueryWrapper<>();
		qw.eq(SeaDefinition::getFormId, form.getId());
		List<SeaDefinition> definitionList = seaDefinitionMapper.selectList(qw);
		if (definitionList.size() != 0) {
			// approve:审批;submit:提交;
			if (type.equals("approve")) {
				LambdaQueryWrapper<SeaInstance> qwIns = new LambdaQueryWrapper<>();
				qwIns.eq(SeaInstance::getBusinessType, form.getId()).eq(SeaInstance::getBusinessKey, request.get("id"));
				SeaInstance seaInstance = seaInstanceMapper.selectOne(qwIns);
				if (seaInstance != null) {
					Map<String, Object> variables = request;
					variables.put("companyId", request.get("companyId"));
					variables.put("creator", request.get("userId"));
					variables.put("businessType", request.get("_formId"));
					variables.put("businessKey", request.get("id"));
					variables.put("approved", request.get("approved"));
					variables.put("comment", request.get("comment"));
					variables.put("rejectNode", request.get("rejectNode"));
					int status = runtimeService.completeTask(variables);
					if (status == 1) {
						// 流程结束后规则
						if (eventService != null) {
							try {
								eventService.flowFinish(request);
							} catch (Exception e) {
								TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
								return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
							}
						}
					}
				}
			} else if (type.equals("submit")) {
				// 获取具体流程
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				SysAccount user = userMapper.selectById(request.get("userId").toString());
				String title = user.getName() + "上报" + form.getName() + "(" + sdf.format(new Date()) + ")";
				String resources = getResources(definitionList, request.get("companyId").toString(),
						request.get("userId").toString());
				Map<String, Object> variables = request;
				variables.put("companyId", request.get("companyId"));
				variables.put("creator", request.get("userId"));
				variables.put("resources", resources);
				variables.put("title", title);
				variables.put("businessType", request.get("_formId"));
				variables.put("businessKey", request.get("id"));
				// 是否是重新提交
				messageMapper.deleteMessage(form.getId(), Long.valueOf(request.get("id").toString()));
				int status = runtimeService.startProcessInstance(variables);
				if (status == 1) {
					// 流程结束后规则
					if (eventService != null) {
						try {
							eventService.flowFinish(request);
						} catch (Exception e) {
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
						}
					}
				}
			}
		} else if (options.getBooleanValue("freeProcess")) {
			// 自己设计流程
			if (type.equals("approve")) {
				Map<String, Object> variables = request;
				variables.put("companyId", request.get("companyId"));
				variables.put("creator", request.get("userId"));
				variables.put("businessType", request.get("_formId"));
				variables.put("businessKey", request.get("id"));
				variables.put("approved", request.get("approved"));
				variables.put("comment", request.get("comment"));
				variables.put("rejectNode", request.get("rejectNode"));
				runtimeService.completeTask(variables);
			} else if (type.equals("submit")) {
				// 获取具体流程
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				SysAccount user = userMapper.selectById(request.get("userId").toString());
				String title = user.getName() + "上报" + form.getName() + "(" + sdf.format(new Date()) + ")";
				Map<String, Object> variables = request;
				variables.put("companyId", request.get("companyId"));
				variables.put("creator", request.get("userId"));
				variables.put("resources", request.get("resources"));
				variables.put("title", title);
				variables.put("businessType", request.get("_formId"));
				variables.put("businessKey", request.get("id"));
				messageMapper.deleteMessage(form.getId(), Long.valueOf(request.get("id").toString()));
				runtimeService.startProcessInstance(variables);
			}
		}

		// 更新后规则
		if (eventService != null) {
			try {
				eventService.updateAfter(request);
			} catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
			}
		}
		return ResultData.success(null);
	}

	public void updateLogic(Map<String, Object> request, Form form) {
		BusinessTable businessTable = businessTableMapper.selectById(form.getDataSource());
		List<Map<String, Object>> businessFieldList = businessFieldMapper.queryByTableName(businessTable.getName());
		// 主表数据处理
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", request.get("id"));
		for (int i = 0; i < businessFieldList.size(); i++) {
			Map<String, Object> businessField = businessFieldList.get(i);
			Object value = request.get(businessField.get("name").toString());
			if (!StringUtils.isEmpty(value)) {
				if (datasourceUrl.contains("oracle")) {
					if ("date".equals(businessField.get("type"))) {
						params.put(businessField.get("name").toString(), "TO_DATE('" + value + "', 'yyyy-MM-dd'),");
					} else if ("timestamp".equals(businessField.get("type"))) {
						params.put(businessField.get("name").toString(),
								"TO_DATE('" + value + "', 'yyyy-MM-dd hh24:mi:ss'),");
					} else {
						params.put(businessField.get("name").toString(), value);
					}
				} else {
					params.put(businessField.get("name").toString(), value);
				}
			}
		}
		jdbcTemplateUtils.updateById(businessTable.getName(), params);
	}

	@Override
	public ResultData queryListById(Long companyId, Long userId, Long id, Integer pageNo, Integer pageSize,
			String search) {
		Form form = formMapper.selectById(id);
		JSONObject optionsJson = JSON.parseObject(form.getOptions());
		String sql = optionsJson.getString("sql");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", form.getId());
		result.put("mark", form.getMark());
		result.put("name", form.getName());
		result.put("workbook", JSON.parseObject(form.getWorkbook()));
		LambdaQueryWrapper<TableColumnConfig> qw = new LambdaQueryWrapper<>();
		qw.eq(TableColumnConfig::getUserId, userId).eq(TableColumnConfig::getFormId, id);
		TableColumnConfig tableColumnConfig = tableColumnConfigMapper.selectOne(qw);
		List<String> summary = new ArrayList<>();
		if (tableColumnConfig != null) {
			JSONArray tableColumn = JSON.parseArray(tableColumnConfig.getOptions());
			optionsJson.put("tableColumn", tableColumn);
			optionsJson.put("tableColumnConfig", tableColumn);
			for (int i = 0; i < tableColumn.size(); i++) {
				JSONObject column = tableColumn.getJSONObject(i);
				if (column.getInteger("total").equals(1)) {
					summary.add(column.getString("prop"));
				}
			}
		} else {
			JSONArray tableColumn = optionsJson.getJSONArray("tableColumn");
			optionsJson.put("tableColumnConfig", tableColumn);
			for (int i = 0; i < tableColumn.size(); i++) {
				JSONObject column = tableColumn.getJSONObject(i);
				if (column.getInteger("total").equals(1)) {
					summary.add(column.getString("prop"));
				}
			}
		}
		result.put("options", optionsJson);
		Map<String, Object> params = (Map<String, Object>) JSONObject.parseObject(search);
		try {
			sql = XmlUtils.toSql(sql, params, "getList");
			if (StringUtils.isEmpty(sql.trim())) {
				return ResultData.warn(ResultCode.GRAMMAR_ERROR, "查询不到select元素id属性为getList的元素");
			}
		} catch (Exception e) {
			return ResultData.warn(ResultCode.GRAMMAR_ERROR, e.getMessage());
		}
		JSONObject condition = queryUserScope(form.getId(), companyId, userId);
		sql = JsqlparserUtils.parser(sql, condition);
		System.err.println(sql);
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>> list = formMapper.queryPublicList(sql);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		result.put("tableData", pageInfo);
		Map<String, Object> summaryData = new HashMap<String, Object>();
		if (summary.size() != 0) {
			StringBuilder summarySql = new StringBuilder();
			summarySql.append("select ");
			for (int i = 0; i < summary.size(); i++) {
				summarySql.append("sum(");
				summarySql.append(summary.get(i));
				summarySql.append(")");
				summarySql.append(" AS ");
				summarySql.append(summary.get(i));
				if (i != (summary.size() - 1)) {
					summarySql.append(",");
				}
				summarySql.append(" ");
			}
			summarySql.append("from (");
			summarySql.append(sql);
			summarySql.append(") tt");
			summaryData = jdbcTemplate.queryForMap(summarySql.toString());
		}
		result.put("summaryData", summaryData);
		return ResultData.success(result);
	}

	@Transactional
	@Override
	public ResultData deleteCustom(String businessType, String businessKeys) {
		Form form = formMapper.selectById(businessType);
		// 删除前规则
		RuleHandler eventService = RuleHandlerFactory.getHandler(form.getMark());
		Map<String, Object> params = new HashMap<>();
		params.put("businessKeys", businessKeys);
		if (eventService != null) {
			try {
				eventService.deleteBefore(params);
			} catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
			}
		}
		BusinessTable businessTable = businessTableMapper.selectById(form.getDataSource());
		String[] businessKeyAry = businessKeys.split(",");
		for (int i = 0; i < businessKeyAry.length; i++) {
			// 删除主表
			String sql = "DELETE FROM " + businessTable.getName() + " WHERE id = " + businessKeyAry[i];
			jdbcTemplate.execute(sql);

			// 判断是否有流程
			LambdaQueryWrapper<SeaDefinition> qw = new LambdaQueryWrapper<>();
			qw.eq(SeaDefinition::getFormId, form.getId());
			long count = seaDefinitionMapper.selectCount(qw);
			if (count != 0) {
				seaInstanceMapper.deleteProcess(businessType, businessKeyAry[i]);
				messageMapper.deleteMessage(Long.valueOf(businessType), Long.valueOf(businessKeyAry[i]));
			}
		}
		// 删除后规则
		if (eventService != null) {
			try {
				eventService.deleteAfter(params);
			} catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
			}
		}
		return ResultData.success(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultData queryDynamic(Long companyId, String path, String type) {
		if ("department".equals(path)) {
			// Company company = companyMapper.selectById(companyId);
			List<Map<String, Object>> list = departmentMapper.queryByCompanyId(companyId);
			if (StringUtils.isEmpty(type) || type.equals("tree")) {
				return ResultData.success(TreeUtils.categoryTreeHandle(list, "parentId", 0L));
			} else {
				return ResultData.success(list);
			}
		} else if ("member".equals(path)) {
			List<Map<String, Object>> companyList = new ArrayList<>();
			try {
				companyList = (List<Map<String, Object>>) companyService.queryAll(companyId).getData();
			} catch (Exception e) {
				companyList = null;
			}
			List<Map<String, Object>> userList = new ArrayList<>();
			if (companyList == null || companyList.size() == 0) {
				userList = userMapper.queryByCompanyId(companyId);
			} else {
				userList = userMapper.queryUserByCompanyIds(companyList.stream().map(map -> map.get("id").toString())
						.collect(Collectors.toList()).toArray(new String[companyList.size()]));
			}
			// 过滤单位或部门不同的相同用户
			if (null != userList && userList.size() > 0) {
				userList = userList.stream()
						.collect(Collectors.collectingAndThen(
								Collectors.toCollection(
										() -> new TreeSet<>(Comparator.comparing(m -> m.get("id").toString()))),
								ArrayList::new));
			}
			return ResultData.success(userList);
		} else if ("company".equals(path)) {
			// 查询所有单位
			Company company = companyMapper.selectById(companyId);
			String prefix = company.getCode().substring(0, 4);
			return ResultData.success(companyMapper.queryByPrefix(prefix));
		}
		return ResultData.success(null);
	}

	@Override
	public void export(HttpServletRequest request, HttpServletResponse response) {
		Form form = formMapper.selectById(request.getParameter("id"));
		JSONObject options = JSON.parseObject(form.getOptions());
		String sql = options.getString("sql");
		Map<String, Object> searchObject = (Map<String, Object>) JSONObject.parseObject(request.getParameter("search"));
		Long companyId = Long.valueOf(request.getParameter("companyId"));
		Long userId = Long.valueOf(request.getParameter("userId"));
		sql = XmlUtils.toSql(sql, searchObject, "getList");
		JSONObject condition = queryUserScope(form.getId(), companyId, userId);
		sql = JsqlparserUtils.parser(sql, condition);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		JSONArray tableColumn = options.getJSONArray("tableColumn");
		OutputStream out = null;
		Workbook workbook = null;
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((form.getName() + ".xlsx").getBytes("GBK"), "ISO-8859-1"));
			response.setContentType("application/octet-stream;charset=UTF-8");
			ExportParams params = new ExportParams(null, form.getName(), ExcelType.XSSF);
			params.setStyle(ExcelStyleUtils.class);
			// 列名
			List<ExcelExportEntity> entityList = new ArrayList<>();
			// 自动新增首列序号
			ExcelExportEntity index = new ExcelExportEntity("序号", "index");
			index.setFormat("isAddIndex");
			entityList.add(index);
			out = response.getOutputStream();
			JSONObject dic = new JSONObject();
			for (int i = 0; i < tableColumn.size(); i++) {
				JSONObject column = tableColumn.getJSONObject(i);
				if (column.getIntValue("width") > 0) {
					entityList.add(new ExcelExportEntity(column.getString("label"), column.getString("prop"),
							column.getIntValue("width") * 25 / 200));
				} else {
					entityList.add(new ExcelExportEntity(column.getString("label"), column.getString("prop"), 25));
				}
				if (!StringUtils.isEmpty(column.getString("formatter"))) {
					JSONArray dicOptions = column.getJSONArray("options");
					JSONObject items = new JSONObject();
					for (int j = 0; j < dicOptions.size(); j++) {
						items.put(dicOptions.getJSONObject(j).getString("code"),
								dicOptions.getJSONObject(j).getString("name"));
					}
					dic.put(column.getString("prop"), items);
				}
			}
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> item = list.get(i);
				for (String key : dic.keySet()) {
					if (!StringUtils.isEmpty(item.get(key))) {
						item.put(key, dic.getJSONObject(key).getString(item.get(key).toString()));
					}
				}
			}
			workbook = ExcelExportUtil.exportExcel(params, entityList, list);
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Transactional
	@Override
	public ResultData importExcel(MultipartFile file, HttpServletRequest request) {
		try {
			Form form = formMapper.selectById(request.getParameter("id"));
			LambdaQueryWrapper<ImportRule> qw = new LambdaQueryWrapper<>();
			qw.eq(ImportRule::getFormId, request.getParameter("id"));
			ImportRule importRule = importRuleMapper.selectOne(qw);
			JSONObject validRule = new JSONObject();
			JSONObject options = new JSONObject();
			RuleHandler eventService = RuleHandlerFactory.getHandler(form.getMark());
			if (importRule != null) {
				JSONArray rules = JSONArray.parseArray(importRule.getRules());
				for (int i = 0; i < rules.size(); i++) {
					JSONObject ruleObject = rules.getJSONObject(i);
					JSONObject fieldObject = new JSONObject();
					fieldObject.put("field", ruleObject.getString("prop"));
					fieldObject.put("label", ruleObject.getString("label"));
					String ruleStr = ruleObject.getString("rule");
					JSONArray ruleArray = new JSONArray();
					if (!StringUtils.isEmpty(ruleStr)) {
						String[] lines = ruleStr.split("\n");
						for (String line : lines) {
							if (line.trim().equals("@Company()")) {
								// 单位
								if (!options.containsKey("company")) {
									String companyId = request.getParameter("companyId");
									Company company = companyMapper.selectById(companyId);
									LambdaQueryWrapper<Company> qwComapny = new LambdaQueryWrapper<>();
									qwComapny.likeRight(Company::getCode, company.getCode().substring(0, 4));
									List<Company> companyList = companyMapper.selectList(qwComapny);
									JSONObject data = new JSONObject();
									for (int j = 0; j < companyList.size(); j++) {
										data.put(companyList.get(j).getName(), companyList.get(j).getId());
									}
									options.put("company", data);
								}
							} else if (line.trim().equals("@Department()")) {
								// 部门
								if (!options.containsKey("department")) {
									String companyId = request.getParameter("companyId");
									LambdaQueryWrapper<Department> qwDepartment = new LambdaQueryWrapper<>();
									qwDepartment.eq(Department::getCompanyId, companyId);
									List<Department> departmentList = departmentMapper.selectList(qwDepartment);
									JSONObject data = new JSONObject();
									for (int j = 0; j < departmentList.size(); j++) {
										data.put(departmentList.get(j).getName(), departmentList.get(j).getId());
									}
									options.put("department", data);
								}
							} else if (line.trim().equals("@User()")) {
								// 人员
								if (!options.containsKey("user")) {
									String companyId = request.getParameter("companyId");
									List<Map<String, Object>> userList = userMapper
											.queryByCompanyId(Long.valueOf(companyId));
									JSONObject data = new JSONObject();
									for (int j = 0; j < userList.size(); j++) {
										data.put(userList.get(j).get("name").toString(), userList.get(j).get("id"));
									}
									options.put("user", data);
								}
							} else if (line.trim().startsWith("@Replace(")) {
								// 字典
								if (!options.containsKey(ruleObject.getString("prop"))) {
									JSONObject annotationJson = new JSONObject();
									String[] annotationAry = line.substring(9, line.length() - 1).split(",");
									for (int k = 0; k < annotationAry.length; k++) {
										String[] dicAry = annotationAry[k].split("=");
										annotationJson.put(dicAry[0].trim(),
												dicAry[1].trim().substring(1, dicAry[1].trim().length() - 1));
									}
									String value = annotationJson.getString("value");
									Long companyId = Long.valueOf(request.getParameter("companyId"));
									Company company = companyMapper.selectById(companyId);
									if (company.getParentId() != null) {
										LambdaQueryWrapper<Company> qwComapny = new LambdaQueryWrapper<>();
										qwComapny.eq(Company::getCode, company.getCode().substring(0, 4));
										companyId = companyMapper.selectOne(qwComapny).getId();
									}
									List<Map<String, Object>> dicList = dicClassifyMapper.queryByName(companyId, value);
									JSONObject data = new JSONObject();
									for (int j = 0; j < dicList.size(); j++) {
										data.put(dicList.get(j).get("name").toString(), dicList.get(j).get("code"));
									}
									options.put(ruleObject.getString("prop"), data);
								}
							}
							ruleArray.add(line);
						}
					}
					fieldObject.put("rule", ruleArray);
					validRule.put(String.valueOf(i), fieldObject);
				}
			}
			Workbook workbook = ExcelUtils.readWorkbook(new ByteArrayInputStream(file.getBytes()));
			ImportResult importResult = ExcelUtils.readCell(request, workbook.getSheetAt(0), importRule.getStartLine(),
					validRule, form.getMark(), options);
			workbook.close();
			// 判断是否有错误
			if (importResult.isVerifyFail()) {
				return ResultData.warn(ResultCode.INVALID_ERROR, "导入验证不通过", importResult.getFailList());
			} else {
				BusinessTable businessTable = businessTableMapper.selectById(form.getDataSource());
				// 处理导入数据
				if (eventService != null) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("tableName", businessTable.getName());
					try {
						eventService.importHandle(request, importResult.getList(), params);
						return ResultData.success(null);
					} catch (Exception e) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return ResultData.warn(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
					}
				} else {
					try {
						if (importResult.getList().size() != 0) {
							jdbcTemplateUtils.batchInsert(businessTable.getName(), importResult.getList());
						}
						return ResultData.success(null);
					} catch (Exception e) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return ResultData.warn(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			return ResultData.error(ResultCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResultData queryCustomDetail(Long companyId, Long userId, Long formId, Long id) {
		JSONObject result = new JSONObject();
		Form form = formMapper.selectById(formId);
		RuleHandler eventService = RuleHandlerFactory.getHandler(form.getMark());
		result.put("mark", form.getMark());
		LambdaQueryWrapper<SeaDefinition> definitionQw = new LambdaQueryWrapper<>();
		definitionQw.eq(SeaDefinition::getFormId, form.getId());
		long count = seaDefinitionMapper.selectCount(definitionQw);
		JSONObject options = JSON.parseObject(form.getOptions());
		result.put("isFlow", count != 0 || options.getBooleanValue("freeProcess"));
		result.put("options", options);
		result.put("workbook", JSON.parseObject(form.getWorkbook()));
		result.put("freeProcess", options.getBooleanValue("freeProcess"));
		result.put("submitButton", false);
		result.put("approvalButton", false);
		result.put("stageButton", false);
		result.put("revokeButton", false);
		int type = 1;
		if (id != null) {
			type = 2;
			BusinessTable businessTable = businessTableMapper.selectById(form.getDataSource());
			String sql = "select * from " + businessTable.getName() + " where id=" + id;
			Map<String, Object> data = jdbcTemplate.queryForMap(sql);
			if (eventService != null) {
				Map<String, Object> detailData = eventService.detailData(data);
				if (detailData != null) {
					data.putAll(detailData);
				}
			}
			result.put("data", data);
		} else {
			JSONObject empty = new JSONObject();
			result.put("data", empty);
		}
		// 字段权限
		List<String> fieldList = formAthorityMapper.queryUserField(formId, type, companyId, userId);
		result.put("fieldOptions", fieldList);
		// 按钮权限
		LambdaQueryWrapper<SeaInstance> qw = new LambdaQueryWrapper<>();
		qw.eq(SeaInstance::getBusinessType, formId).eq(SeaInstance::getBusinessKey, id);
		SeaInstance seaInstance = seaInstanceMapper.selectOne(qw);
		JSONArray rejectNodeList = new JSONArray();
		if (seaInstance != null) {
			result.put("processInstanceId", seaInstance.getId());
			// 状态(0:活动;1:完成;2:暂停;3:终止;)
			int status = seaInstance.getStatus();
			boolean submitButton = false;// 撤回（发起人）
			boolean approvalButton = false;// 审批
			if (status == 0) {
				// 审批按钮
				List<Map<String, Object>> currentNodeList = seaNodeMapper.queryCurrentNodeDetail(seaInstance.getId(),
						String.valueOf(userId));
				if (currentNodeList.size() > 0) {
					// 审批按钮权限
					approvalButton = true;
					String[] pathArray = currentNodeList.get(0).get("path").toString().split("_");
					JSONObject resources = JSONObject.parseObject(currentNodeList.get(0).get("resources").toString());
					JSONArray nodes = resources.getJSONArray("nodes");
					JSONObject nodeObj = new JSONObject();
					for (int i = 0; i < nodes.size(); i++) {
						JSONObject node = nodes.getJSONObject(i);
						nodeObj.put(node.getString("id"), node);
					}
					for (int i = 0; i < pathArray.length - 1; i++) {
						if (nodeObj.containsKey(pathArray[i])) {
							rejectNodeList.add(nodeObj.getJSONObject(pathArray[i]));
						}
					}
				}
				if (seaInstance.getUserId().equals(userId)) {
					result.put("revokeButton", true);
				}
			} else if (status == 3) {
				// 提交按钮
				if (seaInstance.getUserId().equals(userId)) {
					submitButton = true;
				}
			}
			result.put("submitButton", submitButton);
			result.put("approvalButton", approvalButton);
		} else {
			result.put("stageButton", true);
			result.put("submitButton", true);
		}
		result.put("rejectNodeList", rejectNodeList);
		return ResultData.success(result);
	}

	@Override
	public ResultData queryBusinessTypes(Long companyId) {
		Company company = companyMapper.selectById(companyId);
		LambdaQueryWrapper<Company> qwCompany = new LambdaQueryWrapper<>();
		qwCompany.eq(Company::getCode, company.getCode().substring(0, 4));
		Company parentCompany = companyMapper.selectOne(qwCompany);
		LambdaQueryWrapper<Form> qw = new LambdaQueryWrapper<>();
		qw.eq(Form::getCompanyId, parentCompany.getId());
		return ResultData.success(formMapper.selectList(qw));
	}

	@Override
	public ResultData queryOptions(String formId, String value) {
		Map<String, Object> result = new HashMap<String, Object>();
		Form form = formMapper.selectById(formId);
		if (form != null) {
			BusinessTable businessTable = businessTableMapper.selectById(form.getDataSource());
			String sql = "SELECT * FROM " + businessTable.getName() + " WHERE id = " + value;
			result = jdbcTemplate.queryForMap(sql);
		}
		return ResultData.success(result);
	}

	@Override
	public ResultData queryBill(String formId, String field, String serial) {
		Form form = formMapper.selectById(formId);
		BusinessTable businessTable = businessTableMapper.selectById(form.getDataSource());
		Serial serialData = serialMapper.selectById(serial);
		if (serialData == null) {
			return ResultData.warn(ResultCode.OTHER_ERROR, "编号设置不存在");
		}
		JSONArray options = JSONArray.parseArray(serialData.getOptions());
		String prefix = "";
		int suffixLength = 0;
		for (int i = 0; i < options.size(); i++) {
			JSONObject option = options.getJSONObject(i);
			if (option.getInteger("type").equals(1)) {
				// 固定码
				String value = option.getString("value");
				prefix = prefix + value;
			} else if (option.getInteger("type").equals(2)) {
				// 年月日
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat(option.getString("format"));
				String value = sdf.format(date);
				prefix = prefix + value;
			} else if (option.getInteger("type").equals(3)) {
				// 顺序码
				suffixLength = option.getIntValue("length");
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT code FROM ");
		sql.append(businessTable.getName());
		sql.append(" WHERE ");
		sql.append(field);
		sql.append(" like ");
		sql.append("'");
		sql.append(prefix);
		sql.append("%'");
		sql.append(" and ");
		sql.append("LENGTH(");
		sql.append(field);
		sql.append(")");
		sql.append(" = ");
		sql.append(prefix.length() + suffixLength);
		sql.append(" ORDER BY create_time desc limit 1 ");
		try {
			String code = jdbcTemplate.queryForObject(sql.toString(), String.class);
			Long number = 0L;
			if (!StringUtils.isEmpty(code)) {
				number = Long.valueOf(code.substring(code.length() - suffixLength, code.length()));
			}
			Long max = number + 1;
			if (lpad(suffixLength, max).length() != suffixLength) {
				return ResultData.warn(ResultCode.OTHER_ERROR, "顺序码超过" + suffixLength + "位数,请联系管理员!");
			}
			return ResultData.success(prefix + lpad(suffixLength, max));
		} catch (Exception e) {
			return ResultData.success(prefix + lpad(suffixLength, 1L));
		}
	}

	/**
	 * 补齐不足长度
	 *
	 * @param length 长度
	 * @param number 数字
	 * @return
	 */
	private String lpad(int length, Long number) {
		String f = "%0" + length + "d";
		return String.format(f, number);
	}

	@Override
	public void download(HttpServletResponse response, Long companyId, Long id) {
		OutputStream outputStream = null;
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(("model.xlsx").getBytes("GBK"), "ISO-8859-1"));
			response.setContentType("application/octet-stream;charset=UTF-8");
			outputStream = response.getOutputStream();
			JSONArray headers = new JSONArray();
			LambdaQueryWrapper<ImportRule> qw = new LambdaQueryWrapper<>();
			qw.eq(ImportRule::getFormId, id);
			ImportRule importRule = importRuleMapper.selectOne(qw);
			JSONArray data = new JSONArray();
			if (importRule != null) {
				JSONArray ruleList = JSONArray.parseArray(importRule.getRules());
				for (int i = 0; i < ruleList.size(); i++) {
					JSONObject item = new JSONObject();
					JSONObject ruleObject = ruleList.getJSONObject(i);
					String label = ruleObject.getString("label");
					String field = ruleObject.getString("prop");
					item.put("label", label);
					item.put("required", false);
					String ruleStr = ruleObject.getString("rule");
					if (!StringUtils.isEmpty(ruleStr)) {
						String[] lines = ruleStr.split("\n");
						for (String line : lines) {
							if (line.contains("@NotNull")) {
								item.put("required", true);
							} else if (line.contains("@Company()")) {
								JSONObject itemData = new JSONObject();
								Company company = companyMapper.selectById(companyId);
								LambdaQueryWrapper<Company> qwComapny = new LambdaQueryWrapper<>();
								qwComapny.likeRight(Company::getCode, company.getCode().substring(0, 4));
								List<Company> companyList = companyMapper.selectList(qwComapny);
								JSONArray options = new JSONArray();
								for (int j = 0; j < companyList.size(); j++) {
									options.add(companyList.get(j).getName());
								}
								itemData.put("field", field);
								itemData.put("col", i);
								itemData.put("options", options);
								data.add(itemData);
							} else if (line.contains("@Replace(")) {
								JSONObject itemData = new JSONObject();
								JSONObject annotationJson = new JSONObject();
								String[] annotationAry = line.substring(9, line.length() - 1).split(",");
								for (int k = 0; k < annotationAry.length; k++) {
									String[] dicAry = annotationAry[k].split("=");
									annotationJson.put(dicAry[0].trim(),
											dicAry[1].trim().substring(1, dicAry[1].trim().length() - 1));
								}
								String value = annotationJson.getString("value");
								Company company = companyMapper.selectById(companyId);
								if (company.getParentId() != null) {
									LambdaQueryWrapper<Company> qwComapny = new LambdaQueryWrapper<>();
									qwComapny.eq(Company::getCode, company.getCode().substring(0, 4));
									companyId = companyMapper.selectOne(qwComapny).getId();
								}
								List<String> dicList = dicClassifyMapper.queryLastStageByName(companyId, value);
								JSONArray options = new JSONArray();
								for (int j = 0; j < dicList.size(); j++) {
									options.add(dicList.get(j));
								}
								itemData.put("field", field);
								itemData.put("col", i);
								itemData.put("options", options);
								data.add(itemData);
							}
						}
					}
					headers.add(item);
				}
			}
			ExcelUtils.createTemplateByHeader(headers, outputStream, importRule.getStartLine(), data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public String print(Long businessType, Long businessKey) {
		Form form = formMapper.selectById(businessType);
		JSONObject options = JSONObject.parseObject(form.getOptions());
		JSONArray printPath = options.getJSONArray("printPath");
		String url = printPath.getJSONObject(0).getString("url");
		String fileName = System.currentTimeMillis() + ".docx";
		OutputStream out = null;
		try {
			out = new FileOutputStream(workingDir + "/" + fileName);
			Map<String, Object> variable = new HashMap<>();
			variable.put("businessType", businessType);
			variable.put("businessKey", businessKey);
			ExportUtils.exportWord(url, variable, out, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return workingDir + "/" + fileName;
	}

	public JSONObject queryUserScope(Long formId, Long companyId, Long userId) {
		List<String> scopeList = formAthorityMapper.queryUserScope(formId, companyId, userId);
		List<String> companyList = new ArrayList<>();
		List<String> deptList = new ArrayList<>();
		List<String> userList = new ArrayList<>();
		JSONObject condition = new JSONObject();
		for (String scope : scopeList) {
			JSONArray scopeArray = JSON.parseArray(scope);
			for (int i = 0; i < scopeArray.size(); i++) {
				JSONObject scopeObject = scopeArray.getJSONObject(i);
				String type = scopeObject.getString("type");
				String value = scopeObject.getString("value");
				if (type.equals("company")) {
					companyList.add(value);
				} else if (type.equals("department")) {
					deptList.add(value);
				} else if (type.equals("user")) {
					userList.add(value);
				} else if (type.equals("all-data")) {
					return condition;
				} else if (type.equals("self-data")) {
					userList.add(String.valueOf(userId));
				} else if (type.equals("standard-data")) {
					companyList.add(String.valueOf(companyId));
				} else if (type.equals("standard-subunit-data")) {
					Company company = companyMapper.selectById(companyId);
					LambdaQueryWrapper<Company> qw = new LambdaQueryWrapper<>();
					qw.likeRight(Company::getCode, company.getCode());
					List<Company> list = companyMapper.selectList(qw);
					for (int j = 0; j < list.size(); j++) {
						companyList.add(String.valueOf(list.get(j).getId()));
					}
				} else if (type.equals("dept-data")) {
					LambdaQueryWrapper<DeptUser> qw = new LambdaQueryWrapper<>();
					qw.eq(DeptUser::getCompanyId, companyId).eq(DeptUser::getUserId, userId);
					List<DeptUser> list = deptUserMapper.selectList(qw);
					for (int j = 0; j < list.size(); j++) {
						deptList.add(String.valueOf(list.get(j).getDepartmentId()));
					}
				} else if (type.equals("dept-subdept-data")) {
					LambdaQueryWrapper<DeptUser> qw = new LambdaQueryWrapper<>();
					qw.eq(DeptUser::getCompanyId, companyId).eq(DeptUser::getUserId, userId);
					List<DeptUser> list = deptUserMapper.selectList(qw);
					for (int j = 0; j < list.size(); j++) {
						Department department = departmentMapper.selectById(list.get(j).getId());
						LambdaQueryWrapper<Department> qwSub = new LambdaQueryWrapper<>();
						qwSub.likeRight(Department::getCode, department.getCode());
						List<Department> subList = departmentMapper.selectList(qwSub);
						for (int k = 0; k < subList.size(); k++) {
							deptList.add(String.valueOf(subList.get(k).getId()));
						}
					}
				}
			}
		}
		if (deptList.size() != 0) {
			userList.addAll(deptUserMapper.queryByDepartmentIds(deptList));
		}
		if (companyList.size() != 0) {
			companyList.add(String.valueOf(companyId));
			Set<String> set = new LinkedHashSet<>(companyList);
			List<String> distinctList = new ArrayList<>(set);
			String companyStr = String.join(",", distinctList);
			condition.put("companyStr", companyStr);
		} else {
			condition.put("companyStr", companyId);
		}
		if (userList.size() != 0) {
			Set<String> set = new LinkedHashSet<>(userList);
			List<String> distinctList = new ArrayList<>(set);
			String userStr = String.join(",", distinctList);
			condition.put("userStr", userStr);
		}
		return condition;
	}

	public List<String> validFormData(JSONObject module, Map<String, Object> data) {
		List<String> failList = new ArrayList<>();
		if (module != null && !module.isEmpty()) {
			for (String key : module.keySet()) {
				JSONObject value = module.getJSONObject(key);
				if (value.containsKey("required")) {
					boolean required = value.getBooleanValue("required");
					if (required) {
						if (StringUtils.isEmpty(data.get(value.getString("field")))) {
							failList.add(value.getString("label") + "不能为空");
						}
					}
				}
			}
		}
		return failList;
	}

}
