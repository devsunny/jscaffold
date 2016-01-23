package com.asksunny.schema.generator;

import com.asksunny.schema.Field;

public class UserNameGenerator implements Generator<String> {

	private Field field;

	public UserNameGenerator(Field field) {
		super();
		this.field = field;
	}

	@Override
	public String nextStringValue() {
		return nextValue();
	}

	@Override
	public String nextValue() {

		if (field.isNullable() && RandomUtil.getInstance().isOddEnough()) {
			return null;
		}

		return PersonNameUtils.getInstance().getFirstName().charAt(0) + PersonNameUtils.getInstance().getLastName();
	}

}
