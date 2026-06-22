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

DROP TABLE IF EXISTS "public"."leave_request";
CREATE TABLE "public"."leave_request" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"applicant_id" BIGINT NOT NULL,
	"leave_type" INTEGER NOT NULL,
	"start_time" TIMESTAMP NOT NULL,
	"end_time" TIMESTAMP NOT NULL,
	"duration" NUMERIC(10,2) NOT NULL,
	"reason" VARCHAR(500) NOT NULL,
	"status" INTEGER DEFAULT 0,
	"submit_time" TIMESTAMP,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."leave_request"."id" IS '主键';
COMMENT ON COLUMN "public"."leave_request"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."leave_request"."applicant_id" IS '申请人id';
COMMENT ON COLUMN "public"."leave_request"."leave_type" IS '请假类型(1:事假;2:病假;3:年假;4:调休;5:婚假;6:产假;7:丧假;8:其他;)';
COMMENT ON COLUMN "public"."leave_request"."start_time" IS '开始时间';
COMMENT ON COLUMN "public"."leave_request"."end_time" IS '结束时间';
COMMENT ON COLUMN "public"."leave_request"."duration" IS '请假时长';
COMMENT ON COLUMN "public"."leave_request"."reason" IS '请假事由';
COMMENT ON COLUMN "public"."leave_request"."status" IS '状态(0:草稿;1:审批中;2:已撤销;3:已通过;4:已驳回;)';
COMMENT ON COLUMN "public"."leave_request"."submit_time" IS '提交时间';
COMMENT ON COLUMN "public"."leave_request"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."leave_request"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."leave_request" IS '请假单';


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
COMMENT ON COLUMN "public"."sys_menu"."type" IS '类型(1:目录;2:页面;3:按钮;)';
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
	"business_type" VARCHAR(50),
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
INSERT INTO "public"."company" VALUES (1, NULL, 'seagox', '1001', '默认单位', '默认单位', NULL, 1, now(), now());
SELECT setval('company_id_seq',(SELECT max(id) FROM "public"."company"));

INSERT INTO "public"."department" VALUES (1, 1, NULL, '101', '默认部门', NULL, NULL, 1, now(), now());
select setval('department_id_seq',(SELECT max(id) FROM "public"."department"));

