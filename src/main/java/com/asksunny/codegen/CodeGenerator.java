package com.asksunny.codegen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.asksunny.codegen.CodeGenConfig.CodeOverwriteStrategy;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Field;
import com.asksunny.schema.Schema;
import com.asksunny.schema.parser.SQLScriptParser;

public abstract class CodeGenerator {

	protected static final String INDENDENT_1 = "    ";
	protected static final String INDENDENT_2 = "        ";
	protected static final String INDENDENT_3 = "            ";
	protected static final String INDENDENT_4 = "                ";
	protected static final String NEW_LINE = "\n";

	protected CodeGenConfig configuration;
	protected Entity entity;
	protected Schema schema;
	protected String keyParamURI = "";
	protected String keyStateParameters = "";

	protected File viewDir = null;
	protected File controllerDir = null;

	protected void loadSchema() throws IOException {
		String schemaFiles = configuration.getSchemaFiles();
		if (schemaFiles == null) {
			throw new IOException("Schema DDL file has not been specified");
		}
		String[] sfs = schemaFiles.split("\\s*[,;]\\s*");
		for (int i = 0; i < sfs.length; i++) {
			InputStream in = getClass().getResourceAsStream(String.format("/%s", sfs[i]));
			if (in == null) {
				in = new FileInputStream(sfs[i]);
			}
			try {
				SQLScriptParser parser = new SQLScriptParser(new InputStreamReader(in));
				if (configuration.isDebug()) {
					parser.setDebug(true);
				}
				Schema schemax = parser.parseSql();
				if (schema == null) {
					schema = schemax;
				} else {
					schema.getAllEntities().addAll(schemax.getAllEntities());
				}
			} finally {
				in.close();
			}
		}
	}

	public CodeGenerator(CodeGenConfig configuration, Entity entity) {
		super();
		this.configuration = configuration;
		this.entity = entity;
		genKeyParams();
		setupDirectories();
	}

	public CodeGenerator(CodeGenConfig configuration, Schema schema) {
		super();
		this.configuration = configuration;
		this.schema = schema;
		setupDirectories();
	}

	public abstract void doCodeGen() throws IOException;

	protected void setupDirectories() {
		viewDir = new File(configuration.getWebBaseSrcDir(), "views");
		controllerDir = new File(configuration.getWebBaseSrcDir(), "scripts/controllers");
		if (!viewDir.exists() && !viewDir.mkdirs()) {
			throw new RuntimeException("Permission denied to created directory " + viewDir.toString());
		}
		if (!controllerDir.exists() && !controllerDir.mkdirs()) {
			throw new RuntimeException("Permission denied to created directory " + controllerDir.toString());
		}
	}

	protected void genKeyParams() {
		if (entity == null) {
			return;
		}
		List<String> keyVars = new ArrayList<String>();
		StringBuilder keyUri = new StringBuilder();
		List<Field> keyFields = entity.getKeyFields();
		if (keyFields.size() == 0) {
			keyFields = entity.getUniqueFields();
		}
		if (keyFields.size() == 0)
			return;
		for (Field field : keyFields) {
			keyUri.append("/:").append(field.getVarName());
			keyVars.add(String.format("\"%s\":null", field.getVarName()));
		}

		keyParamURI = keyUri.toString();
		keyStateParameters = StringUtils.join(keyVars, ",\n");

	}

	protected void writeCode(File dir, String fileName, String code) throws IOException {

		File fj = new File(dir, fileName);
		if (configuration.getOverwriteStrategy() == CodeOverwriteStrategy.IGNORE && fj.exists()) {
			return;
		} else if (configuration.getOverwriteStrategy() == CodeOverwriteStrategy.RENAME_EXISTING && fj.exists()) {
			File newFile = null;
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				newFile = new File(dir, String.format("%s.%03d", fileName, i));
				if (!newFile.exists()) {
					break;
				}
			}
			if (!fj.renameTo(newFile)) {
				throw new IOException("Failed to rename existing file");
			}
		} else if (configuration.getOverwriteStrategy() == CodeOverwriteStrategy.OVERWRITE && fj.exists()) {
			//
		} else if (fj.exists()) {
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				fj = new File(dir, String.format("%s.%03d", fileName, i));
				if (!fj.exists()) {
					break;
				}
			}
		}

		if (fj.getParentFile().exists() == false && fj.getParentFile().mkdirs() == false) {
			throw new IOException("Permission denied to created directory:" + fj.getParentFile().toString());
		}

		FileWriter fw = new FileWriter(fj);
		try {
			fw.write(code);
			fw.flush();
		} finally {
			fw.close();
		}
	}

	public String getKeyParamURI() {
		return keyParamURI;
	}

	public String generateInterpolateURL(List<Field> fields) {
		List<String> parts = new ArrayList<String>();
		for (Field fd : fields) {
			parts.add(String.format("/{{%s}}", fd.getVarName()));
		}
		return StringUtils.join(parts, "");
	}

	public String getKeyStateParameters() {
		return keyStateParameters;
	}

	public String getClassTemplate(Class<?> neighbor, String sysid) throws IOException {
		String tmpl = "";
		InputStream bin = neighbor.getResourceAsStream(sysid);
		try {
			tmpl = IOUtils.toString(bin);
		} finally {
			bin.close();
		}
		return tmpl;
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

}
