package ${REST_PACKAGE_NAME};

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import ${DOMAIN_PACKAGE_NAME}.${ENTITY_NAME};
import ${MAPPER_PACKAGE_NAME}.${ENTITY_NAME}Mapper;



@RestController
@RequestMapping(value = "/${ENTITY_VAR_NAME}")
public class ${ENTITY_NAME}RestController {

	static Logger logger = LoggerFactory.getLogger(${ENTITY_NAME}RestController.class);
	@Autowired
	private ${ENTITY_NAME}Mapper ${ENTITY_VAR_NAME}Mapper;
	
    
    @RequestMapping(method = { RequestMethod.POST })
    @ResponseBody
    public ${ENTITY_NAME} add${ENTITY_NAME}(@RequestBody ${ENTITY_NAME} ${ENTITY_VAR_NAME}<#if entity.usePrincipal >, java.security.Principal principal</#if>){
        if(logger.isDebugEnabled()){
    		 logger.debug("Receive insert request:{}", ${ENTITY_VAR_NAME});
    	}
    	<#if entity.usePrincipal >
    		if(principal==null){
    			<#list entity.fields as field>
				 	<#if field.principal?? >
				 		${ENTITY_VAR_NAME}.set${field.objectname}("${field.principal}");
				 	</#if> 
				</#list>
    		}else{
	    		<#list entity.fields as field>
				 	<#if field.principal?? >
				 		${ENTITY_VAR_NAME}.set${field.objectname}(principal.getName());
				 	</#if> 
				</#list>
    		}
    		
    	</#if>    	
        this.${ENTITY_VAR_NAME}Mapper.insert${ENTITY_NAME}(${ENTITY_VAR_NAME});
        return ${ENTITY_VAR_NAME};
    }
    
    @RequestMapping(value="/new", method = { RequestMethod.GET })
    @ResponseBody
    public ${ENTITY_NAME} new${ENTITY_NAME}(){
    	if(logger.isDebugEnabled()){
    		 logger.debug("Receive new object request");
    	}
    	return new ${ENTITY_NAME}();
    }

    @RequestMapping(method = { RequestMethod.GET })
    @ResponseBody
    public java.util.List<${ENTITY_NAME}> get${ENTITY_NAME}(){
        if(logger.isDebugEnabled()){
    		 logger.debug("Receive get All request.");
    	}
        java.util.List<${ENTITY_NAME}> ret = this.${ENTITY_VAR_NAME}Mapper.select${ENTITY_NAME}();
        return ret;
    }
    
    <#if (entity.keyFields?size == 1) >
    	<#assign keyField = entity.keyFields?first >
    	@RequestMapping(value="/{${keyField.varname}}", method = { RequestMethod.GET })
    	@ResponseBody
    	public ${ENTITY_NAME} get${ENTITY_NAME}By${keyField.objectname}(@PathVariable("${keyField.varname}") ${keyField.javaTypeName} ${keyField.varname})
    	{
    		if(logger.isDebugEnabled()){
    			logger.debug("Select ${entity.entityObjectName} by ${keyField.objectname} [{}]",  ${keyField.varname});
    		}
    		return this.${entity.entityVarName}Mapper.select${entity.entityObjectName}By${keyField.objectname}(${keyField.varname});
    	} 
    	
