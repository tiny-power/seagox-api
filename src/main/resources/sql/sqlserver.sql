DROP TABLE IF EXISTS dbo.business_field;
CREATE TABLE dbo.business_field (
	id bigint PRIMARY KEY IDENTITY(1,1),
  	business_table_id bigint  NOT NULL,
  	name nvarchar(64)  NOT NULL,
  	remark nvarchar(64)  NOT NULL,
  	type nvarchar(20)  NOT NULL,
  	kind nvarchar(20)  NOT NULL,
  	length int  NULL,
  	decimals int  NULL,
  	not_null int  NULL,
  	default_value nvarchar(200)  NULL,
  	target_table_id bigint  DEFAULT NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'业务表id',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'business_table_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'注释',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'remark'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'类型',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'种类',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'kind'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'长度',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'length'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'小数',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'decimals'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'不为空(0:否;1:是;)',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'not_null'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'默认值',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'default_value'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'目标模型 ',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'target_table_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'业务字段',
	'SCHEMA', N'dbo',
	'TABLE', N'business_field'
GO

DROP TABLE IF EXISTS dbo.business_table;
CREATE TABLE dbo.business_table (
	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	name nvarchar(64)  NOT NULL,
  	remark nvarchar(64)  NOT NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'business_table',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'business_table',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'business_table',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'注释',
	'SCHEMA', N'dbo',
	'TABLE', N'business_table',
	'COLUMN', N'remark'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'business_table',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'business_table',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'业务表',
	'SCHEMA', N'dbo',
	'TABLE', N'business_table'
GO

DROP TABLE IF EXISTS dbo.company;
CREATE TABLE dbo.company (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	parent_id bigint  NULL,
  	mark nvarchar(30)  NOT NULL,
  	code nvarchar(30)  NOT NULL,
  	name nvarchar(30)  NOT NULL,
  	alias nvarchar(30)  NOT NULL,
  	logo nvarchar(100)  DEFAULT NULL,
  	sort int DEFAULT 0,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'company',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'上级id',
	'SCHEMA', N'dbo',
	'TABLE', N'company',
	'COLUMN', N'parent_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'编码',
	'SCHEMA', N'dbo',
	'TABLE', N'company',
	'COLUMN', N'code'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'标识',
	'SCHEMA', N'dbo',
	'TABLE', N'company',
	'COLUMN', N'mark'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'company',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'简称',
	'SCHEMA', N'dbo',
	'TABLE', N'company',
	'COLUMN', N'alias'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'logo',
	'SCHEMA', N'dbo',
	'TABLE', N'company',
	'COLUMN', N'logo'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'排序',
	'SCHEMA', N'dbo',
	'TABLE', N'company',
	'COLUMN', N'sort'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'company',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'company',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司',
	'SCHEMA', N'dbo',
	'TABLE', N'company'
GO

DROP TABLE IF EXISTS dbo.department;
CREATE TABLE dbo.department (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	parent_id bigint  NULL,
  	code nvarchar(30)  NOT NULL,
  	name nvarchar(30)  NOT NULL,
  	director nvarchar(500)  NULL,
  	charge_leader nvarchar(500)  NULL,
  	sort int DEFAULT 0,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'department',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'department',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'上级id',
	'SCHEMA', N'dbo',
	'TABLE', N'department',
	'COLUMN', N'parent_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'编码',
	'SCHEMA', N'dbo',
	'TABLE', N'department',
	'COLUMN', N'code'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'department',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'直接主管',
	'SCHEMA', N'dbo',
	'TABLE', N'department',
	'COLUMN', N'director'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'分管领导',
	'SCHEMA', N'dbo',
	'TABLE', N'department',
	'COLUMN', N'charge_leader'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'分管领导',
	'SCHEMA', N'dbo',
	'TABLE', N'department',
	'COLUMN', N'sort'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'department',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'department',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'部门',
	'SCHEMA', N'dbo',
	'TABLE', N'department'
GO

DROP TABLE IF EXISTS dbo.dept_user;
CREATE TABLE dbo.dept_user  (
    id bigint PRIMARY KEY IDENTITY(1,1),
    company_id bigint NOT NULL,
    department_id bigint NOT NULL,
    user_id bigint NOT NULL,
    create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO
EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', dept_user',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', dept_user',
	'COLUMN', company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'部门id',
	'SCHEMA', N'dbo',
	'TABLE', dept_user',
	'COLUMN', department_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'用户id',
	'SCHEMA', N'dbo',
	'TABLE', dept_user',
	'COLUMN', user_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', dept_user',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', dept_user',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'部门用户',
	'SCHEMA', N'dbo',
	'TABLE', dept_user'
GO

DROP TABLE IF EXISTS dbo.dic_classify;
CREATE TABLE dbo.dic_classify (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	name nvarchar(30)  NOT NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_classify',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_classify',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_classify',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_classify',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_classify',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'字典分类',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_classify'
GO

DROP TABLE IF EXISTS dbo.dic_detail;
CREATE TABLE dbo.dic_detail (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	parent_id bigint  NULL,
  	classify_id bigint  NOT NULL,
  	code nvarchar(30)  NOT NULL,
  	name nvarchar(30)  NOT NULL,
  	sort int  NULL,
  	status int  NULL,
  	last_stage int  NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'上级id',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail',
	'COLUMN', N'parent_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'字典分类id',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail',
	'COLUMN', N'classify_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'编码',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail',
	'COLUMN', N'code'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'排序',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail',
	'COLUMN', N'sort'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'状态(0:禁用;1:启用)',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail',
	'COLUMN', N'status'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'末级(0:否;1:是)',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail',
	'COLUMN', N'last_stage'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'字典详情',
	'SCHEMA', N'dbo',
	'TABLE', N'dic_detail'
GO

DROP TABLE IF EXISTS dbo.door;
CREATE TABLE dbo.door (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	name nvarchar(30)  NOT NULL,
  	authority nvarchar(max)  NULL,
  	path bigint NOT NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'door',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'door',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'door',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'权限',
	'SCHEMA', N'dbo',
	'TABLE', N'door',
	'COLUMN', N'authority'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'页面路径',
	'SCHEMA', N'dbo',
	'TABLE', N'door',
	'COLUMN', N'path'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'door',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'door',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'门户管理',
	'SCHEMA', N'dbo',
	'TABLE', N'door'
GO

DROP TABLE IF EXISTS dbo.form;
CREATE TABLE dbo.form (
	id bigint PRIMARY KEY IDENTITY(1,1),
	company_id bigint  NOT NULL,
	mark nvarchar(100)  NOT NULL,
	name nvarchar(30)  NOT NULL,
	data_source bigint  NOT NULL,
	workbook nvarchar(max)  NULL,
	options nvarchar(max)  NULL,
    create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
    update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'form',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'form',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'标识',
	'SCHEMA', N'dbo',
	'TABLE', N'form',
	'COLUMN', N'mark'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'form',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'数据源配置',
	'SCHEMA', N'dbo',
	'TABLE', N'form',
	'COLUMN', N'data_source'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'excel配置',
	'SCHEMA', N'dbo',
	'TABLE', N'form',
	'COLUMN', N'workbook'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'其他参数',
	'SCHEMA', N'dbo',
	'TABLE', N'form',
	'COLUMN', N'options'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'form',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'form',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'表单管理',
	'SCHEMA', N'dbo',
	'TABLE', N'form'
GO

DROP TABLE IF EXISTS dbo.form_athority;
CREATE TABLE dbo.form_athority (
	id bigint PRIMARY KEY IDENTITY(1,1),
	form_id bigint  NOT NULL,
	name nvarchar(30)  NOT NULL,
	members nvarchar(max)  NULL,
	type int  NULL,
	field nvarchar(max)  NULL,
	scope nvarchar(max)  NULL,
    create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
    update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'form_athority',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'表单id',
	'SCHEMA', N'dbo',
	'TABLE', N'form_athority',
	'COLUMN', N'form_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'form_athority',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'成员',
	'SCHEMA', N'dbo',
	'TABLE', N'form_athority',
	'COLUMN', N'members'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'类型(1:提交状态;2:查看状态;)',
	'SCHEMA', N'dbo',
	'TABLE', N'form_athority',
	'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'字段权限',
	'SCHEMA', N'dbo',
	'TABLE', N'form_athority',
	'COLUMN', N'field'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'数据范围',
	'SCHEMA', N'dbo',
	'TABLE', N'form_athority',
	'COLUMN', N'scope'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'form_athority',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'form_athority',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'表单权限',
	'SCHEMA', N'dbo',
	'TABLE', N'form_athority'
GO

DROP TABLE IF EXISTS dbo.gauge;
CREATE TABLE dbo.gauge (
	id bigint PRIMARY KEY IDENTITY(1,1),
	company_id bigint  NOT NULL,
	name nvarchar(30)  NOT NULL,
	config nvarchar(max)  NULL,
	script nvarchar(max)  NULL,
	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'gauge',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'gauge',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'gauge',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'配置',
	'SCHEMA', N'dbo',
	'TABLE', N'gauge',
	'COLUMN', N'config'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'脚本',
	'SCHEMA', N'dbo',
	'TABLE', N'gauge',
	'COLUMN', N'script'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'gauge',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'gauge',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'仪表板',
	'SCHEMA', N'dbo',
	'TABLE', N'gauge'
GO

DROP TABLE IF EXISTS dbo.import_rule;
CREATE TABLE dbo.import_rule  (
    id bigint PRIMARY KEY IDENTITY(1,1),
    form_id bigint NOT NULL,
    start_line bigint NOT NULL,
    rules text,
    create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'import_rule',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'表单id',
	'SCHEMA', N'dbo',
	'TABLE', N'import_rule',
	'COLUMN', N'form_id'
GO


EXEC sp_addextendedproperty
	'MS_Description', N'数据起始行',
	'SCHEMA', N'dbo',
	'TABLE', N'import_rule',
	'COLUMN', N'start_line'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'规则',
	'SCHEMA', N'dbo',
	'TABLE', N'import_rule',
	'COLUMN', N'rules'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'import_rule',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'import_rule',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'导入规则',
	'SCHEMA', N'dbo',
	'TABLE', N'import_rule'
GO


DROP TABLE IF EXISTS dbo.job;
CREATE TABLE dbo.job (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	name nvarchar(30)  NOT NULL,
  	cron nvarchar(30)  NOT NULL,
  	mark nvarchar(500)  NOT NULL,
  	status int  NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'job',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'job',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'job',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'表达式',
	'SCHEMA', N'dbo',
	'TABLE', N'job',
	'COLUMN', N'cron'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'标识',
	'SCHEMA', N'dbo',
	'TABLE', N'job',
	'COLUMN', N'mark'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'状态(0:未启动;1:已启动;)',
	'SCHEMA', N'dbo',
	'TABLE', N'job',
	'COLUMN', N'status'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'job',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'job',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'任务调度',
	'SCHEMA', N'dbo',
	'TABLE', N'job'
GO

DROP TABLE IF EXISTS dbo.sea_definition;
CREATE TABLE dbo.sea_definition (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	form_id bigint  NOT NULL,
  	name nvarchar(30)  NOT NULL,
  	resources nvarchar(max)  NULL,
  	empower nvarchar(max)  NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_definition',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'表单id',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_definition',
	'COLUMN', N'form_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_definition',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'流程文件',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_definition',
	'COLUMN', N'resources'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'授权',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_definition',
	'COLUMN', N'empower'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_definition',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_definition',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'流程定义',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_definition'
GO

DROP TABLE IF EXISTS dbo.sea_instance;
CREATE TABLE dbo.sea_instance (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	user_id bigint  NOT NULL,
  	name nvarchar(50)  NOT NULL,
  	business_type nvarchar(50)  NOT NULL,
  	business_key nvarchar(50)  NOT NULL,
  	resources nvarchar(max)  NOT NULL,
  	status int  NULL,
  	start_time datetime2(7)  NULL,
  	end_time datetime2(7)  NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'用户id',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'业务类型',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'business_type'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'业务key',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'business_key'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'流程文件',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'resources'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'状态(0:活动;1:完成;2:暂停;3:终止;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'status'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'开始时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'start_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'结束时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'end_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'流程实例',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_instance'
GO

DROP TABLE IF EXISTS dbo.sea_node;
CREATE TABLE dbo.sea_node (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	def_id bigint  NOT NULL,
  	mark nvarchar(50)  NOT NULL,
  	label nvarchar(50)  NOT NULL,
  	type int  NULL,
  	status int  NULL,
  	start_time datetime2(7)  NULL,
  	end_time datetime2(7)  NULL,
  	precede nvarchar(500) NULL,
  	path nvarchar(5000)  NOT NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'流程实例id',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'def_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'节点id',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'mark'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'节点名称',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'label'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'类型(1:审批;2:抄送;3:撤回;4:驳回;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'状态(0:待办;1:通过;2:不通过;3:作废;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'status'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'开始时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'start_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'结束时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'end_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'前导',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'precede'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'路径',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'path'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'流程节点',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node'
GO

DROP TABLE IF EXISTS dbo.sea_node_detail;
CREATE TABLE dbo.sea_node_detail (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	node_id bigint  NOT NULL,
  	name nvarchar(30)  NOT NULL,
  	assignee nvarchar(30)  NOT NULL,
  	status int  NULL,
  	remark nvarchar(255)  NULL,
  	start_time datetime2(7)  NULL,
  	end_time datetime2(7)  NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'流程节点id',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'node_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'签收人或被委托id',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'assignee'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'状态(0:待办;1:同意;2:拒绝;3:已阅;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'status'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'评论',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'remark'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'开始时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'start_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'结束时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'end_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'流程节点详情',
	'SCHEMA', N'dbo',
	'TABLE', N'sea_node_detail'
GO

DROP TABLE IF EXISTS dbo.serial;
CREATE TABLE dbo.serial (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	name nvarchar(30)  NOT NULL,
  	options nvarchar(max) NOT NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'serial',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'serial',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'serial',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'要素',
	'SCHEMA', N'dbo',
	'TABLE', N'serial',
	'COLUMN', N'options'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'serial',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'serial',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'编号设置',
	'SCHEMA', N'dbo',
	'TABLE', N'serial'
GO

DROP TABLE IF EXISTS dbo.shortcut;
CREATE TABLE dbo.shortcut  (
    id bigint PRIMARY KEY IDENTITY(1,1),
    company_id bigint NOT NULL,
    user_id bigint NOT NULL,
    menu_id bigint NOT NULL,
    create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO
EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', shortcut',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', shortcut',
	'COLUMN', company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'用户id',
	'SCHEMA', N'dbo',
	'TABLE', shortcut',
	'COLUMN', user_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'菜单id',
	'SCHEMA', N'dbo',
	'TABLE', shortcut',
	'COLUMN', menu_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', shortcut',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', shortcut',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'快捷入口',
	'SCHEMA', N'dbo',
	'TABLE', shortcut'
GO

DROP TABLE IF EXISTS dbo.sys_account;
CREATE TABLE dbo.sys_account (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	avatar nvarchar(255)  NULL,
  	account nvarchar(50)  NOT NULL,
  	email nvarchar(50)  NULL,
  	phone nvarchar(30)  NULL,
  	name nvarchar(50)  NOT NULL,
  	sex int  NULL,
  	password nvarchar(255)  NOT NULL,
  	position nvarchar(50)  NULL,
  	status int  NULL,
  	type int  NULL,
  	openid nvarchar(50)  NULL,
  	sort int  NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'头像',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'avatar'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'账号',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'account'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'邮箱',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'email'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'手机号',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'phone'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'姓名',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'性别(1:男;2:女;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'sex'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'密码',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'password'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'职位',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'position'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'状态(1:启用;2:禁用;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'status'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'类型(1:普通成员;2:管理员;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'openid',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'openid'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'排序',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'sort'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'用户',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_account'
GO

DROP TABLE IF EXISTS dbo.sys_icon;
CREATE TABLE dbo.sys_icon  (
    id bigint PRIMARY KEY IDENTITY(1,1),
    name nvarchar(30) NOT NULL,
    font nvarchar(50) NOT NULL
)
GO
EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_icon',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_icon',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'font_class',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_icon',
	'COLUMN', N'font'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'icon数据',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_icon'
GO

DROP TABLE IF EXISTS dbo.sys_log;
CREATE TABLE dbo.sys_log (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	user_id bigint  NOT NULL,
  	name nvarchar(50)  NOT NULL,
  	ip nvarchar(50)  NOT NULL,
  	uri nvarchar(200)  NOT NULL,
  	method nvarchar(255)  NOT NULL,
  	params nvarchar(max)  NULL,
  	ua nvarchar(500)  NULL,
  	status int  NULL,
  	cost_time int  NOT NULL,
  	result nvarchar(max)  NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'用户id',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'操作名称',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'ip',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'ip'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'uri',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'uri'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'方法',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'method'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'请求参数',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'params'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'浏览器信息',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'ua'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'状态(1:成功;2:失败;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'status'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'花费时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'cost_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'返回结果',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'result'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'操作日记',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_log'
GO

DROP TABLE IF EXISTS dbo.sys_menu;
CREATE TABLE dbo.sys_menu (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	parent_id bigint  NULL,
  	classify int  DEFAULT 1,
  	type int  NULL,
  	name nvarchar(30)  NOT NULL,
  	icon nvarchar(50)  NOT NULL,
  	path nvarchar(50)  NULL,
  	status int  NULL,
  	sort int  NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'上级id',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'parent_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'类别(1:PC端;2:移动端;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'classify'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'类型(1:表单列表;2:按钮;3:新增表单;4:系统菜单;5:目录;6:仪表板;7:单页面;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'图标',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'icon'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'路径',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'path'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'状态(1:启用;2:禁用;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'status'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'排序',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'sort'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'菜单',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_menu'
GO

DROP TABLE IF EXISTS dbo.sys_message;
CREATE TABLE dbo.sys_message (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	type int  NULL,
  	from_user_id bigint  NOT NULL,
  	to_user_id bigint  NOT NULL,
  	title nvarchar(50)  NOT NULL,
  	business_type bigint  NULL,
  	business_key bigint  NULL,
  	status int  NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'类型(1:暂存数据;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'用户id(来自)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'from_user_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'用户id(给谁)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'to_user_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'标题',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'title'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'业务类型',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'business_type'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'业务key',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'business_key'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'状态(0:未读;1:已读;)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'status'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'消息表',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_message'
GO

DROP TABLE IF EXISTS dbo.sys_role;
CREATE TABLE dbo.sys_role (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	company_id bigint  NOT NULL,
  	name nvarchar(30)  NOT NULL,
  	path nvarchar(max)  NOT NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_role',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'公司id',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_role',
	'COLUMN', N'company_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'名称',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_role',
	'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'菜单权限(以,隔开)',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_role',
	'COLUMN', N'path'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_role',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_role',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'系统角色',
	'SCHEMA', N'dbo',
	'TABLE', N'sys_role'
GO

DROP TABLE IF EXISTS dbo.table_column_config;
CREATE TABLE dbo.table_column_config (
  	id bigint PRIMARY KEY IDENTITY(1,1),
  	user_id bigint  NOT NULL,
  	form_id bigint  NOT NULL,
  	options nvarchar(max)  NOT NULL,
  	create_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP,
  	update_time datetime2(7)  DEFAULT CURRENT_TIMESTAMP
)
GO

EXEC sp_addextendedproperty
	'MS_Description', N'主键',
	'SCHEMA', N'dbo',
	'TABLE', N'table_column_config',
	'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'用户id',
	'SCHEMA', N'dbo',
	'TABLE', N'table_column_config',
	'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'表单id',
	'SCHEMA', N'dbo',
	'TABLE', N'table_column_config',
	'COLUMN', N'form_id'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'配置',
	'SCHEMA', N'dbo',
	'TABLE', N'table_column_config',
	'COLUMN', N'options'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'创建时间',
	'SCHEMA', N'dbo',
	'TABLE', N'table_column_config',
	'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'更新时间',
	'SCHEMA', N'dbo',
	'TABLE', N'table_column_config',
	'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
	'MS_Description', N'表头配置',
	'SCHEMA', N'dbo',
	'TABLE', N'table_column_config'
GO

INSERT INTO dbo.company VALUES (1, NULL, 'seagox', '1001', '默认单位', '默认单位', NULL, 1, now(), now());
INSERT INTO dbo.department VALUES (1, 1, NULL, '101', '默认部门', NULL, NULL, 0, now(), now());
INSERT INTO dbo.sys_role VALUES (1, 1, '管理员', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19', now(), now());
INSERT INTO dbo.sys_account VALUES (1, NULL, 'admin', NULL, NULL, '管理员', 1, '$2a$10$7xaqWKLFZRc2mg7JIX.B/OCtijP2zYZack60pbC3WxDGvtfvKld3W', NULL, 1, 2, NULL, 0, now(), now());
INSERT INTO dbo.user_role VALUES (1, 1, 1, 1, now(), now());
INSERT INTO dbo.dept_user VALUES (1, 1, 1, 1, now(), now());
INSERT INTO dbo.sys_menu VALUES (1, 1, NULL, 5, '组织架构', 'iconfont icon-xihuan', 'organization', 1, 1, now(), now());
INSERT INTO dbo.sys_menu VALUES (2, 1, 1, 4, '人员管理', 'iconfont icon-xihuan', 'contact', 1, 1,  now(), now());
INSERT INTO dbo.sys_menu VALUES (3, 1, 1, 4, '角色管理', 'iconfont icon-xihuan', 'role', 1, 2,  now(), now());
INSERT INTO dbo.sys_menu VALUES (4, 1, 2, 2, '导出用户', 'iconfont icon-xihuan', 'user:export', 1, 3,  now(), now());
INSERT INTO dbo.sys_menu VALUES (5, 1, 2, 2, '导出用户模板', 'iconfont icon-xihuan', 'user:download', 1, 1,  now(), now());
INSERT INTO dbo.sys_menu VALUES (6, 1, 2, 2, '导出部门模板', 'iconfont icon-xihuan', 'dept:download', 1, 1,  now(), now());
INSERT INTO dbo.sys_menu VALUES (7, 1, 2, 2, '新增用户', 'iconfont icon-xihuan', 'user:add', 1, 4,  now(), now());
INSERT INTO dbo.sys_menu VALUES (8, 1, 2, 2, '编辑用户', 'iconfont icon-xihuan', 'user:edit', 1, 5,  now(), now());
INSERT INTO dbo.sys_menu VALUES (9, 1, 2, 2, '删除用户', 'iconfont icon-xihuan', 'user:delete', 1, 6,  now(), now());
INSERT INTO dbo.sys_menu VALUES (10, 1, 2, 2, '密码重置', 'iconfont icon-xihuan', 'user:reset', 1, 7,  now(), now());
INSERT INTO dbo.sys_menu VALUES (11, 1, 2, 2, '导入用户', 'iconfont icon-xihuan', 'user:import', 1, 2,  now(), now());
INSERT INTO dbo.sys_menu VALUES (12, 1, 2, 2, '新增部门', 'iconfont icon-xihuan', 'dept:add', 1, 8,  now(), now());
INSERT INTO dbo.sys_menu VALUES (13, 1, 2, 2, '编辑部门', 'iconfont icon-xihuan', 'dept:edit', 1, 9,  now(), now());
INSERT INTO dbo.sys_menu VALUES (14, 1, 2, 2, '删除部门', 'iconfont icon-xihuan', 'dept:delete', 1, 10,  now(), now());
INSERT INTO dbo.sys_menu VALUES (15, 1, 3, 2, '新增', 'iconfont icon-xihuan', 'role:add', 1, 1,  now(), now());
INSERT INTO dbo.sys_menu VALUES (16, 1, 3, 2, '编辑', 'iconfont icon-xihuan', 'role:edit', 1, 2,  now(), now());
INSERT INTO dbo.sys_menu VALUES (17, 1, 3, 2, '删除', 'iconfont icon-xihuan', 'role:delete', 1, 3,  now(), now());
INSERT INTO dbo.sys_menu VALUES (18, 1, 2, 2, '导入部门', 'iconfont icon-xihuan', 'dept:import', 1, 11,  now(), now());
INSERT INTO dbo.sys_menu VALUES (19, 1, 3, 2, '授权', 'iconfont icon-xihuan', 'role:authorize', 1, 4,  now(), now());
GO
