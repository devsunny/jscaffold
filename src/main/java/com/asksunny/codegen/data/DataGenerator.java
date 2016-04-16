package com.asksunny.codegen.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.schema.Schema;
import com.asksunny.schema.parser.SQLScriptParser;

public class DataGenerator extends CodeGenerator {

	public DataGenerator() {
		super(new CodeGenConfig(), (Schema) null);
		super.schema = configuration.getSchema();
	}

	public DataGenerator(CodeGenConfig configuration) {
		super(configuration, (Schema) null);
		super.schema = configuration.getSchema();
	}

	@Override
	public void doCodeGen() throws IOException {
		String schemaFiles = configuration.getSchemaFiles();
		if (schemaFiles == null) {
			throw new IOException("Schema DDL file has not been specified");
		}
		BottomUpSchemaDataGenerator dataGenerator = new BottomUpSchemaDataGenerator(configuration,
				configuration.getSchema());
		dataGenerator.generateData();
	}

}
