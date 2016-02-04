package com.asksunny.codegen;

import org.junit.Test;

import com.asksunny.codegen.CodeGenConfig.CodeOverwriteStrategy;
import com.asksunny.codegen.java.JavaCodeGen;
import com.asksunny.schema.Schema;
import com.asksunny.schema.parser.SQLScriptLexer;
import com.asksunny.schema.parser.SQLScriptParser;

public class JavaCodeGenTest {

	@Test
	public void test() throws Exception {
		CodeGenConfig config = new CodeGenConfig();
		config.setMapperPackageName("com.xperia.management.mappers");
		config.setDomainPackageName("com.xperia.management.domain");
		config.setRestPackageName("com.xperia.management.web.rest");
		config.setBaseSrcDir("./target");
		config.setWebappContext("management");	
		config.setAppBootstrapClassName("ManConsoleBoostrap");		
		config.setOverwriteStrategy(CodeOverwriteStrategy.OVERWRITE);		
		config.setGenPomXml(false);
		config.setUseRestfulEnvelope(true);
		SQLScriptLexer lexer = new SQLScriptLexer(getClass().getResourceAsStream("/TestAngularGen.ddl.sql"));
		SQLScriptParser tokenReader = new SQLScriptParser(lexer);
		Schema schema = tokenReader.parseSql();		
		JavaCodeGen javaGen  = new JavaCodeGen(config);
		javaGen.doCodeGen(schema);
	}

}
