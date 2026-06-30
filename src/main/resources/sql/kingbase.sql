CREATE TABLE IF NOT EXISTS "public"."disk" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"parent_id" BIGINT,
	"name" VARCHAR(255) NOT NULL,
	"url" VARCHAR(500) NOT NULL,
	"path" VARCHAR(1000) NOT NULL,
	"level" INTEGER DEFAULT 1 NOT NULL,
	"size" BIGINT DEFAULT 0 NOT NULL,
	"type" INTEGER DEFAULT 1,
	"created_by" BIGINT NOT NULL,
	"updated_by" BIGINT NOT NULL,
	"created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON COLUMN "public"."disk"."id" IS '主键';
COMMENT ON COLUMN "public"."disk"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."disk"."parent_id" IS '上级id';
COMMENT ON COLUMN "public"."disk"."name" IS '名称';
COMMENT ON COLUMN "public"."disk"."url" IS '链接';
COMMENT ON COLUMN "public"."disk"."path" IS '完整节点路径，例如 /1/5/12/';
COMMENT ON COLUMN "public"."disk"."level" IS '目录层级';
COMMENT ON COLUMN "public"."disk"."size" IS '大小';
COMMENT ON COLUMN "public"."disk"."type" IS '类型(1:文件夹;2:图片;3:word;4:excel;5:ppt;6:pdf;7:压缩文件;8:txt;9:文档;10:视频;11:其他;)';
COMMENT ON COLUMN "public"."disk"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."disk"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."disk"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."disk"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."disk" IS '网盘';

CREATE TABLE IF NOT EXISTS "public"."requirement" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"project_id" BIGINT NOT NULL,
	"style" VARCHAR(255) NOT NULL,
	"budget" VARCHAR(255) NOT NULL,
	"member" VARCHAR(255) NOT NULL,
	"mark" VARCHAR(500),
	"signature_url" VARCHAR(500),
	"signed_at" TIMESTAMP,
	"status" INTEGER DEFAULT 1,
	"created_by" BIGINT NOT NULL,
	"updated_by" BIGINT NOT NULL,
	"created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON COLUMN "public"."requirement"."id" IS '主键';
COMMENT ON COLUMN "public"."requirement"."project_id" IS '所属项目ID';
COMMENT ON COLUMN "public"."requirement"."style" IS '风格偏好';
COMMENT ON COLUMN "public"."requirement"."budget" IS '预算范围接';
COMMENT ON COLUMN "public"."requirement"."member" IS '家庭成员';
COMMENT ON COLUMN "public"."requirement"."mark" IS '特殊需求';
COMMENT ON COLUMN "public"."requirement"."signature_url" IS '签字文件url';
COMMENT ON COLUMN "public"."requirement"."signed_at" IS '签字时间';
COMMENT ON COLUMN "public"."requirement"."status" IS '状态(1:待提交;2:待审核;3:已完成;)';
COMMENT ON COLUMN "public"."requirement"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."requirement"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."requirement"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."requirement"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."requirement" IS '需求沟通表';

CREATE TABLE IF NOT EXISTS "public"."leave_message" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"project_id" BIGINT NOT NULL,
	"project_member_id" BIGINT NOT NULL,
	"type" INTEGER DEFAULT 1,
	"remark" VARCHAR(500) NOT NULL,
	"created_by" BIGINT NOT NULL,
	"updated_by" BIGINT NOT NULL,
	"created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON COLUMN "public"."leave_message"."id" IS '主键';
COMMENT ON COLUMN "public"."leave_message"."project_id" IS '所属项目ID';
COMMENT ON COLUMN "public"."leave_message"."project_member_id" IS '所属项目角色ID';
COMMENT ON COLUMN "public"."leave_message"."type" IS '类型(1:方案设计;2:施工图出图;)';
COMMENT ON COLUMN "public"."leave_message"."remark" IS '说明';
COMMENT ON COLUMN "public"."leave_message"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."leave_message"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."leave_message"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."leave_message"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."leave_message" IS '留言表';

CREATE TABLE IF NOT EXISTS "public"."solution_design" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"project_id" BIGINT NOT NULL,
	"status" INTEGER DEFAULT 1,
	"created_by" BIGINT NOT NULL,
	"updated_by" BIGINT NOT NULL,
	"created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON COLUMN "public"."solution_design"."id" IS '主键';
COMMENT ON COLUMN "public"."solution_design"."project_id" IS '所属项目ID';
COMMENT ON COLUMN "public"."solution_design"."status" IS '状态(1:待提交;2:待确认;3:已确认;4:已冻结;5:解冻中;6:已完成;)';
COMMENT ON COLUMN "public"."solution_design"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."solution_design"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."solution_design"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."solution_design"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."solution_design" IS '方案设计';

