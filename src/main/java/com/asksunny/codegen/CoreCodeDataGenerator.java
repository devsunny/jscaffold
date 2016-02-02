package com.asksunny.codegen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.asksunny.codegen.angular.AngularUIGenerator;
import com.asksunny.codegen.java.JavaCodeGen;
import com.asksunny.schema.Schema;

public class CoreCodeDataGenerator extends CodeGenerator {

	

	public CoreCodeDataGenerator(CodeGenConfig configuration) {
		super(configuration, (Schema)null);		
	}

	@Override
	public void doCodeGen() throws IOException 
	{		
		if(schema==null){
			loadSchema();
		}

		JavaCodeGen javacodeGen = new JavaCodeGen(configuration, schema);
		javacodeGen.doCodeGen();
		
		AngularUIGenerator uiGen = new AngularUIGenerator(configuration, schema);
		uiGen.doCodeGen();		
		
		if(configuration.isGenSchema()){
			genSchema();
		}
		if(configuration.isGenSeedData()){
			if (new File(configuration.getSpringXmlBaseDir(), "seed_data.sql").exists() == false) {
				writeCode(new File(configuration.getSpringXmlBaseDir()), "seed_data.sql", "");
			}
		}

	}
	
	
	protected void genSchema() throws IOException
	{
		String schemaFiles = configuration.getSchemaFiles();		
		String[] sfs = schemaFiles.split("\\s*[,;]\\s*");
		StringBuilder sqlBuffer = new StringBuilder();
		for (int i = 0; i < sfs.length; i++) {
			InputStream in = getClass().getResourceAsStream(String.format("/%s", sfs[i]));
			sqlBuffer.append(IOUtils.toString(in));
			sqlBuffer.append("\n;");
			in.close();
		}
		writeCode(new File(configuration.getSpringXmlBaseDir()), "schema_ddl.sql", sqlBuffer.toString());
	}
	
	
	
	

}
