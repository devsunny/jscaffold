package com.asksunny.codegen;

import org.junit.Test;

import com.asksunny.codegen.angular.AngularEntityFieldGenerator;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Field;
import com.asksunny.schema.Schema;
import com.asksunny.schema.parser.SQLScriptLexer;
import com.asksunny.schema.parser.SQLScriptParser;




import static org.junit.Assert.*;
public class AngularGenTest {

	@Test
	public void test() throws Exception {
		SQLScriptLexer lexer = new SQLScriptLexer(getClass().getResourceAsStream("/TestAngularGen.ddl.sql"));
		SQLScriptParser tokenReader = new SQLScriptParser(lexer);
		Schema schema = tokenReader.parseSql();		
		Entity entity = schema.get("Persons2");
		assertNotNull(entity);
		Field field = entity.findField("LastName");
		assertNotNull(field);
		AngularEntityFieldGenerator fg = new AngularEntityFieldGenerator(entity, field);
		System.out.println(fg.genField());
		System.out.println("---------------------");		
		AngularEntityFieldGenerator fg2 = new AngularEntityFieldGenerator(entity, entity.findField("STATE"));
		System.out.println(fg2.genField());
		
		System.out.println("---------------------");		
		AngularEntityFieldGenerator fg3 = new AngularEntityFieldGenerator(entity, entity.findField("description"));
		System.out.println(fg3.genField());
		
		
	}

}
