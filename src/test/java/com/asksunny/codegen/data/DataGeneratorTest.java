package com.asksunny.codegen.data;

import org.junit.Test;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.DataOutputType;

public class DataGeneratorTest {

	@Test
	public void test() throws Exception {
		
		CodeGenConfig config = new CodeGenConfig();		
		config.setSchemaFiles("TestAngularGen.ddl.sql");
		config.setGenSeedData(true);
		config.setNumberOfRecords(1000);
		//config.setDebug(true);
		config.setDataOutputDir(null);
		config.setOutputType(DataOutputType.INSERT);
		DataGenerator dg = new DataGenerator(config);
		dg.doCodeGen();
	}

}
