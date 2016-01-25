package ${MAPPER_PACKAGE_NAME};

import ${DOMAIN_PACKAGE}.*;

public interface ${entity.entityObjectName}Mapper
{
	
    ${entity.entityObjectName} insert${ENTITY_NAME}(${entity.entityObjectName} ${entity.entityVarName});    
    java.util.List<${entity.entityObjectName}> select${entity.entityObjectName}();    
    <#if (entity.keyFields?size == 1) >
    <#assign keyField = entity.keyFields?first >    	
    ${ENTITY_NAME} select${ENTITY_NAME}By${keyField.objectname}(${keyField.javaTypeName} ${keyField.varname}) ;   	
    int update${entity.entityObjectName}By${keyField.objectname}(${entity.entityObjectName} ${entity.entityVarName});    	
    int delete${entity.entityObjectName}By${keyField.objectname}(${keyField.javaTypeName} ${keyField.varname});   
    <#elseif (entity.keyFields?size > 1 ) >  
    ${ENTITY_NAME} select${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>(${entity.entityObjectName} ${entity.entityVarName})  ;
    int update${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>(${entity.entityObjectName} ${entity.entityVarName});
    int delete${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>(${entity.entityObjectName} ${entity.entityVarName});    	
	</#if>	
	${MAPPER_METHODS}
}
