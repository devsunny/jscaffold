package com.asksunny.codegen;

import static org.junit.Assert.*;

import org.junit.Test;

public class SSLCertificateGeneratorTest {

	@Test
	public void test() {
		CodeGenConfig cfg = new CodeGenConfig();
		SSLCertificateGenerator gen = new SSLCertificateGenerator(cfg);
		gen.doCodeGen();
	}
	
	
	

}
