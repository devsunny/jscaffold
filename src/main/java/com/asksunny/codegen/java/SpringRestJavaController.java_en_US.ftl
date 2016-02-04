package ${config.restPackageName};

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


import ${config.domainPackageName}.${entity.objectName};
import ${config.mapperPackageName}.${entity.objectName}Mapper;



@RestController
@RequestMapping(value = "/${entity.varName}")
public class ${entity.objectName}RestController {

	static Logger logger = LoggerFactory.getLogger(${entity.objectName}RestController.class);
	@Autowired
	private ${entity.objectName}Mapper ${entity.varName}Mapper;
	
	
	
	@RequestMapping(value="/new", method = { RequestMethod.GET })
    @ResponseBody
    public ${entity.objectName} new${entity.objectName}(<#if entity.usePrincipal >java.security.Principal principal</#if>){
    	if(logger.isDebugEnabled()){
    		 logger.debug("Receive new object request");
    	}
    	return new ${entity.objectName}();
    }

    @RequestMapping(method = { RequestMethod.GET })
    @ResponseBody
    public java.util.List<${entity.objectName}> get${entity.objectName}(<#if entity.usePrincipal >java.security.Principal principal</#if>){
        if(logger.isDebugEnabled()){
    		 logger.debug("Receive get All request.");
    	}
        java.util.List<${entity.objectName}> ret = this.${entity.varName}Mapper.select${entity.objectName}();
        return ret;
    }
	
<#if entity.hasKeyField >
    @RequestMapping(value="<#list entity.keyFields as field>/{${field.varName}}</#list>", method = { RequestMethod.GET })
    @ResponseBody
	public ${entity.objectName}  select${entity.objectName}By${entity.keyFieldNames}(<#list entity.keyFields as field>
				@PathVariable("${field.varName}") ${field.javaTypeName} ${field.varName}<#sep>, 
			</#list><#if entity.usePrincipal >, java.security.Principal principal</#if>)
	{
		if(logger.isDebugEnabled()){
    			logger.debug("select ${entity.objectName} by <#list entity.keyFields as field>${field.varName}=[{}]<#sep>, </#list>",  <#list entity.keyFields as field>${field.varName}<#sep>,</#list>);
    			}
    	<#if (entity.keyFields?size>1) >
    		${entity.objectName} ${entity.varName} = new ${entity.objectName}();
    		<#list entity.keyFields as field>
    			${entity.varName}.set${field.objectName}(${field.varName});
    		</#list>
    		return this.${entity.varName}Mapper.select${entity.objectName}By${entity.keyFieldNames}(${entity.varName});
    	<#else>
    		return this.${entity.varName}Mapper.select${entity.objectName}By${entity.keyFieldNames}(${entity.keyFields?first.varName});
    	</#if>	
	}
</#if>

<#if entity.hasUniqueField >
    @RequestMapping(value="<#list entity.uniqueFields as field>/{${field.varName}}</#list>", method = { RequestMethod.GET })
    @ResponseBody
	public ${entity.objectName}  select${entity.objectName}By${entity.uniqueFieldNames}(<#list entity.uniqueFields as field>
				@PathVariable("${field.varName}") ${field.javaTypeName} ${field.varName}<#sep>, 
			</#list><#if entity.usePrincipal >, java.security.Principal principal</#if>)
	{
		if(logger.isDebugEnabled()){
    			logger.debug("select ${entity.objectName} by <#list entity.uniqueFields as field>${field.varName}=[{}]<#sep>, </#list>",  <#list entity.uniqueFields as field>${field.varName}<#sep>,</#list>);
    			}
    	<#if (entity.uniqueFields?size>1) >
    		${entity.objectName} ${entity.varName} = new ${entity.objectName}();
    		<#list entity.uniqueFields as field>
    			${entity.varName}.set${field.objectName}(${field.varName});
    		</#list>
    		return this.${entity.varName}Mapper.select${entity.objectName}By${entity.uniqueFieldNames}(${entity.varName});
    	<#else>
    		return this.${entity.varName}Mapper.select${entity.objectName}By${entity.uniqueFieldNames}(${entity.uniqueFields?first.varName});
    	</#if>	
	}

</#if>	

<#if entity.hasGroupByField >
	 @RequestMapping(value="/groupBy<#list entity.groupByFields as field>/${field.varName}</#list>", method = { RequestMethod.GET })
     @ResponseBody	
	 public java.util.List<${entity.objectName}>  select${entity.objectName}GroupBy${entity.groupByFieldNames}(<#if entity.usePrincipal >java.security.Principal principal</#if>)
	 {
	 	if(logger.isDebugEnabled()){
    			logger.debug("select ${entity.objectName} Group By <#list entity.groupByFields as field>${field.varName}<#sep>,</#list>");
    			}
    	
    	return this.${entity.varName}Mapper.select${entity.objectName}GroupBy${entity.groupByFieldNames}();
    		
	 }
	 
</#if>	
<#if !entity.readonly>	
	 @RequestMapping( method = { RequestMethod.POST })
     @ResponseBody	
	 public int add${entity.objectName}(${entity.objectName} ${entity.varName}<#if entity.usePrincipal >, java.security.Principal principal</#if>)
	 {
	 	if(logger.isDebugEnabled()){
    			logger.debug("insert ${entity.objectName} ");
    			}
    	<#if entity.usePrincipal >
    	<#list entity.fields as field>
    		<#if field.principal??>    			
    		  	if(principal==null){
    		  		${entity.varName}.set${field.objectName}("${field.principal}");
    		  	}else{
    		  		${entity.varName}.set${field.objectName}(principal.getName());
    		  	}    		  			
    		</#if>
    	</#list>
    	</#if>
    	return this.${entity.varName}Mapper.insert${entity.objectName}(${entity.varName});
    		
	 }
	
<#if entity.hasKeyField >
	 @RequestMapping( method = { RequestMethod.PUT })
     @ResponseBody	
	 public  int update${entity.objectName}By${entity.keyFieldNames}(${entity.objectName} ${entity.varName}<#if entity.usePrincipal >, java.security.Principal principal</#if>)
	 {
	 	if(logger.isDebugEnabled()){
    			logger.debug("update ${entity.objectName} By ${entity.keyFieldNames}");
    			}
    	<#list entity.fields as field>
    		<#if field.principal??>    			
    		  	if(principal==null){
    		  		${entity.varName}.set${field.objectName}("${field.principal}");
    		  	}else{
    		  		${entity.varName}.set${field.objectName}(principal.getName());
    		  	}    		  			
    		</#if>
    	</#list>
    	return this.${entity.varName}Mapper.update${entity.objectName}By${entity.keyFieldNames}(${entity.varName});
    		
	 }
	 	
	 @RequestMapping(value="<#list entity.keyFields as field>/{${field.varName}}</#list>", method = { RequestMethod.DELETE })
     @ResponseBody	
	 public  int delete${entity.objectName}By${entity.keyFieldNames}(<#if (entity.keyFields?size>1) >${entity.objectName} ${entity.varName}<#else>${entity.keyFields?first.javaTypeName} ${entity.keyFields?first.varName}</#if><#if entity.usePrincipal >, java.security.Principal principal</#if>)
	 {
	 	if(logger.isDebugEnabled()){
    			logger.debug("delete ${entity.objectName} By ${entity.keyFieldNames}");
    			}
    	
    	<#if (entity.keyFields?size>1) >
    		${entity.objectName} ${entity.varName} = new ${entity.objectName}();
    		<#list entity.keyFields as field>
    			${entity.varName}.set${field.objectName}(${field.varName});
    		</#list>
    		return this.${entity.varName}Mapper.delete${entity.objectName}By${entity.keyFieldNames}(${entity.varName});
    	<#else>
    		return this.${entity.varName}Mapper.delete${entity.objectName}By${entity.keyFieldNames}(${entity.keyFields?first.varName});
    	</#if>	
    		
	 }	
	
</#if>
<#if entity.hasUniqueField >
	@RequestMapping( method = { RequestMethod.PUT })
     @ResponseBody	
	 public int update${entity.objectName}By${entity.uniqueFieldNames}(${entity.objectName} ${entity.varName}<#if entity.usePrincipal >, java.security.Principal principal</#if>)
	 {
	 	if(logger.isDebugEnabled()){
    			logger.debug("update ${entity.objectName} By ${entity.uniqueFieldNames}");
    			}
    	<#list entity.fields as field>
    		<#if field.principal??>    			
    		  	if(principal==null){
    		  		${entity.varName}.set${field.objectName}("${field.principal}");
    		  	}else{
    		  		${entity.varName}.set${field.objectName}(principal.getName());
    		  	}    		  			
    		</#if>
    	</#list>
    	return this.${entity.varName}Mapper.update${entity.objectName}By${entity.uniqueFieldNames}(${entity.varName});
	 }
	 @RequestMapping(value="<#list entity.uniqueFields as field>/{${field.varName}}</#list>", method = { RequestMethod.DELETE })
     @ResponseBody	
	 public int delete${entity.objectName}By${entity.uniqueFieldNames}(<#if (entity.uniqueFields?size>1) >${entity.objectName} ${entity.varName}<#else>${entity.uniqueFields?first.javaTypeName} ${entity.uniqueFields?first.varName}</#if><#if entity.usePrincipal >, java.security.Principal principal</#if>)
	 {
	 	<#if (entity.uniqueFields?size>1) >
    		${entity.objectName} ${entity.varName} = new ${entity.objectName}();
    		<#list entity.uniqueFields as field>
    			${entity.varName}.set${field.objectName}(${field.varName});
    		</#list>
    		return this.${entity.varName}Mapper.delete${entity.objectName}By${entity.uniqueFieldNames}(${entity.varName});
    	<#else>
    		return this.${entity.varName}Mapper.delete${entity.objectName}By${entity.uniqueFieldNames}(${entity.uniqueFields?first.varName});
    	</#if>	
	 }
</#if>
</#if>    
		
	
}
