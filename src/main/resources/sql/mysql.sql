CREATE TABLE IF NOT EXISTS `disk`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
    `name` varchar(255) NOT NULL COMMENT '名称',
    `url` varchar(500) NOT NULL COMMENT '链接',
    `path` VARCHAR(1000) NOT NULL COMMENT '完整节点路径，例如 /1/5/12/',
    `level` INT NOT NULL DEFAULT 1 COMMENT '目录层级',
    `size` BIGINT NOT NULL DEFAULT 0 COMMENT '大小',
    `type` int(4) DEFAULT 1 COMMENT '类型(1:文件夹;2:图片;3:word;4:excel;5:ppt;6:pdf;7:压缩文件;8:txt;9:文档;10:视频;11:其他;)',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网盘';

CREATE TABLE IF NOT EXISTS `requirement`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `style` varchar(255) NOT NULL COMMENT '风格偏好',
    `budget` varchar(255) NOT NULL COMMENT '预算范围接',
    `member` VARCHAR(255) NOT NULL COMMENT '家庭成员',
    `mark` VARCHAR(500) DEFAULT NULL COMMENT '特殊需求',
    `signature_url` VARCHAR(500) DEFAULT NULL COMMENT '签字文件url',
    `signed_at` DATETIME DEFAULT NULL COMMENT '签字时间',
    `status` int(4) DEFAULT 1 COMMENT '状态(1:待提交;2:待审核;3:已完成;)',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '需求沟通表';


CREATE TABLE IF NOT EXISTS `leave_message`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `project_member_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目角色ID',
    `type` int(4) DEFAULT 1 COMMENT '类型(1:方案设计;2:施工图出图;)',
    `remark` VARCHAR(500) NOT NULL COMMENT '说明',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '留言表';

CREATE TABLE IF NOT EXISTS `solution_design`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `status` int(4) DEFAULT 1 COMMENT '状态(1:待提交;2:待确认;3:已确认;4:已冻结;5:解冻中;6:已完成;)',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '方案设计';

CREATE TABLE IF NOT EXISTS `solution_design_detail`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `solution_design_id` BIGINT UNSIGNED NOT NULL COMMENT '方案设计ID',
    `version` int(4) DEFAULT 1 COMMENT '版本',
    `attachments` JSON NOT NULL COMMENT '效果图',
    `solution_explanation` VARCHAR(1000) NOT NULL COMMENT '方案说明',
    `annotation` VARCHAR(1000) DEFAULT NULL COMMENT '修改注释',
    `defrost_explanation` VARCHAR(1000) DEFAULT NULL COMMENT '解冻说明',
    `apply_defrost_at` DATETIME DEFAULT NULL COMMENT '申请解冻时间',
    `signature_url` VARCHAR(500) DEFAULT NULL COMMENT '签字文件url',
    `signed_at` DATETIME DEFAULT NULL COMMENT '签字时间',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '方案设计详情';

CREATE TABLE IF NOT EXISTS `construction_drawing`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `status` int(4) DEFAULT 1 COMMENT '状态(1:待提交;2:待确认;3:已归档;)',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '施工图出图';

CREATE TABLE IF NOT EXISTS `construction_drawing_detail`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `construction_drawing_id` BIGINT UNSIGNED NOT NULL COMMENT '施工图出图ID',
    `version` int(4) DEFAULT 1 COMMENT '版本',
    `architecture_attachments` JSON NOT NULL COMMENT '建设设计附件',
    `structure_attachments` JSON NOT NULL COMMENT '结构设计附件',
    `decoration_attachments` JSON NOT NULL COMMENT '精装图纸附件',
    `procurement_attachments` JSON NOT NULL COMMENT '主材及软装采购方案附件',
    `solution_explanation` VARCHAR(1000) DEFAULT NULL COMMENT '修改说明',
    `confirm_members` JSON COMMENT '成员确认',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '施工图出图详情';

CREATE TABLE IF NOT EXISTS `repair`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `type` VARCHAR(200) NOT NULL COMMENT '报修类型(水电维修、电路维修、门窗维修、家电维修、墙面地面、管道疏通、卫浴洁具、家具维修、其他问题)',
    `location` VARCHAR(200) NOT NULL COMMENT '报修位置(卫生间、客厅、卧室、厨房、阳台、其他位置)',
    `description` VARCHAR(1000) NOT NULL COMMENT '问题描述',
    `before_attachments` JSON COMMENT '维修前附件',
    `contact` VARCHAR(100) NOT NULL COMMENT '联系人',
    `contact_number` VARCHAR(200) NOT NULL COMMENT '联系电话',
    `status` int(4) DEFAULT 1 COMMENT '状态(1:已提交;2:处理中;3:待确认;4:已完成;)',
    `repair_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报修时间',
    `after_attachments` JSON COMMENT '维修后附件',
    `repair_member_id` BIGINT UNSIGNED COMMENT '维修人员ID',
    `complete_at` DATETIME COMMENT '完成时间',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报修单';

