package com.asksunny.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import freemarker.cache.TemplateLoader;

public class ClasspathTemplateLoader implements TemplateLoader {

	private Class<?> clazz;

	public ClasspathTemplateLoader(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Object findTemplateSource(String name) throws IOException {
		return name;
	}

	@Override
	public long getLastModified(Object templateSource) {		
		return System.currentTimeMillis() - 1000;
	}

	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {		
		InputStream in = this.clazz.getResourceAsStream(templateSource.toString());
		return new InputStreamReader(in, encoding);
	}

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {

	}
	
	
}
