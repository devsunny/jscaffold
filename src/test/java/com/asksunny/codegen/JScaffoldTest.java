package com.asksunny.codegen;

import org.junit.Test;

import com.asksunny.codegen.CodeGenConfig.CodeOverwriteStrategy;
import com.asksunny.codegen.data.DataGenerator;
import com.asksunny.codegen.java.JavaCodeGen;

public class JScaffoldTest {

	@Test
	public void testCodeGen() throws Exception{
		CodeGenConfig config = new CodeGenConfig();
		config.setBaseSrcDir("./target");
		config.setSchemaFiles("agile_project_schema.sql");
		config.setBasePackageName("com.asksunny.pm");
		config.setWebappContext("agile");
		config.setAppBootstrapClassName("AgileProjectBootstrap");
		config.setOverwriteStrategy(CodeOverwriteStrategy.OVERWRITE);		
		config.setDataOutputDir("src/test/resources");		
		config.setGenJunit(true);
		
		JavaCodeGen javaGen = new JavaCodeGen(config);		
		javaGen.doCodeGen();
	}
	
	@Test
	public void testDataGen() throws Exception{
		CodeGenConfig config = new CodeGenConfig();
		config.setBaseSrcDir("./target");
		config.setSchemaFiles("agile_project_schema.sql");
		config.setBasePackageName("com.asksunny.pm");
		config.setWebappContext("agile");
		config.setAppBootstrapClassName("AgileProjectBootstrap");
		config.setOverwriteStrategy(CodeOverwriteStrategy.OVERWRITE);		
		config.setDataOutputDir("src/test/resources");				
		DataGenerator dataGen = new DataGenerator(config);
		config.setNumberOfRecords(40);
		config.setOutputType(DataOutputType.INSERT);
		dataGen.doCodeGen();
	}
	

}
