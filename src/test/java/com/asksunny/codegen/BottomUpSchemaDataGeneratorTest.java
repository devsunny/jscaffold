package com.asksunny.codegen;

import org.junit.Test;

import com.asksunny.codegen.data.BottomUpSchemaDataGenerator;
import com.asksunny.schema.Schema;
import com.asksunny.schema.parser.SQLScriptLexer;
import com.asksunny.schema.parser.SQLScriptParser;

public class BottomUpSchemaDataGeneratorTest {

	@Test
	public void test() throws Exception {
		SQLScriptLexer lexer = new SQLScriptLexer(getClass().getResourceAsStream("/test.schema.ddl.sql"));
		SQLScriptParser tokenReader = new SQLScriptParser(lexer);
		Schema schema = tokenReader.parseSql();		
		BottomUpSchemaDataGenerator sgen = new BottomUpSchemaDataGenerator(schema);
		CodeGenConfig config = new CodeGenConfig();
		config.setNumberOfRecords(10);
		config.setOutputType(DataOutputType.INSERT);
		config.setDataOutputDir(null);
		//config.setDebug(true);
		sgen.setConfig(config);
		sgen.generateData();
	}

}