INSERT INTO "public"."sys_role" VALUES (1, 1, '管理员', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35', now(), now());
SELECT setval('sys_role_id_seq',(SELECT max(id) FROM "public"."sys_role"));

INSERT INTO "public"."sys_account" VALUES (1, NULL, 'admin', NULL, NULL, '管理员', 1, '$2a$10$Y.j6uP.zc9Lpb1vk26IlOOihWA/xc/sEFpfEWE6Dlvcko14vpyVyu', NULL, 1, 2, NULL, 0, now(), now());
SELECT setval('sys_account_id_seq',(SELECT max(id) FROM "public"."sys_account"));

INSERT INTO "public"."user_role" VALUES (1, 1, 1, 1, now(), now());
SELECT setval('user_role_id_seq',(SELECT max(id) FROM "public"."user_role"));

INSERT INTO "public"."dept_user" VALUES (1, 1, 1, 1, now(), now());
SELECT setval('dept_user_id_seq',(SELECT max(id) FROM "public"."dept_user"));

INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (1, 1, NULL, 1, '组织架构', 'iconfont icon-xihuan', 'organization', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (2, 1, 1, 2, '人员管理', 'iconfont icon-xihuan', 'contact', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (3, 1, 1, 2, '角色管理', 'iconfont icon-xihuan', 'role', 1, 2, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (4, 1, 2, 3, '导出用户', 'iconfont icon-xihuan', 'user:export', 1, 3, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (5, 1, 2, 3, '导出用户模板', 'iconfont icon-xihuan', 'user:download', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (6, 1, 2, 3, '导出部门模板', 'iconfont icon-xihuan', 'dept:download', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (7, 1, 2, 3, '新增用户', 'iconfont icon-xihuan', 'user:add', 1, 4, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (8, 1, 2, 3, '编辑用户', 'iconfont icon-xihuan', 'user:edit', 1, 5, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (9, 1, 2, 3, '删除用户', 'iconfont icon-xihuan', 'user:delete', 1, 6, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (10, 1, 2, 3, '密码重置', 'iconfont icon-xihuan', 'user:reset', 1, 7, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (11, 1, 2, 3, '导入用户', 'iconfont icon-xihuan', 'user:import', 1, 2, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (12, 1, 2, 3, '新增部门', 'iconfont icon-xihuan', 'dept:add', 1, 8, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (13, 1, 2, 3, '编辑部门', 'iconfont icon-xihuan', 'dept:edit', 1, 9, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (14, 1, 2, 3, '删除部门', 'iconfont icon-xihuan', 'dept:delete', 1, 10, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (15, 1, 3, 3, '新增', 'iconfont icon-xihuan', 'role:add', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (16, 1, 3, 3, '编辑', 'iconfont icon-xihuan', 'role:edit', 1, 2, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (17, 1, 3, 3, '删除', 'iconfont icon-xihuan', 'role:delete', 1, 3, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (18, 1, 2, 3, '导入部门', 'iconfont icon-xihuan', 'dept:import', 1, 11, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (19, 1, 3, 3, '授权', 'iconfont icon-xihuan', 'role:authorize', 1, 4, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (20, 1, NULL, 1, '我的工作', 'iconfont icon-xihuan', 'myWork', 1, 2, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (21, 1, 20, 2, '待办事项', 'iconfont icon-xihuan', 'todoItem', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (22, 1, 20, 2, '待发事项', 'iconfont icon-xihuan', 'readyItem', 1, 2, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (23, 1, 20, 2, '已办事项', 'iconfont icon-xihuan', 'doneItem', 1, 3, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (24, 1, 20, 2, '抄送事项', 'iconfont icon-xihuan', 'copyItem', 1, 4, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (25, 1, 20, 2, '我发起的', 'iconfont icon-xihuan', 'selfItem', 1, 5, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (26, 1, NULL, 1, '协同办公', 'iconfont icon-xihuan', 'office', 1, 3, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (27, 1, 26, 2, '请假单', 'iconfont icon-xihuan', 'leave', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (28, 1, 27, 3, '新增', 'iconfont icon-xihuan', 'leave:add', 1, 1, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (29, 1, 27, 3, '编辑', 'iconfont icon-xihuan', 'leave:edit', 1, 2, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (30, 1, 27, 3, '删除', 'iconfont icon-xihuan', 'leave:delete', 1, 3, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (31, 1, 27, 3, '提交', 'iconfont icon-xihuan', 'leave:submit', 1, 4, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (32, 1, 27, 3, '撤销', 'iconfont icon-xihuan', 'leave:cancel', 1, 5, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (33, 1, 27, 3, '导入', 'iconfont icon-xihuan', 'leave:import', 1, 6, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (34, 1, 27, 3, '下载模板', 'iconfont icon-xihuan', 'leave:download', 1, 7, now(), now());
INSERT INTO "public"."sys_menu" ("id", "company_id", "parent_id", "type", "name", "icon", "path", "status", "sort", "create_time", "update_time") VALUES (35, 1, 27, 3, '导出', 'iconfont icon-xihuan', 'leave:export', 1, 8, now(), now());
SELECT setval('sys_menu_id_seq',(SELECT max(id) FROM "public"."sys_menu"));
INSERT INTO "public"."sys_menu" VALUES (36,1,NULL,1,'工程管理','iconfont icon-xihuan','engineering',1,4,now(),now()),(37,1,36,2,'项目管理','iconfont icon-xihuan','business/project/project',1,1,now(),now()),(38,1,37,3,'新增','iconfont icon-xihuan','project:add',1,1,now(),now()),(39,1,37,3,'编辑','iconfont icon-xihuan','project:edit',1,2,now(),now()),(40,1,37,3,'删除','iconfont icon-xihuan','project:delete',1,3,now(),now());
UPDATE "public"."sys_role" SET "path"='1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40' WHERE "id"=1;
SELECT setval('sys_menu_id_seq',(SELECT max(id) FROM "public"."sys_menu"));
COMMIT;
