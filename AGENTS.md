# seagox-api Instructions

这是 Spring Boot 后端项目，目录位于 `/Users/xie/Documents/project/seagox-api`。

后端约定：

- 新增接口遵循现有 `controller` / `service` / `mapper` / `entity` 分层。
- 简单 CRUD 优先使用 MyBatis-Plus `BaseMapper` 和 `LambdaQueryWrapper`。
- 分页查询优先沿用 `PageHelper.startPage(...)` + `PageInfo` 的现有模式。
- 接口返回统一使用 `ResultData`。
- 业务校验优先放在 service 层，错误提示使用项目已有中文风格。
- 涉及操作日志时使用 `@LogPoint`。
- 新增数据库表、字段、菜单或初始数据时，需要同步更新 `src/main/resources/sql/` 下所有数据库脚本，不能只改 `mysql.sql`。

验证：

```bash
mvn -q -DskipTests compile
```
