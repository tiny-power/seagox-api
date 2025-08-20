CREATE TABLE business_field (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    business_table_id NUMBER(20) NOT NULL,
    name VARCHAR2(64) NOT NULL,
    remark VARCHAR2(64) NOT NULL,
    type VARCHAR2(20) NOT NULL,
    kind VARCHAR2(20) NOT NULL,
    length NUMBER(4) DEFAULT 0,
    decimals NUMBER(4) DEFAULT 0,
    not_null NUMBER(4) DEFAULT 0,
    default_value VARCHAR2(200),
    target_table_id NUMBER(20) DEFAULT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence business_field_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN business_field.id IS '主键';
COMMENT ON COLUMN business_field.business_table_id IS '业务表id';
COMMENT ON COLUMN business_field.name IS '名称';
COMMENT ON COLUMN business_field.remark IS '注释';
COMMENT ON COLUMN business_field.type IS '类型';
COMMENT ON COLUMN business_field.kind IS '种类';
COMMENT ON COLUMN business_field.length IS '长度';
COMMENT ON COLUMN business_field.decimals IS '小数';
COMMENT ON COLUMN business_field.not_null IS '不为空(1:是;0:否;)';
COMMENT ON COLUMN business_field.default_value IS '默认值';
COMMENT ON COLUMN business_field.target_table_id IS '目标模型';
COMMENT ON COLUMN business_field.create_time IS '创建时间';
COMMENT ON COLUMN business_field.update_time IS '更新时间';
COMMENT ON TABLE business_field IS '业务字段';

CREATE TABLE business_table (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    name VARCHAR2(64) NOT NULL,
    remark VARCHAR2(64) NOT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence business_table_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN business_table.id IS '主键';
COMMENT ON COLUMN business_table.company_id IS '公司id';
COMMENT ON COLUMN business_table.name IS '名称';
COMMENT ON COLUMN business_table.remark IS '注释';
COMMENT ON COLUMN business_table.create_time IS '创建时间';
COMMENT ON COLUMN business_table.update_time IS '更新时间';
COMMENT ON TABLE business_table IS '业务表';

CREATE TABLE company (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    parent_id NUMBER(20),
    mark VARCHAR2(30) NOT NULL,
    code VARCHAR2(30) NOT NULL,
    name VARCHAR2(30) NOT NULL,
    alias VARCHAR2(30) NOT NULL,
    logo VARCHAR2(100) DEFAULT NULL,
    sort NUMBER(4) DEFAULT 0,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence company_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN company.id IS '主键';
COMMENT ON COLUMN company.parent_id IS '上级id';
COMMENT ON COLUMN company.mark IS '标识';
COMMENT ON COLUMN company.code IS '编码';
COMMENT ON COLUMN company.name IS '名称';
COMMENT ON COLUMN company.alias IS '简称';
COMMENT ON COLUMN company.logo IS 'logo';
COMMENT ON COLUMN company.sort IS '排序';
COMMENT ON COLUMN company.create_time IS '创建时间';
COMMENT ON COLUMN company.update_time IS '更新时间';
COMMENT ON TABLE company IS '公司';

CREATE TABLE department (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    parent_id NUMBER(20),
    code VARCHAR2(30) NOT NULL,
    name VARCHAR2(30) NOT NULL,
    director VARCHAR2(500),
    charge_leader VARCHAR2(500),
    sort NUMBER(4) DEFAULT 0,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence department_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN department.id IS '主键';
COMMENT ON COLUMN department.company_id IS '公司id';
COMMENT ON COLUMN department.parent_id IS '上级id';
COMMENT ON COLUMN department.code IS '编码';
COMMENT ON COLUMN department.name IS '名称';
COMMENT ON COLUMN department.director IS '直接主管';
COMMENT ON COLUMN department.charge_leader IS '分管领导';
COMMENT ON COLUMN department.sort IS '排序';
COMMENT ON COLUMN department.create_time IS '创建时间';
COMMENT ON COLUMN department.update_time IS '更新时间';
COMMENT ON TABLE department IS '部门';

CREATE TABLE dept_user (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    department_id NUMBER(20) NOT NULL,
    user_id NUMBER(20) NOT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence dept_user_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN dept_user.id IS '主键';
COMMENT ON COLUMN dept_user.company_id IS '公司id';
COMMENT ON COLUMN dept_user.department_id IS '部门id';
COMMENT ON COLUMN dept_user.user_id IS '用户id';
COMMENT ON COLUMN dept_user.create_time IS '创建时间';
COMMENT ON COLUMN dept_user.update_time IS '更新时间';
COMMENT ON TABLE dept_user IS '部门用户';

CREATE TABLE dic_classify (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    name VARCHAR2(30) NOT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence dic_classify_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN dic_classify.id IS '主键';
COMMENT ON COLUMN dic_classify.company_id IS '公司id';
COMMENT ON COLUMN dic_classify.name IS '名称';
COMMENT ON COLUMN dic_classify.create_time IS '创建时间';
COMMENT ON COLUMN dic_classify.update_time IS '更新时间';
COMMENT ON TABLE dic_classify IS '字典分类';

CREATE TABLE dic_detail (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    parent_id NUMBER(20),
    classify_id NUMBER(20) NOT NULL,
    code VARCHAR2(30) NOT NULL,
    name VARCHAR2(30) NOT NULL,
    sort NUMBER(4) DEFAULT 1,
    status NUMBER(4) DEFAULT 1,
    last_stage NUMBER(4) DEFAULT 1,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence dic_detail_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN dic_detail.id IS '主键';
COMMENT ON COLUMN dic_detail.parent_id IS '上级id';
COMMENT ON COLUMN dic_detail.classify_id IS '字典分类id';
COMMENT ON COLUMN dic_detail.code IS '编码';
COMMENT ON COLUMN dic_detail.name IS '名称';
COMMENT ON COLUMN dic_detail.sort IS '排序';
COMMENT ON COLUMN dic_detail.status IS '状态(0:禁用;1:启用)';
COMMENT ON COLUMN dic_detail.last_stage IS '末级(0:否;1:是)';
COMMENT ON COLUMN dic_detail.create_time IS '创建时间';
COMMENT ON COLUMN dic_detail.update_time IS '更新时间';
COMMENT ON TABLE dic_detail IS '字典详情';

CREATE TABLE door (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    name VARCHAR2(30) NOT NULL,
	authority clob,
	path NUMBER(20) NOT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence door_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN door.id IS '主键';
COMMENT ON COLUMN door.company_id IS '公司id';
COMMENT ON COLUMN door.name IS '名称';
COMMENT ON COLUMN door.authority IS '权限';
COMMENT ON COLUMN door.path IS '页面路径';
COMMENT ON COLUMN door.create_time IS '创建时间';
COMMENT ON COLUMN door.update_time IS '更新时间';
COMMENT ON TABLE door IS '门户管理';

CREATE TABLE form (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    mark VARCHAR2(100) NOT NULL,
    name VARCHAR2(30) NOT NULL,
    data_source NUMBER(20) NOT NULL,
    workbook clob,
    options clob,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence form_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN form.id IS '主键';
COMMENT ON COLUMN form.company_id IS '公司id';
COMMENT ON COLUMN form.mark IS '标识';
COMMENT ON COLUMN form.name IS '名称';
COMMENT ON COLUMN form.data_source IS '数据源配置';
COMMENT ON COLUMN form.workbook IS 'excel配置';
COMMENT ON COLUMN form.options IS '其他参数';
COMMENT ON COLUMN form.create_time IS '创建时间';
COMMENT ON COLUMN form.update_time IS '更新时间';
COMMENT ON TABLE form IS '表单管理';

CREATE TABLE form_athority  (
	id NUMBER(20) PRIMARY KEY NOT NULL,
    form_id NUMBER(20) NOT NULL,
    name VARCHAR2(30) NOT NULL,
    members clob,
    type NUMBER(4),
	field clob,
	scope clob,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence form_athority_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN form_athority.id IS '主键';
COMMENT ON COLUMN form_athority.form_id IS '表单id';
COMMENT ON COLUMN form_athority.name IS '名称';
COMMENT ON COLUMN form_athority.members IS '权限成员';
COMMENT ON COLUMN form_athority.type IS '类型(1:提交状态;2:查看状态;)';
COMMENT ON COLUMN form_athority.field IS '字段权限';
COMMENT ON COLUMN form_athority.scope IS '数据范围';
COMMENT ON TABLE form_athority IS '表单权限';

CREATE TABLE gauge (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    name VARCHAR2(30) NOT NULL,
    config clob,
    script clob,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence gauge_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN gauge.id IS '主键';
COMMENT ON COLUMN gauge.company_id IS '公司id';
COMMENT ON COLUMN gauge.name IS '名称';
COMMENT ON COLUMN gauge.config IS '配置';
COMMENT ON COLUMN gauge.script IS '脚本';
COMMENT ON COLUMN gauge.create_time IS '创建时间';
COMMENT ON COLUMN gauge.update_time IS '更新时间';
COMMENT ON TABLE gauge IS '仪表板';

CREATE TABLE import_rule  (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    form_id NUMBER(20) NOT NULL,
    start_line NUMBER(4) DEFAULT 2,
    rules clob,
    create_time date DEFAULT CURRENT_TIMESTAMP,
	update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence import_rule_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN import_rule.id IS '主键';
COMMENT ON COLUMN import_rule.form_id IS '表单id';
COMMENT ON COLUMN import_rule.start_line IS '数据起始行';
COMMENT ON COLUMN import_rule.rules IS '规则';
COMMENT ON COLUMN import_rule.create_time IS '创建时间';
COMMENT ON COLUMN import_rule.update_time IS '更新时间';
COMMENT ON TABLE import_rule IS '导入规则';

CREATE TABLE job (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    name VARCHAR2(30) NOT NULL,
    cron VARCHAR2(30) NOT NULL,
    mark VARCHAR2(50) NOT NULL,
    status NUMBER(4) DEFAULT 0,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence job_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN job.id IS '主键';
COMMENT ON COLUMN job.company_id IS '公司id';
COMMENT ON COLUMN job.name IS '名称';
COMMENT ON COLUMN job.cron IS '表达式';
COMMENT ON COLUMN job.mark IS '标识';
COMMENT ON COLUMN job.status IS '状态(0:未启动;1:已启动;)';
COMMENT ON COLUMN job.create_time IS '创建时间';
COMMENT ON COLUMN job.update_time IS '更新时间';
COMMENT ON TABLE job IS '任务调度';

CREATE TABLE sea_definition  (
    id NUMBER(20) PRIMARY KEY NOT NULL,
	form_id NUMBER(20) NOT NULL,
	name VARCHAR2(30) NOT NULL,
    resources clob DEFAULT NULL,
    empower clob DEFAULT ,
    create_time date DEFAULT CURRENT_TIMESTAMP,
	update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence sea_definition_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN sea_definition.id IS '主键';
COMMENT ON COLUMN sea_definition.form_id IS '表单id';
COMMENT ON COLUMN sea_definition.name IS '名称';
COMMENT ON COLUMN sea_definition.resources IS '流程文件';
COMMENT ON COLUMN sea_definition.empower IS '授权';
COMMENT ON COLUMN sea_definition.create_time IS '创建时间';
COMMENT ON COLUMN sea_definition.update_time IS '更新时间';
COMMENT ON TABLE sea_definition IS '流程定义';

CREATE TABLE sea_instance (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    user_id NUMBER(20) NOT NULL,
    name VARCHAR2(50) NOT NULL,
    business_type VARCHAR2(50) NOT NULL,
    business_key VARCHAR2(50) NOT NULL,
    resources clob NOT NULL,
    status NUMBER(4) DEFAULT 0,
    start_time date DEFAULT CURRENT_TIMESTAMP,
    end_time date,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence sea_instance_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN sea_instance.id IS '主键';
COMMENT ON COLUMN sea_instance.company_id IS '公司id';
COMMENT ON COLUMN sea_instance.user_id IS '用户id';
COMMENT ON COLUMN sea_instance.name IS '名称';
COMMENT ON COLUMN sea_instance.business_type IS '业务类型';
COMMENT ON COLUMN sea_instance.business_key IS '业务key';
COMMENT ON COLUMN sea_instance.resources IS '流程文件';
COMMENT ON COLUMN sea_instance.status IS '状态(0:活动;1:完成;2:暂停;3:终止;)';
COMMENT ON COLUMN sea_instance.start_time IS '开始时间';
COMMENT ON COLUMN sea_instance.end_time IS '结束时间';
COMMENT ON COLUMN sea_instance.create_time IS '创建时间';
COMMENT ON COLUMN sea_instance.update_time IS '更新时间';
COMMENT ON TABLE sea_instance IS '流程实例';

CREATE TABLE sea_node (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    def_id NUMBER(20) NOT NULL,
    mark VARCHAR2(50) NOT NULL,
    label VARCHAR2(50) NOT NULL,
    type NUMBER(4) DEFAULT 1,
    status NUMBER(4) DEFAULT 0,
    start_time date DEFAULT CURRENT_TIMESTAMP,
    end_time date,
    precede VARCHAR2(500) DEFAULT NULL,
    path VARCHAR2(5000) NOT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence sea_node_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN sea_node.id IS '主键';
COMMENT ON COLUMN sea_node.def_id IS '流程实例id';
COMMENT ON COLUMN sea_node.mark IS '节点id';
COMMENT ON COLUMN sea_node.label IS '节点名称';
COMMENT ON COLUMN sea_node.type IS '类型(1:开始;2:结束;3:审批任务;4:抄送任务;5:脚本任务;6:排它网关;7:并行网关;8:手动选择;9:空节点;)';
COMMENT ON COLUMN sea_node.status IS '状态(0:活动;1:通过;2:不通过;3:完成;4:终止;)';
COMMENT ON COLUMN sea_node.start_time IS '开始时间';
COMMENT ON COLUMN sea_node.end_time IS '结束时间';
COMMENT ON COLUMN sea_node.precede IS '前导';
COMMENT ON COLUMN sea_node.path IS '路径';
COMMENT ON COLUMN sea_node.create_time IS '创建时间';
COMMENT ON COLUMN sea_node.update_time IS '更新时间';
COMMENT ON TABLE sea_node IS '流程节点';

CREATE TABLE sea_node_detail (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    node_id NUMBER(20) NOT NULL,
    name VARCHAR2(30) NOT NULL,
    assignee VARCHAR2(30) NOT NULL,
    status NUMBER(4) DEFAULT 0,
    remark VARCHAR2(255),
    start_time date DEFAULT CURRENT_TIMESTAMP,
    end_time date,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence sea_node_detail_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN sea_node_detail.id IS '主键';
COMMENT ON COLUMN sea_node_detail.company_id IS '公司id';
COMMENT ON COLUMN sea_node_detail.node_id IS '流程节点id';
COMMENT ON COLUMN sea_node_detail.name IS '名称';
COMMENT ON COLUMN sea_node_detail.assignee IS '签收人或被委托id';
COMMENT ON COLUMN sea_node_detail.status IS '状态(0:待办;1:同意;2:拒绝;3:已阅;)';
COMMENT ON COLUMN sea_node_detail.remark IS '评论';
COMMENT ON COLUMN sea_node_detail.start_time IS '开始时间';
COMMENT ON COLUMN sea_node_detail.end_time IS '结束时间';
COMMENT ON COLUMN sea_node_detail.create_time IS '创建时间';
COMMENT ON COLUMN sea_node_detail.update_time IS '更新时间';
COMMENT ON TABLE sea_node_detail IS '流程节点详情';

CREATE TABLE serial (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    name VARCHAR2(30) NOT NULL,
	options clob NOT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence door_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN serial.id IS '主键';
COMMENT ON COLUMN serial.company_id IS '公司id';
COMMENT ON COLUMN serial.name IS '名称';
COMMENT ON COLUMN serial.options IS '要素';
COMMENT ON COLUMN serial.create_time IS '创建时间';
COMMENT ON COLUMN serial.update_time IS '更新时间';
COMMENT ON TABLE serial IS '编号设置';

CREATE TABLE shortcut (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    user_id NUMBER(20) NOT NULL,
    menu_id NUMBER(20) NOT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence shortcut_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN shortcut.id IS '主键';
COMMENT ON COLUMN shortcut.company_id IS '公司id';
COMMENT ON COLUMN shortcut.user_id IS '用户id';
COMMENT ON COLUMN shortcut.menu_id IS '菜单id';
COMMENT ON COLUMN shortcut.create_time IS '创建时间';
COMMENT ON COLUMN shortcut.update_time IS '更新时间';
COMMENT ON TABLE shortcut IS '快捷入口';

CREATE TABLE sys_account (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    avatar VARCHAR2(255) DEFAULT NULL,
    account VARCHAR2(50) NOT NULL,
    email VARCHAR2(50),
    phone VARCHAR2(30),
    name VARCHAR2(50) NOT NULL,
    sex NUMBER(4) DEFAULT 1,
    password VARCHAR2(255) NOT NULL,
    position VARCHAR2(50),
    status NUMBER(4) DEFAULT 1 NOT NULL,
    type NUMBER(4) DEFAULT 1,
    openid VARCHAR2(100),
	sort NUMBER(4) DEFAULT 0,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence sys_account_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN sys_account.id IS '主键';
COMMENT ON COLUMN sys_account.avatar IS '头像';
COMMENT ON COLUMN sys_account.account IS '账号';
COMMENT ON COLUMN sys_account.name IS '姓名';
COMMENT ON COLUMN sys_account.sex IS '性别(1:男;2:女;)';
COMMENT ON COLUMN sys_account.password IS '密码';
COMMENT ON COLUMN sys_account.position IS '职位';
COMMENT ON COLUMN sys_account.status IS '状态(1:启用;2:禁用;)';
COMMENT ON COLUMN sys_account.create_time IS '创建时间';
COMMENT ON COLUMN sys_account.update_time IS '更新时间';
COMMENT ON COLUMN sys_account.type IS '类型(1:普通成员;2:管理员;)';
COMMENT ON COLUMN sys_account.email IS '邮箱';
COMMENT ON COLUMN sys_account.phone IS '手机号';
COMMENT ON COLUMN sys_account.openid IS 'openid';
COMMENT ON COLUMN sys_account.sort IS '排序';
COMMENT ON TABLE sys_account IS '用户';

CREATE TABLE sys_icon  (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    name VARCHAR2(30) NOT NULL,
    font VARCHAR2(50) NOT NULL
);
CREATE sequence sys_icon_seq increment BY 1 start WITH 1 nomaxvalue minvalue 1 ORDER nocycle;
COMMENT ON COLUMN sys_icon.id IS '主键';
COMMENT ON COLUMN sys_icon.name IS '名称';
COMMENT ON COLUMN sys_icon.font IS 'font_class';
COMMENT ON TABLE sys_icon IS 'icon数据';

CREATE TABLE sys_log (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    user_id NUMBER(20) NOT NULL,
    name VARCHAR2(100) NOT NULL,
    ip VARCHAR2(100) NOT NULL,
    uri VARCHAR2(200) NOT NULL,
    method VARCHAR2(500) NOT NULL,
    params clob,
    status NUMBER(4) DEFAULT 1,
    cost_time NUMBER(4) NOT NULL,
    result clob,
    ua VARCHAR2(1000),
    create_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence sys_log_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN sys_log.id IS '主键';
COMMENT ON COLUMN sys_log.company_id IS '公司id';
COMMENT ON COLUMN sys_log.user_id IS '用户id';
COMMENT ON COLUMN sys_log.name IS '操作名称';
COMMENT ON COLUMN sys_log.ip IS 'ip';
COMMENT ON COLUMN sys_log.uri IS 'uri';
COMMENT ON COLUMN sys_log.method IS '方法';
COMMENT ON COLUMN sys_log.params IS '请求参数';
COMMENT ON COLUMN sys_log.status IS '状态(1:成功;2:失败;)';
COMMENT ON COLUMN sys_log.cost_time IS '花费时间';
COMMENT ON COLUMN sys_log.result IS '返回结果';
COMMENT ON COLUMN sys_log.ua IS '浏览器信息';
COMMENT ON COLUMN sys_log.create_time IS '创建时间';
COMMENT ON TABLE sys_log IS '操作日记';

CREATE TABLE sys_menu (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    parent_id NUMBER(20),
    classify NUMBER(4) DEFAULT 1,
    type NUMBER(4) DEFAULT 1,
    name VARCHAR2(30) NOT NULL,
    icon VARCHAR2(50) NOT NULL,
    path VARCHAR2(50),
    status NUMBER(4) DEFAULT 1,
    sort NUMBER(4) DEFAULT 1,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence sys_menu_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN sys_menu.id IS '主键';
COMMENT ON COLUMN sys_menu.company_id IS '公司id';
COMMENT ON COLUMN sys_menu.parent_id IS '上级id';
COMMENT ON COLUMN sys_menu.classify IS '类别(1:PC端;2:移动端;)';
COMMENT ON COLUMN sys_menu.type IS '类型(1:表单列表;2:按钮;3:新增表单;4:系统菜单;5:目录;6:仪表板;7:单页面;)';
COMMENT ON COLUMN sys_menu.name IS '名称';
COMMENT ON COLUMN sys_menu.icon IS '图标';
COMMENT ON COLUMN sys_menu.path IS '路径';
COMMENT ON COLUMN sys_menu.status IS '状态(1:启用;2:禁用;)';
COMMENT ON COLUMN sys_menu.sort IS '排序';
COMMENT ON COLUMN sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_menu.update_time IS '更新时间';
COMMENT ON TABLE sys_menu IS '菜单';

CREATE TABLE sys_message (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    type NUMBER(4) DEFAULT 1,
    from_user_id NUMBER(20) NOT NULL,
    to_user_id NUMBER(20) NOT NULL,
    title VARCHAR2(50) NOT NULL,
    business_type NUMBER(20) NOT NULL,
    business_key NUMBER(20) NOT NULL,
    status NUMBER(4) DEFAULT 0,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence sys_message_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN sys_message.id IS '主键';
COMMENT ON COLUMN sys_message.company_id IS '公司id';
COMMENT ON COLUMN sys_message.type IS '类型(1:暂存数据;)';
COMMENT ON COLUMN sys_message.from_user_id IS '用户id(来自)';
COMMENT ON COLUMN sys_message.to_user_id IS '用户id(给谁)';
COMMENT ON COLUMN sys_message.title IS '标题';
COMMENT ON COLUMN sys_message.business_type IS '业务类型';
COMMENT ON COLUMN sys_message.business_key IS '业务key';
COMMENT ON COLUMN sys_message.status IS '状态(0:未读;1:已读;)';
COMMENT ON COLUMN sys_message.create_time IS '创建时间';
COMMENT ON COLUMN sys_message.update_time IS '更新时间';
COMMENT ON TABLE sys_message IS '消息表';

CREATE TABLE sys_role (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    name VARCHAR2(30) NOT NULL,
    path VARCHAR2(4000) NOT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence sys_role_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN sys_role.id IS '主键';
COMMENT ON COLUMN sys_role.company_id IS '公司id';
COMMENT ON COLUMN sys_role.name IS '名称';
COMMENT ON COLUMN sys_role.path IS '菜单权限(以,隔开)';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
COMMENT ON TABLE sys_role IS '系统角色';

CREATE TABLE table_column_config (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    user_id NUMBER(20) NOT NULL,
    form_id NUMBER(20) NOT NULL,
    options clob NOT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence table_column_config_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN table_column_config.id IS '主键';
COMMENT ON COLUMN table_column_config.user_id IS '用户id';
COMMENT ON COLUMN table_column_config.form_id IS '表单id';
COMMENT ON COLUMN table_column_config.options IS '配置';
COMMENT ON COLUMN table_column_config.create_time IS '创建时间';
COMMENT ON COLUMN table_column_config.update_time IS '更新时间';
COMMENT ON TABLE table_column_config IS '表头配置';

CREATE TABLE user_role (
    id NUMBER(20) PRIMARY KEY NOT NULL,
    company_id NUMBER(20) NOT NULL,
    user_id NUMBER(20) NOT NULL,
    role_id NUMBER(20) NOT NULL,
    create_time date DEFAULT CURRENT_TIMESTAMP,
    update_time date DEFAULT CURRENT_TIMESTAMP
);
CREATE sequence user_role_seq increment by 1 start with 1 nomaxvalue minvalue 1 order nocycle;
COMMENT ON COLUMN user_role.id IS '主键';
COMMENT ON COLUMN user_role.company_id IS '公司id';
COMMENT ON COLUMN user_role.user_id IS '用户id';
COMMENT ON COLUMN user_role.role_id IS '角色id';
COMMENT ON COLUMN user_role.create_time IS '创建时间';
COMMENT ON COLUMN user_role.update_time IS '更新时间';
COMMENT ON TABLE user_role IS '用户角色';

INSERT INTO company VALUES (1, NULL, 'seagox', '1001', '默认单位', '默认单位', NULL, 1, SYSDATE, SYSDATE);
INSERT INTO department VALUES (1, 1, NULL, '101', '默认部门', NULL, NULL, 0, SYSDATE, SYSDATE);
INSERT INTO sys_role VALUES (1, 1, '管理员', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19', SYSDATE, SYSDATE);
INSERT INTO sys_account VALUES (1, NULL, 'admin', NULL, NULL, '管理员', 1, '$2a$10$7xaqWKLFZRc2mg7JIX.B/OCtijP2zYZack60pbC3WxDGvtfvKld3W', NULL, 1, 2, NULL, 0, SYSDATE, SYSDATE);
INSERT INTO user_role VALUES (1, 1, 1, 1, SYSDATE, SYSDATE);
INSERT INTO dept_user VALUES (1, 1, 1, 1, SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (1, 1, NULL, 5, '组织架构', 'iconfont icon-xihuan', 'organization', 1, 1, SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (2, 1, 1, 4, '人员管理', 'iconfont icon-xihuan', 'contact', 1, 1,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (3, 1, 1, 4, '角色管理', 'iconfont icon-xihuan', 'role', 1, 2,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (4, 1, 2, 2, '导出用户', 'iconfont icon-xihuan', 'user:export', 1, 3,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (5, 1, 2, 2, '导出用户模板', 'iconfont icon-xihuan', 'user:download', 1, 1,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (6, 1, 2, 2, '导出部门模板', 'iconfont icon-xihuan', 'dept:download', 1, 1,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (7, 1, 2, 2, '新增用户', 'iconfont icon-xihuan', 'user:add', 1, 4,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (8, 1, 2, 2, '编辑用户', 'iconfont icon-xihuan', 'user:edit', 1, 5,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (9, 1, 2, 2, '删除用户', 'iconfont icon-xihuan', 'user:delete', 1, 6,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (10, 1, 2, 2, '密码重置', 'iconfont icon-xihuan', 'user:reset', 1, 7,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (11, 1, 2, 2, '导入用户', 'iconfont icon-xihuan', 'user:import', 1, 2,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (12, 1, 2, 2, '新增部门', 'iconfont icon-xihuan', 'dept:add', 1, 8,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (13, 1, 2, 2, '编辑部门', 'iconfont icon-xihuan', 'dept:edit', 1, 9,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (14, 1, 2, 2, '删除部门', 'iconfont icon-xihuan', 'dept:delete', 1, 10,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (15, 1, 3, 2, '新增', 'iconfont icon-xihuan', 'role:add', 1, 1,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (16, 1, 3, 2, '编辑', 'iconfont icon-xihuan', 'role:edit', 1, 2,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (17, 1, 3, 2, '删除', 'iconfont icon-xihuan', 'role:delete', 1, 3,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (18, 1, 2, 2, '导入部门', 'iconfont icon-xihuan', 'dept:import', 1, 11,  SYSDATE, SYSDATE);
INSERT INTO sys_menu VALUES (19, 1, 3, 2, '授权', 'iconfont icon-xihuan', 'role:authorize', 1, 4,  SYSDATE, SYSDATE);
CREATE OR REPLACE FUNCTION FIND_IN_SET(piv_str1 varchar2, piv_str2 varchar2, p_sep varchar2 := ',')

RETURN NUMBER IS

  l_idx    number:=0; -- 用于计算piv_str2中分隔符的位置

  str      varchar2(500);  -- 根据分隔符截取的子字符串

  piv_str  varchar2(500) := piv_str2; -- 将piv_str2赋值给piv_str

  res      number:=0; -- 返回结果

  loopIndex number:=0;

BEGIN

-- 如果piv_str中没有分割符，直接判断piv_str1和piv_str是否相等，相等 res=1

IF instr(piv_str, p_sep, 1) = 0 THEN

   IF piv_str = piv_str1 THEN

      res:= 1;

   END IF;

ELSE

-- 循环按分隔符截取piv_str

LOOP

    l_idx := instr(piv_str,p_sep);

     loopIndex:=loopIndex+1;

-- 当piv_str中还有分隔符时

      IF l_idx > 0 THEN



   -- 截取第一个分隔符前的字段str

         str:= substr(piv_str,1,l_idx-1);

   -- 判断 str 和piv_str1 是否相等，相等 res=1 并结束循环判断

         IF str = piv_str1 THEN

           res:= loopIndex;

           EXIT;

         END IF;

        piv_str := substr(piv_str,l_idx+length(p_sep));

      ELSE

   -- 当截取后的piv_str 中不存在分割符时，判断piv_str和piv_str1是否相等，相等 res=1

        IF piv_str = piv_str1 THEN

           res:= loopIndex;

        END IF;

        -- 无论最后是否相等，都跳出循环

        EXIT;

      END IF;

END LOOP;

-- 结束循环

END IF;

-- 返回res

RETURN res;

END FIND_IN_SET;

