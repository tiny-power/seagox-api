package com.seagox.lowcode;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableCaching
@EnableAsync
@MapperScan(basePackages = { "com.seagox.lowcode" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public DatabaseIdProvider databaseIdProvider() {
		DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
		Properties p = new Properties();
		p.setProperty("Oracle", "oracle");
		p.setProperty("MySQL", "mysql");
		p.setProperty("PostgreSQL", "postgresql");
		p.setProperty("KingbaseES", "kingbase");
		p.setProperty("DM DBMS", "dm");
		p.setProperty("SQL Server", "sqlserver");
		databaseIdProvider.setProperties(p);
		return databaseIdProvider;
	}

}
