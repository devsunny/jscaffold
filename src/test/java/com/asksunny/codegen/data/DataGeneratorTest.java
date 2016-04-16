package com.asksunny.codegen.data;

import static org.junit.Assert.*;

import org.junit.Test;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.DataOutputType;
import com.asksunny.codegen.CodeGenConfig.CodeOverwriteStrategy;
import com.asksunny.codegen.java.JavaCodeGen;

public class DataGeneratorTest {

	@Test
	public void test() throws Exception{
		CodeGenConfig config = new CodeGenConfig();
		config.setBaseSrcDir(".");
		config.setSchemaFiles("agile_project_schema.sql");	
		config.setDataOutputDir("./target");		
		//System.out.println(config.getSchema());		
		DataGenerator dataGen = new DataGenerator(config);		
		config.setNumberOfRecords(40);
		config.setOutputType(DataOutputType.CSV);
		dataGen.doCodeGen();
	}

}
