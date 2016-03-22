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
			System.out.println("Loading schema file:" + sfs[i]);
			String prefix = sfs[i].startsWith("/")?"":"/";
			System.out.println("Loading schema file:" + String.format("%s%s", prefix, sfs[i]));
			InputStream in = getClass().getResourceAsStream(String.format("%s%s", prefix, sfs[i]));
			if (in == null && new File(sfs[i]).exists()) {
				in = new FileInputStream(sfs[i]);
			}else if (in == null) {
				System.out.println(String.format("Schema file [%s] does not exist.", prefix, sfs[i]));
				continue;
			}
			try {
				SQLScriptParser parser = new SQLScriptParser(new InputStreamReader(in));
				if(configuration.isDebug()){
					parser.setDebug(true);
				}
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
