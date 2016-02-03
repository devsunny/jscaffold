package ${DOMAIN_PACKAGE_NAME};

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
<#if entity.hasDatetimeField>
${JSON_FORMAT_ANNO_IMPORT}
</#if>

public class ${entity.objectName} implements Serializable {

    private static final long serialVersionUID = 1L;
<#list entity.fields as field>
	<#if field.ignoreView>@JsonIgnore</#if>
	<#if field.datetime >
	@JsonFormat(pattern="${field.format}")
		<#if field.readonly >
			<#if field.jdbcType==types.jdbcDateType>
	private ${field.javaTypeName} ${field.varName} = new java.sql.Date(System.currentTimeMillis());			
			<#elseif field.jdbcType==types.jdbcTimeType>
	private ${field.javaTypeName} ${field.varName} = new java.sql.Time(System.currentTimeMillis());		  
			<#else>
	private ${field.javaTypeName} ${field.varName} = new java.sql.Timestamp(System.currentTimeMillis());			 
			</#if>
		<#else>
	private ${field.javaTypeName} ${field.varName};
		</#if>			
	<#else>
	private ${field.javaTypeName} ${field.varName};
	</#if>		
</#list>

<#list entity.fields as field>
	<#if field.ignoreView>@JsonIgnore</#if>
	public ${field.javaTypeName} get${field.objectName}()
	{
		return this.${field.varName};
	}
	
	public void set${field.objectName}(${field.javaTypeName} ${field.varName})
	{
		this.${field.varName} = ${field.varName};
	}
</#list>
	
	
}