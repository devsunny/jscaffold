package com.asksunny.codegen.java;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.codegen.TemplateRender;
import com.asksunny.codegen.TypesBean;
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

		String generated = TemplateRender.newInstance().setLoaderClass(getClass())
				.setTemplate("myBatisJavaMapper.java.ftl", Locale.US).addTemplateParam("config", configuration)
				.addTemplateParam("entity", entity).addTemplateParam("utils", new TypesBean()).renderTemplate();		
		String mapperPath = configuration.getMapperPackageName().replaceAll("[\\.]", "/");
		writeCode(new File(configuration.getJavaBaseDir(), mapperPath),
				String.format("%sMapper.java", entity.getObjectName()), generated);

	}

}