CREATE TABLE IF NOT EXISTS `company`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
    `mark` varchar(30) NOT NULL COMMENT '标识',
    `code` varchar(30) NOT NULL COMMENT '编码',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `alias` varchar(30) NOT NULL COMMENT '简称',
    `logo` varchar(100) DEFAULT NULL COMMENT 'logo',
    `sort` int(4) DEFAULT 0 COMMENT '排序',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公司';

CREATE TABLE IF NOT EXISTS `department` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
    `code` varchar(30) NOT NULL COMMENT '编码',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `director` varchar(500) DEFAULT NULL COMMENT '直接主管',
    `charge_leader` varchar(500) DEFAULT NULL COMMENT '分管领导',
    `sort` int(4) DEFAULT 0 COMMENT '排序',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门';

CREATE TABLE IF NOT EXISTS`dept_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `department_id` bigint(20) DEFAULT NULL COMMENT '部门id',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门用户';

CREATE TABLE IF NOT EXISTS `dic_classify`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典分类';

CREATE TABLE IF NOT EXISTS `dic_detail`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
    `classify_id` bigint(20) NOT NULL COMMENT '字典分类id',
    `code` varchar(30) NOT NULL COMMENT '编码',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `sort` int(4) DEFAULT 1 COMMENT '排序',
    `status` int(4) DEFAULT 1 COMMENT '状态(0:禁用;1:启用)',
    `last_stage` int(4) DEFAULT 1 COMMENT '末级(0:否;1:是)',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典详情';

CREATE TABLE IF NOT EXISTS `job`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `cron` varchar(30) NOT NULL COMMENT '表达式',
    `mark` varchar(500) NOT NULL COMMENT '标识',
    `status` int(4) DEFAULT 0 COMMENT '状态(0:未启动;1:已启动;)',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务调度';

CREATE TABLE IF NOT EXISTS `leave_request` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `applicant_id` bigint(20) NOT NULL COMMENT '申请人id',
    `leave_type` int(4) NOT NULL COMMENT '请假类型(1:事假;2:病假;3:年假;4:调休;5:婚假;6:产假;7:丧假;8:其他;)',
    `start_time` datetime NOT NULL COMMENT '开始时间',
    `end_time` datetime NOT NULL COMMENT '结束时间',
    `duration` decimal(10,2) NOT NULL COMMENT '请假时长',
    `reason` varchar(500) NOT NULL COMMENT '请假事由',
    `attachments` JSON DEFAULT NULL COMMENT '附件',
    `status` int(4) DEFAULT 0 COMMENT '状态(0:草稿;1:审批中;2:已撤销;3:已通过;4:已驳回;)',
    `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '请假单';

