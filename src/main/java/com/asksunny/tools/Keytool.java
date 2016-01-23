package com.asksunny.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * This is shim wrapper for Oracle Java Keytool
 * 
 * <pre>
 * for server use
 * "CN=myweb.domain.com, OU=R&D, O=Company Ltd., L=New York City, S=New York, C=US" 
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
public class Keytool {

	public static final boolean WINDOWS = File.pathSeparatorChar == ';';
	public static final String JAVA_HOME = System.getProperty("java.home");
	private final String keytoolPath;

	public Keytool() {
		File keytoolFile = WINDOWS ? new File(JAVA_HOME, "bin/keytool.exe") : new File(JAVA_HOME, "bin/keytool");
		keytoolPath = keytoolFile.toString();
	}

	public void generateSelfSignedCertificate(String issue, String alias, int numberOfDays, String keyPass,
			File keyStore, String storePassword) {
		List<String> commands = new ArrayList<String>();
		commands.add(keytoolPath);
		commands.add("-genkey");
		commands.add("-keyalg");
		commands.add("RSA");
		commands.add("-keypass");
		commands.add(keyPass);
		commands.add("-keysize");
		commands.add("2048");
		commands.add("-dname");
		commands.add(issue);
		commands.add("-alias");
		commands.add(alias);
		commands.add("-validity");
		commands.add(String.valueOf(numberOfDays));
		commands.add("-keystore");
		commands.add(keyStore.toString());
		commands.add("-storepass");
		String sp = storePassword == null ? keyPass : storePassword;
		commands.add(sp);
		executeKeyToolCommand(commands);
	}
	
	
	
	public void generateKeyCSR(File csrFile, String issue, String alias, String keyPass, File keyStore, String storePassword)
	{
		generateKey(issue, alias, keyPass, keyStore, storePassword);
		generateCSR(csrFile, alias, keyPass, keyStore, storePassword);		
	}
	
	
	public void generateKey(String issue, String alias, String keyPass, File keyStore, String storePassword)
	{
		List<String> commands = new ArrayList<String>();
		commands.add(keytoolPath);
		commands.add("-genkey");
		commands.add("-keyalg");
		commands.add("RSA");
		commands.add("-keypass");
		commands.add(keyPass);
		commands.add("-keysize");
		commands.add("2048");
		commands.add("-dname");
		commands.add(issue);
		commands.add("-alias");
		commands.add(alias);		
		commands.add("-keystore");
		commands.add(keyStore.toString());
		commands.add("-storepass");
		String sp = storePassword == null ? keyPass : storePassword;
		commands.add(sp);
		executeKeyToolCommand(commands);
	}
		
	public void generateCSR(File csrFile, String alias, String keyPass, File keyStore, String storePassword)
	{
		List<String> commands = new ArrayList<String>();
		commands.add(keytoolPath);
		commands.add("-keystore");
		commands.add(keyStore.toString());
		commands.add("-storepass");
		String sp = storePassword == null ? keyPass : storePassword;
		commands.add(sp);
		commands.add("-certreq");
		commands.add("-alias");
		commands.add(alias);		
		commands.add("-keyalg");
		commands.add("RSA");
		commands.add("-keypass");
		commands.add(keyPass);	
		commands.add("-file");
		commands.add(csrFile.toString());			
		executeKeyToolCommand(commands);
	}
	
	

	protected int executeKeyToolCommand(List<String> commands) {
		String msg = "";
		try {
			ProcessBuilder builder = new ProcessBuilder(commands);
			builder.redirectErrorStream(true);
			Process p = builder.start();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			IOUtils.copy(p.getInputStream(), bout);
			int status = p.waitFor();
			msg = new String(bout.toByteArray());
			if (status != 0) {
				throw new IOException();
			}
			return status;
		} catch (Exception e) {
			throw new RuntimeException(msg, e);
		}

	}

	public String getKeytoolPath() {
		return keytoolPath;
	}

}
