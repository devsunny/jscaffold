package ${MAPPER_PACKAGE_NAME};

import ${DOMAIN_PACKAGE}.*;

public interface ${entity.objectName}Mapper
{
	
    ${entity.objectName} insert${ENTITY_NAME}(${entity.objectName} ${entity.varName});    
    java.util.List<${entity.objectName}> select${entity.objectName}();    
    <#if (entity.keyFields?size == 1) >
    <#assign keyField = entity.keyFields?first >    	
    ${ENTITY_NAME} select${ENTITY_NAME}By${keyField.objectName}(${keyField.javaTypeName} ${keyField.varName}) ;   	
    int update${entity.objectName}By${keyField.objectName}(${entity.objectName} ${entity.varName});    	
    int delete${entity.objectName}By${keyField.objectName}(${keyField.javaTypeName} ${keyField.varName});   
    <#elseif (entity.keyFields?size > 1 ) >  
    ${ENTITY_NAME} select${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>(${entity.objectName} ${entity.varName})  ;
    int update${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>(${entity.objectName} ${entity.varName});
    int delete${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>(${entity.objectName} ${entity.varName});    	
	</#if>	
	${MAPPER_METHODS}
}
