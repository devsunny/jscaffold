package com.asksunny.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asksunny.codegen.CodeGenAnnotation;
import com.asksunny.codegen.GroupFunction;
import com.asksunny.codegen.utils.JavaIdentifierUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Entity {

	private String name;

	private String label;

	private final List<Field> fields = new ArrayList<>();

	@JsonIgnore
	private String varname;
	@JsonIgnore
	private final Map<String, Field> fieldMaps = new HashMap<>();
	@JsonIgnore
	private int itemsPerPage = 10;
	@JsonIgnore
	private boolean ignoreView;
	@JsonIgnore
	private boolean ignoreRest;
	@JsonIgnore
	private boolean ignoreData;
	@JsonIgnore
	private String orderBy;
	@JsonIgnore
	private boolean readonly;
	@JsonIgnore
	private int viewOrder;
	@JsonIgnore
	private boolean usePrincipal;

	public String getName() {
		return name;
	}

	public Entity(String name) {
		super();
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Field> getFields() {
		ArrayList<Field> ff = new ArrayList<>(fields);
		Collections.sort(ff, new FieldOrderComparator());
		if (isReadonly()) {
			for (Field field : ff) {
				field.setReadonly(true);
			}
		}
		return ff;
	}

	public void addField(Field field) {
		field.setContainer(this);
		field.setFieldIndex(this.fields.size());
		if (field.getPrincipal() != null) {
			this.setUsePrincipal(true);
		}
		this.fields.add(field);
		this.fieldMaps.put(field.getName().toUpperCase(), field);
	}

	@JsonIgnore
	public Field getUniqueEnumField() {

		for (Field fd : fields) {
			if (fd.isUnqiueEnum()) {
				return fd;
			}
		}
		return null;
	}

	@JsonIgnore
	public boolean hasDatetimeField() {
		for (Field fd : fields) {
			if (fd.isDatetimeField()) {
				return true;
			}
		}
		return false;
	}

	@JsonIgnore
	public boolean hasUniqueField() {

		for (Field fd : fields) {
			if (fd.isUnique() && !fd.isPrimaryKey()) {
				return true;
			}
		}
		return false;
	}

	@JsonIgnore
	public List<Field> getUniqueFields() {
		List<Field> refs = new ArrayList<Field>();
		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.isUnique() && !fd.isPrimaryKey()) {
					refs.add(fd);
				}
			}
		}
		return refs;
	}

	@JsonIgnore
	public boolean hasKeyField() {

		for (Field fd : fields) {
			if (fd.isUnique()) {
				return true;
			}
		}
		return false;
	}

	public List<Field> getKeyFields() {
		List<Field> refs = new ArrayList<Field>();
		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.isPrimaryKey()) {
					refs.add(fd);
				}

			}
		}
		return refs;
	}

	@JsonIgnore
	public Field getKeyField() {

		for (Field fd : fields) {
			if (fd.isPrimaryKey()) {
				return fd;
			}
		}
		return null;
	}

	public void setFields(List<Field> pfields) {

		for (Field fd : pfields) {
			fd.setContainer(this);
			fd.setFieldIndex(this.fields.size());
			this.fieldMaps.put(fd.getName().toUpperCase(), fd);
		}
		this.fields.addAll(pfields);
	}

	@JsonIgnore
	public Field findField(String name) {
		if (name != null) {
			return fieldMaps.get(name.trim().toUpperCase());
		}
		return null;
	}

	@JsonIgnore
	public List<Field> getGroupByFields() {
		List<Field> refs = new ArrayList<Field>();
		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.getGroupLevel() > 0) {
					refs.add(fd);
				}
			}
		}
		Collections.sort(refs, new FieldGroupLevelComparator());
		return refs;
	}

	@JsonIgnore
	public boolean hasGroupByFields() {

		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.getGroupLevel() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@JsonIgnore
	public List<Field> getDrillDownFields() {
		List<Field> refs = new ArrayList<Field>();
		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.getDrillDown() > 0) {
					refs.add(fd);
				}
			}
		}
		Collections.sort(refs, new FieldDrillDownComparator());
		return refs;
	}

	@JsonIgnore
	public Field getDrillDownRoot() {
		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.getDrillDown() == 1) {
					return fd;
				}
			}
		}
		return null;
	}

	@JsonIgnore
	public boolean hasDrillDownFields() {

		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.getDrillDown() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@JsonIgnore
	public List<Field> getAllReferences() {
		List<Field> refs = new ArrayList<Field>();
		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.reference != null) {
					if (!fd.reference.getReferencedBy().contains(fd)) {
						fd.reference.addReferencedBy(fd);
					}
					refs.add(fd.reference);
				}
			}
		}
		return refs;
	}

	@JsonIgnore
	public boolean hasReferencedBy() {
		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.getReferencedBy() != null && fd.getReferencedBy().size() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@JsonIgnore
	public boolean hasMultiReferencedBy() {
		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.getReferencedBy() != null && fd.getReferencedBy().size() > 1) {
					return true;
				}
			}
		}
		return false;
	}

	@JsonIgnore
	public boolean hasReference() {
		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.reference != null) {
					return true;
				}
			}
		}
		return false;
	}

	@JsonIgnore
	@Override
	public String toString() {
		return "Entity [name=" + name + ", fields=\n" + fields + "\n]";
	}

	@JsonIgnore
	public String getEntityObjectName() {
		return this.varname == null ? JavaIdentifierUtil.toObjectName(this.name)
				: JavaIdentifierUtil.capitalize(this.varname);
	}

	@JsonIgnore
	public String getEntityVarName() {
		return varname == null ? JavaIdentifierUtil.toVariableName(this.name) : varname;
	}

	public Map<String, Field> getFieldMaps() {
		return fieldMaps;
	}

	@JsonIgnore
	public String getLabel() {
		return label == null ? getEntityObjectName() : label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@JsonIgnore
	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public void setItemsPerPage(String itemsPerPageStr) {
		if (itemsPerPageStr != null && itemsPerPageStr.matches("^\\d+$")) {
			this.itemsPerPage = Integer.valueOf(itemsPerPageStr);
		}
	}

	@JsonIgnore
	public boolean isIgnoreView() {
		return ignoreView;
	}

	public void setIgnoreView(boolean ignoreView) {
		this.ignoreView = ignoreView;
	}

	public void setIgnoreView(String ignoreViewstr) {
		this.ignoreView = ignoreViewstr != null && ignoreViewstr.equalsIgnoreCase("true");
	}

	@JsonIgnore
	public boolean isIgnoreRest() {
		return ignoreRest;
	}

	public void setIgnoreRest(boolean ignoreRest) {
		this.ignoreRest = ignoreRest;
	}

	public void setIgnoreRest(String ignoreRestStr) {
		this.ignoreRest = ignoreRestStr != null && ignoreRestStr.equalsIgnoreCase("true");
	}

	@JsonIgnore
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setAnnotation(CodeGenAnnotation anno) {

		if (anno.getLabel() != null) {
			this.setLabel(anno.getLabel());
		}
		if (anno.getVarname() != null) {
			this.setVarname(anno.getVarname());
		}
		if (anno.getIgnoreRest() != null) {
			this.setIgnoreRest(anno.getIgnoreRest());
		}
		if (anno.getItemsPerPage() != null) {
			this.setItemsPerPage(anno.getItemsPerPage());
		}
		if (anno.getIgnoreView() != null) {
			this.setIgnoreView(anno.getIgnoreView());
		}
		if (anno.getOrderBy() != null) {
			this.setOrderBy(anno.getOrderBy());
		}
		this.setIgnoreData(anno.getIgnoreData());
		this.setReadonly(anno.getReadonly());
		this.setViewOrder(anno.getOrder());
	}

	@JsonIgnore
	public String getVarname() {
		return varname == null ? JavaIdentifierUtil.toVariableName(name) : varname;
	}

	public void setVarname(String varname) {
		this.varname = JavaIdentifierUtil.decapitalize(varname);
	}

	@JsonIgnore
	public Field getGroupFunctionField() {
		if (this.fields != null) {
			for (Field fd : this.fields) {
				if (fd.getGroupFunction() != GroupFunction.NONE) {
					return fd;
				}
			}
		}
		return null;
	}

	@JsonIgnore
	public boolean isIgnoreData() {
		return ignoreData;
	}

	public void setIgnoreData(boolean ignoreData) {
		this.ignoreData = ignoreData;
	}

	public void setIgnoreData(String ignoreDatastr) {
		this.ignoreData = ignoreDatastr != null && ignoreDatastr.equalsIgnoreCase("true");
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly != null && readonly.equalsIgnoreCase("true");
	}

	public int getViewOrder() {
		return viewOrder;
	}

	public void setViewOrder(int viewOrder) {
		this.viewOrder = viewOrder;
	}

	public void setViewOrder(String viewOrder) {
		this.viewOrder = viewOrder != null ? Integer.valueOf(viewOrder) : 10;
	}

	public boolean isUsePrincipal() {
		return usePrincipal;
	}

	public void setUsePrincipal(boolean usePrincipal) {
		this.usePrincipal = usePrincipal;
	}
}