CREATE TABLE IF NOT EXISTS `project` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code` VARCHAR(50) NOT NULL COMMENT '项目编号',
    `cover` VARCHAR(200) NOT NULL COMMENT '封面图',
    `name` VARCHAR(200) NOT NULL COMMENT '项目名称',
    `address` VARCHAR(500) NOT NULL COMMENT '地址',
    `owner_name` VARCHAR(100) NOT NULL COMMENT '业主姓名',
    `owner_phone` VARCHAR(50) NOT NULL COMMENT '业主联系电话',
    `budget_amount` DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '预算金额',
    `status` INT UNSIGNED DEFAULT 1 COMMENT '项目状态(1:待启动;2:进行中;3:暂停;4:已交付;5:售后中;6:已完结;7:已取消;)',
    `health_status` INT UNSIGNED DEFAULT 1 COMMENT '健康状态(1:正常;2:预警;3:滞后;4:异常;)',
    `pause_reason` VARCHAR(500) DEFAULT NULL COMMENT '暂停原因',
    `cancel_reason` VARCHAR(500) DEFAULT NULL COMMENT '取消原因',
    `planned_start_date` DATE NULL COMMENT '计划开始日期',
    `planned_end_date` DATE NULL COMMENT '计划结束日期',
    `actual_start_date` DATE NULL COMMENT '实际开始日期',
    `actual_end_date` DATE NULL COMMENT '实际结束日期',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '项目';

CREATE TABLE IF NOT EXISTS `project_stage` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `flow_type` INT UNSIGNED DEFAULT NULL COMMENT '流程类型(1:筹备;2:设计;3:土建;4:精装;5:交付;6:售后;)',
    `stage_name` VARCHAR(100) NOT NULL COMMENT '阶段名称',
    `status` INT UNSIGNED DEFAULT 1 COMMENT '状态(1:未开始;2:进行中;3:待验收;4:整改中;5:已完成;)',
    `manager_user_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '阶段负责人用户ID',
    `planned_start_date` DATE DEFAULT NULL COMMENT '计划开始日期',
    `planned_end_date` DATE DEFAULT NULL COMMENT '计划完成日期',
    `planned_days` INT UNSIGNED DEFAULT NULL COMMENT '计划工期天数',
    `actual_start_date` DATE DEFAULT NULL COMMENT '实际开始日期',
    `actual_end_date` DATE DEFAULT NULL COMMENT '实际完成日期',
    `completed_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '完成人用户ID',
    `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
    `remark` VARCHAR(1000) DEFAULT NULL COMMENT '备注',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='项目阶段';

CREATE TABLE IF NOT EXISTS `project_stage_dependency` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '项目ID',
    `stage_id` BIGINT UNSIGNED NOT NULL COMMENT '当前阶段ID',
    `predecessor_stage_id` BIGINT UNSIGNED NOT NULL COMMENT '前置阶段ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='项目阶段前置依赖表';

CREATE TABLE IF NOT EXISTS `stage_inspection_item` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '项目ID',
    `stage_id` BIGINT UNSIGNED NOT NULL COMMENT '当前阶段ID',
    `name` VARCHAR(200) NOT NULL COMMENT '名称',
    `status` INT UNSIGNED DEFAULT 0 COMMENT '状态(0:未开始;1:进行中;2:已完成;)',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='阶段验收条目';

CREATE TABLE IF NOT EXISTS `project_member` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '项目id',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户id',
    `role_code` INT UNSIGNED DEFAULT 1 COMMENT '项目角色(1:设计师;2:设计助理;3:土建项目经理;4:精装项目经理;5:施工员;6:质检员;7:成控人员;8:财务人员;9:老板/管理层;10:业主;11:业主家属;)',
    `joined_at` DATETIME NOT NULL COMMENT '加入项目时间',
    `left_at` DATETIME NULL COMMENT '退出项目时间',
    `status`  INT UNSIGNED DEFAULT 1 COMMENT '状态(1:有效;2:禁用;)',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT UNSIGNED NOT NULL COMMENT '修改人',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='项目成员';

CREATE TABLE IF NOT EXISTS `construction_log` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `stage_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '当前项目阶段ID',
    `log_date` DATE NOT NULL COMMENT '日记日期',
    `filled_by` BIGINT UNSIGNED NOT NULL COMMENT '填写人用户ID',
    `current_progress_summary` VARCHAR(1000) DEFAULT NULL COMMENT '今日工作内容',
    `expected_completion_at` DATETIME DEFAULT NULL COMMENT '预计完成时间',
    `next_day_plan` TEXT NOT NULL COMMENT '明日计划',
    `site_issues` JSON DEFAULT NULL COMMENT '需要协调事项',
    `has_issue` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否存在现场问题(0:否;1:是;)',
    `assistants` JSON DEFAULT NULL COMMENT '配合人员',
    `submitted_at` DATETIME DEFAULT NULL COMMENT '提交时间',
    `attachments` JSON DEFAULT NULL COMMENT '附件',
    `status`  INT UNSIGNED DEFAULT 1 COMMENT '状态(1:已提交;2:草稿;)',
    `created_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '创建人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '修改人',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='施工日志';