CREATE TABLE IF NOT EXISTS "public"."solution_design_detail" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"solution_design_id" BIGINT NOT NULL,
	"version" INTEGER DEFAULT 1,
	"attachments" TEXT NOT NULL,
	"solution_explanation" VARCHAR(1000) NOT NULL,
	"annotation" VARCHAR(1000),
	"defrost_explanation" VARCHAR(1000),
	"apply_defrost_at" TIMESTAMP,
	"signature_url" VARCHAR(500),
	"signed_at" TIMESTAMP,
	"created_by" BIGINT NOT NULL,
	"updated_by" BIGINT NOT NULL,
	"created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON COLUMN "public"."solution_design_detail"."id" IS '主键';
COMMENT ON COLUMN "public"."solution_design_detail"."solution_design_id" IS '方案设计ID';
COMMENT ON COLUMN "public"."solution_design_detail"."version" IS '版本';
COMMENT ON COLUMN "public"."solution_design_detail"."attachments" IS '效果图';
COMMENT ON COLUMN "public"."solution_design_detail"."solution_explanation" IS '方案说明';
COMMENT ON COLUMN "public"."solution_design_detail"."annotation" IS '修改注释';
COMMENT ON COLUMN "public"."solution_design_detail"."defrost_explanation" IS '解冻说明';
COMMENT ON COLUMN "public"."solution_design_detail"."apply_defrost_at" IS '申请解冻时间';
COMMENT ON COLUMN "public"."solution_design_detail"."signature_url" IS '签字文件url';
COMMENT ON COLUMN "public"."solution_design_detail"."signed_at" IS '签字时间';
COMMENT ON COLUMN "public"."solution_design_detail"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."solution_design_detail"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."solution_design_detail"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."solution_design_detail"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."solution_design_detail" IS '方案设计详情';

