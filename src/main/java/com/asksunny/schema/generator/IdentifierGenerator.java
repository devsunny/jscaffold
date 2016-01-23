package com.asksunny.schema.generator;

import com.asksunny.schema.Field;

public class IdentifierGenerator implements Generator<String> {

	private static final char[] LETTERS_ARRAY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private static final char[] CHAR_ARRAY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".toCharArray();
	private int size;
	private Field field;

	public IdentifierGenerator(Field field) {
		super();
		this.field = field;
		this.size = field.getDisplaySize();
		if (this.size == 0) {
			this.size = field.getPrecision();
		}		
		if (size == 0) {
			size = 8;
		}
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
		int gsize = field.getMaxValue()!=null?Integer.valueOf(field.getMaxValue()):field.getDisplaySize();		
		int i  = RandomUtil.getInstance().getUnsignedInt(gsize);
		StringBuilder buf = new StringBuilder();
		buf.append(LETTERS_ARRAY[ RandomUtil.getInstance().getUnsignedInt(LETTERS_ARRAY.length)]);
		for(int j=0; j<i; j++){
			buf.append(CHAR_ARRAY[ RandomUtil.getInstance().getUnsignedInt(CHAR_ARRAY.length)]);
		}		
		return buf.toString();
	}

}
