<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx" xmlns:jdbc="http://www.springframework.org/schema/jdbc"
	xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:task="http://www.springframework.org/schema/task"
	xmlns:cache="http://www.springframework.org/schema/cache" xmlns:util="http://www.springframework.org/schema/util"
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
    ">
	<!-- uncomment the following line to externalize the properties -->
	<!-- <context:property-placeholder location="classpath:app-booststrap.properties" /> -->

	
	<bean id="devServletHolder" class="org.eclipse.jetty.servlet.ServletHolder">
		<constructor-arg>
			<bean id="devServlet" class="${config.basePackageName}.NoCacheStaticResourceServlet">
				<property name="staticResourceCPBase" value="/${WEBAPP_CONTEXT}/app"></property>
				<property name="requestBasePath" value="/${WEBAPP_CONTEXT}/app"></property>
			</bean>
		</constructor-arg>
	</bean>

	<!-- , classpath:web-view-context.xml -->


	<bean id="iconServletHolder" class="org.eclipse.jetty.servlet.ServletHolder">
		<constructor-arg>
			<bean id="iconServlet" class="${config.basePackageName}.FavorIconStaticResourceServlet">
				<property name="favicon" value="/META-INF/app/favicon.ico"></property>
			</bean>
		</constructor-arg>
	</bean>
	<bean
		class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
		<property name="targetObject">
			<ref bean="servletContextHandler" />
		</property>
		<property name="targetMethod">
			<value>addServlet</value>
		</property>
		<property name="arguments">
			<list>
				<ref bean="iconServletHolder"></ref>
				<value>/favicon.ico</value>
			</list>
		</property>
	</bean>
	
	


	<bean id="springDispatcher" class="org.springframework.web.servlet.DispatcherServlet">
		<property name="contextConfigLocation"
			value="<#if config.enableSpringSecurity >classpath:${WEBAPP_CONTEXT}-spring-security-context.xml,</#if>classpath:${WEBAPP_CONTEXT}-spring-mybatis-context.xml, classpath:${WEBAPP_CONTEXT}-spring-ui-context.xml"></property>
	</bean>
	<!-- , classpath:web-view-context.xml -->
	<bean id="springDispatcherHolder" class="org.eclipse.jetty.servlet.ServletHolder">
		<constructor-arg>
			<ref bean="springDispatcher"></ref>
		</constructor-arg>
	</bean>
	<bean
		class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
		<property name="targetObject">
			<ref bean="servletContextHandler" />
		</property>
		<property name="targetMethod">
			<value>addServlet</value>
		</property>
		<property name="arguments">
			<list>
				<ref bean="springDispatcherHolder"></ref>
				<value>/${WEBAPP_CONTEXT}/*</value>
			</list>
		</property>
	</bean>

	<bean id="servletContextHandler" class="org.eclipse.jetty.servlet.ServletContextHandler">
	</bean>

	<#if config.enableSpringSecurity >
	<bean id="allDispatchTypes" class="${config.basePackageName}.DispatchTypeEnumSetFactoryBean">
	</bean>
	
	<bean id="springFilterProxy" class="${config.basePackageName}.EmbeddedDelegatingFilterProxy">
		<constructor-arg value="springSecurityFilterChain"></constructor-arg>
		<constructor-arg ref="springDispatcher"></constructor-arg>
	</bean>
	<bean id="filterHolder" class="org.eclipse.jetty.servlet.FilterHolder">
			<constructor-arg ref="springFilterProxy"></constructor-arg>
	</bean>
	
	
	<bean
		class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
		<property name="targetObject">
			<ref bean="servletContextHandler" />
		</property>
		<property name="targetMethod">
			<value>addFilter</value>
		</property>
		<property name="arguments">
			<list>
				<ref bean="filterHolder"></ref>
				<value>/${WEBAPP_CONTEXT}/*</value>
				<ref bean="allDispatchTypes"/>
			</list>
		</property>
	</bean>
	</#if>	

	 <#if config.enableSSL >
	 <bean id="sslContextFactory" class="org.eclipse.jetty.util.ssl.SslContextFactory"> 
	 	<property name="KeyStorePath" value="${config.keyStoreDirectory}/${config.keystoreName}"></property> 
	 	<property name="KeyStorePassword" value="${config.keypass}"></property>
	 	<property name="KeyManagerPassword" value="${config.keypass}"></property>
		<property name="TrustStorePath" value="${config.keyStoreDirectory}/${config.keystoreName}"></property>
	 	<property name="TrustStorePassword" value="${config.keypass}"></property>
	 </bean>
	 </#if>
	 
	<bean id="server" name="Main" class="org.eclipse.jetty.server.Server"
		init-method="start" destroy-method="stop">
		<constructor-arg>
			<bean id="threadPool" class="org.eclipse.jetty.util.thread.QueuedThreadPool">
				<property name="minThreads" value="10" />
				<property name="maxThreads" value="50" />
			</bean>
		</constructor-arg>
		<property name="connectors">
			<list>
				<bean id="connector" class="org.eclipse.jetty.server.ServerConnector">
					<constructor-arg ref="server" />
					 <#if config.enableSSL > <constructor-arg ref="sslContextFactory" /> </#if>
					<property name="port" value="${config.webserverPort}" />
				</bean>
			</list>
		</property>
		<property name="handler">
			<bean id="handlers" class="org.eclipse.jetty.server.handler.HandlerCollection">
				<property name="handlers">
					<list>
						<ref bean="servletContextHandler" />
						<bean id="defaultHandler" class="org.eclipse.jetty.server.handler.DefaultHandler" />
					</list>
				</property>
			</bean>
		</property>
	</bean>
	
</beans>
