package com.asksunny.codegen.java;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.codegen.TemplateRender;
import com.asksunny.schema.Entity;

public class JavaDomainObjectGenerator extends CodeGenerator {

	public static final String JSON_FORMAT_IMPORT = "import com.fasterxml.jackson.annotation.JsonFormat;";

	public JavaDomainObjectGenerator(CodeGenConfig config, Entity entity) {
		super(config, entity);
	}

	public void doCodeGen() throws IOException {
		if (!configuration.isGenDomainObject()) {
			return;
		}

		String src = TemplateRender.newInstance().setLoaderClass(getClass())
				.setTemplate("JavaDomainObject.java.ftl", Locale.US).addTemplateParam("entity", entity)
				.addTemplateParam("DOMAIN_PACKAGE_NAME", configuration.getDomainPackageName())
				.addTemplateParam("JSON_FORMAT_ANNO_IMPORT", JSON_FORMAT_IMPORT).renderTemplate();
		String filePath = configuration.getDomainPackageName().replaceAll("[\\.]", "/");
		writeCode(new File(configuration.getJavaBaseDir(), filePath), String.format("%s.java", entity.getObjectName()),
				src);

	}

}