CREATE TABLE IF NOT EXISTS `inspection` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '项目id',
    `stage_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '项目阶段ID',
    `inspection_items` JSON DEFAULT NULL COMMENT '项目阶段条目，格式为条目ID、条目名称、条目状态等',
    `plan_inspection_time` DATETIME DEFAULT NULL COMMENT '计划验收时间',
    `site_photos` JSON DEFAULT NULL COMMENT '验收现场总体照片',
    `participants` JSON DEFAULT NULL COMMENT '参与人员，格式为用户ID、姓名、角色等',
    `signatures` JSON DEFAULT NULL COMMENT '签字信息，包含签字人、角色、签字文件、签字时间',
    `passed_at` DATETIME DEFAULT NULL COMMENT '验收通过时间',
    `acceptance_comments` VARCHAR(1000) DEFAULT NULL COMMENT '验收意见',
    `remark` VARCHAR(1000) DEFAULT NULL COMMENT '备注',
    `status` INT UNSIGNED DEFAULT 1 COMMENT '状态(1:待验收;2:验收中;3:已完成;)',
    `created_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '创建人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '修改人',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='验收单';

CREATE TABLE IF NOT EXISTS `issue_ticket` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `source_type` INT UNSIGNED DEFAULT 1 COMMENT '来源类型(1:施工日记;2:验收单;3:交接单;)',
    `source_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '来源ID',
    `title` VARCHAR(200) NOT NULL COMMENT '问题标题',
    `description` TEXT NOT NULL COMMENT '问题描述',
    `issue_attachments` JSON COMMENT '问题附件',
    `reported_by` BIGINT UNSIGNED NOT NULL COMMENT '问题发现人用户ID',
    `reported_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '问题发现时间',
    `assigned_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '分配人用户ID',
    `assigned_at` DATETIME DEFAULT NULL COMMENT '分配时间',
    `rectification_deadline` DATETIME DEFAULT NULL COMMENT '整改截止时间',
    `rectification_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '整改提交次数',
    `rectification_description` TEXT DEFAULT NULL COMMENT '整改说明',
    `rectification_attachments` JSON COMMENT '整改附件',
    `rectification_user_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '整改责任人用户ID',
    `rectification_submitted_at` DATETIME DEFAULT NULL COMMENT '整改提交时间',
    `review_user_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '复验人或质检员用户ID',
    `review_result` INT UNSIGNED DEFAULT 1 COMMENT '复验结果(1:通过;2:不通过)',
    `review_remark` VARCHAR(1000) DEFAULT NULL COMMENT '复验说明',
    `review_attachments` JSON DEFAULT NULL COMMENT '复验附件',
    `reviewed_at` DATETIME DEFAULT NULL COMMENT '复验时间',
    `closed_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '关闭人用户ID',
    `closed_at` DATETIME DEFAULT NULL COMMENT '问题关闭时间',
    `status` INT UNSIGNED DEFAULT 1 COMMENT '状态(1:待整改;2:整改中;3:待复验;4:已关闭;)',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '修改人',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='问题单';

CREATE TABLE IF NOT EXISTS `payment_request` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `company_id` BIGINT UNSIGNED NOT NULL COMMENT '公司id',
    `amount` DECIMAL(18,2) NOT NULL COMMENT '金额',
    `reason` TEXT COMMENT '请款事由',
    `attachments` JSON COMMENT '附件',
    `applicant_id` BIGINT UNSIGNED NOT NULL COMMENT '申请人id',
    `submit_time` DATETIME DEFAULT NULL COMMENT '提交时间',
    `status` INT UNSIGNED DEFAULT 0 COMMENT '状态(0:草稿;1:审批中;2:已撤销;3:已通过;4:已驳回;)',
	`created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '修改人',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='请款单';

CREATE TABLE IF NOT EXISTS `material_arrival` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `name` VARCHAR(200) NOT NULL COMMENT '材料名称',
    `arrival_at` DATETIME NOT NULL COMMENT '到达时间',
    `remark` VARCHAR(1000) DEFAULT NULL COMMENT '备注',
    `attachments` JSON COMMENT '附件',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '修改人',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='材料到场记录';

CREATE TABLE IF NOT EXISTS `project_handover` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `project_id` BIGINT UNSIGNED NOT NULL COMMENT '所属项目ID',
    `handover_type` INT UNSIGNED DEFAULT 1 COMMENT '交接类型(1:设计->土建交接;2:设计->精装交接;3:土建->精装交接;)',
    `handover_time` DATETIME DEFAULT NULL COMMENT '交接时间',
    `handover_content` VARCHAR(1000) DEFAULT NULL COMMENT '交接内容',
    `handover_user_id` BIGINT UNSIGNED NOT NULL COMMENT '移交负责人用户ID',
    `receiver_user_id` BIGINT UNSIGNED NOT NULL COMMENT '接收负责人用户ID',
    `handover_signature_url` VARCHAR(500) NOT NULL COMMENT '移交方签字文件url',
    `receiver_signature_url` VARCHAR(500) DEFAULT NULL COMMENT '接收方签字文件url',
    `handover_signed_at` DATETIME DEFAULT NULL COMMENT '移交方签字时间',
    `receiver_signed_at` DATETIME DEFAULT NULL COMMENT '接收方签字时间',
    `attachment` JSON COMMENT '附件',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '修改人',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='交接单';