    	@RequestMapping(method = { RequestMethod.PUT })
    	@ResponseBody
    	public int update${entity.entityObjectName}By${keyField.objectname}(@RequestBody ${entity.entityObjectName} ${entity.entityVarName}<#if entity.usePrincipal >, java.security.Principal principal</#if>)
    	{
    		if(logger.isDebugEnabled()){
    			logger.debug("Update ${entity.entityObjectName} by ${keyField.objectname} [{}]",  ${entity.entityVarName});
    		}
    		<#if entity.usePrincipal >
    		if(principal==null){
    			<#list entity.fields as field>
				 	<#if field.principal?? >
				 ${entity.entityVarName}.set${field.objectname}("${field.principal}");
				 	</#if> 
				</#list>
    		}else{
	    		<#list entity.fields as field>
				 	<#if field.principal?? >
				 ${entity.entityVarName}.set${field.objectname}(principal.getName());
				 	</#if> 
				</#list>
    		}    		
	    	</#if>    	
	        return this.${entity.entityVarName}Mapper.update${entity.entityObjectName}By${keyField.objectname}(${entity.entityVarName});
    		    		
    	}
    	
    	@RequestMapping(value="/{${keyField.varname}}", method = {RequestMethod.DELETE })
    	@ResponseBody
    	public int delete${entity.entityObjectName}By${keyField.objectname}(@PathVariable("${keyField.varname}") ${keyField.javaTypeName} ${keyField.varname}<#if entity.usePrincipal >, java.security.Principal principal</#if>)
    	{
    		if(logger.isDebugEnabled()){
    			logger.debug("Delete ${entity.entityObjectName} by ${keyField.objectname} [{}]",  ${keyField.varname});
    		}
	        return this.${entity.entityVarName}Mapper.delete${entity.entityObjectName}By${keyField.objectname}(${keyField.varname});
    		    		
    	}
    	
    	   
    <#elseif (entity.keyFields?size > 1 ) >    	    
    	@RequestMapping(value="<#list entity.keyFields as keyField>/{${keyField.varname}}</#list>", method = { RequestMethod.GET })
    	@ResponseBody
    	public ${ENTITY_NAME} get${ENTITY_NAME}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>(
    	<#list entity.keyFields as keyField>
    	@PathVariable("${keyField.varname}") ${keyField.javaTypeName} ${keyField.varname}<#sep>,
    	</#list>
    	<#if entity.usePrincipal >, java.security.Principal principal</#if>    	
    	)    	
    	{
    		${entity.entityObjectName} ${entity.entityVarName} = new ${entity.entityObjectName}();
    		<#list entity.keyFields as keyField>
    		${entity.entityVarName}.set${keyField.objectname}(${keyField.varname});
    		</#list>    		
    		return this.${entity.entityVarName}Mapper.select${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>(${entity.entityVarName});
    	}     	
    	@RequestMapping(method = { RequestMethod.PUT })
    	@ResponseBody
    	public int update${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>(@RequestBody ${entity.entityObjectName} ${entity.entityVarName}<#if entity.usePrincipal >, java.security.Principal principal</#if>)
    	{
    		if(logger.isDebugEnabled()){
    			logger.debug("Update ${entity.entityObjectName} by <#list entity.keyFields as keyField>${keyField.objectname}<#sep>,</#list> ",  ${entity.entityVarName});
    		}
    		<#if entity.usePrincipal >
    		if(principal==null){
    			<#list entity.fields as field>
				 	<#if field.principal?? >
				 ${entity.entityVarName}.set${field.objectname}("${field.principal}");
				 	</#if> 
				</#list>
    		}else{
	    		<#list entity.fields as field>
				 	<#if field.principal?? >
				 ${entity.entityVarName}.set${field.objectname}(principal.getName());
				 	</#if> 
				</#list>
    		}    		
	    	</#if>    	
	        return this.${entity.entityVarName}Mapper.update${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>(${entity.entityVarName});
    		    		
    	}
    	
    	@RequestMapping(value="<#list entity.keyFields as keyField>/{${keyField.varname}}</#list>", method = {RequestMethod.DELETE })
    	@ResponseBody
    	public int delete${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>(
    	<#list entity.keyFields as keyField>
    	@PathVariable("${keyField.varname}") ${keyField.javaTypeName} ${keyField.varname}<#sep>,
    	</#list>    	
    	<#if entity.usePrincipal >, java.security.Principal principal</#if>)
    	{
    		if(logger.isDebugEnabled()){
    			logger.debug("Delete ${entity.entityObjectName} by <#list entity.keyFields as keyField>${keyField.objectname} [{}]<#sep>,</#list> ",  <#list entity.keyFields as keyField>${keyField.varname}<#sep>, </#list>);
    		}
    		${entity.entityObjectName} ${entity.entityVarName} = new ${entity.entityObjectName}();
    		<#list entity.keyFields as keyField>
    		${entity.entityVarName}.set${keyField.objectname}(${keyField.varname});
    		</#list>    
	        return this.${entity.entityVarName}Mapper.delete${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>(${entity.entityVarName});    		    		
    	}    	
	</#if>
    
    
    ${MORE_REST_METHODS}

	
	
}
