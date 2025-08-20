DROP TABLE IF EXISTS "public"."business_field";
CREATE TABLE "public"."business_field" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"business_table_id" BIGINT NOT NULL,
	"name" VARCHAR(64) NOT NULL,
	"remark" VARCHAR(64) NOT NULL,
	"type" VARCHAR(20) NOT NULL,
	"kind" VARCHAR(20) NOT NULL,
	"length" INTEGER DEFAULT 0,
	"decimals" INTEGER DEFAULT 0,
	"not_null" INTEGER DEFAULT 0,
	"default_value" VARCHAR(200),
	"target_table_id" BIGINT DEFAULT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."business_field"."id" IS '主键';
COMMENT ON COLUMN "public"."business_field"."business_table_id" IS '业务表id';
COMMENT ON COLUMN "public"."business_field"."name" IS '名称';
COMMENT ON COLUMN "public"."business_field"."remark" IS '注释';
COMMENT ON COLUMN "public"."business_field"."type" IS '类型';
COMMENT ON COLUMN "public"."business_field"."kind" IS '种类';
COMMENT ON COLUMN "public"."business_field"."length" IS '长度';
COMMENT ON COLUMN "public"."business_field"."decimals" IS '小数';
COMMENT ON COLUMN "public"."business_field"."not_null" IS '不为空(1:是;0:否;)';
COMMENT ON COLUMN "public"."business_field"."default_value" IS '默认值';
COMMENT ON COLUMN "public"."business_field"."target_table_id" IS '目标模型';
COMMENT ON COLUMN "public"."business_field"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."business_field"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."business_field" IS '业务字段';

DROP TABLE IF EXISTS "public"."business_table";
CREATE TABLE "public"."business_table" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"name" VARCHAR(64) NOT NULL,
	"remark" VARCHAR(64) NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."business_table"."id" IS '主键';
COMMENT ON COLUMN "public"."business_table"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."business_table"."name" IS '名称';
COMMENT ON COLUMN "public"."business_table"."remark" IS '注释';
COMMENT ON COLUMN "public"."business_table"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."business_table"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."business_table" IS '业务表';

DROP TABLE IF EXISTS "public"."company";
CREATE TABLE "public"."company" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"parent_id" BIGINT,
	"mark" VARCHAR(30) NOT NULL,
	"code" VARCHAR(30) NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"alias" VARCHAR(30) NOT NULL,
	"logo" VARCHAR(100) DEFAULT NULL,
    "sort" INTEGER DEFAULT 0,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."company"."id" IS '主键';
COMMENT ON COLUMN "public"."company"."parent_id" IS '上级id';
COMMENT ON COLUMN "public"."company"."mark" IS '标识';
COMMENT ON COLUMN "public"."company"."code" IS '编码';
COMMENT ON COLUMN "public"."company"."name" IS '名称';
COMMENT ON COLUMN "public"."company"."alias" IS '简称';
COMMENT ON COLUMN "public"."company"."logo" IS 'logo';
COMMENT ON COLUMN "public"."company"."sort" IS '排序';
COMMENT ON COLUMN "public"."company"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."company"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."company" IS '公司';

DROP TABLE IF EXISTS "public"."department";
CREATE TABLE "public"."department" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"parent_id" BIGINT,
	"code" VARCHAR(30) NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"director" VARCHAR(500),
	"charge_leader" VARCHAR(500),
	"sort" INTEGER DEFAULT 0,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."department"."id" IS '主键';
COMMENT ON COLUMN "public"."department"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."department"."parent_id" IS '上级id';
COMMENT ON COLUMN "public"."department"."code" IS '编码';
COMMENT ON COLUMN "public"."department"."name" IS '名称';
COMMENT ON COLUMN "public"."department"."director" IS '直接主管';
COMMENT ON COLUMN "public"."department"."charge_leader" IS '分管领导';
COMMENT ON COLUMN "public"."department"."sort" IS '排序';
COMMENT ON COLUMN "public"."department"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."department"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."department" IS '部门';

