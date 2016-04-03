package ${config.restPackageName};


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
 
import ${config.mapperPackageName}.*;
import ${config.domainPackageName}.*;

/**
 * Please refer http://spring.io/guides/tutorials/bookmarks/ 
 * for detail how to create test for spring rest service
 * 
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:${config.webappContext}-spring-mybatis-context.xml", "classpath:${config.webappContext}-spring-ui-context.xml"})
public class ${entity.objectName}RestControllerTest {
 
 
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    @Autowired
    private ${entity.objectName}Mapper ${entity.varName}MapperMock;
    
        

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();        
    }
   
       
    @Test  
    public void test_add${entity.objectName}() throws Exception
    {
        ${entity.objectName} ${entity.varName} = new ${entity.objectName}();
    	ObjectMapper mapper = new ObjectMapper();
    	String json = mapper.writeValueAsString( ${entity.varName});
    	RequestBuilder request = post("/${entity.varName}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json.getBytes("UTF-8"));
    	this.mockMvc.perform(request);      	
        fail("Not yet implemented");
    }
    
   @Test  
    public void test_new${entity.objectName}()  throws Exception{
    	RequestBuilder request = get("/${entity.varName}/new").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	this.mockMvc.perform(request);    
    	fail("Not yet implemented");
    }

   @Test  
    public void test_get${entity.objectName}()  throws Exception{
        RequestBuilder request = get("/${entity.varName}.json").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	this.mockMvc.perform(request);    
        fail("Not yet implemented");
    }
    
    <#if (entity.keyFields?size == 1) >
    	<#assign keyField = entity.keyFields?first >
    	@Test  
    	public void test_get${entity.objectName}By${keyField.objectName}()  throws Exception
    	{
    		 RequestBuilder request = get("/${entity.varName}/{${keyField.varName}}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	     this.mockMvc.perform(request);    
    		fail("Not yet implemented");
    	} 
    	
    	@Test
    	public void test_update${entity.objectName}By${keyField.objectName}()  throws Exception
    	{
    		${entity.objectName} ${entity.varName} = new ${entity.objectName}();
	    	ObjectMapper mapper = new ObjectMapper();
	    	String json = mapper.writeValueAsString( ${entity.varName});
	    	RequestBuilder request = put("/${entity.varName}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json.getBytes("UTF-8"));
	    	this.mockMvc.perform(request);  
    		fail("Not yet implemented");
    		    		
    	}
    	
    	@Test
    	public void test_delete${entity.objectName}By${keyField.objectName}()  throws Exception
    	{
    		 
    		 RequestBuilder request = delete("/${entity.varName}/{${keyField.varName}}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	     this.mockMvc.perform(request);    
    		 fail("Not yet implemented"); 		
    	}
    	
    	   
    <#elseif (entity.keyFields?size > 1 ) >    	    
    	@Test
    	public void test_get${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>()  throws Exception   	
    	{
    		 
    		RequestBuilder request = get("/${entity.varName}<#list entity.keyFields as keyField>/{${keyField.varName}}</#list>.json").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	    this.mockMvc.perform(request); 
    		fail("Not yet implemented"); 	
    	}     	
    	@Test
    	public void test_update${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>()  throws Exception
    	{
    		${entity.objectName} ${entity.varName} = new ${entity.objectName}();
	    	ObjectMapper mapper = new ObjectMapper();
	    	String json = mapper.writeValueAsString( ${entity.varName});
	    	RequestBuilder request = put("/${entity.varName}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json.getBytes("UTF-8"));
	    	this.mockMvc.perform(request);  
    		fail("Not yet implemented"); 	
      	}
    	
    	@Test
    	public void test_delete${entity.objectName}By<#list entity.keyFields as keyField>${keyField.objectName}</#list>()  throws Exception
    	{
    		RequestBuilder request = delete("/${entity.varName}/<#list entity.keyFields as keyField>/{${keyField.varName}}</#list>.json").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	    this.mockMvc.perform(request); 
    		fail("Not yet implemented"); 
    	}    	
	</#if>
    
     <#if (entity.groupByFields?size >0 ) >
 		@Test
    	public void test_select${entity.objectName}GroupBy<#list entity.groupByFields as keyField>${keyField.objectName}</#list>()  throws Exception
    	{
    		   		
    		RequestBuilder request = get("/${entity.varName}/groupby<#list entity.keyFields as keyField>/{${keyField.varName}}</#list>.json").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
    	    this.mockMvc.perform(request); 
    		fail("Not yet implemented"); 
    	}  
    </#if>
}