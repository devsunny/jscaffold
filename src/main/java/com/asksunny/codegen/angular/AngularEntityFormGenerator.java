package com.asksunny.codegen.angular;

import java.io.IOException;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.codegen.TemplateRender;
import com.asksunny.codegen.utils.ParamMapBuilder;
import com.asksunny.codegen.utils.TemplateUtil;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Field;

public class AngularEntityFormGenerator extends CodeGenerator {

	public AngularEntityFormGenerator(CodeGenConfig configuration, Entity entity) {
		super(configuration, entity);
	}

	@Override
	public void doCodeGen() throws IOException {
		writeCode(this.viewDir, String.format("%sForm.html", entity.getObjectName()), genForm());
		writeCode(this.controllerDir, String.format("%sFormController.js", entity.getObjectName()),
				genFormController());
	}

	// gen form Controller
	// gen save function
	// gen delete function if key exist

	public String genForm() throws IOException {
		StringBuilder fields = new StringBuilder();
		for (Field field : entity.getFields()) {
			AngularEntityFieldGenerator fg = new AngularEntityFieldGenerator(entity, field);
			fields.append(fg.genField());
		}
		String generated = TemplateRender.newInstance().setLoaderClass(AngularEntityFormGenerator.class)
				.setTemplate("angularEntityForm.html_en_US.ftl", Locale.US).addTemplateParam("config", configuration)
				.addTemplateParam("entity", entity).addTemplateParam("FORM_FIELDS", fields.toString()).renderTemplate();
		return generated;
	}

	public String genNavigationItem() throws IOException {
		String label = entity.getLabel() == null ? entity.getName() : entity.getLabel();
		String generated = TemplateUtil.renderTemplate(
				IOUtils.toString(getClass().getResourceAsStream("AngularNavigationItem.html.tmpl")),
				ParamMapBuilder.newBuilder().addMapEntry("ANGULAR_APP_NAME", configuration.getAngularAppName())
						.addMapEntry("NAVIGATION_STATE_NAME", entity.getVarName())
						.addMapEntry("ENTITY_NAME", entity.getObjectName()).addMapEntry("NAVIGATION_LABEL", label)
						.buildMap());
		return generated;
	}

	public String genFormController() throws IOException {
		String generated = TemplateRender.newInstance().setLoaderClass(AngularEntityFormGenerator.class)
				.setTemplate("angularEntityFormController.js_en_US.ftl", Locale.US)
				.addTemplateParam("config", configuration).addTemplateParam("entity", entity).renderTemplate();
		return generated;
	}

	public String genAngularState() throws IOException {

		Field kf = entity.getKeyField();
		String pkName = kf == null ? "uniqueId" : kf.getVarName();

		String generated = TemplateUtil
				.renderTemplate(
						IOUtils.toString(
								getClass()
										.getResourceAsStream(
												"angularEntityFormState.js.tmpl")),
						ParamMapBuilder.newBuilder().addMapEntry("ANGULAR_APP_NAME", configuration.getAngularAppName())
								.addMapEntry("KEY_PARAMS", getKeyStateParameters())
								.addMapEntry("KEY_URI_PARAMS", getKeyParamURI())
								.addMapEntry("PK_FIELD_VAR_NAME", pkName).addMapEntry("VIEW_NAME", entity.getVarName())
								.addMapEntry("ENTITY_VAR_NAME", entity.getVarName())
								.addMapEntry("ENTITY_NAME", entity.getObjectName())
								.addMapEntry("ENTITY_LABEL", entity.getLabel()).buildMap());
		return generated;
	}

}
