package com.seagox.lowcode.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.BusinessField;
import com.seagox.lowcode.entity.BusinessTable;
import com.seagox.lowcode.mapper.BusinessFieldMapper;
import com.seagox.lowcode.mapper.BusinessTableMapper;
import com.seagox.lowcode.service.IBusinessTableService;
import com.seagox.lowcode.util.TreeUtils;

@Service
public class BusinessTableService implements IBusinessTableService {

	@Autowired
	private BusinessTableMapper businessTableMapper;

	@Autowired
	private BusinessFieldMapper businessFieldMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value(value = "${spring.datasource.url}")
	private String datasourceUrl;

	@Override
	public ResultData queryAll(Long companyId, String name, String remark) {
		LambdaQueryWrapper<BusinessTable> qw = new LambdaQueryWrapper<>();
    	qw.in(BusinessTable::getCompanyId, companyId)
    	.like(!StringUtils.isEmpty(name), BusinessTable::getName, name)
    	.like(!StringUtils.isEmpty(remark), BusinessTable::getRemark, remark)
    	.orderByAsc(BusinessTable::getName);
        List<BusinessTable> list = businessTableMapper.selectList(qw);
		return ResultData.success(list);
	}

	@Transactional
	@Override
	public ResultData insert(BusinessTable businessTable) {
		LambdaQueryWrapper<BusinessTable> qw = new LambdaQueryWrapper<>();
		qw.eq(BusinessTable::getName, businessTable.getName());
		Long count = businessTableMapper.selectCount(qw);
		if (count == 0) {
			businessTableMapper.insert(businessTable);

			StringBuffer sql = new StringBuffer();
			if (datasourceUrl.contains("mysql")) {
				sql.append("CREATE TABLE ");
				sql.append(businessTable.getName());
				sql.append(" (");
				sql.append("id bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',");
			} else if (datasourceUrl.contains("postgresql") || datasourceUrl.contains("kingbase8")) {
				sql.append("CREATE TABLE \"");
				sql.append(businessTable.getName());
				sql.append("\" (");
				sql.append("\"id\" bigserial NOT NULL PRIMARY KEY,");
			} else if (datasourceUrl.contains("oracle")) {
				sql.append("CREATE TABLE ");
				sql.append(businessTable.getName());
				sql.append(" (");
				sql.append("id number(20) NOT NULL PRIMARY KEY,");
			} else if (datasourceUrl.contains("dm")) {
				sql.append("CREATE TABLE \"");
				sql.append(businessTable.getName());
				sql.append("\" (");
				sql.append("\"id\" BIGINT(20) IDENTITY(1,1) PRIMARY KEY NOT NULL,");
			} else if (datasourceUrl.contains("sqlserver")) {
				sql.append("CREATE TABLE \"");
				sql.append(businessTable.getName());
				sql.append("\" (");
				sql.append("\"id\" bigint IDENTITY(1,1) PRIMARY KEY NOT NULL,");
			}
			if (!StringUtils.isEmpty(businessTable.getInitialize())) {
				String[] initializeArray = businessTable.getInitialize().split(",");
				for (String str : initializeArray) {
					BusinessField businessField = new BusinessField();
					businessField.setBusinessTableId(businessTable.getId());
					businessField.setName(str);
					businessField.setNotNull(1);
					if (str.equals("company_id")) {
						if (datasourceUrl.contains("mysql")) {
							sql.append("company_id bigint(20) NOT NULL COMMENT '公司id',");
						} else if (datasourceUrl.contains("postgresql") || datasourceUrl.contains("kingbase8")
								|| datasourceUrl.contains("sqlserver")) {
							sql.append("\"company_id\" bigint NOT NULL,");
						} else if (datasourceUrl.contains("oracle")) {
							sql.append("company_id number(20) NOT NULL,");
						}
						businessField.setRemark("所属公司");
						businessField.setType("bigint");
						businessField.setLength(20);
						businessField.setKind("company");
					} else if (str.equals("dept_id")) {
						if (datasourceUrl.contains("mysql")) {
							sql.append("dept_id bigint(20) NOT NULL COMMENT '部门id',");
						} else if (datasourceUrl.contains("postgresql") || datasourceUrl.contains("kingbase8")
								|| datasourceUrl.contains("sqlserver")) {
							sql.append("\"dept_id\" bigint NOT NULL,");
						} else if (datasourceUrl.contains("oracle")) {
							sql.append("dept_id number(20) NOT NULL,");
						}
						businessField.setRemark("所属部门");
						businessField.setType("bigint");
						businessField.setLength(20);
						businessField.setKind("department");
					} else if (str.equals("user_id")) {
						if (datasourceUrl.contains("mysql")) {
							sql.append("user_id bigint(20) NOT NULL COMMENT '用户id',");
						} else if (datasourceUrl.contains("postgresql") || datasourceUrl.contains("kingbase8")
								|| datasourceUrl.contains("dm") || datasourceUrl.contains("sqlserver")) {
							sql.append("\"user_id\" bigint NOT NULL,");
						} else if (datasourceUrl.contains("oracle")) {
							sql.append("user_id number(20) NOT NULL,");
						}
						businessField.setRemark("创建者");
						businessField.setType("bigint");
						businessField.setLength(20);
						businessField.setKind("member");
					}
					businessFieldMapper.insert(businessField);
				}
			}
			if (datasourceUrl.contains("mysql")) {
				sql.append("create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',");
				sql.append("update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'");
				sql.append(" ) ");
				sql.append(
						"ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '");
				sql.append(businessTable.getRemark());
				sql.append("'");
			} else if (datasourceUrl.contains("postgresql") || datasourceUrl.contains("kingbase8")
					|| datasourceUrl.contains("dm")) {
				sql.append("\"create_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
				sql.append("\"update_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
				sql.append(" );");
				// 添加表注释
				sql.append("COMMENT ON TABLE \"");
				sql.append(businessTable.getName());
				sql.append("\" IS ");
				sql.append("'");
				sql.append(businessTable.getRemark());
				sql.append("'");
				sql.append(";");
				// 添加列id注释
				sql.append("COMMENT ON COLUMN \"");
				sql.append(businessTable.getName());
				sql.append("\".\"id\" IS ");
				sql.append("'主键';");
				// 添加列companyId注释
				sql.append("COMMENT ON COLUMN \"");
				sql.append(businessTable.getName());
				sql.append("\".\"company_id\" IS ");
				sql.append("'公司id';");
				// 添加列userId注释
				sql.append("COMMENT ON COLUMN \"");
				sql.append(businessTable.getName());
				sql.append("\".\"user_id\" IS ");
				sql.append("'用户id';");
				// 添加列create_time注释
				sql.append("COMMENT ON COLUMN \"");
				sql.append(businessTable.getName());
				sql.append("\".\"create_time\" IS ");
				sql.append("'创建时间';");
				// 添加列update_time注释
				sql.append("COMMENT ON COLUMN \"");
				sql.append(businessTable.getName());
				sql.append("\".\"update_time\" IS ");
				sql.append("'更新时间';");
			} else if (datasourceUrl.contains("oracle")) {
				sql.append("create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
				sql.append("update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
				sql.append(" );");
				// 添加表注释
				sql.append("COMMENT ON TABLE ");
				sql.append(businessTable.getName());
				sql.append(" IS ");
				sql.append("'");
				sql.append(businessTable.getRemark());
				sql.append("'");
				sql.append(";");
				// 添加列id注释
				sql.append("COMMENT ON COLUMN ");
				sql.append(businessTable.getName());
				sql.append(".id IS ");
				sql.append("'主键';");
				// 添加列companyId注释
				sql.append("COMMENT ON COLUMN ");
				sql.append(businessTable.getName());
				sql.append(".company_id IS ");
				sql.append("'公司id';");
				// 添加列userId注释
				sql.append("COMMENT ON COLUMN ");
				sql.append(businessTable.getName());
				sql.append(".user_id IS ");
				sql.append("'用户id';");
				// 添加列create_time注释
				sql.append("COMMENT ON COLUMN ");
				sql.append(businessTable.getName());
				sql.append(".create_time IS ");
				sql.append("'创建时间';");
				// 添加列update_time注释
				sql.append("COMMENT ON COLUMN ");
				sql.append(businessTable.getName());
				sql.append(".update_time IS ");
				sql.append("'更新时间'");
			} else if (datasourceUrl.contains("sqlserver")) {
				sql.append("\"create_time\" datetime2(7) DEFAULT CURRENT_TIMESTAMP,");
				sql.append("\"update_time\" datetime2(7) DEFAULT CURRENT_TIMESTAMP");
				sql.append(" );");
				// 添加表注释
				sql.append("EXEC sp_addextendedproperty ");
				sql.append("'MS_Description', N'");
				sql.append(businessTable.getRemark());
				sql.append("',");
				sql.append("'SCHEMA', N'dbo',");
				sql.append("'TABLE', N'");
				sql.append(businessTable.getName());
				sql.append("';");
				// 添加列id注释
				sql.append("EXEC sp_addextendedproperty ");
				sql.append("'MS_Description', N'");
				sql.append("主键");
				sql.append("',");
				sql.append("'SCHEMA', N'dbo',");
				sql.append("'TABLE', N'");
				sql.append(businessTable.getName());
				sql.append("',");
				sql.append("'COLUMN', N'");
				sql.append("id';");
				// 添加列companyId注释
				sql.append("EXEC sp_addextendedproperty ");
				sql.append("'MS_Description', N'");
				sql.append("公司id");
				sql.append("',");
				sql.append("'SCHEMA', N'dbo',");
				sql.append("'TABLE', N'");
				sql.append(businessTable.getName());
				sql.append("',");
				sql.append("'COLUMN', N'");
				sql.append("company_id';");
				// 添加列userId注释
				sql.append("EXEC sp_addextendedproperty ");
				sql.append("'MS_Description', N'");
				sql.append("用户id");
				sql.append("',");
				sql.append("'SCHEMA', N'dbo',");
				sql.append("'TABLE', N'");
				sql.append(businessTable.getName());
				sql.append("',");
				sql.append("'COLUMN', N'");
				sql.append("user_id';");
				// 添加列create_time注释
				sql.append("EXEC sp_addextendedproperty ");
				sql.append("'MS_Description', N'");
				sql.append("创建时间");
				sql.append("',");
				sql.append("'SCHEMA', N'dbo',");
				sql.append("'TABLE', N'");
				sql.append(businessTable.getName());
				sql.append("',");
				sql.append("'COLUMN', N'");
				sql.append("create_time';");
				// 添加列update_time注释
				sql.append("EXEC sp_addextendedproperty ");
				sql.append("'MS_Description', N'");
				sql.append("更新时间");
				sql.append("',");
				sql.append("'SCHEMA', N'dbo',");
				sql.append("'TABLE', N'");
				sql.append(businessTable.getName());
				sql.append("',");
				sql.append("'COLUMN', N'");
				sql.append("update_time';");
			}
			if (datasourceUrl.contains("oracle")) {
				String[] splitSql = sql.toString().split(";");
				for (String oracleSql : splitSql) {
					jdbcTemplate.execute(oracleSql);
				}
				// 创建序列
				String sequenceSql = "create sequence " + businessTable.getName().trim()
						+ "_seq increment by 1 start with 1 nomaxvalue minvalue 1 nocycle";
				jdbcTemplate.execute(sequenceSql);
			} else {
				try {
					jdbcTemplate.execute(sql.toString());
    			} catch (BadSqlGrammarException e) {
    				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    	            return ResultData.warn(ResultCode.OTHER_ERROR, "数据库关键字，请重新输入");
    			}
			}
			return ResultData.success(businessTable.getId());
		} else {
			return ResultData.warn(ResultCode.OTHER_ERROR, "表名已经存在");
		}
	}

	@Transactional
	@Override
	public ResultData update(BusinessTable businessTable) {
		BusinessTable originalBusinessTable = businessTableMapper.selectById(businessTable.getId());
		if (!originalBusinessTable.getName().equals(businessTable.getName())) {
			// 更改表名
			StringBuffer sql = new StringBuffer();
			if (datasourceUrl.contains("mysql") || datasourceUrl.contains("postgresql")
					|| datasourceUrl.contains("kingbase8") || datasourceUrl.contains("dm")) {
				sql.append("ALTER TABLE ");
				sql.append(originalBusinessTable.getName());
				sql.append(" rename to ");
				sql.append(businessTable.getName());
			} else if (datasourceUrl.contains("sqlserver")) {
				sql.append("sp_rename ");
				sql.append(originalBusinessTable.getName());
				sql.append(",");
				sql.append(businessTable.getName());
			} else if (datasourceUrl.contains("oracle")) {
				sql.append("ALTER TABLE ");
				sql.append(originalBusinessTable.getName());
				sql.append(" rename to ");
				sql.append(businessTable.getName());
				// 更新原表名序列
				String updateSequenceSql = "rename " + originalBusinessTable.getName().trim() + "_seq" + " to "
						+ businessTable.getName().trim() + "_seq";
				jdbcTemplate.execute(updateSequenceSql);
			}
			try {
				jdbcTemplate.execute(sql.toString());
			} catch (BadSqlGrammarException e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	            return ResultData.warn(ResultCode.OTHER_ERROR, "数据库关键字，请重新输入");
			}
		}
		if (!originalBusinessTable.getRemark().equals(businessTable.getRemark())) {
			// 更新表注释
			StringBuffer sql = new StringBuffer();
			if (datasourceUrl.contains("mysql")) {
				sql.append("ALTER TABLE ");
				sql.append(businessTable.getName());
				sql.append(" comment ");
				sql.append("'");
				sql.append(businessTable.getRemark());
				sql.append("'");
			} else if (datasourceUrl.contains("postgresql") || datasourceUrl.contains("kingbase8")
					|| datasourceUrl.contains("dm")) {
				sql.append("COMMENT ON TABLE ");
				sql.append(businessTable.getName());
				sql.append(" IS ");
				sql.append("'");
				sql.append(businessTable.getRemark());
				sql.append("'");
			} else if (datasourceUrl.contains("sqlserver")) {
				sql.append("EXEC SP_UPDATEEXTENDEDPROPERTY ");
				sql.append("'MS_Description', N'");
				sql.append(businessTable.getRemark());
				sql.append("',");
				sql.append("'SCHEMA', N'dbo',");
				sql.append("'TABLE', N'");
				sql.append(businessTable.getName());
				sql.append("'");
			} else if (datasourceUrl.contains("oracle")) {
				sql.append("COMMENT ON TABLE ");
				sql.append(businessTable.getName());
				sql.append(" IS ");
				sql.append("'");
				sql.append(businessTable.getRemark());
				sql.append("'");
			}
			jdbcTemplate.execute(sql.toString());
		}
		businessTableMapper.updateById(businessTable);
		return ResultData.success(null);
	}

	@Transactional
	@Override
	public ResultData delete(Long id) {
		LambdaQueryWrapper<BusinessField> qw = new LambdaQueryWrapper<>();
		qw.eq(BusinessField::getBusinessTableId, id);
		Long businessTableCount = businessFieldMapper.selectCount(qw);
		if (businessTableCount > 0) {
			return ResultData.warn(ResultCode.OTHER_ERROR, "有关联业务字段，不可删除");
		} else {
			BusinessTable businessTable = businessTableMapper.selectById(id);
			StringBuffer sql = new StringBuffer();
			if (datasourceUrl.contains("oracle")) {
				sql.append("DROP TABLE ");
				// 删除原表名序列
				String deleteSequenceSql = "drop sequence " + businessTable.getName().trim() + "_seq";
				jdbcTemplate.execute(deleteSequenceSql);
			} else {
				sql.append("DROP TABLE IF EXISTS ");
			}
			sql.append(businessTable.getName());
			jdbcTemplate.execute(sql.toString());
			businessTableMapper.deleteById(id);
			return ResultData.success(null);
		}
	}

	@Override
	public ResultData queryById(Long id) {
		return ResultData.success(businessTableMapper.selectById(id));
	}

	@Override
	public ResultData queryCascader(Long id, String rule) {
		BusinessTable businessTable = businessTableMapper.selectById(id);
		if (businessTable != null) {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ");
			sql.append(businessTable.getName());
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());
			JSONArray result = TreeUtils.listToTreeByRule(JSONArray.parseArray(JSON.toJSONString(list)), "code",
					"children", rule);
			return ResultData.success(result);
		}
		return ResultData.success(null);
	}

}
