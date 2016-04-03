package com.asksunny.codegen.angular;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenConfig.CodeOverwriteStrategy;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.codegen.TemplateRender;
import com.asksunny.codegen.TypesBean;
import com.asksunny.codegen.utils.ParamMapBuilder;
import com.asksunny.codegen.utils.TemplateUtil;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Schema;

public class AngularUIGenerator extends CodeGenerator {

	File appJsDir = null;
	File navigationDir = null;
	File controllerDir = null;
	File viewDir = null;

	public AngularUIGenerator(CodeGenConfig configuration, Schema schema) {
		super(configuration, schema);
		appJsDir = new File(configuration.getWebBaseSrcDir(), "scripts");
		navigationDir = new File(configuration.getWebBaseSrcDir(), "scripts/directives/sidebar");
		controllerDir = new File(configuration.getWebBaseSrcDir(), "scripts/controllers");
		viewDir = new File(configuration.getWebBaseSrcDir(), "views");
	}

	@Override
	public void doCodeGen() throws IOException {
		if (!configuration.isGenAngular()) {
			return;
		}
		expandTemplate();
		genStates();
	}

	protected void genStates() throws IOException {

		StringBuilder states = new StringBuilder();
		StringBuilder navigations = new StringBuilder();
		List<Entity> entities = schema.getAllEntities();
		for (Entity entity : entities) {
			if (configuration.getIncludes().size() > 0 && !configuration.shouldInclude(entity.getName())) {
				continue;
			}
			if (configuration.shouldIgnore(entity.getName())) {
				continue;
			}
			if (entity.isIgnoreView()) {
				continue;
			} else {
				genEntityForm(states, navigations, entity);
			}
		}
		String appjs = TemplateUtil.renderTemplate(
				IOUtils.toString(getClass().getResourceAsStream("angularApp.js.tmpl")),
				ParamMapBuilder.newBuilder().addMapEntry("APPLICATION_STATES", states.toString()).buildMap());
		writeCode(appJsDir, "app.js", appjs);

		String navgiator = TemplateUtil.renderTemplate(
				IOUtils.toString(getClass().getResourceAsStream("AngularNavigation.html.tmpl")),
				ParamMapBuilder.newBuilder().addMapEntry("APP_NAVIGATIONS", navigations.toString()).buildMap());
		writeCode(navigationDir, "sidebar.html", navgiator);

		String generated = TemplateRender.newInstance().setLoaderClass(getClass())
				.setTemplate("index.html.ftl", Locale.US).addTemplateParam("config", configuration).renderTemplate();
		writeCode(new File(configuration.getWebBaseSrcDir()), "index.html", generated);

		String html = TemplateUtil.renderTemplate(
				IOUtils.toString(getClass().getResourceAsStream("JScaffold.html.tmpl")),
				ParamMapBuilder.newBuilder().addMapEntry("APP_NAVIGATIONS", navigations.toString()).buildMap());
		writeCode(viewDir, "JScaffold.html", html);

		html = TemplateUtil.renderTemplate(IOUtils.toString(getClass().getResourceAsStream("JScaffoldCtrl.js.templ")),
				ParamMapBuilder.newBuilder().addMapEntry("APP_NAVIGATIONS", navigations.toString()).buildMap());
		writeCode(controllerDir, "JScaffoldCtrlController.js", html);

	}

	protected void genEntityForm(StringBuilder states, StringBuilder navigations, Entity entity) throws IOException {
		AngularEntityFormGenerator formGen = new AngularEntityFormGenerator(configuration, entity);
		formGen.doCodeGen();
		states.append(formGen.genAngularState());
		AngularEntityListGenerator listGen = new AngularEntityListGenerator(configuration, entity);
		listGen.doCodeGen();
		states.append(listGen.genAngularState());
		navigations.append(listGen.genNavigationItem());

	}

	protected void expandTemplate() throws IOException {
		File webappPath = new File(configuration.getWebBaseSrcDir());
		ZipInputStream zipin = new ZipInputStream(getClass().getResourceAsStream("/sbadmin-template.zip"));
		try {
			ZipEntry entry = null;
			while ((entry = zipin.getNextEntry()) != null) {
				File path = new File(webappPath, entry.getName());
				if (path.exists() || entry.isDirectory()) {
					continue;
				}
				if (!path.getParentFile().exists() && !path.getParentFile().mkdirs()) {
					throw new IOException("Failed to extract template, permission denied:" + path.getParentFile());
				}

				FileOutputStream fout = new FileOutputStream(path);
				try {
					IOUtils.copy(zipin, fout);
				} finally {
					fout.close();
				}
			}
		} finally {
			zipin.close();
		}

	}

}
