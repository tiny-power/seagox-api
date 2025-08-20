###1、postgresql中as区分大小写，要加双引号
###2、postgresql、达梦、人大金仓中mybatis参数类型要与数据库类型一致
###3、达梦数据库设置忽略关键字查询：修改安装路径下的 /data/实例名/dm.ini文件，修改属性EXCLUDE_RESERVED_WORDS = type,comment,path。重启数据库服务即可。
###4、时间格式化

```xml
<if test="_databaseId == 'mysql'">
	DATE_FORMAT(create_time, '%Y-%m-%d %H:%i:%s') AS "createTime" 
</if>
<if test="_databaseId == 'postgresql'">
	to_char(create_time,'yyyy-MM-dd HH24:MI:ss') AS "createTime"
</if>
<if test="_databaseId == 'kingbase'">
	to_char(create_time,'yyyy-MM-dd HH24:MI:ss') AS "createTime"
</if>
<if test="_databaseId == 'dm'">
	to_char(create_time,'yyyy-MM-dd HH24:MI:ss') AS "createTime"
</if>
<if test="_databaseId == 'oracle'">
	to_char(create_time,'yyyy-MM-dd hh24:mi:ss') AS "createTime"
</if>
<if test="_databaseId == 'sqlserver'">
	CONVERT(varchar, create_time, 120 ) AS "createTime"
</if>
```

###5、like语句
```xml
<if test="_databaseId == 'mysql'">
    code LIKE CONCAT('%', #{prefix}, '%')
</if>
<if test="_databaseId == 'postgresql'">
	code LIKE '%' || #{prefix} || '%'
</if>
<if test="_databaseId == 'kingbase'">
    code LIKE '%' || #{prefix} || '%'
</if>
<if test="_databaseId == 'dm'">
    code LIKE '%' || #{prefix} || '%'
</if>
<if test="_databaseId == 'oracle'">
    code LIKE '%' || #{prefix} || '%'
</if>
<if test="_databaseId == 'sqlserver'">
    code LIKE '%' + #{prefix} + '%'
</if>
```
###6、系统时间

```xml
<if test="_databaseId == 'mysql'">
	end_time = NOW()
</if>
<if test="_databaseId == 'postgresql'">
	end_time = NOW()
</if>
<if test="_databaseId == 'kingbase'">
    end_time = NOW()
</if>
<if test="_databaseId == 'dm'">
    end_time = NOW()
</if>
<if test="_databaseId == 'oralce'">
    end_time = NOW()
</if>
<if test="_databaseId == 'sqlserver'">
    end_time = GETDATE()
</if>
 ```
 
###7、FIND_IN_SET

```xml
<if test="_databaseId == 'mysql'">
    FIND_IN_SET(str,strlist)
</if>
<if test="_databaseId == 'postgresql'">
    cast(str as VARCHAR) = ANY (STRING_TO_ARRAY(cast(strlist as VARCHAR),','))
</if>
<if test="_databaseId == 'kingbase'">
    cast(str as VARCHAR) = ANY (STRING_TO_ARRAY(cast(strlist as VARCHAR),','))
</if>
<if test="_databaseId == 'dm'">
    position(str in strlist)
</if>
<if test="_databaseId == 'oracle'">
    FIND_IN_SET(str, strlist)
</if>
<if test="_databaseId == 'sqlserver'">
	CHARINDEX(str, strlist) > 0
</if>
 ```