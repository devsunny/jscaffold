package com.asksunny.codegen.java;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.codegen.utils.FMParamMapBuilder;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Field;
import com.asksunny.schema.FieldDrillDownComparator;
import com.asksunny.schema.FieldGroupLevelComparator;
import com.asksunny.schema.parser.JdbcSqlTypeMap;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class JavaMyBatisMapperGenerator extends CodeGenerator {

	static final String INDENDENT_1 = "    ";
	static final String INDENDENT_2 = "        ";
	static final String INDENDENT_3 = "            ";
	static final String INDENDENT_4 = "                ";

	public JavaMyBatisMapperGenerator(CodeGenConfig config, Entity entity) {
		super(config, entity);
	}

	public void doCodeGen() throws IOException {
		if (!configuration.isGenMyBatisMapper()) {
			return;
		}
		List<Field> keyFields = entity.getKeyFields();
		if (keyFields.size() == 0 && entity.isHasUniqueField()) {
			keyFields = entity.getUniqueFields();
		}

		StringBuilder methods = new StringBuilder();		
		List<Field> gbFields = entity.getGroupByFields();
		Collections.sort(gbFields, new FieldGroupLevelComparator());
		if (gbFields.size() == 1) {
			Field keyField = gbFields.get(0);
			methods.append(String.format("%2$sjava.util.List<%1$s> select%1$sGroupBy%3$s(%5$s %4$s);",
					entity.getObjectName(), INDENDENT_2, keyField.getObjectName(), keyField.getVarName(),
					JdbcSqlTypeMap.toJavaTypeName(keyField))).append("\n");

		} else if (gbFields.size() > 1) {
			StringBuilder buf = new StringBuilder();
			for (Field keyField : gbFields) {
				buf.append(keyField.getObjectName());
			}
			methods.append(String.format("%2$sjava.util.List<%1$s> select%1$sGroupBy%4$s(%1$s %3$s);",
					entity.getObjectName(), INDENDENT_2, entity.getVarName(), buf.toString())).append("\n");
		}

		List<Field> ddFields = entity.getDrillDownFields();
		Collections.sort(ddFields, new FieldDrillDownComparator());
		if (ddFields.size() > 0) {
			Field dd0 = ddFields.get(0);
			methods.append(String.format("%1$sjava.util.List<%2$s> select%2$sDrilldownBy%4$s(%2$s %3$s);",
					INDENDENT_2, entity.getObjectName(), entity.getVarName(), dd0.getObjectName(), dd0.getVarName(),
					JdbcSqlTypeMap.toJavaTypeName(dd0))).append("\n");

		}
		if (ddFields.size() > 1) {
			Field dd1 = ddFields.get(0);
			StringBuilder restPath = new StringBuilder();
			restPath.append(String.format("/{%s}", dd1.getVarName()));
			StringBuilder uiPath = new StringBuilder();
			uiPath.append(String.format("/:%s", dd1.getVarName()));
			for (int i = 1; i < ddFields.size(); i++) {
				Field dd0 = ddFields.get(i);
				methods.append(String.format("%2$sjava.util.List<%1$s> select%1$sDrilldownBy%3$s(%1$s %4$s);",
						entity.getObjectName(), INDENDENT_2, dd0.getObjectName(), entity.getVarName(),
						JdbcSqlTypeMap.toJavaTypeName(dd0), entity.getVarName())).append("\n");

			}

		}

		if (ddFields.size() > 0) {
			StringBuilder restPath = new StringBuilder();
			StringBuilder uiPath = new StringBuilder();
			for (int i = 0; i < ddFields.size(); i++) {
				Field dd0 = ddFields.get(i);
				restPath.append(String.format("/{%s}", dd0.getVarName()));
				uiPath.append(String.format("/:%s", dd0.getVarName()));
			}

			methods.append(String.format("%1$sjava.util.List<%2$s> select%2$sDrilldownDetail(%2$s %3$s);\n",
					INDENDENT_2, entity.getObjectName(), entity.getVarName()));
		}
		
		try {
			Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);		
			cfg.setClassForTemplateLoading(getClass(), "");			
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			Template temp = cfg.getTemplate("myBatisJavaMapper.java.ftl", Locale.US);
			StringWriter out = new StringWriter();
			temp.process(FMParamMapBuilder.newBuilder()
					.addMapEntry("MAPPER_PACKAGE_NAME", configuration.getMapperPackageName())
					.addMapEntry("DOMAIN_PACKAGE", configuration.getDomainPackageName())
					.addMapEntry("MAPPER_METHODS", methods.toString())					
					.addMapEntry("entity", entity)					
					.addMapEntry("ENTITY_VAR_NAME", entity.getVarName())
					.addMapEntry("ENTITY_NAME", entity.getObjectName())
					.addMapEntry("ENTITY_LABEL", entity.getLabel()).buildMap(), out);
			out.flush();
			String mapperPath = configuration.getMapperPackageName().replaceAll("[\\.]", "/");
			writeCode(new File(configuration.getJavaBaseDir(), mapperPath),
					String.format("%sMapper.java", entity.getObjectName()), out.toString());
		} catch (TemplateException e) {
			throw new IOException("Failed to render template", e);
		}
	}

}
