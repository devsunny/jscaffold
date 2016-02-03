package com.asksunny.codegen.java;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

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
		StringBuilder methods = new StringBuilder();		
		doGroupBy(methods);
		doDrillDown(methods);		
		try {
			Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);		
			cfg.setClassForTemplateLoading(getClass(), "");			
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			Template temp = cfg.getTemplate("SpringRestJavaController.java.ftl", Locale.US);
			StringWriter out = new StringWriter();
			temp.process(FMParamMapBuilder.newBuilder()
					.addMapEntry("MAPPER_PACKAGE_NAME", configuration.getMapperPackageName())
					.addMapEntry("DOMAIN_PACKAGE_NAME", configuration.getDomainPackageName())
					.addMapEntry("REST_PACKAGE_NAME", configuration.getRestPackageName())
					.addMapEntry("entity", entity)
					.addMapEntry("MORE_REST_METHODS", methods.toString())
					.addMapEntry("ENTITY_VAR_NAME", entity.getVarName())
					.addMapEntry("ENTITY_NAME", entity.getObjectName())
					.addMapEntry("ENTITY_LABEL", entity.getLabel()).buildMap(), out);
			out.flush();
			String filePath = configuration.getRestPackageName().replaceAll("[\\.]", "/");
			writeCode(new File(configuration.getJavaBaseDir(), filePath),
					String.format("%sRestController.java", entity.getObjectName()), out.toString());
			
			if(configuration.isGenJunit()){
				 out = new StringWriter();
				 temp = cfg.getTemplate("SpringRestJavaControllerTest.java.ftl", Locale.US);
				 temp.process(FMParamMapBuilder.newBuilder()
						.addMapEntry("MAPPER_PACKAGE_NAME", configuration.getMapperPackageName())
						.addMapEntry("DOMAIN_PACKAGE_NAME", configuration.getDomainPackageName())
						.addMapEntry("REST_PACKAGE_NAME", configuration.getRestPackageName())
						.addMapEntry("WEBAPP_CONTEXT", configuration.getWebappContext())	
						.addMapEntry("entity", entity)						
						.buildMap(), out);
				out.flush();				
				writeCode(new File(configuration.getJunitBaseDir(), filePath),
						String.format("%sRestControllerTest.java", entity.getObjectName()), out.toString());				
				
			}			
		} catch (TemplateException e) {
			throw new IOException("Failed to render template", e);
		}
	}


	protected void doGroupBy(StringBuilder methods) throws IOException {
		if (!entity.isHasGroupByFields()) {
			return;
		}
		List<Field> gbFields = entity.getGroupByFields();
		Collections.sort(gbFields, new FieldGroupLevelComparator());
		StringBuilder uri = new StringBuilder();
		uri.append("/groupby");
		for (Field kf : gbFields) {
			uri.append("/{").append(kf.getVarName()).append("}");
		}
		methods.append(String.format("%1$s@RequestMapping(value=\"%2$s\", method = { RequestMethod.GET })\n",
				INDENDENT_2, uri.toString()));
		methods.append(INDENDENT_2).append("@ResponseBody\n");
		if (gbFields.size() == 1) {
			Field keyField = gbFields.get(0);
			methods.append(String.format("%2$spublic java.util.List<%1$s> select%1$sGroupBy%3$s(%5$s %4$s){\n",
					entity.getObjectName(), INDENDENT_2, keyField.getObjectName(), keyField.getVarName(),
					JdbcSqlTypeMap.toJavaTypeName(keyField)));
			methods.append(String.format("%2$s return this.%6$sMapper.select%1$sGroupBy%3$s(%4$s);\n",
					entity.getObjectName(), INDENDENT_2, keyField.getObjectName(), keyField.getVarName(),
					JdbcSqlTypeMap.toJavaTypeName(keyField), entity.getVarName()));

		} else if (gbFields.size() > 1) {
			StringBuilder buf = new StringBuilder();
			for (Field keyField : gbFields) {
				buf.append(keyField.getObjectName());
			}
			methods.append(String.format("%1$spublic java.util.List<%2$s> select%2$sGroupBy%4$s(%2$s %3$s){\n",
					INDENDENT_2, entity.getObjectName(), entity.getVarName(), buf.toString()));

			methods.append(String.format("%1$s return this.%3$sMapper.select%2$sGroupBy%4$s(%3$s){\n", INDENDENT_2,
					entity.getObjectName(), entity.getVarName(), buf.toString()));

		}
		methods.append(INDENDENT_2).append("}\n\n");
	}

	protected void doDrillDown(StringBuilder methods) throws IOException {
		if (!entity.hasDrillDownFields()) {
			return;
		}
		List<Field> ddFields = entity.getDrillDownFields();
		Collections.sort(ddFields, new FieldDrillDownComparator());

		StringBuilder restPath = new StringBuilder();
		restPath.append("/drilldown");
		for (int i = 0; i <= ddFields.size(); i++) {
			// Field dd0 = ddFields.get(i);
			String drilldownName = (i == ddFields.size()) ? "Detail"
					: String.format("By%s", ddFields.get(i).getObjectName());
			if (i > 0) {
				restPath.append(String.format("/{%s}", ddFields.get(i - 1).getVarName()));
			}
			String drillDownRestPath = restPath.toString();
			List<String> params = new ArrayList<String>();
			for (int k = 1; k <= i; k++) {
				Field ddk = ddFields.get(k - 1);
				params.add(String.format("@PathVariable(\"%2$s\")%1$s %2$s", JdbcSqlTypeMap.toJavaTypeName(ddk),
						ddk.getVarName()));
			}
			String paramstr = StringUtils.join(params, ", ");
			methods.append(String.format("%1$s@RequestMapping(value=\"%2$s\", method = { RequestMethod.GET })\n",
					INDENDENT_2, drillDownRestPath));
			methods.append(INDENDENT_2).append("@ResponseBody\n");
			methods.append(String.format("%1$s public java.util.List<%2$s> select%2$sDrilldown%4$s(%5$s){\n",
					INDENDENT_2, entity.getObjectName(), entity.getVarName(), drilldownName, paramstr));

			methods.append(String.format("%1$s %2$s %3$s = new %2$s();\n", INDENDENT_2, entity.getObjectName(),
					entity.getVarName()));
			for (int k = 1; k <= i; k++) {
				Field ddk = ddFields.get(k - 1);
				methods.append(String.format("%1$s %3$s.set%4$s(%5$s);\n", INDENDENT_2, entity.getObjectName(),
						entity.getVarName(), ddk.getObjectName(), ddk.getVarName()));
			}
			methods.append(String.format("%1$s return this.%3$sMapper.select%2$sDrilldown%4$s(%3$s);\n", INDENDENT_2,
					entity.getObjectName(), entity.getVarName(), drilldownName));
			methods.append(INDENDENT_2).append("}\n\n");

		}
	}

}