CREATE TABLE IF NOT EXISTS `phone_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `phone` varchar(30) NOT NULL COMMENT '手机号',
  `code` varchar(10) NOT NULL COMMENT '验证码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='手机验证码';

CREATE TABLE IF NOT EXISTS `sys_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `account` varchar(50) NOT NULL COMMENT '账号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(30) DEFAULT NULL COMMENT '手机号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `sex` int(4) DEFAULT 1 COMMENT '性别(1:男;2:女;)',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `position` varchar(50) DEFAULT NULL COMMENT '职位',
  `status` int(4) DEFAULT 1 COMMENT '状态(1:启用;2:禁用;)',
  `type` int(4) DEFAULT 1 COMMENT '类型(1:普通成员;2:管理员;)',
  `openid` varchar(50) DEFAULT NULL COMMENT 'openid',
  `sort` int(4) DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户';

CREATE TABLE IF NOT EXISTS `sys_icon`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `font` varchar(50) NOT NULL COMMENT 'font_class',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'icon数据';

CREATE TABLE IF NOT EXISTS `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` bigint(20) NOT NULL COMMENT '公司id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `name` varchar(50) NOT NULL COMMENT '操作名称',
  `ip` varchar(50) NOT NULL COMMENT 'ip',
  `uri` varchar(200) NOT NULL COMMENT 'uri',
  `method` varchar(255) NOT NULL COMMENT '方法',
  `params` longtext NULL COMMENT '请求参数',
  `ua` varchar(500) DEFAULT NULL COMMENT '浏览器信息',
  `status` int(4) DEFAULT 1 COMMENT '状态(1:成功;2:失败;)',
  `cost_time` int(4) NOT NULL COMMENT '花费时间',
  `result` longtext NULL COMMENT '返回结果',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日记';

CREATE TABLE IF NOT EXISTS `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` bigint(20) NOT NULL COMMENT '公司id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
  `type` int(4) DEFAULT 1 COMMENT '类型(1:目录;2:页面;3:按钮;)',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `icon` varchar(50) NOT NULL COMMENT '图标',
  `path` varchar(50) DEFAULT NULL COMMENT '路径',
  `status` int(4) DEFAULT 1 COMMENT '状态(1:启用;2:禁用;)',
  `sort` int(4) DEFAULT 1 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单';

CREATE TABLE IF NOT EXISTS `sys_message`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `type` int(4) DEFAULT 1 COMMENT '类型(1:暂存数据;2:系统通知;3:项目动态;4:待办事项;5:互动消息;6:管家提醒;)',
    `from_user_id` bigint(20) NOT NULL COMMENT '用户id(来自)',
    `to_user_id` bigint(20) NOT NULL COMMENT '用户id(给谁)',
    `title` varchar(50) NOT NULL COMMENT '标题',
    `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
    `business_key` bigint(20) DEFAULT NULL COMMENT '业务key',
    `status` int(4) DEFAULT 0 COMMENT '状态(0:未读;1:已读;)',
    `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建人',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT UNSIGNED DEFAULT NULL COMMENT '修改人',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息表';


CREATE TABLE IF NOT EXISTS `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` bigint(20) NOT NULL COMMENT '公司id',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `path` text NOT NULL COMMENT '菜单权限(以,隔开)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色';

CREATE TABLE IF NOT EXISTS `user_role` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色';


