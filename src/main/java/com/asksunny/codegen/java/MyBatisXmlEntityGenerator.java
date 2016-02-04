package com.asksunny.codegen.java;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.codegen.TemplateRender;
import com.asksunny.codegen.TypesBean;
import com.asksunny.schema.Entity;

public class MyBatisXmlEntityGenerator extends CodeGenerator {

	public void doCodeGen() throws IOException {
		if (!configuration.isGenMyBatisXmlMapper()) {
			return;
		}
		String generated = TemplateRender.newInstance().setLoaderClass(getClass()).setTemplate("MyBatis.xml_en_US.ftl", Locale.US)
				.addTemplateParam("config", configuration)
				.addTemplateParam("entity", entity)
				.addTemplateParam("utils", new TypesBean())
				.renderTemplate();		
		String filePath = configuration.getMapperPackageName().replaceAll("[\\.]", "/");
		writeCode(new File(configuration.getMyBatisXmlBaseDir(), filePath),
				String.format("%sMapper.xml", entity.getObjectName()), generated);
	}

	public MyBatisXmlEntityGenerator(CodeGenConfig configuration, Entity entity) {
		super(configuration, entity);
	}

}
