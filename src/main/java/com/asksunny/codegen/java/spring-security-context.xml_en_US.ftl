<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:security="http://www.springframework.org/schema/security"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security.xsd">

	<!-- Disable security on static resources -->
	<security:http pattern="/static/**" security="none" />

	<!-- Enable Spring Security -->
	<security:http create-session="stateless"
		use-expressions="true">
		<security:intercept-url pattern="/**"
			access="hasRole('ROLE_USER')" />
		<security:http-basic />
	</security:http>

	<!-- Configures in-memory implementation of the UserDetailsService implementation -->
	<security:authentication-manager alias="authenticationManager">
		<security:authentication-provider>
			<security:user-service>
				<security:user name="user" password="password"
					authorities="ROLE_USER" />
			</security:user-service>
		</security:authentication-provider>
	</security:authentication-manager>
	
	

</beans>