package com.asksunny.codegen;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class SSLCertificateGeneratorTest {

	@Test
	public void test() throws IOException{
		CodeGenConfig cfg = new CodeGenConfig();
		SSLCertificateGenerator gen = new SSLCertificateGenerator(cfg);
		gen.doCodeGen();
	}
	
	
	

}
