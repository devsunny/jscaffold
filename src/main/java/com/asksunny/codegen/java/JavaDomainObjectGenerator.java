package com.asksunny.codegen.java;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.codegen.utils.ParamMapBuilder;
import com.asksunny.codegen.utils.TemplateUtil;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Field;
import com.asksunny.schema.parser.JdbcSqlTypeMap;


public class JavaDomainObjectGenerator extends CodeGenerator {

	public static final String JSON_FORMAT_IMPORT = "import com.fasterxml.jackson.annotation.JsonFormat;";

	public JavaDomainObjectGenerator(CodeGenConfig config, Entity entity) {
		super(config, entity);
	}

	public void doCodeGen() throws IOException {
		if (!configuration.isGenDomainObject()) {
			return;
		}

		StringBuilder declarations = new StringBuilder();
		StringBuilder methods = new StringBuilder();
		List<Field> fields = entity.getFields();
		for (Field field : fields) {
			String fmt = (field.isDatetimeField() && field.getFormat()!=null)?field.getFormat():null;
			String fmtanno =  field.isDatetimeField()?String.format("@JsonFormat(pattern=\"%s\")", fmt):"";
			declarations.append(TemplateUtil.renderTemplate(
					IOUtils.toString(getClass().getResourceAsStream("JavaDomainObjectDeclaration.java.templ")),
					ParamMapBuilder.newBuilder().addMapEntry("FIELD_TYPE", JdbcSqlTypeMap.toJavaTypeName(field))
					.addMapEntry("JSON_DATEFORMAT_ANNO", fmtanno)
							.addMapEntry("FIELD_VAR_NAME", field.getVarname()).buildMap())).append(NEW_LINE);
			
			methods.append(TemplateUtil.renderTemplate(
					IOUtils.toString(getClass().getResourceAsStream("JavaDomainObjectAccessor.java.templ")),
					ParamMapBuilder.newBuilder().addMapEntry("FIELD_TYPE", JdbcSqlTypeMap.toJavaTypeName(field))
					.addMapEntry("FIELD_NAME", field.getObjectname())
							.addMapEntry("FIELD_VAR_NAME", field.getVarname()).buildMap())).append(NEW_LINE);
		}

		
		String jsonFmtImport  = entity.hasDatetimeField()?JSON_FORMAT_IMPORT:"";
		String generated = TemplateUtil.renderTemplate(
				IOUtils.toString(getClass().getResourceAsStream("JavaDomainObject.java.templ")),
				ParamMapBuilder.newBuilder()
						.addMapEntry("DOMAIN_PACKAGE_NAME", configuration.getDomainPackageName())						
						.addMapEntry("FIELD_DECLARATIONS", declarations.toString())
						.addMapEntry("JSON_FORMAT_ANNO_IMPORT", jsonFmtImport)
						.addMapEntry("FIELD_ACCESSORS", methods.toString())
						.addMapEntry("ENTITY_VAR_NAME", entity.getEntityVarName())
						.addMapEntry("ENTITY_NAME", entity.getEntityObjectName())
						.buildMap());
		String filePath = configuration.getDomainPackageName().replaceAll("[\\.]", "/");
		writeCode(new File(configuration.getJavaBaseDir(), filePath),
				String.format("%s.java", entity.getEntityObjectName()), generated);

	}

	
}
