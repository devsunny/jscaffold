package com.asksunny.codegen.angular;

import java.sql.Types;
import java.util.Locale;

import org.junit.Test;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.FieldDomainType;
import com.asksunny.codegen.TemplateRender;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Field;

public class FormCnntrollerTempateTest {

	@Test
	public void test() throws Exception{
		CodeGenConfig configuration = new CodeGenConfig();
		Entity entity = new Entity("My_Table"); 
		Field f = new Field(Types.INTEGER, 0, 0, 12, false, "id", FieldDomainType.SEQUENCE, "0", "12", null, "0");
		f.setPrimaryKey(true);
		entity.addField(f);
		entity.addField(new Field(Types.VARCHAR, 0, 0, 12, false, "Name", FieldDomainType.CITY, "0", "12", null, "0"));
		entity.addField(new Field(Types.INTEGER, 0, 0, 12, false, "TEST", FieldDomainType.BIGINTEGER, "0", "12", null, "0"));
		entity.addField(new Field(Types.DATE, 0, 0, 12, false, "DOB", FieldDomainType.DATE, "0", "12", "yyyy-MM-dd", "0"));
		
		String generated = TemplateRender.newInstance().setLoaderClass(AngularEntityFormGenerator.class)
				.setTemplate("angularEntityFormController.js_en_US.ftl", Locale.US)
				.addTemplateParam("config", configuration)
				.addTemplateParam("entity", entity)
				.renderTemplate();
		
		System.out.println(generated);
	}
	
	@Test
	public void testFormTemplate() throws Exception{
		CodeGenConfig configuration = new CodeGenConfig();
		Entity entity = new Entity("My_Table"); 
		Field f = new Field(Types.INTEGER, 0, 0, 12, false, "id", FieldDomainType.SEQUENCE, "0", "12", null, "0");
		f.setPrimaryKey(true);
		entity.addField(f);
		entity.addField(new Field(Types.VARCHAR, 0, 0, 12, false, "Name", FieldDomainType.CITY, "0", "12", null, "0"));
		entity.addField(new Field(Types.INTEGER, 0, 0, 12, false, "TEST", FieldDomainType.BIGINTEGER, "0", "12", null, "0"));
		entity.addField(new Field(Types.DATE, 0, 0, 12, false, "DOB", FieldDomainType.DATE, "0", "12", "yyyy-MM-dd", "0"));
		
		String generated = TemplateRender.newInstance().setLoaderClass(AngularEntityFormGenerator.class)
				.setTemplate("angularEntityForm.html_en_US.ftl", Locale.US)
				.addTemplateParam("config", configuration)
				.addTemplateParam("entity", entity)
				.addTemplateParam("FORM_FIELDS", "")				
				.renderTemplate();		
		System.out.println(generated);
	}
	
	

}
