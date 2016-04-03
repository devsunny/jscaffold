package com.asksunny.codegen.java;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.codegen.TemplateRender;
import com.asksunny.codegen.TypesBean;
import com.asksunny.schema.Entity;

public class JavaRestControllerGenerator extends CodeGenerator {

	static final String INDENDENT_1 = "    ";
	static final String INDENDENT_2 = "        ";
	static final String INDENDENT_3 = "            ";
	static final String INDENDENT_4 = "                ";

	public JavaRestControllerGenerator(CodeGenConfig config, Entity entity) {
		super(config, entity);
	}

	public void doCodeGen() throws IOException {
		if (!configuration.isGenRestController()) {
			return;
		}
		
		if(configuration.isUseRestfulEnvelope()){
			String generated = TemplateRender.newInstance().setLoaderClass(getClass())
					.setTemplate("RestfulResponse.java.ftl", Locale.US).addTemplateParam("config", configuration)
					.addTemplateParam("entity", entity).addTemplateParam("utils", new TypesBean()).renderTemplate();		
			
			String mapperPath = configuration.getDomainPackageName().replaceAll("[\\.]", "/");
			writeCode(new File(configuration.getJavaBaseDir(), mapperPath),
					"RestfulResponse.java", generated);
		}
		
		String generated = TemplateRender.newInstance().setLoaderClass(getClass())
				.setTemplate("SpringRestJavaController.java.ftl", Locale.US).addTemplateParam("config", configuration)
				.addTemplateParam("entity", entity).addTemplateParam("utils", new TypesBean()).renderTemplate();		
		
		String mapperPath = configuration.getRestPackagePath();
		writeCode(new File(configuration.getJavaBaseDir(), mapperPath),
				String.format("%sRestController.java", entity.getObjectName()), generated);
		
		if(!configuration.isGenJunit()){
			return;
		}
		
		generated = TemplateRender.newInstance().setLoaderClass(getClass())
				.setTemplate("SpringRestJavaControllerTest.java.ftl", Locale.US).addTemplateParam("config", configuration)
				.addTemplateParam("entity", entity).addTemplateParam("utils", new TypesBean()).renderTemplate();
		
		String filePath = configuration.getRestPackagePath();
		writeCode(new File(configuration.getJunitBaseDir(), filePath),
				String.format("%sRestControllerTest.java", entity.getObjectName()), generated);
		
	}
}
