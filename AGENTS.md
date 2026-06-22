# seagox-api Instructions

这是 Spring Boot 后端项目，目录位于 `/Users/xie/Documents/project/seagox-api`。

后端约定：

- 新增接口遵循现有 `controller` / `service` / `mapper` / `entity` 分层。
- 简单 CRUD 优先使用 MyBatis-Plus `BaseMapper` 和 `LambdaQueryWrapper`。
- 分页查询优先沿用 `PageHelper.startPage(...)` + `PageInfo` 的现有模式。
- 接口返回统一使用 `ResultData`。
- 业务校验优先放在 service 层，错误提示使用项目已有中文风格。
- 涉及操作日志时使用 `@LogPoint`。
- MyBatis XML 不要使用 `<sql>` 标签定义公共 SQL 片段，也不要使用对应的 `<include>` 标签，SQL 需直接写在具体语句中。
- 新增表、字段、菜单或初始数据时，需要同步维护该目录下所有数据库脚本，保持多数据库初始化一致。
- 提交或交付前必须格式化本次修改的代码：Java 代码按现有风格整理 import、缩进、换行和大括号，SQL 新增语句保持字段分行、结构清晰。
- Controller、DTO、Entity、Mapper、Service 及 Mapper XML 的新增类、字段、接口方法和关键 SQL 必须添加清晰的中文注释。

导入导出约定：

- 导入导出实现优先参考用户模块：`UserController#importHandle`、`UserService#importHandle`、`UserService#export`、`AccountModel`、`AccountExcelVerifyHandler`。
- Excel 模板统一放在 `src/main/resources/template/`，导入模板命名建议为 `xxxModel.xlsx`，导出模板命名建议为 `xxxExport.xlsx`。
- 导入使用 EasyPOI：新增导入模型放在对应业务 `template` 包下并继承 `ImportModel`，字段使用 `@Excel`、JSR-303 注解描述列名与基础校验。
- 复杂导入校验使用 `IExcelVerifyHandler`，校验器放在对应业务 `verify` 包下，负责存在性、归属关系、枚举值、时间格式、重复数据等校验。
- 导入接口使用 `ExcelImportUtil.importExcelMore(...)`，开启 `setNeedVerify(true)` 并设置校验器；失败时按现有格式返回 `第X行的错误是：...`。
- 导入落库逻辑放在 service 层，保持事务边界；状态类业务默认值要与页面新增保持一致。
- 导出使用 `ExportUtils.exportExcelTemplate(...)`，模板使用 JXLS 注释标记 `jx:area` / `jx:each`，参数默认放入 `list`。
- 导出查询 SQL 放在对应 Mapper XML 中，筛选参数名与接口和前端保持一致；日期、模糊查询等多数据库差异需要按现有 `_databaseId` 写法补齐。
- 新增导入、导出、下载模板按钮权限时，需要同步维护 `src/main/resources/sql/` 下所有数据库脚本中的 `sys_menu` 初始数据和默认角色权限。
- 完成后优先运行 `mvn -q -DskipTests compile`，涉及模板时至少校验 `.xlsx` 压缩包完整性和关键 XML/JXLS 标记。

验证：

```bash
mvn -q -DskipTests compile
```
