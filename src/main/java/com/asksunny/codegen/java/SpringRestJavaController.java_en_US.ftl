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
				 		${ENTITY_VAR_NAME}.set${field.objectName}("${field.principal}");
				 	</#if> 
				</#list>
    		}else{
	    		<#list entity.fields as field>
				 	<#if field.principal?? >
				 		${ENTITY_VAR_NAME}.set${field.objectName}(principal.getName());
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
    	@RequestMapping(value="/{${keyField.varName}}", method = { RequestMethod.GET })
    	@ResponseBody
    	public ${ENTITY_NAME} get${ENTITY_NAME}By${keyField.objectName}(@PathVariable("${keyField.varName}") ${keyField.javaTypeName} ${keyField.varName})
    	{
    		if(logger.isDebugEnabled()){
    			logger.debug("Select ${entity.objectName} by ${keyField.objectName} [{}]",  ${keyField.varName});
    		}
    		return this.${entity.varName}Mapper.select${entity.objectName}By${keyField.objectName}(${keyField.varName});
    	} 
    	
    	@RequestMapping(method = { RequestMethod.PUT })
    	@ResponseBody
    	public int update${entity.objectName}By${keyField.objectName}(@RequestBody ${entity.objectName} ${entity.varName}<#if entity.usePrincipal >, java.security.Principal principal</#if>)
    	{
    		if(logger.isDebugEnabled()){
    			logger.debug("Update ${entity.objectName} by ${keyField.objectName} [{}]",  ${entity.varName});
    		}
    		<#if entity.usePrincipal >
    		if(principal==null){
    			<#list entity.fields as field>
				 	<#if field.principal?? >
				 ${entity.varName}.set${field.objectName}("${field.principal}");
				 	</#if> 
				</#list>
    		}else{
	    		<#list entity.fields as field>
				 	<#if field.principal?? >
				 ${entity.varName}.set${field.objectName}(principal.getName());
				 	</#if> 
				</#list>
    		}    		
	    	</#if>    	
	        return this.${entity.varName}Mapper.update${entity.objectName}By${keyField.objectName}(${entity.varName});
    		    		
    	}
    	
    	@RequestMapping(value="/{${keyField.varName}}", method = {RequestMethod.DELETE })
    	@ResponseBody
    	public int delete${entity.objectName}By${keyField.objectName}(@PathVariable("${keyField.varName}") ${keyField.javaTypeName} ${keyField.varName}<#if entity.usePrincipal >, java.security.Principal principal</#if>)
    	{
    		if(logger.isDebugEnabled()){
    			logger.debug("Delete ${entity.objectName} by ${keyField.objectName} [{}]",  ${keyField.varName});
    		}
	        return this.${entity.varName}Mapper.delete${entity.objectName}By${keyField.objectName}(${keyField.varName});
    		    		
    	}
    	
    	   
    <#elseif (entity.keyFields?size > 1 ) >    	    
    	@RequestMapping(value="<#list entity.keyFields as keyField>/{${keyField.varName}}</#list>", method = { RequestMethod.GET })
    	@ResponseBody
    	public ${ENTITY_NAME} get${ENTITY_NAME}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>(
    	<#list entity.keyFields as keyField>
    	@PathVariable("${keyField.varName}") ${keyField.javaTypeName} ${keyField.varName}<#sep>,
    	</#list>
    	<#if entity.usePrincipal >, java.security.Principal principal</#if>    	
    	)    	
    	{
    		${entity.objectName} ${entity.varName} = new ${entity.objectName}();
    		<#list entity.keyFields as keyField>
    		${entity.varName}.set${keyField.objectName}(${keyField.varName});
    		</#list>    		
    		return this.${entity.varName}Mapper.select${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>(${entity.varName});
    	}     	
    	@RequestMapping(method = { RequestMethod.PUT })
    	@ResponseBody
    	public int update${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>(@RequestBody ${entity.objectName} ${entity.varName}<#if entity.usePrincipal >, java.security.Principal principal</#if>)
    	{
    		if(logger.isDebugEnabled()){
    			logger.debug("Update ${entity.objectName} by <#list entity.keyFields as keyField>${keyField.objectName}<#sep>,</#list> ",  ${entity.varName});
    		}
    		<#if entity.usePrincipal >
    		if(principal==null){
    			<#list entity.fields as field>
				 	<#if field.principal?? >
				 ${entity.varName}.set${field.objectName}("${field.principal}");
				 	</#if> 
				</#list>
    		}else{
	    		<#list entity.fields as field>
				 	<#if field.principal?? >
				 ${entity.varName}.set${field.objectName}(principal.getName());
				 	</#if> 
				</#list>
    		}    		
	    	</#if>    	
	        return this.${entity.varName}Mapper.update${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>(${entity.varName});
    		    		
    	}
    	
    	@RequestMapping(value="<#list entity.keyFields as keyField>/{${keyField.varName}}</#list>", method = {RequestMethod.DELETE })
    	@ResponseBody
    	public int delete${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>(
    	<#list entity.keyFields as keyField>
    	@PathVariable("${keyField.varName}") ${keyField.javaTypeName} ${keyField.varName}<#sep>,
    	</#list>    	
    	<#if entity.usePrincipal >, java.security.Principal principal</#if>)
    	{
    		if(logger.isDebugEnabled()){
    			logger.debug("Delete ${entity.objectName} by <#list entity.keyFields as keyField>${keyField.objectName} [{}]<#sep>,</#list> ",  <#list entity.keyFields as keyField>${keyField.varName}<#sep>, </#list>);
    		}
    		${entity.objectName} ${entity.varName} = new ${entity.objectName}();
    		<#list entity.keyFields as keyField>
    		${entity.varName}.set${keyField.objectName}(${keyField.varName});
    		</#list>    
	        return this.${entity.varName}Mapper.delete${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>(${entity.varName});    		    		
    	}    	
	</#if>
    
    
    ${MORE_REST_METHODS}

	
	
}
