package com.asksunny.schema.generator;

import com.asksunny.schema.Field;

public class EnumGenerator implements Generator<String> {

	private Field field;
	private String[] enumValues;
	private int enumPos = 0;

	public EnumGenerator(Field field) {
		super();
		this.field = field;
		if (this.field.getEnumValues() != null) {
			enumValues = this.field.getEnumValues().split("\\s*\\|\\s*");
		}
	}

	@Override
	public String nextStringValue() {
		return nextValue();
	}

	@Override
	public String nextValue() {
		if ((this.field.isNullable() && RandomUtil.getInstance().isOddEnough()) || enumValues == null) {
			return null;
		}
		if (field.isUnqiueEnum()) {
			return enumValues[enumPos++%enumValues.length];
		} else {
			int idx = RandomUtil.getInstance().getUnsignedInt(enumValues.length);
			return enumValues[idx];
		}
	}

}
