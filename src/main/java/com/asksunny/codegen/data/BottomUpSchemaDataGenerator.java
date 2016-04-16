package com.asksunny.codegen.data;

import java.util.List;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Schema;

public class BottomUpSchemaDataGenerator {

	private Schema schema;
	private CodeGenConfig config;
	private String schemaUri;

	public void generateData() {
		System.out.println(config.getSchema().getAllEntities().size());
		config.getSchema().buildRelationship();
		List<Entity> entities = config.getSchema().getAllEntities();
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			Entity entity = entities.get(i);
			System.out.println("BottomUpEntityDataGenerator for entity:" + entity.getName());
			if (this.config.isDebug()) {
				System.out.println("Generating Data for entity:" + entity.getName());
			}
			if (entity.isIgnoreData()) {
				System.out.println("Ignore data generation for entity:" + entity.getName());
				continue;
			}
			BottomUpEntityDataGenerator entityGen = EntityGeneratorFactory.createEntityGenerator(entity, config);
			try {
				entityGen.generateFullDataSet();
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
		for (BottomUpEntityDataGenerator entityGen : EntityGeneratorFactory.getAllCachedEntityGenerators()) {			
			try {
				entityGen.generateFullDataSet();
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}

	}

	public BottomUpSchemaDataGenerator(CodeGenConfig config, Schema schema) {
		super();
		this.config = config;
		this.schema = schema;
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public BottomUpSchemaDataGenerator(Schema schema) {
		super();
		this.schema = schema;
	}

	public BottomUpSchemaDataGenerator() {

	}

	public String getSchemaUri() {
		return schemaUri;
	}

	public void setSchemaUri(String schemaUri) {
		this.schemaUri = schemaUri;
	}

	public CodeGenConfig getConfig() {
		return config;
	}

	public void setConfig(CodeGenConfig config) {
		this.config = config;
	}

	public void close() {
		for (BottomUpEntityDataGenerator entityGen : EntityGeneratorFactory.getAllCachedEntityGenerators()) {
			try {
				entityGen.close();
			} catch (Exception e) {
				;
			}
		}
	}

}