BEGIN;
INSERT INTO company VALUES (1, NULL, 'seagox', '1001', '默认单位', '默认单位', NULL, 1, now(), now());
INSERT INTO department VALUES (1, 1, NULL, '101', '默认部门', NULL, NULL, 0, now(), now());
INSERT INTO sys_role VALUES (1, 1, '管理员', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35', now(), now());
INSERT INTO sys_account VALUES (1, NULL, 'admin', NULL, NULL, '管理员', 1, '$2a$10$Y.j6uP.zc9Lpb1vk26IlOOihWA/xc/sEFpfEWE6Dlvcko14vpyVyu', NULL, 1, 2, NULL, 0, now(), now());
INSERT INTO user_role VALUES (1, 1, 1, 1, now(), now());
INSERT INTO dept_user VALUES (1, 1, 1, 1, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (1, 1, NULL, 1, '组织架构', 'iconfont icon-xihuan', 'organization', 1, 1, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (2, 1, 1, 2, '人员管理', 'iconfont icon-xihuan', 'contact', 1, 1,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (3, 1, 1, 2, '角色管理', 'iconfont icon-xihuan', 'role', 1, 2,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (4, 1, 2, 3, '导出用户', 'iconfont icon-xihuan', 'user:export', 1, 3,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (5, 1, 2, 3, '导出用户模板', 'iconfont icon-xihuan', 'user:download', 1, 1,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (6, 1, 2, 3, '导出部门模板', 'iconfont icon-xihuan', 'dept:download', 1, 1,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (7, 1, 2, 3, '新增用户', 'iconfont icon-xihuan', 'user:add', 1, 4,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (8, 1, 2, 3, '编辑用户', 'iconfont icon-xihuan', 'user:edit', 1, 5,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (9, 1, 2, 3, '删除用户', 'iconfont icon-xihuan', 'user:delete', 1, 6,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (10, 1, 2, 3, '密码重置', 'iconfont icon-xihuan', 'user:reset', 1, 7,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (11, 1, 2, 3, '导入用户', 'iconfont icon-xihuan', 'user:import', 1, 2,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (12, 1, 2, 3, '新增部门', 'iconfont icon-xihuan', 'dept:add', 1, 8,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (13, 1, 2, 3, '编辑部门', 'iconfont icon-xihuan', 'dept:edit', 1, 9,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (14, 1, 2, 3, '删除部门', 'iconfont icon-xihuan', 'dept:delete', 1, 10,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (15, 1, 3, 3, '新增', 'iconfont icon-xihuan', 'role:add', 1, 1,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (16, 1, 3, 3, '编辑', 'iconfont icon-xihuan', 'role:edit', 1, 2,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (17, 1, 3, 3, '删除', 'iconfont icon-xihuan', 'role:delete', 1, 3,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (18, 1, 2, 3, '导入部门', 'iconfont icon-xihuan', 'dept:import', 1, 11,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (19, 1, 3, 3, '授权', 'iconfont icon-xihuan', 'role:authorize', 1, 4,  now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (20, 1, NULL, 1, '我的工作', 'iconfont icon-xihuan', 'myWork', 1, 2, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (21, 1, 20, 2, '待办事项', 'iconfont icon-xihuan', 'todoItem', 1, 1, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (22, 1, 20, 2, '待发事项', 'iconfont icon-xihuan', 'readyItem', 1, 2, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (23, 1, 20, 2, '已办事项', 'iconfont icon-xihuan', 'doneItem', 1, 3, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (24, 1, 20, 2, '抄送事项', 'iconfont icon-xihuan', 'copyItem', 1, 4, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (25, 1, 20, 2, '我发起的', 'iconfont icon-xihuan', 'selfItem', 1, 5, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (26, 1, NULL, 1, '协同办公', 'iconfont icon-xihuan', 'office', 1, 3, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (27, 1, 26, 2, '请假单', 'iconfont icon-xihuan', 'leave', 1, 1, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (28, 1, 27, 3, '新增', 'iconfont icon-xihuan', 'leave:add', 1, 1, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (29, 1, 27, 3, '编辑', 'iconfont icon-xihuan', 'leave:edit', 1, 2, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (30, 1, 27, 3, '删除', 'iconfont icon-xihuan', 'leave:delete', 1, 3, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (31, 1, 27, 3, '提交', 'iconfont icon-xihuan', 'leave:submit', 1, 4, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (32, 1, 27, 3, '撤销', 'iconfont icon-xihuan', 'leave:cancel', 1, 5, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (33, 1, 27, 3, '导入', 'iconfont icon-xihuan', 'leave:import', 1, 6, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (34, 1, 27, 3, '下载模板', 'iconfont icon-xihuan', 'leave:download', 1, 7, now(), now());
INSERT INTO sys_menu (`id`, `company_id`, `parent_id`, `type`, `name`, `icon`, `path`, `status`, `sort`, `create_time`, `update_time`) VALUES (35, 1, 27, 3, '导出', 'iconfont icon-xihuan', 'leave:export', 1, 8, now(), now());
INSERT INTO sys_menu VALUES (36,1,NULL,1,'工程管理','iconfont icon-xihuan','engineering',1,4,now(),now());
INSERT INTO sys_menu VALUES (37,1,36,2,'项目管理','iconfont icon-xihuan','project',1,1,now(),now());
INSERT INTO sys_menu VALUES (38,1,37,3,'新增','iconfont icon-xihuan','project:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (39,1,37,3,'编辑','iconfont icon-xihuan','project:edit',1,2,now(),now());
INSERT INTO sys_menu VALUES (40,1,37,3,'删除','iconfont icon-xihuan','project:delete',1,3,now(),now());
INSERT INTO sys_menu VALUES (41,1,26,2,'知识库','iconfont icon-xihuan','disk',1,2,now(),now());
INSERT INTO sys_menu VALUES (42,1,41,3,'新增文件夹','iconfont icon-xihuan','disk:addFolder',1,1,now(),now());
INSERT INTO sys_menu VALUES (43,1,41,3,'上传文件','iconfont icon-xihuan','disk:upload',1,2,now(),now());
INSERT INTO sys_menu VALUES (44,1,41,3,'重命名','iconfont icon-xihuan','disk:edit',1,3,now(),now());
INSERT INTO sys_menu VALUES (45,1,41,3,'删除','iconfont icon-xihuan','disk:delete',1,4,now(),now());
INSERT INTO sys_menu VALUES (46,1,36,2,'需求沟通','iconfont icon-xihuan','requirement',1,2,now(),now());
INSERT INTO sys_menu VALUES (47,1,46,3,'新增','iconfont icon-xihuan','requirement:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (48,1,46,3,'编辑','iconfont icon-xihuan','requirement:edit',1,2,now(),now());
INSERT INTO sys_menu VALUES (49,1,46,3,'删除','iconfont icon-xihuan','requirement:delete',1,3,now(),now());
INSERT INTO sys_menu VALUES (50,1,46,3,'提交/签字','iconfont icon-xihuan','requirement:submit',1,4,now(),now());
INSERT INTO sys_menu VALUES (51,1,36,2,'方案设计','iconfont icon-xihuan','solutionDesign',1,3,now(),now());
INSERT INTO sys_menu VALUES (52,1,51,3,'新增','iconfont icon-xihuan','solutionDesign:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (53,1,51,3,'编辑','iconfont icon-xihuan','solutionDesign:edit',1,2,now(),now());
INSERT INTO sys_menu VALUES (54,1,51,3,'删除','iconfont icon-xihuan','solutionDesign:delete',1,3,now(),now());
INSERT INTO sys_menu VALUES (55,1,51,3,'提交','iconfont icon-xihuan','solutionDesign:submit',1,4,now(),now());
INSERT INTO sys_menu VALUES (56,1,51,3,'确认/冻结','iconfont icon-xihuan','solutionDesign:confirm',1,5,now(),now());
INSERT INTO sys_menu VALUES (57,1,36,2,'财务管理','iconfont icon-xihuan','finance',1,4,now(),now());
INSERT INTO sys_menu VALUES (58,1,57,2,'请款单','iconfont icon-xihuan','paymentRequest',1,1,now(),now());
INSERT INTO sys_menu VALUES (59,1,58,3,'新增','iconfont icon-xihuan','paymentRequest:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (60,1,58,3,'编辑','iconfont icon-xihuan','paymentRequest:edit',1,2,now(),now());
INSERT INTO sys_menu VALUES (61,1,58,3,'删除','iconfont icon-xihuan','paymentRequest:delete',1,3,now(),now());
INSERT INTO sys_menu VALUES (62,1,58,3,'提交','iconfont icon-xihuan','paymentRequest:submit',1,4,now(),now());
INSERT INTO sys_menu VALUES (63,1,58,3,'撤销','iconfont icon-xihuan','paymentRequest:cancel',1,5,now(),now());
INSERT INTO sys_menu VALUES (64,1,36,2,'施工图出图','iconfont icon-xihuan','constructionDrawing',1,5,now(),now());
INSERT INTO sys_menu VALUES (65,1,64,3,'新增','iconfont icon-xihuan','constructionDrawing:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (66,1,64,3,'编辑','iconfont icon-xihuan','constructionDrawing:edit',1,2,now(),now());
INSERT INTO sys_menu VALUES (67,1,64,3,'提交','iconfont icon-xihuan','constructionDrawing:submit',1,3,now(),now());
INSERT INTO sys_menu VALUES (68,1,64,3,'确认阅读','iconfont icon-xihuan','constructionDrawing:confirmRead',1,4,now(),now());
INSERT INTO sys_menu VALUES (69,1,64,3,'取消归档','iconfont icon-xihuan','constructionDrawing:cancelArchive',1,5,now(),now());
INSERT INTO sys_menu VALUES (70,1,64,3,'删除','iconfont icon-xihuan','constructionDrawing:delete',1,6,now(),now());
INSERT INTO sys_menu VALUES (71,1,36,2,'施工日志','iconfont icon-xihuan','constructionLog',1,6,now(),now());
INSERT INTO sys_menu VALUES (72,1,71,3,'新增','iconfont icon-xihuan','constructionLog:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (73,1,71,3,'编辑','iconfont icon-xihuan','constructionLog:edit',1,2,now(),now());
INSERT INTO sys_menu VALUES (74,1,71,3,'删除','iconfont icon-xihuan','constructionLog:delete',1,3,now(),now());
INSERT INTO sys_menu VALUES (75,1,36,2,'验收单','iconfont icon-xihuan','inspection',1,7,now(),now());
INSERT INTO sys_menu VALUES (76,1,75,3,'新增','iconfont icon-xihuan','inspection:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (77,1,75,3,'编辑','iconfont icon-xihuan','inspection:edit',1,2,now(),now());
INSERT INTO sys_menu VALUES (78,1,75,3,'完成','iconfont icon-xihuan','inspection:complete',1,3,now(),now());
INSERT INTO sys_menu VALUES (79,1,75,3,'删除','iconfont icon-xihuan','inspection:delete',1,4,now(),now());
INSERT INTO sys_menu VALUES (80,1,36,2,'问题单','iconfont icon-xihuan','issueTicket',1,8,now(),now());
INSERT INTO sys_menu VALUES (81,1,80,3,'新增','iconfont icon-xihuan','issueTicket:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (82,1,80,3,'编辑','iconfont icon-xihuan','issueTicket:edit',1,2,now(),now());
INSERT INTO sys_menu VALUES (83,1,80,3,'指派整改','iconfont icon-xihuan','issueTicket:assign',1,3,now(),now());
INSERT INTO sys_menu VALUES (84,1,80,3,'提交整改','iconfont icon-xihuan','issueTicket:rectify',1,4,now(),now());
INSERT INTO sys_menu VALUES (85,1,80,3,'复验','iconfont icon-xihuan','issueTicket:review',1,5,now(),now());
INSERT INTO sys_menu VALUES (86,1,80,3,'删除','iconfont icon-xihuan','issueTicket:delete',1,6,now(),now());
INSERT INTO sys_menu VALUES (87,1,36,2,'材料到场','iconfont icon-xihuan','materialArrival',1,9,now(),now());
INSERT INTO sys_menu VALUES (88,1,87,3,'新增','iconfont icon-xihuan','materialArrival:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (89,1,87,3,'删除','iconfont icon-xihuan','materialArrival:delete',1,2,now(),now());
INSERT INTO sys_menu VALUES (90,1,36,2,'交接单','iconfont icon-xihuan','projectHandover',1,10,now(),now());
INSERT INTO sys_menu VALUES (91,1,90,3,'新增','iconfont icon-xihuan','projectHandover:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (92,1,90,3,'确认','iconfont icon-xihuan','projectHandover:confirm',1,2,now(),now());
INSERT INTO sys_menu VALUES (93,1,36,2,'报修单','iconfont icon-xihuan','repair',1,11,now(),now());
INSERT INTO sys_menu VALUES (94,1,93,3,'新增','iconfont icon-xihuan','repair:add',1,1,now(),now());
INSERT INTO sys_menu VALUES (95,1,93,3,'编辑','iconfont icon-xihuan','repair:edit',1,2,now(),now());
INSERT INTO sys_menu VALUES (96,1,93,3,'指派','iconfont icon-xihuan','repair:assign',1,3,now(),now());
INSERT INTO sys_menu VALUES (97,1,93,3,'完成','iconfont icon-xihuan','repair:complete',1,4,now(),now());
INSERT INTO sys_menu VALUES (98,1,93,3,'确认','iconfont icon-xihuan','repair:confirm',1,5,now(),now());
INSERT INTO sys_menu VALUES (99,1,93,3,'删除','iconfont icon-xihuan','repair:delete',1,6,now(),now());
UPDATE sys_role SET path='1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99' WHERE id=1;
COMMIT;
