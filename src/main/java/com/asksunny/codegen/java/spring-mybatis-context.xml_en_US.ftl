<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx" xmlns:jdbc="http://www.springframework.org/schema/jdbc"
	xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:task="http://www.springframework.org/schema/task"
	xmlns:cache="http://www.springframework.org/schema/cache" xmlns:util="http://www.springframework.org/schema/util"
	xmlns:mybatis="http://mybatis.org/schema/mybatis-spring"
	xsi:schemaLocation="http://www.springframework.org/schema/beans    
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd
    http://www.springframework.org/schema/tx
    http://www.springframework.org/schema/tx/spring-tx.xsd
    http://www.springframework.org/schema/jdbc
    http://www.springframework.org/schema/jdbc/spring-jdbc.xsd   
    http://www.springframework.org/schema/mvc
    http://www.springframework.org/schema/mvc/spring-mvc.xsd   
    http://www.springframework.org/schema/task
    http://www.springframework.org/schema/task/spring-task.xsd  
    http://www.springframework.org/schema/cache
    http://www.springframework.org/schema/cache/spring-cache.xsd   
    http://www.springframework.org/schema/util
    http://www.springframework.org/schema/util/spring-util.xsd   
    http://mybatis.org/schema/mybatis-spring 
    http://mybatis.org/schema/mybatis-spring.xsd
    ">
	<!-- uncomment the following line to externalize the properties -->
	<!-- <context:property-placeholder location="classpath:web-app.properties" /> -->
	
	<!-- uncomment the following dataSource to use other database for production -->
	<jdbc:embedded-database id="dataSource" type="H2">
		<#list config.schemaFileList as scfile>
			<jdbc:script location="classpath:${scfile}" />	
		</#list>		
	</jdbc:embedded-database>

	<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource" />
	</bean>

	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="transactionFactory">
			<bean
				class="org.apache.ibatis.transaction.managed.ManagedTransactionFactory" />
		</property>
		<property name="mapperLocations" value="classpath*:${config.mapperPackagePath}/*.xml" />
		<property name="typeAliasesPackage" value="${config.domainPackagePath}" />
	</bean>	
	
	${MYBATIS_MAPPERS}
	
	<task:annotation-driven executor="poolExecutor"
		scheduler="poolScheduler" />
	<task:executor id="poolExecutor" pool-size="5" />
	<task:scheduler id="poolScheduler" pool-size="10" />	
	
	<context:component-scan
		base-package="${config.restPackageName}"  />
</beans>
