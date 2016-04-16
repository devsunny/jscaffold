package com.asksunny.schema.generator;

import com.asksunny.schema.Field;

public class UserNameGenerator implements Generator<String> {

	private Field field;
	private int maxLength = 1;

	public UserNameGenerator(Field field) {
		super();
		this.field = field;
		maxLength = field.getMaxValue()!=null?Integer.valueOf(field.getMaxValue()):field.getDisplaySize();
		
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
		String text = PersonNameUtils.getInstance().getFirstName().charAt(0) + PersonNameUtils.getInstance().getLastName();
		if(text.length()>maxLength){
			text = text.substring(0, maxLength);
		}
		return text;
	}

}
