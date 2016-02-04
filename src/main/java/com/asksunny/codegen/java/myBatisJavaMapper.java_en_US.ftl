package ${config.mapperPackageName};

import ${config.domainPackageName}.*;

public interface ${entity.objectName}Mapper
{
	java.util.List<${entity.objectName}>  select${entity.objectName}();	
<#if entity.hasKeyField >
	${entity.objectName}  select${entity.objectName}By${entity.keyFieldNames}(<#if (entity.keyFields?size>1) >${entity.objectName} ${entity.varName}<#else>${entity.keyFields?first.javaTypeName} ${entity.keyFields?first.varName}</#if>);
</#if>
<#if entity.hasUniqueField >
	${entity.objectName}  select${entity.objectName}By${entity.uniqueFieldNames}(<#if (entity.uniqueFields?size>1) >${entity.objectName} ${entity.varName}<#else>${entity.uniqueFields?first.javaTypeName} ${entity.uniqueFields?first.varName}</#if>);
</#if>	
<#if entity.hasGroupByField >
	java.util.List<${entity.objectName}>  select${entity.objectName}GroupBy${entity.groupByFieldNames}();
</#if>	
<#if !entity.readonly>
	int insert${entity.objectName}(${entity.objectName} ${entity.varName});
<#if entity.hasKeyField >
	int update${entity.objectName}By${entity.keyFieldNames}(${entity.objectName} ${entity.varName});
	int delete${entity.objectName}By${entity.keyFieldNames}(<#if (entity.keyFields?size>1) >${entity.objectName} ${entity.varName}<#else>${entity.keyFields?first.javaTypeName} ${entity.keyFields?first.varName}</#if>);
</#if>
<#if entity.hasUniqueField >
	int update${entity.objectName}By${entity.uniqueFieldNames}(${entity.objectName} ${entity.varName});
	int delete${entity.objectName}By${entity.uniqueFieldNames}(<#if (entity.uniqueFields?size>1) >${entity.objectName} ${entity.varName}<#else>${entity.uniqueFields?first.javaTypeName} ${entity.uniqueFields?first.varName}</#if>);
</#if>
</#if>    
}
