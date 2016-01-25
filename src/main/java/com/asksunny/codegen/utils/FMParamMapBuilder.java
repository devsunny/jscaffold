package com.asksunny.codegen.utils;

import java.util.HashMap;
import java.util.Map;

public class FMParamMapBuilder {

	public static FMParamMapBuilder newBuilder() {
		return new FMParamMapBuilder();
	}

	public FMParamMapBuilder addMapEntry(String key, Object value) {
		params.put(key, value);
		return this;
	}

	public FMParamMapBuilder addMapEntry(String[] pairs) {
		if (pairs.length >= 2) {
			for (int i = 0; i < pairs.length; i = i + 2) {
				String key = pairs[i];
				String value = (i + 1 < pairs.length) ? pairs[i + 1] : null;
				params.put(key, value);
			}
		}
		return this;
	}

	public Map<String, Object> buildMap() {
		return params;
	}

	Map<String, Object> params;

	public FMParamMapBuilder() {
		params = new HashMap<>();
	}

}