DROP TABLE IF EXISTS "public"."dept_user";
CREATE TABLE "public"."dept_user" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"department_id" BIGINT NOT NULL,
	"user_id" BIGINT NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."dept_user"."id" IS '主键';
COMMENT ON COLUMN "public"."dept_user"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."dept_user"."department_id" IS '部门id';
COMMENT ON COLUMN "public"."dept_user"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."dept_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."dept_user"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."dept_user" IS '部门用户';

DROP TABLE IF EXISTS "public"."dic_classify";
CREATE TABLE "public"."dic_classify" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."dic_classify"."id" IS '主键';
COMMENT ON COLUMN "public"."dic_classify"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."dic_classify"."name" IS '名称';
COMMENT ON COLUMN "public"."dic_classify"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."dic_classify"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."dic_classify" IS '字典分类';

DROP TABLE IF EXISTS "public"."dic_detail";
CREATE TABLE "public"."dic_detail" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"parent_id" BIGINT,
	"classify_id" BIGINT NOT NULL,
	"code" VARCHAR(30) NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"sort" INTEGER DEFAULT 1,
	"status" INTEGER DEFAULT 1,
	"last_stage" INTEGER DEFAULT 1,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."dic_detail"."id" IS '主键';
COMMENT ON COLUMN "public"."dic_detail"."parent_id" IS '上级id';
COMMENT ON COLUMN "public"."dic_detail"."classify_id" IS '字典分类id';
COMMENT ON COLUMN "public"."dic_detail"."code" IS '编码';
COMMENT ON COLUMN "public"."dic_detail"."name" IS '名称';
COMMENT ON COLUMN "public"."dic_detail"."sort" IS '排序';
COMMENT ON COLUMN "public"."dic_detail"."status" IS '状态(0:禁用;1:启用)';
COMMENT ON COLUMN "public"."dic_detail"."last_stage" IS '末级(0:否;1:是)';
COMMENT ON COLUMN "public"."dic_detail"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."dic_detail"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."dic_detail" IS '字典详情';

DROP TABLE IF EXISTS "public"."door";
CREATE TABLE "public"."door" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"authority" TEXT,
	"path" BIGINT NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."door"."id" IS '主键';
COMMENT ON COLUMN "public"."door"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."door"."name" IS '名称';
COMMENT ON COLUMN "public"."door"."authority" IS '权限';
COMMENT ON COLUMN "public"."door"."path" IS '页面路径';
COMMENT ON COLUMN "public"."door"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."door"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."door" IS '门户管理';

DROP TABLE IF EXISTS "public"."form";
CREATE TABLE "public"."form" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"mark" VARCHAR(100) NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"data_source" BIGINT NOT NULL,
	"workbook" TEXT NOT NULL,
	"options" TEXT,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."form"."id" IS '主键';
COMMENT ON COLUMN "public"."form"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."form"."mark" IS '标识';
COMMENT ON COLUMN "public"."form"."name" IS '名称';
COMMENT ON COLUMN "public"."form"."data_source" IS '数据源配置';
COMMENT ON COLUMN "public"."form"."workbook" IS 'excel配置';
COMMENT ON COLUMN "public"."form"."options" IS '其他参数';
COMMENT ON COLUMN "public"."form"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."form"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."form" IS '表单管理';

