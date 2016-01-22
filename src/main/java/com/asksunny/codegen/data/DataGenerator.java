package com.asksunny.codegen.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.schema.Schema;
import com.asksunny.schema.parser.SQLScriptParser;

public class DataGenerator extends CodeGenerator {

	public DataGenerator() {
		super(new CodeGenConfig(), (Schema) null);
	}

	public DataGenerator(CodeGenConfig configuration) {
		super(configuration, (Schema) null);
	}

	@Override
	public void doCodeGen() throws IOException {

		String schemaFiles = configuration.getSchemaFiles();
		if (schemaFiles == null) {
			throw new IOException("Schema DDL file has not been specified");
		}
		Schema schema = null;
		String[] sfs = schemaFiles.split("\\s*[,;]\\s*");
		for (int i = 0; i < sfs.length; i++) {
			InputStream in = getClass().getResourceAsStream(String.format("/%s", sfs[i]));
			if (in == null) {
				in = new FileInputStream(sfs[i]);
			}
			try {
				SQLScriptParser parser = new SQLScriptParser(new InputStreamReader(in));
				Schema schemax = parser.parseSql();
				if (schema == null) {
					schema = schemax;
				} else {
					schema.getAllEntities().addAll(schemax.getAllEntities());
				}
			} finally {
				in.close();
			}
		}

		BottomUpSchemaDataGenerator dataGenerator = new BottomUpSchemaDataGenerator(configuration, schema);		
		dataGenerator.generateData();

	}

}
