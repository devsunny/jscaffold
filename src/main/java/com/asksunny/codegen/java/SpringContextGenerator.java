package com.asksunny.codegen.java;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.codegen.TemplateRender;
import com.asksunny.codegen.utils.FMParamMapBuilder;
import com.asksunny.codegen.utils.ParamMapBuilder;
import com.asksunny.codegen.utils.TemplateUtil;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Schema;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class SpringContextGenerator extends CodeGenerator {

	public void doCodeGen() throws IOException {
		StringBuilder mapperBeans = new StringBuilder();
		List<Entity> entities = schema.getAllEntities();
		for (Entity entity : entities) {
			mapperBeans.append(genSpringMyBatisBeanXml(entity)).append("\n");
			
		}
		String generated;
		generated = TemplateRender.newInstance().setTemplate("spring-mybatis-context.xml.ftl", Locale.US)
		.setLoaderClass(getClass())
		.addTemplateParam("config", configuration)
		.addTemplateParam("MYBATIS_MAPPERS", mapperBeans.toString()).renderTemplate();
		
		
		String myBatisSpringContext = String.format("%s-spring-mybatis-context.xml", configuration.getWebappContext());
		writeCode(new File(configuration.getSpringXmlBaseDir()), myBatisSpringContext, generated);

		generated = TemplateRender.newInstance().setTemplate("NoCacheStaticResourceServlet.java.ftl", Locale.US)
				.setLoaderClass(getClass())
				.addTemplateParam("config", configuration).renderTemplate();
		writeCode(new File(configuration.getJavaBaseDir(), configuration.getBasePackagePath()), "NoCacheStaticResourceServlet.java", generated);
		
		generated = TemplateRender.newInstance().setTemplate("FavorIconStaticResourceServlet.java.ftl", Locale.US)
				.setLoaderClass(getClass())
				.addTemplateParam("config", configuration).renderTemplate();
		writeCode(new File(configuration.getJavaBaseDir(), configuration.getBasePackagePath()), "FavorIconStaticResourceServlet.java", generated);
			
		String uicontext = TemplateUtil.renderTemplate(
				IOUtils.toString(getClass().getResourceAsStream("spring-webui-context.xml.tmpl")),
				ParamMapBuilder.newBuilder().buildMap());
		// System.out.println(uicontext);
		String uiSpringContext = String.format("%s-spring-ui-context.xml", configuration.getWebappContext());
		writeCode(new File(configuration.getSpringXmlBaseDir()), uiSpringContext, uicontext);

		try {
			Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
			cfg.setClassForTemplateLoading(getClass(), "");
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			Template temp = cfg.getTemplate("spring-jetty-booststrap.xml.ftl", Locale.US);
			StringWriter out = new StringWriter();
			temp.process(FMParamMapBuilder.newBuilder().addMapEntry("config", configuration)
					.addMapEntry("WEBAPP_CONTEXT", configuration.getWebappContext()).buildMap(), out);
			out.flush();
			String bootstrapSpringContext = String.format("%s-spring-bootstrap-context.xml",
					configuration.getWebappContext());
			writeCode(new File(configuration.getSpringXmlBaseDir()), bootstrapSpringContext, out.toString());

			if (configuration.isEnableSpringSecurity()) {
				temp = cfg.getTemplate("DispatchTypeEnumSetFactoryBean.java.ftl", Locale.US);
				out = new StringWriter();
				temp.process(FMParamMapBuilder.newBuilder().addMapEntry("config", configuration)
						.addMapEntry("WEBAPP_CONTEXT", configuration.getWebappContext()).buildMap(), out);
				out.flush();

				String fpath = configuration.getBasePackageName().replaceAll("[\\.]", "/");
				File dir = new File(configuration.getJavaBaseDir(), fpath);
				writeCode(dir, "DispatchTypeEnumSetFactoryBean.java", out.toString());
				
				temp = cfg.getTemplate("EmbeddedDelegatingFilterProxy.java.ftl", Locale.US);
				out = new StringWriter();
				temp.process(FMParamMapBuilder.newBuilder().addMapEntry("config", configuration)
						.addMapEntry("WEBAPP_CONTEXT", configuration.getWebappContext()).buildMap(), out);
				out.flush();			
				writeCode(dir, "EmbeddedDelegatingFilterProxy.java", out.toString());	
				
				temp = cfg.getTemplate("spring-security-context.xml.ftl", Locale.US);
				out = new StringWriter();
				temp.process(FMParamMapBuilder.newBuilder().addMapEntry("config", configuration)
						.addMapEntry("WEBAPP_CONTEXT", configuration.getWebappContext()).buildMap(), out);
				out.flush();			
				String secSpringContext = String.format("%s-spring-security-context.xml", configuration.getWebappContext());
				writeCode(new File(configuration.getSpringXmlBaseDir()), secSpringContext, out.toString());					
			}

		} catch (TemplateException e) {
			throw new IOException("Failed to render template", e);
		}

		if (configuration.getAppBootstrapClassName() != null) {
			String bootstrapClass = writeSpringBootstrap();
			File dir = new File(configuration.getJavaBaseDir());
			if (configuration.getAppBootstrapPackage() != null) {
				String fpath = configuration.getAppBootstrapPackage().replaceAll("[\\.]", "/");
				dir = new File(dir, fpath);
			}
			writeCode(dir, String.format("%s.java", configuration.getAppBootstrapClassName()), bootstrapClass);
		}

	}

	public String writeSpringBootstrap() throws IOException {

		String bootstrappackage = configuration.getAppBootstrapPackage() == null ? ""
				: String.format("package %s;", configuration.getAppBootstrapPackage());
		String boostrapContext = String.format("%s-spring-bootstrap-context.xml", configuration.getWebappContext());
		String generated = TemplateUtil.renderTemplate(
				IOUtils.toString(getClass().getResourceAsStream("AppBootstrap.java.tmpl")),
				ParamMapBuilder.newBuilder().addMapEntry("BOOTSTRAP_PACKAGE", bootstrappackage)
						.addMapEntry("DOMAIN_PACKAGE", configuration.getDomainPackageName())
						.addMapEntry("BOOTSTRAP_CLASSNAME", configuration.getAppBootstrapClassName())
						.addMapEntry("BOOTSTRAP_CONTEXT", boostrapContext).buildMap());

		return generated;

	}

	public String genSpringMyBatisBeanXml(Entity entity) throws IOException {
		String generated = TemplateUtil.renderTemplate(
				IOUtils.toString(getClass().getResourceAsStream("spring-mybatis-mapper.xml.tmpl")),
				ParamMapBuilder.newBuilder().addMapEntry("MAPPER_PACKAGE", configuration.getMapperPackageName())
						.addMapEntry("ENTITY_VAR_NAME", entity.getVarName())
						.addMapEntry("ENTITY_NAME", entity.getObjectName()).buildMap());
		return generated;
	}

	public SpringContextGenerator(CodeGenConfig configuration, Schema schema) {
		super(configuration, schema);
	}

}