DROP TABLE IF EXISTS "public"."form_athority";
CREATE TABLE "public"."form_athority"  (
    "id" BIGSERIAL PRIMARY KEY NOT NULL,
	"form_id" BIGINT NOT NULL,
    "name" VARCHAR(30) NOT NULL,
    "members" text NOT NULL,
    "type" INTEGER DEFAULT NULL,
	"field" text NOT NULL,
	"scope" text,
    "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."form_athority"."id" IS '主键';
COMMENT ON COLUMN "public"."form_athority"."form_id" IS '表单id';
COMMENT ON COLUMN "public"."form_athority"."name" IS '名称';
COMMENT ON COLUMN "public"."form_athority"."members" IS '权限成员';
COMMENT ON COLUMN "public"."form_athority"."type" IS '类型(1:提交状态;2:查看状态;)';
COMMENT ON COLUMN "public"."form_athority"."field" IS '字段权限';
COMMENT ON COLUMN "public"."form_athority"."scope" IS '数据范围';
COMMENT ON TABLE "public"."form_athority" IS '表单权限';

DROP TABLE IF EXISTS "public"."gauge";
CREATE TABLE "public"."gauge" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"config" TEXT,
	"script" TEXT,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."gauge"."id" IS '主键';
COMMENT ON COLUMN "public"."gauge"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."gauge"."name" IS '名称';
COMMENT ON COLUMN "public"."gauge"."config" IS '配置';
COMMENT ON COLUMN "public"."gauge"."script" IS '脚本';
COMMENT ON COLUMN "public"."gauge"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."gauge"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."gauge" IS '仪表板';

DROP TABLE IF EXISTS "public"."import_rule";
CREATE TABLE "public"."import_rule"  (
    "id" BIGSERIAL PRIMARY KEY NOT NULL,
    "form_id" BIGINT NOT NULL,
    "start_line" BIGINT DEFAULT 2,
    "rules" text,
    "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    "update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."import_rule"."id" IS '主键';
COMMENT ON COLUMN "public"."import_rule"."form_id" IS '表单id';
COMMENT ON COLUMN "public"."import_rule"."start_line" IS '数据起始行';
COMMENT ON COLUMN "public"."import_rule"."rules" IS '规则';
COMMENT ON COLUMN "public"."import_rule"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."import_rule"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."import_rule" IS '导入规则';

DROP TABLE IF EXISTS "public"."job";
CREATE TABLE "public"."job" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"cron" VARCHAR(30) NOT NULL,
	"mark" VARCHAR(500) NOT NULL,
	"status" INTEGER DEFAULT 0,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."job"."id" IS '主键';
COMMENT ON COLUMN "public"."job"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."job"."name" IS '名称';
COMMENT ON COLUMN "public"."job"."cron" IS '表达式';
COMMENT ON COLUMN "public"."job"."mark" IS '标识';
COMMENT ON COLUMN "public"."job"."status" IS '状态(0:未启动;1:已启动;)';
COMMENT ON COLUMN "public"."job"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."job"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."job" IS '任务调度';

DROP TABLE IF EXISTS "public"."sea_definition";
CREATE TABLE "public"."sea_definition"  (
    "id" BIGSERIAL PRIMARY KEY NOT NULL,
	"form_id" BIGINT NOT NULL,
	"name" VARCHAR(30) NOT NULL,
    "resources" TEXT DEFAULT NULL,
    "empower" TEXT,
    "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."sea_definition"."id" IS '主键';
COMMENT ON COLUMN "public"."sea_definition"."form_id" IS '表单id';
COMMENT ON COLUMN "public"."sea_definition"."name" IS '名称';
COMMENT ON COLUMN "public"."sea_definition"."resources" IS '流程文件';
COMMENT ON COLUMN "public"."sea_definition"."empower" IS '授权';
COMMENT ON COLUMN "public"."sea_definition"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sea_definition"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sea_definition" IS '流程定义';

DROP TABLE IF EXISTS "public"."sea_instance";
CREATE TABLE "public"."sea_instance" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"user_id" BIGINT NOT NULL,
	"name" VARCHAR(50) NOT NULL,
	"business_type" VARCHAR(50) NOT NULL,
	"business_key" VARCHAR(50) NOT NULL,
	"resources" TEXT NOT NULL,
	"status" INTEGER DEFAULT 0,
	"start_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"end_time" TIMESTAMP,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."sea_instance"."id" IS '主键';
COMMENT ON COLUMN "public"."sea_instance"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."sea_instance"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."sea_instance"."name" IS '名称';
COMMENT ON COLUMN "public"."sea_instance"."business_type" IS '业务类型';
COMMENT ON COLUMN "public"."sea_instance"."business_key" IS '业务key';
COMMENT ON COLUMN "public"."sea_instance"."resources" IS '流程文件';
COMMENT ON COLUMN "public"."sea_instance"."status" IS '状态(0:活动;1:完成;2:暂停;3:终止;)';
COMMENT ON COLUMN "public"."sea_instance"."start_time" IS '开始时间';
COMMENT ON COLUMN "public"."sea_instance"."end_time" IS '结束时间';
COMMENT ON COLUMN "public"."sea_instance"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sea_instance"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sea_instance" IS '流程实例';

DROP TABLE IF EXISTS "public"."sea_node";
CREATE TABLE "public"."sea_node" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"def_id" BIGINT NOT NULL,
	"mark" VARCHAR(50) NOT NULL,
	"lable" VARCHAR(50) NOT NULL,
	"type" INTEGER DEFAULT 1,
	"status" INTEGER DEFAULT 0,
	"start_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"end_time" TIMESTAMP,
	"precede" VARCHAR(500) DEFAULT NULL,
	"path" VARCHAR(5000) NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."sea_node"."id" IS '主键';
COMMENT ON COLUMN "public"."sea_node"."def_id" IS '流程实例id';
COMMENT ON COLUMN "public"."sea_node"."mark" IS '节点id';
COMMENT ON COLUMN "public"."sea_node"."label" IS '节点名称';
COMMENT ON COLUMN "public"."sea_node"."type" IS '类型(1:开始;2:结束;3:审批任务;4:抄送任务;5:脚本任务;6:排它网关;7:并行网关;8:手动选择;9:空节点;)';
COMMENT ON COLUMN "public"."sea_node"."status" IS '状态(0:活动;1:通过;2:不通过;3:完成;4:终止;)';
COMMENT ON COLUMN "public"."sea_node"."start_time" IS '开始时间';
COMMENT ON COLUMN "public"."sea_node"."end_time" IS '结束时间';
COMMENT ON COLUMN "public"."sea_node"."precede" IS '前导';
COMMENT ON COLUMN "public"."sea_node"."path" IS '路径';
COMMENT ON COLUMN "public"."sea_node"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sea_node"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sea_node" IS '流程节点';

DROP TABLE IF EXISTS "public"."sea_node_detail";
CREATE TABLE "public"."sea_node_detail" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"node_id" BIGINT NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"assignee" VARCHAR(30) NOT NULL,
	"status" INTEGER DEFAULT 0,
	"remark" VARCHAR(255),
	"start_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"end_time" TIMESTAMP,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."sea_node_detail"."id" IS '主键';
COMMENT ON COLUMN "public"."sea_node_detail"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."sea_node_detail"."node_id" IS '流程节点id';
COMMENT ON COLUMN "public"."sea_node_detail"."name" IS '名称';
COMMENT ON COLUMN "public"."sea_node_detail"."assignee" IS '签收人或被委托id';
COMMENT ON COLUMN "public"."sea_node_detail"."status" IS '状态(0:待办;1:同意;2:拒绝;3:已阅;)';
COMMENT ON COLUMN "public"."sea_node_detail"."remark" IS '评论';
COMMENT ON COLUMN "public"."sea_node_detail"."start_time" IS '开始时间';
COMMENT ON COLUMN "public"."sea_node_detail"."end_time" IS '结束时间';
COMMENT ON COLUMN "public"."sea_node_detail"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sea_node_detail"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sea_node_detail" IS '流程节点详情';

DROP TABLE IF EXISTS "public"."serial";
CREATE TABLE "public"."serial" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"options" TEXT NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."serial"."id" IS '主键';
COMMENT ON COLUMN "public"."serial"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."serial"."name" IS '名称';
COMMENT ON COLUMN "public"."serial"."options" IS '要素';
COMMENT ON COLUMN "public"."serial"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."serial"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."serial" IS '门户管理';


DROP TABLE IF EXISTS "public"."shortcut";
CREATE TABLE "public"."shortcut" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"user_id" BIGINT NOT NULL,
	"menu_id" BIGINT NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."shortcut"."id" IS '主键';
COMMENT ON COLUMN "public"."shortcut"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."shortcut"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."shortcut"."menu_id" IS '菜单id';
COMMENT ON COLUMN "public"."shortcut"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."shortcut"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."shortcut" IS '快捷入口';

DROP TABLE IF EXISTS "public"."sys_account";
CREATE TABLE "public"."sys_account" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"avatar" VARCHAR(255) DEFAULT NULL,
	"account" VARCHAR(50) NOT NULL,
	"email" VARCHAR(50),
	"phone" VARCHAR(30),
	"name" VARCHAR(50) NOT NULL,
	"sex" INTEGER DEFAULT 1,
	"password" VARCHAR(255) NOT NULL,
	"position" VARCHAR(50),
	"status" INTEGER DEFAULT 1 NOT NULL,
	"type" INTEGER DEFAULT 1,
	"openid" VARCHAR(100),
	"sort" INTEGER DEFAULT 0,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."sys_account"."id" IS '主键';
COMMENT ON COLUMN "public"."sys_account"."avatar" IS '头像';
COMMENT ON COLUMN "public"."sys_account"."account" IS '账号';
COMMENT ON COLUMN "public"."sys_account"."name" IS '姓名';
COMMENT ON COLUMN "public"."sys_account"."sex" IS '性别(1:男;2:女;)';
COMMENT ON COLUMN "public"."sys_account"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_account"."position" IS '职位';
COMMENT ON COLUMN "public"."sys_account"."status" IS '状态(1:启用;2:禁用;)';
COMMENT ON COLUMN "public"."sys_account"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_account"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_account"."type" IS '类型(1:普通成员;2:管理员;)';
COMMENT ON COLUMN "public"."sys_account"."email" IS '邮箱';
COMMENT ON COLUMN "public"."sys_account"."phone" IS '手机号';
COMMENT ON COLUMN "public"."sys_account"."openid" IS 'openid';
COMMENT ON COLUMN "public"."sys_account"."sort" IS '排序';
COMMENT ON TABLE "public"."sys_account" IS '用户';

DROP TABLE IF EXISTS "public"."sys_icon";
CREATE TABLE "public"."sys_icon"  (
    "id" BIGSERIAL PRIMARY KEY NOT NULL,
    "name" varchar(30) NOT NULL,
    "font" varchar(50) NOT NULL
);
COMMENT ON COLUMN "public"."sys_icon"."id" IS '主键';
COMMENT ON COLUMN "public"."sys_icon"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_icon"."font" IS 'font_class';
COMMENT ON TABLE "public"."sys_icon" IS 'icon数据';

DROP TABLE IF EXISTS "public"."sys_log";
CREATE TABLE "public"."sys_log" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"user_id" BIGINT NOT NULL,
	"name" VARCHAR(50) NOT NULL,
	"ip" VARCHAR(50) NOT NULL,
	"uri" VARCHAR(200) NOT NULL,
	"method" VARCHAR(255) NOT NULL,
	"params" TEXT,
	"status" INTEGER DEFAULT 1,
	"cost_time" INTEGER NOT NULL,
	"result" TEXT,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"ua" VARCHAR(1000)
);
COMMENT ON COLUMN "public"."sys_log"."id" IS '主键';
COMMENT ON COLUMN "public"."sys_log"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."sys_log"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."sys_log"."name" IS '操作名称';
COMMENT ON COLUMN "public"."sys_log"."ip" IS 'ip';
COMMENT ON COLUMN "public"."sys_log"."uri" IS 'uri';
COMMENT ON COLUMN "public"."sys_log"."method" IS '方法';
COMMENT ON COLUMN "public"."sys_log"."params" IS '请求参数';
COMMENT ON COLUMN "public"."sys_log"."status" IS '状态(1:成功;2:失败;)';
COMMENT ON COLUMN "public"."sys_log"."cost_time" IS '花费时间';
COMMENT ON COLUMN "public"."sys_log"."result" IS '返回结果';
COMMENT ON COLUMN "public"."sys_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_log"."ua" IS '浏览器信息';
COMMENT ON TABLE "public"."sys_log" IS '操作日记';

DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"parent_id" BIGINT,
	"classify" INTEGER DEFAULT 1,
	"type" INTEGER DEFAULT 1,
	"name" VARCHAR(30) NOT NULL,
	"icon" VARCHAR(50) NOT NULL,
	"path" VARCHAR(50),
	"status" INTEGER DEFAULT 1,
	"sort" INTEGER DEFAULT 1,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."sys_menu"."id" IS '主键';
COMMENT ON COLUMN "public"."sys_menu"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."sys_menu"."parent_id" IS '上级id';
COMMENT ON COLUMN "public"."sys_menu"."classify" IS '类别(1:PC端;2:移动端;)';
COMMENT ON COLUMN "public"."sys_menu"."type" IS '类型(1:表单列表;2:按钮;3:新增表单;4:系统菜单;5:目录;6:仪表板;7:单页面;)';
COMMENT ON COLUMN "public"."sys_menu"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_menu"."icon" IS '图标';
COMMENT ON COLUMN "public"."sys_menu"."path" IS '路径';
COMMENT ON COLUMN "public"."sys_menu"."status" IS '状态(1:启用;2:禁用;)';
COMMENT ON COLUMN "public"."sys_menu"."sort" IS '排序';
COMMENT ON COLUMN "public"."sys_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_menu"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_menu" IS '菜单';

DROP TABLE IF EXISTS "public"."sys_message";
CREATE TABLE "public"."sys_message" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"type" INTEGER DEFAULT 1,
	"from_user_id" BIGINT NOT NULL,
	"to_user_id" BIGINT NOT NULL,
	"title" VARCHAR(50) NOT NULL,
	"business_type" BIGINT NOT NULL,
	"business_key" BIGINT NOT NULL,
	"status" INTEGER DEFAULT 0,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."sys_message"."id" IS '主键';
COMMENT ON COLUMN "public"."sys_message"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."sys_message"."type" IS '类型(1:暂存数据;)';
COMMENT ON COLUMN "public"."sys_message"."from_user_id" IS '用户id(来自)';
COMMENT ON COLUMN "public"."sys_message"."to_user_id" IS '用户id(给谁)';
COMMENT ON COLUMN "public"."sys_message"."title" IS '标题';
COMMENT ON COLUMN "public"."sys_message"."business_type" IS '业务类型';
COMMENT ON COLUMN "public"."sys_message"."business_key" IS '业务key';
COMMENT ON COLUMN "public"."sys_message"."status" IS '状态(0:未读;1:已读;)';
COMMENT ON COLUMN "public"."sys_message"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_message"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_message" IS '消息表';

DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"name" VARCHAR(30) NOT NULL,
	"path" TEXT NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."sys_role"."id" IS '主键';
COMMENT ON COLUMN "public"."sys_role"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."sys_role"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_role"."path" IS '菜单权限(以,隔开)';
COMMENT ON COLUMN "public"."sys_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_role"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_role" IS '系统角色';

DROP TABLE IF EXISTS "public"."table_column_config";
CREATE TABLE "public"."table_column_config" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"user_id" BIGINT NOT NULL,
    "form_id" BIGINT NOT NULL,
	"options" text NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."table_column_config"."id" IS '主键';
COMMENT ON COLUMN "public"."table_column_config"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."table_column_config"."form_id" IS '表单id';
COMMENT ON COLUMN "public"."table_column_config"."options" IS '配置';
COMMENT ON COLUMN "public"."table_column_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."table_column_config"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."table_column_config" IS '表头配置';

DROP TABLE IF EXISTS "public"."user_role";
CREATE TABLE "public"."user_role" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"user_id" BIGINT NOT NULL,
	"role_id" BIGINT NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."user_role"."id" IS '主键';
COMMENT ON COLUMN "public"."user_role"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."user_role"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."user_role"."role_id" IS '角色id';
COMMENT ON COLUMN "public"."user_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."user_role"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."user_role" IS '用户角色';

BEGIN;
INSERT INTO "public"."company" VALUES (1, NULL, 'seagox', '1001', '默认单位', '默认单位', NULL, NULL, 1, now(), now());
SELECT setval('company_id_seq',(SELECT max(id) FROM "public"."company"));

INSERT INTO "public"."department" VALUES (1, 1, NULL, '101', '默认部门', NULL, 1, now(), now());
select setval('department_id_seq',(SELECT max(id) FROM "public"."department"));

INSERT INTO "public"."sys_role" VALUES (1, 1, '管理员', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19', now(), now());
SELECT setval('sys_role_id_seq',(SELECT max(id) FROM "public"."sys_role"));

INSERT INTO "public"."sys_account" VALUES (1, NULL, 'admin', NULL, NULL, '管理员', 1, '$2a$10$7xaqWKLFZRc2mg7JIX.B/OCtijP2zYZack60pbC3WxDGvtfvKld3W', NULL, 1, 2, NULL, 0, now(), now());
SELECT setval('sys_account_id_seq',(SELECT max(id) FROM "public"."sys_account"));

INSERT INTO "public"."user_role" VALUES (1, 1, 1, 1, now(), now());
SELECT setval('user_role_id_seq',(SELECT max(id) FROM "public"."user_role"));

INSERT INTO "public"."dept_user" VALUES (1, 1, 1, 1, now(), now());
SELECT setval('dept_user_id_seq',(SELECT max(id) FROM "public"."dept_user"));

INSERT INTO "public"."sys_menu" VALUES (1, 1, NULL, 5, '组织架构', 'iconfont icon-xihuan', 'organization', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" VALUES (2, 1, 1, 4, '人员管理', 'iconfont icon-xihuan', 'contact', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" VALUES (3, 1, 1, 4, '角色管理', 'iconfont icon-xihuan', 'role', 1, 2, now(), now());
INSERT INTO "public"."sys_menu" VALUES (4, 1, 2, 2, '导出用户', 'iconfont icon-xihuan', 'user:export', 1, 3, now(), now());
INSERT INTO "public"."sys_menu" VALUES (5, 1, 2, 2, '导出用户模板', 'iconfont icon-xihuan', 'user:download', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" VALUES (6, 1, 2, 2, '导出部门模板', 'iconfont icon-xihuan', 'dept:download', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" VALUES (7, 1, 2, 2, '新增用户', 'iconfont icon-xihuan', 'user:add', 1, 4, now(), now());
INSERT INTO "public"."sys_menu" VALUES (8, 1, 2, 2, '编辑用户', 'iconfont icon-xihuan', 'user:edit', 1, 5, now(), now());
INSERT INTO "public"."sys_menu" VALUES (9, 1, 2, 2, '删除用户', 'iconfont icon-xihuan', 'user:delete', 1, 6, now(), now());
INSERT INTO "public"."sys_menu" VALUES (10, 1, 2, 2, '密码重置', 'iconfont icon-xihuan', 'user:reset', 1, 7, now(), now());
INSERT INTO "public"."sys_menu" VALUES (11, 1, 2, 2, '导入用户', 'iconfont icon-xihuan', 'user:import', 1, 2, now(), now());
INSERT INTO "public"."sys_menu" VALUES (12, 1, 2, 2, '新增部门', 'iconfont icon-xihuan', 'dept:add', 1, 8, now(), now());
INSERT INTO "public"."sys_menu" VALUES (13, 1, 2, 2, '编辑部门', 'iconfont icon-xihuan', 'dept:edit', 1, 9, now(), now());
INSERT INTO "public"."sys_menu" VALUES (14, 1, 2, 2, '删除部门', 'iconfont icon-xihuan', 'dept:delete', 1, 10, now(), now());
INSERT INTO "public"."sys_menu" VALUES (15, 1, 3, 2, '新增', 'iconfont icon-xihuan', 'role:add', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" VALUES (16, 1, 3, 2, '编辑', 'iconfont icon-xihuan', 'role:edit', 1, 2, now(), now());
INSERT INTO "public"."sys_menu" VALUES (17, 1, 3, 2, '删除', 'iconfont icon-xihuan', 'role:delete', 1, 3, now(), now());
INSERT INTO "public"."sys_menu" VALUES (18, 1, 2, 2, '导入部门', 'iconfont icon-xihuan', 'dept:import', 1, 11, now(), now());
INSERT INTO "public"."sys_menu" VALUES (19, 1, 3, 2, '授权', 'iconfont icon-xihuan', 'role:authorize', 1, 4, now(), now());
SELECT setval('sys_menu_id_seq',(SELECT max(id) FROM "public"."sys_menu"));
COMMIT;