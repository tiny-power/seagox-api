DROP TABLE IF EXISTS `business_field`;
CREATE TABLE `business_field`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `business_table_id` bigint(20) NOT NULL COMMENT '业务表id',
    `name` varchar(64) NOT NULL COMMENT '名称',
    `remark` varchar(64) NOT NULL COMMENT '注释',
    `type` varchar(20) NOT NULL COMMENT '类型',
    `kind` varchar(20) NOT NULL COMMENT '种类',
    `length` int(4) DEFAULT 0 COMMENT '长度',
    `decimals` int(4) DEFAULT 0 COMMENT '小数',
    `not_null` int(4) DEFAULT 0 COMMENT '不为空(0:否;1:是;)',
    `default_value` varchar(200) DEFAULT NULL COMMENT '默认值',
    `target_table_id` bigint(20) DEFAULT NULL COMMENT '目标模型',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务字段';

DROP TABLE IF EXISTS `business_table`;
CREATE TABLE `business_table`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `name` varchar(64) NOT NULL COMMENT '名称',
    `remark` varchar(64) NOT NULL COMMENT '注释',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务表';

DROP TABLE IF EXISTS `company`;
CREATE TABLE `company`  (
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

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
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

DROP TABLE IF EXISTS `dept_user`;
CREATE TABLE `dept_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `department_id` bigint(20) DEFAULT NULL COMMENT '部门id',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门用户';

DROP TABLE IF EXISTS `dic_classify`;
CREATE TABLE `dic_classify`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典分类';

DROP TABLE IF EXISTS `dic_detail`;
CREATE TABLE `dic_detail`  (
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

DROP TABLE IF EXISTS `door`;
CREATE TABLE `door` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `name` varchar(30) NOT NULL COMMENT '名称',
	`authority` text DEFAULT NULL COMMENT '权限',
	`path` bigint(20) NOT NULL COMMENT '页面路径',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门户管理';

DROP TABLE IF EXISTS `form`;
CREATE TABLE `form`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `mark` varchar(100) NOT NULL COMMENT '标识',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `data_source` bigint(20) NOT NULL COMMENT '数据源配置',
    `workbook` text NOT NULL COMMENT 'excel配置',
    `options` longtext DEFAULT NULL COMMENT '参数json',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单管理';

DROP TABLE IF EXISTS `form_athority`;
CREATE TABLE `form_athority`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `form_id` bigint(20) NOT NULL COMMENT '表单id',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `members` text NOT NULL COMMENT '权限成员',
    `type` int(4) DEFAULT 1 COMMENT '类型(1:提交状态;2:查看状态;)',
    `field` text NOT NULL COMMENT '字段权限',
    `scope` text COMMENT '数据范围',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单权限';

DROP TABLE IF EXISTS `gauge`;
CREATE TABLE `gauge` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `config` text DEFAULT NULL COMMENT '配置',
    `script` text DEFAULT NULL COMMENT '脚本',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仪表板';

DROP TABLE IF EXISTS `import_rule`;
CREATE TABLE `import_rule`  (
    `id` bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '主键',
    `form_id` bigint(20) NOT NULL COMMENT '表单id',
    `start_line` int(4) DEFAULT 2 COMMENT '数据起始行',
    `rules` text COMMENT '规则',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '导入规则';


DROP TABLE IF EXISTS `job`;
CREATE TABLE `job`  (
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

DROP TABLE IF EXISTS `sea_definition`;
CREATE TABLE `sea_definition`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `form_id` bigint(20) NOT NULL COMMENT '表单id',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `resources` text DEFAULT NULL comment '流程文件',
    `empower` text comment '授权',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程定义';

DROP TABLE IF EXISTS `sea_instance`;
CREATE TABLE `sea_instance`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `name` varchar(50) NOT NULL COMMENT '名称',
    `business_type` varchar(50) NOT NULL COMMENT '业务类型',
    `business_key` varchar(50) NOT NULL COMMENT '业务key',
    `resources` text NOT NULL comment '流程文件',
    `status` int(4) DEFAULT 0 COMMENT '状态(0:活动;1:完成;2:暂停;3:终止;)',
    `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程实例';

DROP TABLE IF EXISTS `sea_node`;
CREATE TABLE `sea_node`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `def_id` bigint(20) NOT NULL COMMENT '流程实例id',
    `mark` varchar(50) NOT NULL COMMENT '节点id',
    `label` varchar(50) NOT NULL COMMENT '节点名称',
    `type` int(4) DEFAULT 1 COMMENT '类型(1:开始;2:结束;3:审批任务;4:抄送任务;5:脚本任务;6:排它网关;7:并行网关;8:手动选择;9:空节点;)',
    `status` int(4) DEFAULT 0 COMMENT '状态(0:活动;1:通过;2:不通过;3:完成;4:终止;)',
    `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
    `precede` varchar(500) DEFAULT NULL COMMENT '前导',
    `path` varchar(5000) NOT NULL COMMENT '路径',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点';

DROP TABLE IF EXISTS `sea_node_detail`;
CREATE TABLE `sea_node_detail`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `node_id` bigint(20) NOT NULL COMMENT '流程节点id',
    `name` varchar(30) NOT NULL COMMENT '名称',
    `assignee` varchar(30) NOT NULL COMMENT '签收人或被委托id',
    `status` int(4) DEFAULT 0 COMMENT '状态(0:待办;1:同意;2:拒绝;3:已阅;)',
    `remark` varchar(255) DEFAULT NULL COMMENT '评论',
    `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点详情';

DROP TABLE IF EXISTS `serial`;
CREATE TABLE `serial` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `name` varchar(30) NOT NULL COMMENT '名称',
	`options` text NOT NULL COMMENT '要素',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '编号设置';

DROP TABLE IF EXISTS `shortcut`;
CREATE TABLE `shortcut`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '快捷入口';

DROP TABLE IF EXISTS `sys_account`;
CREATE TABLE `sys_account` (
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

DROP TABLE IF EXISTS `sys_icon`;
CREATE TABLE `sys_icon`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `font` varchar(50) NOT NULL COMMENT 'font_class',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'icon数据';

DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` bigint(20) NOT NULL COMMENT '公司id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `name` varchar(50) NOT NULL COMMENT '操作名称',
  `ip` varchar(50) NOT NULL COMMENT 'ip',
  `uri` varchar(200) NOT NULL COMMENT 'uri',
  `method` varchar(255) NOT NULL COMMENT '方法',
  `params` longtext DEFAULT NULL COMMENT '请求参数',
  `ua` varchar(500) DEFAULT NULL COMMENT '浏览器信息',
  `status` int(4) DEFAULT 1 COMMENT '状态(1:成功;2:失败;)',
  `cost_time` int(4) NOT NULL COMMENT '花费时间',
  `result` longtext DEFAULT NULL COMMENT '返回结果',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日记';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` bigint(20) NOT NULL COMMENT '公司id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
  `classify` int(4) DEFAULT 1 COMMENT '类别(1:PC端;2:移动端;)',
  `type` int(4) DEFAULT 1 COMMENT '类型(1:表单列表;2:按钮;3:新增表单;4:系统菜单;5:目录;6:仪表板;7:单页面;)',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `icon` varchar(50) NOT NULL COMMENT '图标',
  `path` varchar(50) DEFAULT NULL COMMENT '路径',
  `status` int(4) DEFAULT 1 COMMENT '状态(1:启用;2:禁用;)',
  `sort` int(4) DEFAULT 1 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单';

DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `type` int(4) DEFAULT 1 COMMENT '类型(1:暂存数据;)',
    `from_user_id` bigint(20) NOT NULL COMMENT '用户id(来自)',
    `to_user_id` bigint(20) NOT NULL COMMENT '用户id(给谁)',
    `title` varchar(50) NOT NULL COMMENT '标题',
    `business_type` bigint(20) DEFAULT NUll COMMENT '业务类型',
    `business_key` bigint(20) DEFAULT NUll COMMENT '业务key',
    `status` int(4) DEFAULT 0 COMMENT '状态(0:未读;1:已读;)',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息表';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` bigint(20) NOT NULL COMMENT '公司id',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `path` text NOT NULL COMMENT '菜单权限(以,隔开)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色';

DROP TABLE IF EXISTS `table_column_config`;
CREATE TABLE `table_column_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `form_id` bigint(20) NOT NULL COMMENT '表单id',
  `options` longtext NOT NULL COMMENT '配置',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表头配置';

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
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
INSERT INTO sys_role VALUES (1, 1, '管理员', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19', now(), now());
INSERT INTO sys_account VALUES (1, NULL, 'admin', NULL, NULL, '管理员', 1, '$2a$10$7xaqWKLFZRc2mg7JIX.B/OCtijP2zYZack60pbC3WxDGvtfvKld3W', NULL, 1, 2, NULL, 0, now(), now());
INSERT INTO user_role VALUES (1, 1, 1, 1, now(), now());
INSERT INTO dept_user VALUES (1, 1, 1, 1, now(), now());
INSERT INTO sys_menu VALUES (1, 1, NULL, 5, '组织架构', 'iconfont icon-xihuan', 'organization', 1, 1, now(), now());
INSERT INTO sys_menu VALUES (2, 1, 1, 4, '人员管理', 'iconfont icon-xihuan', 'contact', 1, 1,  now(), now());
INSERT INTO sys_menu VALUES (3, 1, 1, 4, '角色管理', 'iconfont icon-xihuan', 'role', 1, 2,  now(), now());
INSERT INTO sys_menu VALUES (4, 1, 2, 2, '导出用户', 'iconfont icon-xihuan', 'user:export', 1, 3,  now(), now());
INSERT INTO sys_menu VALUES (5, 1, 2, 2, '导出用户模板', 'iconfont icon-xihuan', 'user:download', 1, 1,  now(), now());
INSERT INTO sys_menu VALUES (6, 1, 2, 2, '导出部门模板', 'iconfont icon-xihuan', 'dept:download', 1, 1,  now(), now());
INSERT INTO sys_menu VALUES (7, 1, 2, 2, '新增用户', 'iconfont icon-xihuan', 'user:add', 1, 4,  now(), now());
INSERT INTO sys_menu VALUES (8, 1, 2, 2, '编辑用户', 'iconfont icon-xihuan', 'user:edit', 1, 5,  now(), now());
INSERT INTO sys_menu VALUES (9, 1, 2, 2, '删除用户', 'iconfont icon-xihuan', 'user:delete', 1, 6,  now(), now());
INSERT INTO sys_menu VALUES (10, 1, 2, 2, '密码重置', 'iconfont icon-xihuan', 'user:reset', 1, 7,  now(), now());
INSERT INTO sys_menu VALUES (11, 1, 2, 2, '导入用户', 'iconfont icon-xihuan', 'user:import', 1, 2,  now(), now());
INSERT INTO sys_menu VALUES (12, 1, 2, 2, '新增部门', 'iconfont icon-xihuan', 'dept:add', 1, 8,  now(), now());
INSERT INTO sys_menu VALUES (13, 1, 2, 2, '编辑部门', 'iconfont icon-xihuan', 'dept:edit', 1, 9,  now(), now());
INSERT INTO sys_menu VALUES (14, 1, 2, 2, '删除部门', 'iconfont icon-xihuan', 'dept:delete', 1, 10,  now(), now());
INSERT INTO sys_menu VALUES (15, 1, 3, 2, '新增', 'iconfont icon-xihuan', 'role:add', 1, 1,  now(), now());
INSERT INTO sys_menu VALUES (16, 1, 3, 2, '编辑', 'iconfont icon-xihuan', 'role:edit', 1, 2,  now(), now());
INSERT INTO sys_menu VALUES (17, 1, 3, 2, '删除', 'iconfont icon-xihuan', 'role:delete', 1, 3,  now(), now());
INSERT INTO sys_menu VALUES (18, 1, 2, 2, '导入部门', 'iconfont icon-xihuan', 'dept:import', 1, 11,  now(), now());
INSERT INTO sys_menu VALUES (19, 1, 3, 2, '授权', 'iconfont icon-xihuan', 'role:authorize', 1, 4,  now(), now());
COMMIT;
