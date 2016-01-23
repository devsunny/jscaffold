package com.asksunny.codegen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.asksunny.schema.Schema;

/**
 * 
 * Using JDK keytool for selfsigned certificate can reduce dependency of Bouncy castle, openSSL etc.
 * This not Java native way too it, however it does not happen offten.
 * 
 * 
 * <pre>
 * for server use
 * "CN=myweb.domain.com, OU=R&D, O=Company Ltd., L=New York City, S=NY, C=US" 
 * 
 * keytool -genkey -keypass "changeit" -dname "CN=Sample Cert, OU=R&D, O=Company Ltd., L=New York City, S=NY, C=US" 
 *         -keyalg RSA -alias myserver -keystore selfsigned.jks -validity 3650  -keysize 2048 -storepass "changeit"
 *         
 * keytool -v -list -keystore selfsigned.jks -storepass "changeit"
 * 
 * Export public key
 * keytool -export -keystore examplestore -alias signFiles -file Example.cer
 * 
 * keytool -import -alias foo -file certfile.cer -keystore publicKey.store
 * 
 * 
 * 
 * </pre>
 * 
 * @author SunnyLiu
 *
 */

public class SSLCertificateGenerator extends CodeGenerator {

	public static final String SIGNATURE_ALGORITHM = "SHA256WithRSAEncryption";
	public static final String KEY_GENERATION_ALGORITHM = "RSA";
	public static final boolean REGENERATE_FRESH_CA_CERTIFICATE = false;
	public static final int ROOT_KEYSIZE = 2048;
	/**
	 * Current time minus 1 year
	 */
	public static final Date NOT_BEFORE = new Date(System.currentTimeMillis() - 86400000L * 365);

	/**
	 * Hundred years in the future.
	 */
	public static final Date NOT_AFTER = new Date(System.currentTimeMillis() + 86400000L * 365 * 100);

	public SSLCertificateGenerator(CodeGenConfig configuration, Schema schema) {
		super(configuration, schema);
	}

	public SSLCertificateGenerator(CodeGenConfig configuration) {
		super(configuration, (Schema) null);
	}

	public void doCodeGen() {
		String javaHome = System.getProperty("java.home");

		File keytool = File.pathSeparatorChar == ';' ? new File(javaHome, "bin/keytool.exe")
				: new File(javaHome, "bin/keytool");
		System.out.println(keytool);

		try {
			ProcessBuilder builder = new ProcessBuilder(keytool.toString());
			builder.redirectErrorStream(true);
			Process p = builder.start();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			IOUtils.copy(p.getInputStream(), bout);
			System.out.println(new String(bout.toByteArray()));
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
