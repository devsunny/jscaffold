<!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${config.mapperPackageName}.${entity.objectName}Mapper">


<resultMap id="${entity.varName}ResultMap" type="${config.domainPackageName}.${entity.objectName}">
 <#list entity.keyFields as field>
 	<id property="${field.varName}" column="${field.name}" />
 </#list>
 <#list entity.nonKeyFields as field>		 	
	<result property="${field.varName}" column="${field.name}" />
 </#list>
</resultMap>


<select id="select${entity.objectName}" resultMap="${entity.objectName}ResultMap">
  SELECT 
 <#list entity.fields as field>${field.name}<#sep>, </#list>
  FROM 
  ${entity.name}
  <#if entity.orderBy??>
  ORDER BY ${entity.orderBy}
  </#if>
</select>

<#if entity.hasKeyField >
<select id="select${entity.objectName}By${entity.keyFieldNames}" resultMap="${entity.objectName}ResultMap" parameterType="<#if (entity.keyFields?size>1) >${entity.objectName}<#else>${entity.keyFields?first.javaTypeName}</#if>">
  SELECT 
 <#list entity.fields as field>${field.name}<#sep>, </#list>
  FROM 
  ${entity.name}
  WHERE
  <#list entity.keyFields as field>
 	${field.name}=${utils.myBatisParamStartTag}${field.varName},jdbcType=${field.jdbcTypeName},javaType=${field.javaTypeName}${utils.myBatisParamEndTag}
 </#list>
  <#if entity.orderBy??>
  ORDER BY ${entity.orderBy}
  </#if>
</select>	
</#if>
	
<#if entity.hasUniqueField >
<select id="select${entity.objectName}By${entity.uniqueFieldNames}" resultMap="${entity.objectName}ResultMap" parameterType="<#if (entity.uniqueFields?size>1) >${entity.objectName}<#else>${entity.uniqueFields?first.javaTypeName}</#if>">
  SELECT 
 <#list entity.fields as field>${field.name}<#sep>, </#list>
  FROM 
  ${entity.name}
  WHERE
  <#list entity.uniqueFields as field>
 	${field.name}=${utils.myBatisParamStartTag}${field.varName},jdbcType=${field.jdbcTypeName},javaType=${field.javaTypeName}${utils.myBatisParamEndTag}<#sep> AND 
 </#list>
  <#if entity.orderBy??>
  ORDER BY ${entity.orderBy}
  </#if>
</select>	
</#if>

<#if entity.hasGroupByField >
<select id="select${entity.objectName}GroupBy${entity.groupByFieldNames}" resultMap="${entity.objectName}ResultMap">
  SELECT 
  <#list entity.groupByFields as field>${field.name}<#sep>,</#list>
  <#if entity.groupFunctionField??>
  , ${entity.groupFunctionField.groupFunction}(${entity.groupFunctionField.name}) as ${entity.groupFunctionField.name}
  </#if>
  FROM 
  ${entity.name}
  GROUP BY
  <#list entity.groupByFields as field>
 	${field.name}<#sep>,
  </#list>
  
  ORDER BY
   <#list entity.groupByFields as field>
 	${field.name}<#sep>,
  </#list>
  
</select>	
</#if>



<#if !entity.readonly>
<insert  id="insert${entity.objectName}"  parameterType="${entity.objectName}"  flushCache="true">
	INSERT INTO ${entity.name} 
	( <#list entity.fields as field>${field.name}<#sep>, </#list>) 
	VALUES 
	(<#list entity.fields as field>${utils.myBatisParamStartTag}${field.varName},jdbcType=${field.jdbcTypeName},javaType=${field.javaTypeName}${utils.myBatisParamEndTag}<#sep>,
	</#list>)
</insert>

<#if entity.hasKeyField >
<update  id="update${entity.objectName}By${entity.keyFieldNames}" parameterType="${entity.objectName}">
	UPDATE ${entity.name}
	SET
<#list entity.nonKeyFields as field>
 	${field.name}=${utils.myBatisParamStartTag}${field.varName},jdbcType=${field.jdbcTypeName},javaType=${field.javaTypeName}${utils.myBatisParamEndTag}<#sep>,  
 </#list>
 
	WHERE
 <#list entity.keyFields as field>
 	${field.name}=${utils.myBatisParamStartTag}${field.varName},jdbcType=${field.jdbcTypeName},javaType=${field.javaTypeName}${utils.myBatisParamEndTag}<#sep> AND 
 </#list>
 
</update>
<delete  id="delete${entity.objectName}By${entity.keyFieldNames}" parameterType="<#if (entity.keyFields?size>1) >${entity.objectName}<#else>${entity.keyFields?first.javaTypeName}</#if>">
	DELETE FROM ${entity.name}
	WHERE
<#list entity.keyFields as field>
 	${field.name}=${utils.myBatisParamStartTag}${field.varName},jdbcType=${field.jdbcTypeName},javaType=${field.javaTypeName}${utils.myBatisParamEndTag}<#sep> AND 
 </#list>
 
</delete>
</#if>

<#if entity.hasUniqueField >

<update  id="update${entity.objectName}By${entity.uniqueFieldNames}" parameterType="${entity.objectName}">
	UPDATE ${entity.name}
	SET
<#list entity.nonKeyFields as field>
 	${field.name}=${utils.myBatisParamStartTag}${field.varName},jdbcType=${field.jdbcTypeName},javaType=${field.javaTypeName}${utils.myBatisParamEndTag}<#sep>,  
 </#list>
 
	WHERE
 <#list entity.uniqueFields as field>
 	${field.name}=${utils.myBatisParamStartTag}${field.varName},jdbcType=${field.jdbcTypeName},javaType=${field.javaTypeName}${utils.myBatisParamEndTag}<#sep> AND 
 </#list>
  
</update>


<delete  id="delete${entity.objectName}By${entity.uniqueFieldNames}" parameterType="<#if (entity.uniqueFields?size>1) >${entity.objectName}<#else>${entity.uniqueFields?first.javaTypeName}</#if>">
	DELETE FROM ${entity.name}
	WHERE
<#list entity.uniqueFields as field>
 	${field.name}=${utils.myBatisParamStartTag}${field.varName},jdbcType=${field.jdbcTypeName},javaType=${field.javaTypeName}${utils.myBatisParamEndTag}<#sep> AND 
 </#list>
 
</delete>
</#if>


</#if>


</mapper>