CREATE TABLE IF NOT EXISTS "public"."company" (
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

CREATE TABLE IF NOT EXISTS "public"."department" (
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

CREATE TABLE IF NOT EXISTS "public"."dept_user" (
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

CREATE TABLE IF NOT EXISTS "public"."dic_classify" (
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

CREATE TABLE IF NOT EXISTS "public"."dic_detail" (
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

CREATE TABLE IF NOT EXISTS "public"."job" (
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

CREATE TABLE IF NOT EXISTS "public"."leave_request" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"applicant_id" BIGINT NOT NULL,
	"leave_type" INTEGER NOT NULL,
	"start_time" TIMESTAMP NOT NULL,
	"end_time" TIMESTAMP NOT NULL,
	"duration" NUMERIC(10,2) NOT NULL,
	"reason" VARCHAR(500) NOT NULL,
    "attachments" JSONB DEFAULT NULL,
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
COMMENT ON COLUMN "public"."leave_request"."attachments" IS '附件';
COMMENT ON COLUMN "public"."leave_request"."status" IS '状态(0:草稿;1:审批中;2:已撤销;3:已通过;4:已驳回;)';
COMMENT ON COLUMN "public"."leave_request"."submit_time" IS '提交时间';
COMMENT ON COLUMN "public"."leave_request"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."leave_request"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."leave_request" IS '请假单';

CREATE TABLE IF NOT EXISTS "public"."project" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "code" VARCHAR(50) NOT NULL,
    "cover" VARCHAR(200) NOT NULL,
    "name" VARCHAR(200) NOT NULL,
    "address" VARCHAR(500) NOT NULL,
    "owner_name" VARCHAR(100) NOT NULL,
    "owner_phone" VARCHAR(50) NOT NULL,
    "budget_amount" DECIMAL(18,2) NOT NULL DEFAULT 0,
    "status" INTEGER DEFAULT 1,
    "health_status" INTEGER DEFAULT 1,
    "pause_reason" VARCHAR(500) DEFAULT NULL,
    "cancel_reason" VARCHAR(500) DEFAULT NULL,
    "planned_start_date" DATE NULL,
    "planned_end_date" DATE NULL,
    "actual_start_date" DATE NULL,
    "actual_end_date" DATE NULL,
    "created_by" BIGINT NOT NULL,
    "updated_by" BIGINT NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."project"."id" IS '主键';
COMMENT ON COLUMN "public"."project"."code" IS '项目编号';
COMMENT ON COLUMN "public"."project"."cover" IS '封面图';
COMMENT ON COLUMN "public"."project"."name" IS '项目名称';
COMMENT ON COLUMN "public"."project"."address" IS '地址';
COMMENT ON COLUMN "public"."project"."owner_name" IS '业主姓名';
COMMENT ON COLUMN "public"."project"."owner_phone" IS '业主联系电话';
COMMENT ON COLUMN "public"."project"."budget_amount" IS '预算金额';
COMMENT ON COLUMN "public"."project"."status" IS '项目状态(1:待启动;2:进行中;3:暂停;4:已交付;5:售后中;6:已完结;7:已取消;)';
COMMENT ON COLUMN "public"."project"."health_status" IS '健康状态(1:正常;2:预警;3:滞后;4:异常;)';
COMMENT ON COLUMN "public"."project"."pause_reason" IS '暂停原因';
COMMENT ON COLUMN "public"."project"."cancel_reason" IS '取消原因';
COMMENT ON COLUMN "public"."project"."planned_start_date" IS '计划开始日期';
COMMENT ON COLUMN "public"."project"."planned_end_date" IS '计划结束日期';
COMMENT ON COLUMN "public"."project"."actual_start_date" IS '实际开始日期';
COMMENT ON COLUMN "public"."project"."actual_end_date" IS '实际结束日期';
COMMENT ON COLUMN "public"."project"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."project"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."project"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."project"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."project" IS '项目';

CREATE TABLE IF NOT EXISTS "public"."project_stage" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "project_id" BIGINT NOT NULL,
    "flow_type" INTEGER DEFAULT NULL,
    "stage_name" VARCHAR(100) NOT NULL,
    "status" INTEGER DEFAULT 1,
    "manager_user_id" BIGINT DEFAULT NULL,
    "planned_start_date" DATE DEFAULT NULL,
    "planned_end_date" DATE DEFAULT NULL,
    "planned_days" INTEGER DEFAULT NULL,
    "actual_start_date" DATE DEFAULT NULL,
    "actual_end_date" DATE DEFAULT NULL,
    "completed_by" BIGINT DEFAULT NULL,
    "completed_at" TIMESTAMP DEFAULT NULL,
    "remark" VARCHAR(1000) DEFAULT NULL,
    "created_by" BIGINT NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_by" BIGINT NOT NULL,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."project_stage"."id" IS '主键';
COMMENT ON COLUMN "public"."project_stage"."project_id" IS '所属项目ID';
COMMENT ON COLUMN "public"."project_stage"."flow_type" IS '流程类型(1:筹备;2:设计;3:土建;4:精装;5:交付;6:售后;)';
COMMENT ON COLUMN "public"."project_stage"."stage_name" IS '阶段名称';
COMMENT ON COLUMN "public"."project_stage"."status" IS '状态(1:未开始;2:进行中;3:已完成;)';
COMMENT ON COLUMN "public"."project_stage"."manager_user_id" IS '阶段负责人用户ID';
COMMENT ON COLUMN "public"."project_stage"."planned_start_date" IS '计划开始日期';
COMMENT ON COLUMN "public"."project_stage"."planned_end_date" IS '计划完成日期';
COMMENT ON COLUMN "public"."project_stage"."planned_days" IS '计划工期天数';
COMMENT ON COLUMN "public"."project_stage"."actual_start_date" IS '实际开始日期';
COMMENT ON COLUMN "public"."project_stage"."actual_end_date" IS '实际完成日期';
COMMENT ON COLUMN "public"."project_stage"."completed_by" IS '完成人用户ID';
COMMENT ON COLUMN "public"."project_stage"."completed_at" IS '完成时间';
COMMENT ON COLUMN "public"."project_stage"."remark" IS '备注';
COMMENT ON COLUMN "public"."project_stage"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."project_stage"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."project_stage"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."project_stage"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."project_stage" IS '项目阶段';

CREATE TABLE IF NOT EXISTS "public"."project_stage_dependency" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "project_id" BIGINT NOT NULL,
    "stage_id" BIGINT NOT NULL,
    "predecessor_stage_id" BIGINT NOT NULL
);
COMMENT ON COLUMN "public"."project_stage_dependency"."id" IS '主键';
COMMENT ON COLUMN "public"."project_stage_dependency"."project_id" IS '项目ID';
COMMENT ON COLUMN "public"."project_stage_dependency"."stage_id" IS '当前阶段ID';
COMMENT ON COLUMN "public"."project_stage_dependency"."predecessor_stage_id" IS '前置阶段ID';
COMMENT ON TABLE "public"."project_stage_dependency" IS '项目阶段前置依赖表';

CREATE TABLE IF NOT EXISTS "public"."stage_inspection_item" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "project_id" BIGINT NOT NULL,
    "stage_id" BIGINT NOT NULL,
    "name" VARCHAR(200) NOT NULL,
    "status" INTEGER DEFAULT 0
);
COMMENT ON COLUMN "public"."stage_inspection_item"."id" IS '主键';
COMMENT ON COLUMN "public"."stage_inspection_item"."project_id" IS '项目ID';
COMMENT ON COLUMN "public"."stage_inspection_item"."stage_id" IS '当前阶段ID';
COMMENT ON COLUMN "public"."stage_inspection_item"."name" IS '名称';
COMMENT ON COLUMN "public"."stage_inspection_item"."status" IS '状态(0:未开始;1:进行中;2:已完成;)';
COMMENT ON TABLE "public"."stage_inspection_item" IS '阶段验收条目';

CREATE TABLE IF NOT EXISTS "public"."project_member" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "project_id" BIGINT NOT NULL,
    "user_id" BIGINT NOT NULL,
    "role_code" INTEGER DEFAULT 1,
    "joined_at" TIMESTAMP NOT NULL,
    "left_at" TIMESTAMP NULL,
    "status" INTEGER DEFAULT 1,
    "created_by" BIGINT NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_by" BIGINT NOT NULL,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."project_member"."id" IS '主键';
COMMENT ON COLUMN "public"."project_member"."project_id" IS '项目id';
COMMENT ON COLUMN "public"."project_member"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."project_member"."role_code" IS '项目角色(1:设计师;2:设计助理;3:土建项目经理;4:精装项目经理;5:施工员;6:质检员;7:成控人员;8:财务人员;9:老板/管理层;10:业主;11:业主家属;)';
COMMENT ON COLUMN "public"."project_member"."joined_at" IS '加入项目时间';
COMMENT ON COLUMN "public"."project_member"."left_at" IS '退出项目时间';
COMMENT ON COLUMN "public"."project_member"."status" IS '状态(1:有效;2:禁用;)';
COMMENT ON COLUMN "public"."project_member"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."project_member"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."project_member"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."project_member"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."project_member" IS '项目成员';

CREATE TABLE IF NOT EXISTS "public"."construction_log" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "project_id" BIGINT NOT NULL,
    "stage_id" BIGINT DEFAULT NULL,
    "log_date" DATE NOT NULL,
    "filled_by" BIGINT NOT NULL,
    "current_progress_summary" VARCHAR(1000) DEFAULT NULL,
    "expected_completion_at" TIMESTAMP DEFAULT NULL,
    "next_day_plan" TEXT NOT NULL,
    "site_issues" JSONB DEFAULT NULL,
    "has_issue" SMALLINT NOT NULL DEFAULT 0,
    "assistants" JSONB DEFAULT NULL,
    "submitted_at" TIMESTAMP DEFAULT NULL,
    "attachments" JSONB DEFAULT NULL,
    "status" INTEGER DEFAULT 1,
    "created_by" BIGINT DEFAULT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_by" BIGINT DEFAULT NULL,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."construction_log"."id" IS '主键';
COMMENT ON COLUMN "public"."construction_log"."project_id" IS '所属项目ID';
COMMENT ON COLUMN "public"."construction_log"."stage_id" IS '当前项目阶段ID';
COMMENT ON COLUMN "public"."construction_log"."log_date" IS '日记日期';
COMMENT ON COLUMN "public"."construction_log"."filled_by" IS '填写人用户ID';
COMMENT ON COLUMN "public"."construction_log"."current_progress_summary" IS '今日工作内容';
COMMENT ON COLUMN "public"."construction_log"."expected_completion_at" IS '预计完成时间';
COMMENT ON COLUMN "public"."construction_log"."next_day_plan" IS '明日计划';
COMMENT ON COLUMN "public"."construction_log"."site_issues" IS '需要协调事项';
COMMENT ON COLUMN "public"."construction_log"."has_issue" IS '是否存在现场问题(0:否;1:是;)';
COMMENT ON COLUMN "public"."construction_log"."assistants" IS '配合人员';
COMMENT ON COLUMN "public"."construction_log"."submitted_at" IS '提交时间';
COMMENT ON COLUMN "public"."construction_log"."attachments" IS '附件';
COMMENT ON COLUMN "public"."construction_log"."status" IS '状态(1:已提交;2:草稿;)';
COMMENT ON COLUMN "public"."construction_log"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."construction_log"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."construction_log"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."construction_log"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."construction_log" IS '施工日志';

CREATE TABLE IF NOT EXISTS "public"."inspection" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "project_id" BIGINT NOT NULL,
    "stage_id" BIGINT DEFAULT NULL,
    "inspection_items" JSONB DEFAULT NULL,
    "plan_inspection_time" TIMESTAMP DEFAULT NULL,
    "site_photos" JSONB DEFAULT NULL,
    "participants" JSONB DEFAULT NULL,
    "signatures" JSONB DEFAULT NULL,
    "passed_at" TIMESTAMP DEFAULT NULL,
    "acceptance_comments" VARCHAR(1000) DEFAULT NULL,
    "remark" VARCHAR(1000) DEFAULT NULL,
    "status" INTEGER DEFAULT 1,
    "created_by" BIGINT DEFAULT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_by" BIGINT DEFAULT NULL,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."inspection"."id" IS '主键';
COMMENT ON COLUMN "public"."inspection"."project_id" IS '项目id';
COMMENT ON COLUMN "public"."inspection"."stage_id" IS '项目阶段ID';
COMMENT ON COLUMN "public"."inspection"."inspection_items" IS '项目阶段条目，格式为条目ID、条目名称、条目状态等';
COMMENT ON COLUMN "public"."inspection"."plan_inspection_time" IS '计划验收时间';
COMMENT ON COLUMN "public"."inspection"."site_photos" IS '验收现场总体照片';
COMMENT ON COLUMN "public"."inspection"."participants" IS '参与人员，格式为用户ID、姓名、角色等';
COMMENT ON COLUMN "public"."inspection"."signatures" IS '签字信息，包含签字人、角色、签字文件、签字时间';
COMMENT ON COLUMN "public"."inspection"."passed_at" IS '验收通过时间';
COMMENT ON COLUMN "public"."inspection"."acceptance_comments" IS '验收意见';
COMMENT ON COLUMN "public"."inspection"."remark" IS '备注';
COMMENT ON COLUMN "public"."inspection"."status" IS '状态(1:待验收;2:验收中;3:已完成;)';
COMMENT ON COLUMN "public"."inspection"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."inspection"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."inspection"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."inspection"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."inspection" IS '验收单';

CREATE TABLE IF NOT EXISTS "public"."issue_ticket" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "project_id" BIGINT NOT NULL,
    "source_type" INTEGER DEFAULT 1,
    "source_id" BIGINT DEFAULT NULL,
    "title" VARCHAR(200) NOT NULL,
    "description" TEXT NOT NULL,
    "issue_attachments" JSONB,
    "reported_by" BIGINT NOT NULL,
    "reported_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "assigned_by" BIGINT DEFAULT NULL,
    "assigned_at" TIMESTAMP DEFAULT NULL,
    "rectification_deadline" TIMESTAMP DEFAULT NULL,
    "rectification_count" INTEGER NOT NULL DEFAULT 0,
    "rectification_description" TEXT DEFAULT NULL,
    "rectification_attachments" JSONB,
    "rectification_user_id" BIGINT DEFAULT NULL,
    "rectification_submitted_at" TIMESTAMP DEFAULT NULL,
    "review_user_id" BIGINT DEFAULT NULL,
    "review_result" INTEGER DEFAULT 1,
    "review_remark" VARCHAR(1000) DEFAULT NULL,
    "review_attachments" JSONB DEFAULT NULL,
    "reviewed_at" TIMESTAMP DEFAULT NULL,
    "closed_by" BIGINT DEFAULT NULL,
    "closed_at" TIMESTAMP DEFAULT NULL,
    "status" INTEGER DEFAULT 1,
    "created_by" BIGINT NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_by" BIGINT DEFAULT NULL,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."issue_ticket"."id" IS '主键';
COMMENT ON COLUMN "public"."issue_ticket"."project_id" IS '所属项目ID';
COMMENT ON COLUMN "public"."issue_ticket"."source_type" IS '来源类型(1:施工日记;2:验收单;3:交接单;)';
COMMENT ON COLUMN "public"."issue_ticket"."source_id" IS '来源ID';
COMMENT ON COLUMN "public"."issue_ticket"."title" IS '问题标题';
COMMENT ON COLUMN "public"."issue_ticket"."description" IS '问题描述';
COMMENT ON COLUMN "public"."issue_ticket"."issue_attachments" IS '问题附件';
COMMENT ON COLUMN "public"."issue_ticket"."reported_by" IS '问题发现人用户ID';
COMMENT ON COLUMN "public"."issue_ticket"."reported_at" IS '问题发现时间';
COMMENT ON COLUMN "public"."issue_ticket"."assigned_by" IS '分配人用户ID';
COMMENT ON COLUMN "public"."issue_ticket"."assigned_at" IS '分配时间';
COMMENT ON COLUMN "public"."issue_ticket"."rectification_deadline" IS '整改截止时间';
COMMENT ON COLUMN "public"."issue_ticket"."rectification_count" IS '整改提交次数';
COMMENT ON COLUMN "public"."issue_ticket"."rectification_description" IS '整改说明';
COMMENT ON COLUMN "public"."issue_ticket"."rectification_attachments" IS '整改附件';
COMMENT ON COLUMN "public"."issue_ticket"."rectification_user_id" IS '整改责任人用户ID';
COMMENT ON COLUMN "public"."issue_ticket"."rectification_submitted_at" IS '整改提交时间';
COMMENT ON COLUMN "public"."issue_ticket"."review_user_id" IS '复验人或质检员用户ID';
COMMENT ON COLUMN "public"."issue_ticket"."review_result" IS '复验结果(1:通过;2:不通过)';
COMMENT ON COLUMN "public"."issue_ticket"."review_remark" IS '复验说明';
COMMENT ON COLUMN "public"."issue_ticket"."review_attachments" IS '复验附件';
COMMENT ON COLUMN "public"."issue_ticket"."reviewed_at" IS '复验时间';
COMMENT ON COLUMN "public"."issue_ticket"."closed_by" IS '关闭人用户ID';
COMMENT ON COLUMN "public"."issue_ticket"."closed_at" IS '问题关闭时间';
COMMENT ON COLUMN "public"."issue_ticket"."status" IS '状态(1:待整改;2:整改中;3:待复验;4:已关闭;)';
COMMENT ON COLUMN "public"."issue_ticket"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."issue_ticket"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."issue_ticket"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."issue_ticket"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."issue_ticket" IS '问题单';

CREATE TABLE IF NOT EXISTS "public"."payment_request" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "project_id" BIGINT NOT NULL,
    "company_id" BIGINT NOT NULL,
    "amount" DECIMAL(18,2) NOT NULL,
    "reason" TEXT,
    "attachments" JSONB,
    "applicant_id" BIGINT NOT NULL,
    "submit_time" TIMESTAMP DEFAULT NULL,
    "status" INTEGER DEFAULT 0,
    "created_by" BIGINT NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_by" BIGINT DEFAULT NULL,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."payment_request"."id" IS '主键';
COMMENT ON COLUMN "public"."payment_request"."project_id" IS '所属项目ID';
COMMENT ON COLUMN "public"."payment_request"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."payment_request"."amount" IS '金额';
COMMENT ON COLUMN "public"."payment_request"."reason" IS '请款事由';
COMMENT ON COLUMN "public"."payment_request"."attachments" IS '附件';
COMMENT ON COLUMN "public"."payment_request"."applicant_id" IS '申请人id';
COMMENT ON COLUMN "public"."payment_request"."submit_time" IS '提交时间';
COMMENT ON COLUMN "public"."payment_request"."status" IS '状态(0:草稿;1:审批中;2:已撤销;3:已通过;4:已驳回;)';
COMMENT ON COLUMN "public"."payment_request"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."payment_request"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."payment_request"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."payment_request"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."payment_request" IS '请款单';

CREATE TABLE IF NOT EXISTS "public"."material_arrival" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "project_id" BIGINT NOT NULL,
    "name" VARCHAR(200) NOT NULL,
    "arrival_at" TIMESTAMP NOT NULL,
    "remark" VARCHAR(1000) DEFAULT NULL,
    "attachments" JSONB,
    "created_by" BIGINT NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_by" BIGINT DEFAULT NULL,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."material_arrival"."id" IS '主键';
COMMENT ON COLUMN "public"."material_arrival"."project_id" IS '所属项目ID';
COMMENT ON COLUMN "public"."material_arrival"."name" IS '材料名称';
COMMENT ON COLUMN "public"."material_arrival"."arrival_at" IS '到达时间';
COMMENT ON COLUMN "public"."material_arrival"."remark" IS '备注';
COMMENT ON COLUMN "public"."material_arrival"."attachments" IS '附件';
COMMENT ON COLUMN "public"."material_arrival"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."material_arrival"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."material_arrival"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."material_arrival"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."material_arrival" IS '材料到场记录';

CREATE TABLE IF NOT EXISTS "public"."project_handover" (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "project_id" BIGINT NOT NULL,
    "handover_type" INTEGER DEFAULT 1,
    "handover_time" TIMESTAMP DEFAULT NULL,
    "handover_content" VARCHAR(1000) DEFAULT NULL,
    "handover_user_id" BIGINT NOT NULL,
    "receiver_user_id" BIGINT NOT NULL,
    "handover_signature_url" VARCHAR(500) NOT NULL,
    "receiver_signature_url" VARCHAR(500) DEFAULT NULL,
    "handover_signed_at" TIMESTAMP DEFAULT NULL,
    "receiver_signed_at" TIMESTAMP DEFAULT NULL,
    "attachment" JSONB,
    "created_by" BIGINT NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_by" BIGINT DEFAULT NULL,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."project_handover"."id" IS '主键';
COMMENT ON COLUMN "public"."project_handover"."project_id" IS '所属项目ID';
COMMENT ON COLUMN "public"."project_handover"."handover_type" IS '交接类型(1:设计->土建交接;2:设计->精装交接;3:土建->精装交接;)';
COMMENT ON COLUMN "public"."project_handover"."handover_time" IS '交接时间';
COMMENT ON COLUMN "public"."project_handover"."handover_content" IS '交接内容';
COMMENT ON COLUMN "public"."project_handover"."handover_user_id" IS '移交负责人用户ID';
COMMENT ON COLUMN "public"."project_handover"."receiver_user_id" IS '接收负责人用户ID';
COMMENT ON COLUMN "public"."project_handover"."handover_signature_url" IS '移交方签字文件url';
COMMENT ON COLUMN "public"."project_handover"."receiver_signature_url" IS '接收方签字文件url';
COMMENT ON COLUMN "public"."project_handover"."handover_signed_at" IS '移交方签字时间';
COMMENT ON COLUMN "public"."project_handover"."receiver_signed_at" IS '接收方签字时间';
COMMENT ON COLUMN "public"."project_handover"."attachment" IS '附件';
COMMENT ON COLUMN "public"."project_handover"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."project_handover"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."project_handover"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."project_handover"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."project_handover" IS '交接单';

CREATE TABLE IF NOT EXISTS "public"."phone_code" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"phone" VARCHAR(30) NOT NULL,
	"code" VARCHAR(10) NOT NULL,
	"create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"expire_time" TIMESTAMP NOT NULL
);
COMMENT ON COLUMN "public"."phone_code"."id" IS '主键';
COMMENT ON COLUMN "public"."phone_code"."phone" IS '手机号';
COMMENT ON COLUMN "public"."phone_code"."code" IS '验证码';
COMMENT ON COLUMN "public"."phone_code"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."phone_code"."expire_time" IS '过期时间';
COMMENT ON TABLE "public"."phone_code" IS '手机验证码';

CREATE TABLE IF NOT EXISTS "public"."sys_account" (
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

CREATE TABLE IF NOT EXISTS "public"."sys_icon"  (
    "id" BIGSERIAL PRIMARY KEY NOT NULL,
    "name" varchar(30) NOT NULL,
    "font" varchar(50) NOT NULL
);
COMMENT ON COLUMN "public"."sys_icon"."id" IS '主键';
COMMENT ON COLUMN "public"."sys_icon"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_icon"."font" IS 'font_class';
COMMENT ON TABLE "public"."sys_icon" IS 'icon数据';

CREATE TABLE IF NOT EXISTS "public"."sys_log" (
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

CREATE TABLE IF NOT EXISTS "public"."sys_menu" (
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

CREATE TABLE IF NOT EXISTS "public"."sys_message" (
	"id" BIGSERIAL PRIMARY KEY NOT NULL,
	"company_id" BIGINT NOT NULL,
	"type" INTEGER DEFAULT 1,
	"from_user_id" BIGINT NOT NULL,
	"to_user_id" BIGINT NOT NULL,
	"title" VARCHAR(50) NOT NULL,
	"business_type" VARCHAR(50),
	"business_key" BIGINT,
	"status" INTEGER DEFAULT 0,
	"created_by" BIGINT NOT NULL,
	"created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	"updated_by" BIGINT,
	"updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN "public"."sys_message"."id" IS '主键';
COMMENT ON COLUMN "public"."sys_message"."company_id" IS '公司id';
COMMENT ON COLUMN "public"."sys_message"."type" IS '类型(1:暂存数据;2:系统通知;3:项目动态;4:待办事项;5:互动消息;6:管家提醒;)';
COMMENT ON COLUMN "public"."sys_message"."from_user_id" IS '用户id(来自)';
COMMENT ON COLUMN "public"."sys_message"."to_user_id" IS '用户id(给谁)';
COMMENT ON COLUMN "public"."sys_message"."title" IS '标题';
COMMENT ON COLUMN "public"."sys_message"."business_type" IS '业务类型';
COMMENT ON COLUMN "public"."sys_message"."business_key" IS '业务key';
COMMENT ON COLUMN "public"."sys_message"."status" IS '状态(0:未读;1:已读;)';
COMMENT ON COLUMN "public"."sys_message"."created_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_message"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_message"."updated_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_message"."updated_at" IS '修改时间';
COMMENT ON TABLE "public"."sys_message" IS '消息表';

CREATE TABLE IF NOT EXISTS "public"."sys_role" (
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

CREATE TABLE IF NOT EXISTS "public"."user_role" (
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
INSERT INTO "public"."company" VALUES (1, NULL, 'mark', '1001', '默认单位', '默认单位', NULL, 1, now(), now());
SELECT setval('company_id_seq',(SELECT max(id) FROM "public"."company"));

INSERT INTO "public"."department" VALUES (1, 1, NULL, '101', '默认部门', NULL, NULL, 0, now(), now());
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
INSERT INTO "public"."sys_menu" VALUES (36,1,NULL,1,'工程管理','iconfont icon-xihuan','engineering',1,4,now(),now()),(37,1,36,2,'项目管理','iconfont icon-xihuan','project',1,1,now(),now()),(38,1,37,3,'新增','iconfont icon-xihuan','project:add',1,1,now(),now()),(39,1,37,3,'编辑','iconfont icon-xihuan','project:edit',1,2,now(),now()),(40,1,37,3,'删除','iconfont icon-xihuan','project:delete',1,3,now(),now());
INSERT INTO "public"."sys_menu" VALUES (41,1,26,2,'知识库','iconfont icon-xihuan','disk',1,2,now(),now()),(42,1,41,3,'新增文件夹','iconfont icon-xihuan','disk:addFolder',1,1,now(),now()),(43,1,41,3,'上传文件','iconfont icon-xihuan','disk:upload',1,2,now(),now()),(44,1,41,3,'重命名','iconfont icon-xihuan','disk:edit',1,3,now(),now()),(45,1,41,3,'删除','iconfont icon-xihuan','disk:delete',1,4,now(),now());
INSERT INTO "public"."sys_menu" VALUES (46,1,36,2,'需求沟通','iconfont icon-xihuan','requirement',1,2,now(),now()),(47,1,46,3,'新增','iconfont icon-xihuan','requirement:add',1,1,now(),now()),(48,1,46,3,'编辑','iconfont icon-xihuan','requirement:edit',1,2,now(),now()),(49,1,46,3,'删除','iconfont icon-xihuan','requirement:delete',1,3,now(),now()),(50,1,46,3,'提交/签字','iconfont icon-xihuan','requirement:submit',1,4,now(),now());
INSERT INTO "public"."sys_menu" VALUES (51,1,36,2,'方案设计','iconfont icon-xihuan','solutionDesign',1,3,now(),now()),(52,1,51,3,'新增','iconfont icon-xihuan','solutionDesign:add',1,1,now(),now()),(53,1,51,3,'编辑','iconfont icon-xihuan','solutionDesign:edit',1,2,now(),now()),(54,1,51,3,'删除','iconfont icon-xihuan','solutionDesign:delete',1,3,now(),now()),(55,1,51,3,'提交','iconfont icon-xihuan','solutionDesign:submit',1,4,now(),now()),(56,1,51,3,'确认/冻结','iconfont icon-xihuan','solutionDesign:confirm',1,5,now(),now());
INSERT INTO "public"."sys_menu" VALUES (57,1,36,2,'财务管理','iconfont icon-xihuan','finance',1,4,now(),now()),(58,1,57,2,'请款单','iconfont icon-xihuan','paymentRequest',1,1,now(),now()),(59,1,58,3,'新增','iconfont icon-xihuan','paymentRequest:add',1,1,now(),now()),(60,1,58,3,'编辑','iconfont icon-xihuan','paymentRequest:edit',1,2,now(),now()),(61,1,58,3,'删除','iconfont icon-xihuan','paymentRequest:delete',1,3,now(),now()),(62,1,58,3,'提交','iconfont icon-xihuan','paymentRequest:submit',1,4,now(),now()),(63,1,58,3,'撤销','iconfont icon-xihuan','paymentRequest:cancel',1,5,now(),now());
INSERT INTO "public"."sys_menu" VALUES (64,1,36,2,'施工图出图','iconfont icon-xihuan','constructionDrawing',1,5,now(),now()),(65,1,64,3,'新增','iconfont icon-xihuan','constructionDrawing:add',1,1,now(),now()),(66,1,64,3,'编辑','iconfont icon-xihuan','constructionDrawing:edit',1,2,now(),now()),(67,1,64,3,'提交','iconfont icon-xihuan','constructionDrawing:submit',1,3,now(),now()),(68,1,64,3,'确认阅读','iconfont icon-xihuan','constructionDrawing:confirmRead',1,4,now(),now()),(69,1,64,3,'取消归档','iconfont icon-xihuan','constructionDrawing:cancelArchive',1,5,now(),now()),(70,1,64,3,'删除','iconfont icon-xihuan','constructionDrawing:delete',1,6,now(),now());
INSERT INTO "public"."sys_menu" VALUES (71,1,36,2,'施工日志','iconfont icon-xihuan','constructionLog',1,6,now(),now()),(72,1,71,3,'新增','iconfont icon-xihuan','constructionLog:add',1,1,now(),now()),(73,1,71,3,'编辑','iconfont icon-xihuan','constructionLog:edit',1,2,now(),now()),(74,1,71,3,'删除','iconfont icon-xihuan','constructionLog:delete',1,3,now(),now());
INSERT INTO "public"."sys_menu" VALUES (75,1,36,2,'验收单','iconfont icon-xihuan','inspection',1,7,now(),now()),(76,1,75,3,'新增','iconfont icon-xihuan','inspection:add',1,1,now(),now()),(77,1,75,3,'编辑','iconfont icon-xihuan','inspection:edit',1,2,now(),now()),(78,1,75,3,'完成','iconfont icon-xihuan','inspection:complete',1,3,now(),now()),(79,1,75,3,'删除','iconfont icon-xihuan','inspection:delete',1,4,now(),now());
INSERT INTO "public"."sys_menu" VALUES (80,1,36,2,'问题单','iconfont icon-xihuan','issueTicket',1,8,now(),now()),(81,1,80,3,'新增','iconfont icon-xihuan','issueTicket:add',1,1,now(),now()),(82,1,80,3,'编辑','iconfont icon-xihuan','issueTicket:edit',1,2,now(),now()),(83,1,80,3,'指派整改','iconfont icon-xihuan','issueTicket:assign',1,3,now(),now()),(84,1,80,3,'提交整改','iconfont icon-xihuan','issueTicket:rectify',1,4,now(),now()),(85,1,80,3,'复验','iconfont icon-xihuan','issueTicket:review',1,5,now(),now()),(86,1,80,3,'删除','iconfont icon-xihuan','issueTicket:delete',1,6,now(),now());
INSERT INTO "public"."sys_menu" VALUES (87,1,36,2,'材料到场','iconfont icon-xihuan','materialArrival',1,9,now(),now()),(88,1,87,3,'新增','iconfont icon-xihuan','materialArrival:add',1,1,now(),now()),(89,1,87,3,'删除','iconfont icon-xihuan','materialArrival:delete',1,2,now(),now());
INSERT INTO "public"."sys_menu" VALUES (90,1,36,2,'交接单','iconfont icon-xihuan','projectHandover',1,10,now(),now()),(91,1,90,3,'新增','iconfont icon-xihuan','projectHandover:add',1,1,now(),now()),(92,1,90,3,'确认','iconfont icon-xihuan','projectHandover:confirm',1,2,now(),now());
INSERT INTO "public"."sys_menu" VALUES (93,1,36,2,'报修单','iconfont icon-xihuan','repair',1,11,now(),now()),(94,1,93,3,'新增','iconfont icon-xihuan','repair:add',1,1,now(),now()),(95,1,93,3,'编辑','iconfont icon-xihuan','repair:edit',1,2,now(),now()),(96,1,93,3,'指派','iconfont icon-xihuan','repair:assign',1,3,now(),now()),(97,1,93,3,'完成','iconfont icon-xihuan','repair:complete',1,4,now(),now()),(98,1,93,3,'确认','iconfont icon-xihuan','repair:confirm',1,5,now(),now()),(99,1,93,3,'删除','iconfont icon-xihuan','repair:delete',1,6,now(),now());
UPDATE "public"."sys_role" SET "path"='1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99' WHERE "id"=1;
SELECT setval('sys_menu_id_seq',(SELECT max(id) FROM "public"."sys_menu"));
COMMIT;
