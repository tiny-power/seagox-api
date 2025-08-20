package com.seagox.lowcode.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.BusinessField;
import com.seagox.lowcode.entity.BusinessTable;
import com.seagox.lowcode.mapper.BusinessFieldMapper;
import com.seagox.lowcode.mapper.BusinessTableMapper;
import com.seagox.lowcode.service.IBusinessFieldService;
import com.seagox.lowcode.template.FieldModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class BusinessFieldService implements IBusinessFieldService {

    @Autowired
    private BusinessFieldMapper businessFieldMapper;

    @Autowired
    private BusinessTableMapper businessTableMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value(value = "${spring.datasource.url}")
    private String datasourceUrl;

    @Override
    public ResultData queryByTableName(String tableName) {
        LambdaQueryWrapper<BusinessTable> tableQw = new LambdaQueryWrapper<>();
        tableQw.eq(BusinessTable::getName, tableName);
        BusinessTable businessTable = businessTableMapper.selectOne(tableQw);
        if (businessTable != null) {
            LambdaQueryWrapper<BusinessField> fieldQw = new LambdaQueryWrapper<>();
            fieldQw.eq(BusinessField::getBusinessTableId, businessTable.getId());
            List<BusinessField> list = businessFieldMapper.selectList(fieldQw);
            return ResultData.success(list);
        } else {
            return ResultData.success(null);
        }
    }

    @Override
    public ResultData queryByTableId(Long businessTableId, String name, String remark) {
        LambdaQueryWrapper<BusinessField> qw = new LambdaQueryWrapper<>();
        qw.eq(BusinessField::getBusinessTableId, businessTableId)
                .like(!StringUtils.isEmpty(name), BusinessField::getName, name)
                .like(!StringUtils.isEmpty(remark), BusinessField::getRemark, remark);
        List<BusinessField> list = businessFieldMapper.selectList(qw);
        return ResultData.success(list);
    }

    @Transactional
    @Override
    public ResultData insert(BusinessField businessField) {
        LambdaQueryWrapper<BusinessField> qw = new LambdaQueryWrapper<>();
        qw.eq(BusinessField::getBusinessTableId, businessField.getBusinessTableId())
                .eq(BusinessField::getName, businessField.getName());

        Long count = businessFieldMapper.selectCount(qw);
        if (count > 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "字段已经存在");
        }
        try {
        	addColumn(businessField);
		} catch (BadSqlGrammarException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultData.warn(ResultCode.OTHER_ERROR, "数据库关键字，请重新输入");
		}
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData update(BusinessField businessField) {
        BusinessField originalBusinessField = businessFieldMapper.selectById(businessField.getId());
        if (originalBusinessField.getName().equals(businessField.getName())) {
            BusinessTable businessTable = businessTableMapper.selectById(businessField.getBusinessTableId());
            try {
            	modifyColumn(businessTable.getName(), businessField);
			} catch (BadSqlGrammarException e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	            return ResultData.warn(ResultCode.OTHER_ERROR, "数据库关键字，请重新输入");
			}
        } else {
            LambdaQueryWrapper<BusinessField> qw = new LambdaQueryWrapper<>();
            qw.eq(BusinessField::getBusinessTableId, businessField.getBusinessTableId())
                    .eq(BusinessField::getName, businessField.getName());

            Long count = businessFieldMapper.selectCount(qw);
            if (count > 0) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "字段已经存在");
            } else {
                BusinessTable businessTable = businessTableMapper.selectById(businessField.getBusinessTableId());
                try {
                	changeColumn(businessTable.getName(), businessField, originalBusinessField);
    			} catch (BadSqlGrammarException e) {
    				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    	            return ResultData.warn(ResultCode.OTHER_ERROR, "数据库关键字，请重新输入");
    			}
            }
        }
        businessFieldMapper.updateById(businessField);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData delete(Long id) {
        BusinessField businessField = businessFieldMapper.selectById(id);
        BusinessTable businessTable = businessTableMapper.selectById(businessField.getBusinessTableId());
        StringBuffer sql = new StringBuffer();
        sql.append("ALTER TABLE ");
        sql.append(businessTable.getName());
        sql.append(" DROP ");
        if (datasourceUrl.contains("oracle")
                || datasourceUrl.contains("sqlserver")) {
            sql.append(" column ");
        }
        sql.append(businessField.getName());
        jdbcTemplate.execute(sql.toString());
        businessFieldMapper.deleteById(id);
        return ResultData.success(null);
    }

    @Override
    public ResultData queryByTableId(Long tableId) {
        LambdaQueryWrapper<BusinessField> qw = new LambdaQueryWrapper<>();
        qw.eq(BusinessField::getBusinessTableId, tableId);
        List<BusinessField> list = businessFieldMapper.selectList(qw);
        return ResultData.success(list);
    }

	@Override
	public ResultData importHandle(Long tableId, List<FieldModel> resultList) {
        for(int i=0;i<resultList.size();i++) {
        	FieldModel fieldModel = resultList.get(i);
        	LambdaQueryWrapper<BusinessField> qw = new LambdaQueryWrapper<>();
        	qw.eq(BusinessField::getBusinessTableId, tableId)
                    .eq(BusinessField::getName, fieldModel.getName());
            Long count = businessFieldMapper.selectCount(qw);
            if (count > 0) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "第" + (i+2) + "行的错误是：" + "字段名" + fieldModel.getName() +"已经存在");
            }
        }
        for(int i=0;i<resultList.size();i++) {
        	FieldModel fieldModel = resultList.get(i);
        	BusinessField businessField = new BusinessField();
        	businessField.setBusinessTableId(tableId);
        	businessField.setName(fieldModel.getName());
        	businessField.setRemark(fieldModel.getRemark());
        	businessField.setType(fieldModel.getType());
        	businessField.setKind(fieldModel.getKind());
        	if(fieldModel.getLength() == null) {
        		businessField.setLength(30);
        	} else {
        		businessField.setLength(fieldModel.getLength());
        	}
        	if(fieldModel.getDecimals() == null) {
        		businessField.setDecimals(0);
        	} else {
        		businessField.setDecimals(fieldModel.getDecimals());
        	}
        	if(fieldModel.getNotNull() == null) {
        		businessField.setNotNull(0);
        	} else {
        		businessField.setNotNull(fieldModel.getNotNull());
        	}
        	businessField.setDefaultValue(fieldModel.getDefaultValue());
        	addColumn(businessField);
        }
        return ResultData.success(null);
	}

    @Override
    public ResultData queryByTableIds(String tableIds) {
        LambdaQueryWrapper<BusinessField> qw = new LambdaQueryWrapper<>();
        qw.in(!StringUtils.isEmpty(tableIds), BusinessField::getBusinessTableId, Arrays.asList(tableIds.split(",")));
        List<BusinessField> list = businessFieldMapper.selectList(qw);
        List<Map<String, Object>> tableHeaderList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> tableHeader = new HashMap<>();
            tableHeader.put("id", i+1);
            tableHeader.put("prop", list.get(i).getName());
            tableHeader.put("label", list.get(i).getRemark());
            tableHeader.put("locking", 3);
            tableHeader.put("summary", 2);
            tableHeader.put("total", 2);
            tableHeader.put("target", 0);
            tableHeader.put("sort", i);
            tableHeaderList.add(tableHeader);
        }
        return ResultData.success(tableHeaderList);
    }

    public void addColumn(BusinessField businessField) {
		BusinessTable businessTable = businessTableMapper.selectById(businessField.getBusinessTableId());
        if (datasourceUrl.contains("mysql")) {
            String sql = "ALTER TABLE table_name ADD COLUMN column_name column_type column_default COMMENT 'comment_name'";
            sql = sql.replaceAll("table_name", businessTable.getName());
            sql = sql.replaceAll("column_name", businessField.getName());
            if (businessField.getType().equals("decimal")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + 18 + "," + businessField.getDecimals() + ")");
            } else if (businessField.getType().equals("varchar")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + ")");
            } else if (businessField.getType().equals("timestamp")) {
                sql = sql.replaceAll("column_type", "datetime");
            } else if (businessField.getType().equals("text")) {
                sql = sql.replaceAll("column_type", "longtext");
            } else {
                sql = sql.replaceAll("column_type", businessField.getType());
            }
            if (businessField.getNotNull() == 1) {
                //不为空
                String columnDefault = "NOT NULL";
                if (!StringUtils.isEmpty(businessField.getDefaultValue())) {
                    columnDefault = columnDefault + " DEFAULT " + businessField.getDefaultValue();
                }
                sql = sql.replaceAll("column_default", columnDefault);
            } else {
                //为空
                String columnDefault = "";
                if (!StringUtils.isEmpty(businessField.getDefaultValue())) {
                    columnDefault = "DEFAULT " + businessField.getDefaultValue();
                } else {
                    columnDefault = "DEFAULT NULL";
                }
                sql = sql.replaceAll("column_default", columnDefault);
            }
            sql = sql.replaceAll("comment_name", businessField.getRemark());
            jdbcTemplate.execute(sql);
        } else if (datasourceUrl.contains("postgresql")
                || datasourceUrl.contains("kingbase8")
                || datasourceUrl.contains("dm")) {
            String sql = "ALTER TABLE table_name ADD COLUMN column_name column_type column_default;COMMENT ON COLUMN table_name.column_name IS 'comment_name';";
            sql = sql.replaceAll("table_name", businessTable.getName());
            sql = sql.replaceAll("column_name", businessField.getName());
            if (businessField.getNotNull() == 1) {
                //不为空
                String columnDefault = "NOT NULL";
                if (!StringUtils.isEmpty(businessField.getDefaultValue())) {
                    columnDefault = columnDefault + " DEFAULT " + businessField.getDefaultValue();
                }
                sql = sql.replaceAll("column_default", columnDefault);
            } else {
                //为空
                String columnDefault = "";
                if (!StringUtils.isEmpty(businessField.getDefaultValue())) {
                    columnDefault = "DEFAULT " + businessField.getDefaultValue();
                } else {
                    columnDefault = "DEFAULT NULL";
                }
                sql = sql.replaceAll("column_default", columnDefault);
            }

            if (businessField.getType().equals("decimal")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + 18 + "," + businessField.getDecimals() + ")");
            } else if (businessField.getType().equals("varchar")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + ")");
            } else {
                sql = sql.replaceAll("column_type", businessField.getType());
            }
            sql = sql.replaceAll("comment_name", businessField.getRemark());
            jdbcTemplate.execute(sql);
        } else if (datasourceUrl.contains("oracle")) {
            String sql = "ALTER TABLE table_name ADD column_name column_type column_default;COMMENT ON COLUMN table_name.column_name IS 'comment_name'";
            sql = sql.replaceAll("table_name", businessTable.getName());
            sql = sql.replaceAll("column_name", businessField.getName());
            if (businessField.getNotNull() == 1) {
                //不为空
                String columnDefault = "NOT NULL";
                if (!StringUtils.isEmpty(businessField.getDefaultValue())) {
                    columnDefault = columnDefault + " DEFAULT '" + businessField.getDefaultValue() + "'";
                }
                sql = sql.replaceAll("column_default", columnDefault);
            } else {
                //为空
                String columnDefault = "";
                if (!StringUtils.isEmpty(businessField.getDefaultValue())) {
                    columnDefault = "DEFAULT '" + businessField.getDefaultValue() + "'";
                } else {
                    columnDefault = "DEFAULT NULL";
                }
                sql = sql.replaceAll("column_default", columnDefault);
            }

            if (businessField.getType().equals("integer")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + ")");
            } else if (businessField.getType().equals("bigint")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + ")");
            } else if (businessField.getType().equals("decimal")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + "," + businessField.getDecimals() + ")");
            } else if (businessField.getType().equals("varchar")) {
                sql = sql.replaceAll("column_type", "varchar2" + "(" + businessField.getLength() + ")");
            } else if (businessField.getType().equals("text")) {
                sql = sql.replaceAll("column_type", "clob");
            } else {
                sql = sql.replaceAll("column_type", businessField.getType());
            }
            sql = sql.replaceAll("comment_name", businessField.getRemark());
            String[] splitSql = sql.split(";");
            for (String oracleSql : splitSql) {
                jdbcTemplate.execute(oracleSql);
            }
        }
        businessFieldMapper.insert(businessField);
	}
    
    public void modifyColumn(String tableName, BusinessField businessField) {
    	if (datasourceUrl.contains("mysql")) {
            String sql = "ALTER TABLE table_name MODIFY COLUMN column_name column_type column_default COMMENT 'comment_name'";
            sql = sql.replaceAll("table_name", tableName);
            sql = sql.replaceAll("column_name", businessField.getName());
            if (businessField.getType().equals("decimal")) {
                sql = "ALTER TABLE " + tableName + " ALTER COLUMN " + businessField.getName() + " SET DEFAULT 0;" + sql;
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + 18 + "," + businessField.getDecimals() + ")");
                businessField.setLength(18);
            } else if (businessField.getType().equals("varchar")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + ")");
            } else if (businessField.getType().equals("timestamp")) {
                sql = sql.replaceAll("column_type", "datetime");
            } else if (businessField.getType().equals("text")) {
                sql = sql.replaceAll("column_type", "longtext");
            } else {
                sql = sql.replaceAll("column_type", businessField.getType());
            }
            if (businessField.getNotNull() == 1) {
                //不为空
                String columnDefault = "NOT NULL";
                if (!StringUtils.isEmpty(businessField.getDefaultValue())) {
                    columnDefault = columnDefault + " DEFAULT " + businessField.getDefaultValue();
                }
                sql = sql.replaceAll("column_default", columnDefault);
            } else {
                //为空
                String columnDefault = "";
                if (!StringUtils.isEmpty(businessField.getDefaultValue())) {
                    columnDefault = "DEFAULT " + businessField.getDefaultValue();
                } else {
                    columnDefault = "DEFAULT NULL";
                }
                sql = sql.replaceAll("column_default", columnDefault);
            }
            sql = sql.replaceAll("comment_name", businessField.getRemark());
            String[] splitSql = sql.split(";");
            for (String oracleSql : splitSql) {
                jdbcTemplate.execute(oracleSql);
            }
        } else if (datasourceUrl.contains("postgresql")
                || datasourceUrl.contains("kingbase8")
                || datasourceUrl.contains("dm")) {
            String sql = "ALTER TABLE table_name ALTER COLUMN column_name TYPE column_type USING column_name::column_type;COMMENT ON COLUMN table_name.column_name IS 'comment_name';";
            sql = sql.replaceAll("table_name", tableName);
            sql = sql.replaceAll("column_name", businessField.getName());
            if (businessField.getType().equals("decimal")) {
                sql = "ALTER TABLE " + tableName + " ALTER COLUMN " + businessField.getName() + " SET DEFAULT 0;" + sql;
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + 18 + "," + businessField.getDecimals() + ")");
                businessField.setLength(18);
            } else if (businessField.getType().equals("varchar")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + ")");
            } else {
                sql = sql.replaceAll("column_type", businessField.getType());
            }
            sql = sql.replaceAll("comment_name", businessField.getRemark());
            jdbcTemplate.execute(sql);
        } else if (datasourceUrl.contains("oracle")) {
            String sql = "ALTER TABLE table_name MODIFY (column_name column_type);COMMENT ON COLUMN table_name.column_name IS 'comment_name'";
            sql = sql.replaceAll("table_name", tableName);
            sql = sql.replaceAll("column_name", businessField.getName());
            if (businessField.getType().equals("integer")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + ")");
            } else if (businessField.getType().equals("bigint")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + ")");
            } else if (businessField.getType().equals("decimal")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + "," + businessField.getDecimals() + ")");
            } else if (businessField.getType().equals("varchar")) {
                sql = sql.replaceAll("column_type", "varchar2" + "(" + businessField.getLength() + ")");
            } else if (businessField.getType().equals("text")) {
                sql = sql.replaceAll("column_type", "clob");
            } else {
                sql = sql.replaceAll("column_type", businessField.getType());
            }
            sql = sql.replaceAll("comment_name", businessField.getRemark());
            String[] splitSql = sql.split(";");
            for (String oracleSql : splitSql) {
                jdbcTemplate.execute(oracleSql);
            }
        }
    }
    
    public void changeColumn(String tableName, BusinessField businessField, BusinessField originalBusinessField) {
    	if (datasourceUrl.contains("mysql")) {
            String sql = "ALTER TABLE table_name CHANGE old_column_name new_column_name column_type column_default COMMENT 'comment_name'";
            sql = sql.replaceAll("table_name", tableName);
            sql = sql.replaceAll("old_column_name", originalBusinessField.getName());
            sql = sql.replaceAll("new_column_name", businessField.getName());
            if (businessField.getType().equals("decimal")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + "," + businessField.getDecimals() + ")");
            } else if (businessField.getType().equals("varchar")) {
                sql = sql.replaceAll("column_type", businessField.getType() + "(" + businessField.getLength() + ")");
            } else {
                sql = sql.replaceAll("column_type", businessField.getType());
            }
            if (businessField.getNotNull() == 1) {
                //不为空
                String columnDefault = "NOT NULL";
                if (!StringUtils.isEmpty(businessField.getDefaultValue())) {
                    columnDefault = columnDefault + " DEFAULT " + businessField.getDefaultValue();
                }
                sql = sql.replaceAll("column_default", columnDefault);
            } else {
                //为空
                String columnDefault = "";
                if (!StringUtils.isEmpty(businessField.getDefaultValue())) {
                    columnDefault = "DEFAULT " + businessField.getDefaultValue();
                } else {
                    columnDefault = "DEFAULT NULL";
                }
                sql = sql.replaceAll("column_default", columnDefault);
            }
            sql = sql.replaceAll("comment_name", businessField.getRemark());
            jdbcTemplate.execute(sql);
        } else if (datasourceUrl.contains("postgresql")
                || datasourceUrl.contains("kingbase8")
                || datasourceUrl.contains("dm")) {
            String sql = "ALTER TABLE table_name RENAME old_column_name TO new_column_name;COMMENT ON COLUMN table_name.new_column_name IS 'comment_name';";
            sql = sql.replaceAll("table_name", tableName);
            sql = sql.replaceAll("old_column_name", originalBusinessField.getName());
            sql = sql.replaceAll("new_column_name", businessField.getName());
            sql = sql.replaceAll("comment_name", businessField.getRemark());
            jdbcTemplate.execute(sql);
        } else if (datasourceUrl.contains("oracle")) {
            String sql = "ALTER TABLE table_name RENAME COLUMN old_column_name TO new_column_name;COMMENT ON COLUMN table_name.new_column_name IS 'comment_name'";
            sql = sql.replaceAll("table_name", tableName);
            sql = sql.replaceAll("old_column_name", originalBusinessField.getName());
            sql = sql.replaceAll("new_column_name", businessField.getName());
            sql = sql.replaceAll("comment_name", businessField.getRemark());
            String[] splitSql = sql.split(";");
            for (String oracleSql : splitSql) {
                jdbcTemplate.execute(oracleSql);
            }
        }
    }
    
    @Transactional
	@Override
	public ResultData batch(String data) {
		JSONArray result = JSONArray.parseArray(data);
		for(int i=0;i<result.size();i++) {
			BusinessField businessField = JSON.toJavaObject(result.getJSONObject(i), BusinessField.class);
			LambdaQueryWrapper<BusinessField> qw = new LambdaQueryWrapper<>();
	        qw.eq(BusinessField::getBusinessTableId, businessField.getBusinessTableId())
	                .eq(BusinessField::getName, businessField.getName());

	        Long count = businessFieldMapper.selectCount(qw);
	        if (count > 0) {
	            return ResultData.warn(ResultCode.OTHER_ERROR, "第" + (i+1) + "行字段名已经存在");
	        }
	        try {
	        	addColumn(businessField);
			} catch (BadSqlGrammarException e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	            return ResultData.warn(ResultCode.OTHER_ERROR, "数据库关键字，请重新输入");
			}
		}
		return ResultData.success(null);
	}

}
