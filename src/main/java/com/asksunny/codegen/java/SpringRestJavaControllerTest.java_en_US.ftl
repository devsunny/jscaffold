package ${REST_PACKAGE_NAME};


import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 
import ${MAPPER_PACKAGE_NAME}.*;
import ${DOMAIN_PACKAGE_NAME}.*;

/**
 * Please refer http://spring.io/guides/tutorials/bookmarks/ 
 * for detail how to create test for spring rest service
 * 
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:${WEBAPP_CONTEXT}-spring-mybatis-context.xml", "classpath:${WEBAPP_CONTEXT}-spring-ui-context.xml"})
public class ${entity.entityObjectName}RestControllerTest {
 
 
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    @Autowired
    private ${entity.entityObjectName}Mapper ${entity.entityVarName}MapperMock;
    
        

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        
    }
   
       
    @Test  
    public void test_add${entity.entityObjectName}() throws Exception
    {
        ${entity.entityObjectName} ${entity.entityVarName} = new ${entity.entityObjectName}();
    	ObjectMapper mapper = new ObjectMapper();
    	String json = mapper.writeValueAsString( ${entity.entityVarName});
    	RequestBuilder request = post("/${entity.entityVarName}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json.getBytes("UTF-8"));
    	this.mockMvc.perform(request);      	
        fail("Not yet implemented");
    }
    
   @Test  
    public void test_new${entity.entityObjectName}()  throws Exception{
    	RequestBuilder request = get("/${entity.entityVarName}/new").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	this.mockMvc.perform(request);    
    	fail("Not yet implemented");
    }

   @Test  
    public void test_get${entity.entityObjectName}()  throws Exception{
        RequestBuilder request = get("/${entity.entityVarName}.json").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	this.mockMvc.perform(request);    
        fail("Not yet implemented");
    }
    
    <#if (entity.keyFields?size == 1) >
    	<#assign keyField = entity.keyFields?first >
    	@Test  
    	public void test_get${entity.entityObjectName}By${keyField.objectname}()  throws Exception
    	{
    		 RequestBuilder request = get("/${entity.entityVarName}/{${keyField.varname}}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	     this.mockMvc.perform(request);    
    		fail("Not yet implemented");
    	} 
    	
    	@Test
    	public void test_update${entity.entityObjectName}By${keyField.objectname}()  throws Exception
    	{
    		${entity.entityObjectName} ${entity.entityVarName} = new ${entity.entityObjectName}();
	    	ObjectMapper mapper = new ObjectMapper();
	    	String json = mapper.writeValueAsString( ${entity.entityVarName});
	    	RequestBuilder request = put("/${entity.entityVarName}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json.getBytes("UTF-8"));
	    	this.mockMvc.perform(request);  
    		fail("Not yet implemented");
    		    		
    	}
    	
    	@Test
    	public void test_delete${entity.entityObjectName}By${keyField.objectname}()  throws Exception
    	{
    		 
    		 RequestBuilder request = delete("/${entity.entityVarName}/{${keyField.varname}}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	     this.mockMvc.perform(request);    
    		 fail("Not yet implemented"); 		
    	}
    	
    	   
    <#elseif (entity.keyFields?size > 1 ) >    	    
    	@Test
    	public void test_get${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>()  throws Exception   	
    	{
    		 
    		RequestBuilder request = get("/${entity.entityVarName}<#list entity.keyFields as keyField>/{${keyField.varname}}</#list>.json").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	    this.mockMvc.perform(request); 
    		fail("Not yet implemented"); 	
    	}     	
    	@Test
    	public void test_update${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>()  throws Exception
    	{
    		${entity.entityObjectName} ${entity.entityVarName} = new ${entity.entityObjectName}();
	    	ObjectMapper mapper = new ObjectMapper();
	    	String json = mapper.writeValueAsString( ${entity.entityVarName});
	    	RequestBuilder request = put("/${entity.entityVarName}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json.getBytes("UTF-8"));
	    	this.mockMvc.perform(request);  
    		fail("Not yet implemented"); 	
      	}
    	
    	@Test
    	public void test_delete${entity.entityObjectName}By<#list entity.keyFields as keyField>${keyField.objectname}</#list>()  throws Exception
    	{
    		RequestBuilder request = delete("/${entity.entityVarName}/<#list entity.keyFields as keyField>/{${keyField.varname}}</#list>.json").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	    this.mockMvc.perform(request); 
    		fail("Not yet implemented"); 
    	}    	
	</#if>
    
     <#if (entity.groupByFields?size >0 ) >
 		@Test
    	public void test_select${entity.entityObjectName}GroupBy<#list entity.groupByFields as keyField>${keyField.objectname}</#list>()  throws Exception
    	{
    		   		
    		RequestBuilder request = get("/${entity.entityVarName}/groupby<#list entity.keyFields as keyField>/{${keyField.varname}}</#list>.json").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	    this.mockMvc.perform(request); 
    		fail("Not yet implemented"); 
    	}  
    </#if>
}