package com.asksunny.schema.generator;

import com.asksunny.schema.Field;

public class BinaryGenerator implements Generator<byte[]> {

	private int minValue;
	private int maxValue;
	private Field field;

	public BinaryGenerator(Field field) {
		this.field = field;
		this.minValue = field.getMinValue() == null ? 0 : Integer.valueOf(field.getMinValue());
		this.maxValue = field.getMaxValue() == null ? 1024 : Integer.valueOf(field.getMaxValue());
	}

	public BinaryGenerator(int minValue, int maxValue) {
		if (minValue < 0 || maxValue < 0) {
			throw new IllegalArgumentException("Unsigned int has to be positive value");
		}
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public BinaryGenerator(int maxValue) {
		if (maxValue < 0) {
			throw new IllegalArgumentException("Unsigned int has to be positive value");
		}
		this.minValue = 0;
		this.maxValue = maxValue;
	}

	public BinaryGenerator() {
		this.minValue = 0;
		this.maxValue = 1024;
	}

	public String nextStringValue() {
		byte[] out = nextValue();
		return out == null ? null : ("0x" + bytesToHex(out));
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public byte[] nextValue() {
		if (this.field.isNullable() && RandomUtil.getInstance().isOddEnough()) {
			return null;
		}
		int len = this.minValue + RandomUtil.getInstance().getUnsignedInt(this.maxValue - this.minValue);
		byte[] buf = new byte[len];
		RandomUtil.getInstance().getRandom().nextBytes(buf);
		return buf;
	}

}
