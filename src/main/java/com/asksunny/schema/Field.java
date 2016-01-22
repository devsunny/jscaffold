package com.asksunny.schema;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.asksunny.codegen.CodeGenAnnotation;
import com.asksunny.codegen.CodeGenType;
import com.asksunny.codegen.GroupFunction;
import com.asksunny.codegen.GroupView;
import com.asksunny.codegen.utils.JavaIdentifierUtil;
import com.asksunny.schema.parser.JdbcSqlTypeMap;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Field {

	
	int scale;
	int precision;
	int displaySize;
	boolean nullable;
	boolean primaryKey;
	String name;
	String label;
	String dbTypeName;
	int fieldIndex;
	boolean unique;

	@JsonIgnore
	Entity container;
	@JsonIgnore
	int jdbcType;
	
	@JsonIgnore
	String varname;

	@JsonIgnore
	String format;
	@JsonIgnore
	String maxValue;
	@JsonIgnore
	String minValue;
	@JsonIgnore
	String step;

	@JsonIgnore
	CodeGenType dataType;

	@JsonIgnore
	String enumValues;
	@JsonIgnore
	String uitype;

	@JsonIgnore
	List<Field> referencedBy = new ArrayList<Field>();
	@JsonIgnore
	Field reference;

	@JsonIgnore
	int groupLevel;
	@JsonIgnore
	int drillDown;
	@JsonIgnore
	int order;
	@JsonIgnore
	boolean ignoreView;
	@JsonIgnore
	boolean autogen;
	@JsonIgnore
	GroupFunction groupFunction = GroupFunction.NONE;
	@JsonIgnore
	GroupView groupView = GroupView.TABLE;

	public Field() {
		super();
	}

	public Field(int jdbcType, int scale, int precision, int displaySize, boolean nullable, String name,
			CodeGenType dataType, String minValue, String maxValue, String format, String step) {
		super();
		this.jdbcType = jdbcType;
		this.scale = scale;
		this.precision = precision;
		this.displaySize = displaySize;
		this.nullable = nullable;
		this.name = name;
		this.dataType = dataType;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
		this.step = step;
	}

	public String getJdbcTypeName() {
		return JdbcSqlTypeMap.getJdbcTyepName(getJdbcType());
	}

	public int getFieldIndex() {
		return fieldIndex;
	}

	public void setFieldIndex(int fieldIndex) {
		this.fieldIndex = fieldIndex;
	}

	public static Field newField(int jdbcType, int scale, int precision, int displaySize, boolean nullable, String name,
			CodeGenType dataType, String minValue, String maxValue, String format, String step) {
		return new Field(jdbcType, scale, precision, displaySize, nullable, name, dataType, minValue, maxValue, format,
				step);
	}

	public boolean isNumericField() {
		return this.jdbcType == Types.BIGINT || this.jdbcType == Types.INTEGER || this.jdbcType == Types.SMALLINT
				|| this.jdbcType == Types.TINYINT || this.jdbcType == Types.DOUBLE || this.jdbcType == Types.FLOAT
				|| this.jdbcType == Types.REAL || this.jdbcType == Types.DECIMAL || this.jdbcType == Types.NUMERIC;
	}

	public List<Field> getReferencedBy() {
		return referencedBy;
	}

	public void addReferencedBy(List<Field> referencedBy) {
		this.referencedBy.addAll(referencedBy);
	}

	public void addReferencedBy(Field referencedBy) {
		this.referencedBy.add(referencedBy);
	}

	public Field getReference() {
		return reference;
	}

	public void setReference(Field reference) {
		this.reference = reference;
	}

	public int getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(int jdbcType) {
		this.jdbcType = jdbcType;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getDisplaySize() {
		return displaySize;
	}

	public void setDisplaySize(int displaySize) {
		this.displaySize = displaySize;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CodeGenType getDataType() {
		return dataType;
	}

	public void setDataType(CodeGenType dataType) {
		this.dataType = dataType;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public boolean isUnqiueEnum() {
		return getDataType() == CodeGenType.ENUM && (isPrimaryKey() || isUnique());
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public Entity getContainer() {
		return container;
	}

	public void setContainer(Entity container) {
		this.container = container;
	}

	public boolean isDatetimeField() {
		if (getJdbcType() == Types.DATE || getJdbcType() == Types.TIME || getJdbcType() == Types.TIMESTAMP) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (this.referencedBy != null) {
			for (Field fd : this.referencedBy) {
				sb.append(String.format("%s.%s", fd.getContainer().getName(), fd.getName())).append(", ");
			}
		}
		if (sb.length() > 2) {
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");
		return "Field [container=" + container.getName() + ", name=" + name + ", fieldIndex=" + fieldIndex
				+ ", jdbcType=" + jdbcType + ", precision=" + precision + ", scale=" + scale + ", displaySize="
				+ displaySize + ", nullable=" + nullable + ", dataType=" + dataType + ", format=" + format
				+ ", minValue=" + minValue + ", maxValue=" + maxValue + ", reference=" + reference + ", step=" + step
				+ ", referencedBy=" + sb.toString() + "]\n";
	}

	public String getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(String enumValues) {
		this.enumValues = enumValues;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getVarname() {
		return varname == null ? JavaIdentifierUtil.toVariableName(name) : varname;
	}

	public String getObjectname() {
		return varname == null ? JavaIdentifierUtil.toObjectName(name) : JavaIdentifierUtil.capitalize(varname);
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}

	public String getLabel() {
		return label == null ? getObjectname() : label;
	}

	public void setLabel(String uiname) {
		this.label = uiname;
	}

	public boolean isUnique() {
		return unique || this.primaryKey;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getUitype() {
		return uitype;
	}

	public void setUitype(String uitype) {
		this.uitype = uitype;
	}

	public int getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(int groupLevel) {
		this.groupLevel = groupLevel;
	}

	public void setGroupLevel(String groupLevelstr) {

		if (groupLevelstr != null && groupLevelstr.matches("^\\d+$")) {
			this.groupLevel = Integer.valueOf(groupLevelstr);
		}

	}

	public GroupFunction getGroupFunction() {
		return groupFunction;
	}

	public void setGroupFunction(GroupFunction groupFunction) {
		this.groupFunction = groupFunction;
	}

	public void setGroupFunction(String groupFunctionStr) {
		if (groupFunctionStr != null && groupFunctionStr.trim().length() > 0) {
			this.groupFunction = GroupFunction.valueOf(groupFunctionStr.trim().toUpperCase());
		}

	}

	public GroupView getGroupView() {
		return groupView;
	}

	public void setGroupView(GroupView groupView) {
		this.groupView = groupView;
	}

	public void setGroupView(String groupViewstr) {

		if (groupViewstr != null && groupViewstr.trim().length() > 0) {
			this.groupView = GroupView.valueOf(groupViewstr.trim().toUpperCase());
		}
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setOrder(String orderstr) {
		if (orderstr != null && orderstr.matches("^\\d+$")) {
			this.order = Integer.valueOf(orderstr);
		}

	}

	public boolean isIgnoreView() {
		return ignoreView;
	}

	public void setIgnoreView(boolean ignoreView) {
		this.ignoreView = ignoreView;
	}

	public void setIgnoreView(String ignoreViewstr) {
		this.ignoreView = ignoreViewstr != null && ignoreViewstr.trim().equalsIgnoreCase("true");
	}

	public boolean isAutogen() {
		return autogen;
	}

	public void setAutogen(boolean autogen) {
		this.autogen = autogen;
	}

	public void setAutogen(String autogenstr) {
		this.autogen = autogenstr != null && autogenstr.trim().equalsIgnoreCase("true");
		;
	}

	public void setAnnotation(CodeGenAnnotation anno) {
		this.setEnumValues(anno.getEnumValues());
		this.setFormat(anno.getFormat());
		this.setLabel(anno.getLabel());
		this.setMaxValue(anno.getMaxValue());
		this.setMinValue(anno.getMinValue());
		this.setStep(anno.getStep());
		this.setUitype(anno.getUitype());
		this.setVarname(anno.getVarname());
		this.setIgnoreView(anno.getIgnoreView());
		this.setOrder(anno.getOrder());
		this.setGroupFunction(anno.getGroupFunction());
		this.setGroupLevel(anno.getGroupLevel());
		this.setGroupView(anno.getGroupView());
		this.setAutogen(anno.getAutogen());
		this.setDrillDown(anno.getDrillDown());
	}

	public int getDrillDown() {
		return drillDown;
	}

	public void setDrillDown(int drillDown) {
		this.drillDown = drillDown;
	}

	public void setDrillDown(String drillDownStr) {
		if (drillDownStr != null && drillDownStr.matches("^\\d+$")) {
			this.drillDown = Integer.valueOf(drillDownStr);
		}
	}

	public String getDbTypeName() {
		return dbTypeName;
	}

	public void setDbTypeName(String dbTypeName) {
		this.dbTypeName = dbTypeName;
	}

}
