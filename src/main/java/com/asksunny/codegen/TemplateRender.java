package com.asksunny.codegen;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class TemplateRender {

	final Map<String, Object> tempplateParams;
	private Class<?> templateLoaderClass;
	private String templateName;
	private Locale locale;

	private TemplateRender() {
		tempplateParams = new HashMap<String, Object>();
		tempplateParams.put("types", new TypesBean());
	}

	public static TemplateRender newInstance() {
		return new TemplateRender();
	}

	public TemplateRender setLoaderClass(Class<?> templateLoaderClass) {
		this.templateLoaderClass = templateLoaderClass;
		return this;
	}

	public TemplateRender setTemplate(String tplName, Locale locale) {
		this.templateName = tplName;
		this.locale = locale;
		return this;
	}

	public TemplateRender addTemplateParam(String paramName, Object bean) {
		if (paramName.equals("types")) {
			throw new RuntimeException("Reserved parameter name for TypesBean");
		}
		tempplateParams.put(paramName, bean);
		return this;
	}

	public String renderTemplate() throws IOException {

		try {
			Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
			cfg.setClassForTemplateLoading(this.templateLoaderClass, "");
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			Template temp = cfg.getTemplate(this.templateName, this.locale);
			StringWriter out = new StringWriter();
			temp.process(this.tempplateParams, out);
			out.flush();
			return out.toString();
		} catch (TemplateException e) {
			throw new IOException("Failed to render template", e);
		}
	}

}
