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

DROP TABLE IF EXISTS `leave_request`;
CREATE TABLE `leave_request` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `applicant_id` bigint(20) NOT NULL COMMENT '申请人id',
    `leave_type` int(4) NOT NULL COMMENT '请假类型(1:事假;2:病假;3:年假;4:调休;5:婚假;6:产假;7:丧假;8:其他;)',
    `start_time` datetime NOT NULL COMMENT '开始时间',
    `end_time` datetime NOT NULL COMMENT '结束时间',
    `duration` decimal(10,2) NOT NULL COMMENT '请假时长',
    `reason` varchar(500) NOT NULL COMMENT '请假事由',
    `status` int(4) DEFAULT 0 COMMENT '状态(0:草稿;1:审批中;2:已撤销;3:已通过;4:已驳回;)',
    `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '请假单';


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
  `params` longtext NULL COMMENT '请求参数',
  `ua` varchar(500) DEFAULT NULL COMMENT '浏览器信息',
  `status` int(4) DEFAULT 1 COMMENT '状态(1:成功;2:失败;)',
  `cost_time` int(4) NOT NULL COMMENT '花费时间',
  `result` longtext NULL COMMENT '返回结果',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日记';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
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

DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` bigint(20) NOT NULL COMMENT '公司id',
    `type` int(4) DEFAULT 1 COMMENT '类型(1:暂存数据;)',
    `from_user_id` bigint(20) NOT NULL COMMENT '用户id(来自)',
    `to_user_id` bigint(20) NOT NULL COMMENT '用户id(给谁)',
    `title` varchar(50) NOT NULL COMMENT '标题',
    `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
    `business_key` bigint(20) DEFAULT NULL COMMENT '业务key',
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
COMMIT;
