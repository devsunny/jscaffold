package com.asksunny.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.asksunny.collections.CaselessHashSet;

public class CodeGenConfig {

	public static enum CodeOverwriteStrategy {
		OVERWRITE, IGNORE, SUFFIX_SEQUENCE, RENAME_EXISTING
	};

	String javaBaseDir = "src/main/java";
	String junitBaseDir = "src/test/java";
	String myBatisXmlBaseDir = "src/main/resources";
	String springXmlBaseDir = "src/main/resources";
	String webappContext = "spring";
	String webBaseSrcDir = "src/main/resources/META-INF/app";
	String baseSrcDir = ".";
	String basePackageName = "com.foo";

	String domainPackageName;
	String mapperPackageName;
	String restPackageName;
	String schemaFiles = null;
	String angularAppName = "sbAdminApp";
	String appBootstrapPackage;
	String appBootstrapClassName;
	

	private DataOutputType outputType;
	private String outputUri;
	private long numberOfRecords;
	private boolean debug = false;

	private String webserverPort = "8181";

	boolean genAngularView = true;
	boolean genAngularRoute = true;
	boolean genAngularController = true;
	
	
	boolean genDomainObject = true;
	boolean genMyBatisMapper = true;
	boolean genMyBatisXmlMapper = true;
	boolean genRestController = true;
	boolean genSpringContext = true;
	boolean enableSpringSecurity = true;
	boolean genMyBatisSpringBeans = true;
	boolean suffixSequenceIfExists = true;
	boolean genScaffoldTools = true;
	boolean genPomXml = true;
	boolean genJunit = false;
	boolean genSchema = true;
	boolean genSeedData = false;
	boolean useRestfulEnvelope = false;
	

	boolean enableSSL = true;
	String SSLIssuerDN = null;
	String keypass = "changeit";
	String keyStoreDirectory = "src/test/resources";
	String keystoreName = "web_server.jks";
	String sslCertAlias = "webserver";

	CaselessHashSet includes = new CaselessHashSet();
	CaselessHashSet excludes = new CaselessHashSet();

	CodeOverwriteStrategy overwriteStrategy = CodeOverwriteStrategy.RENAME_EXISTING;

	public CodeGenConfig() {

	}
	
	
	public void setAngularUIOnly()
	{
		this.genAngularView = true;
		this.genAngularRoute = true;
		this.genAngularController = true;
		
		this.genDomainObject = false;
		this.genMyBatisMapper = false;
		this.genMyBatisXmlMapper = false;
		this.genRestController = false;
		this.genSpringContext = false;
		this.enableSpringSecurity = false;
		this.genMyBatisSpringBeans = false;		
		this.genScaffoldTools = false;
		this.genPomXml = false;
		this.genJunit = false;
		this.genSchema = false;
		this.genSeedData = false;
		this.useRestfulEnvelope = false;
	}
	
	
	
	public void setModelOnly()
	{
		this.genAngularView = false;
		this.genAngularRoute = false;
		this.genAngularController = false;
		this.genDomainObject = true;
		this.genMyBatisMapper = true;
		this.genMyBatisXmlMapper = true;
		this.genRestController = false;
		this.genSpringContext = false;
		this.enableSpringSecurity = false;
		this.genMyBatisSpringBeans = false;		
		this.genScaffoldTools = false;
		this.genPomXml = false;
		this.genJunit = false;
		this.genSchema = false;
		this.genSeedData = false;
		this.useRestfulEnvelope = false;
	}

	public String getJavaBaseDir() {
		return javaBaseDir == null ? String.format("%s/src/main/java", getBaseSrcDir())
				: String.format("%s/%s", getBaseSrcDir(), javaBaseDir);
	}

	public String getJunitBaseDir() {
		return junitBaseDir == null ? String.format("%s/src/test/java", getBaseSrcDir())
				: String.format("%s/%s", getBaseSrcDir(), junitBaseDir);
	}

	public String getMyBatisXmlBaseDir() {
		return myBatisXmlBaseDir == null ? String.format("%s/src/main/resources", getBaseSrcDir())
				: String.format("%s/%s", getBaseSrcDir(), myBatisXmlBaseDir);
	}

	public void setGenAngularController(boolean genAngularController) {
		this.genAngularController = genAngularController;
	}

	public String getSpringXmlBaseDir() {
		return springXmlBaseDir == null ? String.format("%s/src/main/resources", getBaseSrcDir())
				: String.format("%s/%s", getBaseSrcDir(), springXmlBaseDir);
	}

	public String getWebBaseSrcDir() {
		return webBaseSrcDir == null ? String.format("%s/src/main/resources/META-INF/app", getBaseSrcDir())
				: String.format("%s/%s", getBaseSrcDir(), webBaseSrcDir);
	}

	public String getKeyStoreDirectory() {
		return keyStoreDirectory == null ? String.format("%s/src/test/resources", getBaseSrcDir())
				: String.format("%s/%s", getBaseSrcDir(), keyStoreDirectory);
	}

	public void setKeyStoreDirectory(String keyStoreDirectory) {
		this.keyStoreDirectory = keyStoreDirectory;
	}

	public Set<String> getIgnores() {
		return excludes;
	}

	public void setIgnores(String ignoresCsv) {
		String[] igs = ignoresCsv.split("\\s*[,;]\\s*");
		for (int i = 0; i < igs.length; i++) {
			excludes.add(igs[i]);
		}
	}

	public void setIncludes(String includessCsv) {
		String[] igs = includessCsv.split("\\s*[,;]\\s*");
		for (int i = 0; i < igs.length; i++) {
			includes.add(igs[i]);
		}
	}

	public boolean shouldIgnore(String tableName) {
		return tableName == null || StringUtils.isBlank(tableName) || this.excludes.contains(tableName);
	}

	public boolean shouldInclude(String tableName) {
		return tableName != null && (!StringUtils.isBlank(tableName)) && this.includes.contains(tableName);
	}

	public void setJavaBaseDir(String javaBaseDir) {
		this.javaBaseDir = javaBaseDir;
	}

	public void setMyBatisXmlBaseDir(String myBatisXmlBaseDir) {
		this.myBatisXmlBaseDir = myBatisXmlBaseDir;
	}

	public String getDomainPackageName() {
		return domainPackageName == null ? String.format("%s.domain", getBasePackageName()) : domainPackageName;
	}

	public void setDomainPackageName(String domainPackageName) {
		this.domainPackageName = domainPackageName;
	}
	
	public String getDomainPackagePath() {
		return getDomainPackageName().replaceAll("[\\.]", "/");
	}


	public String getMapperPackageName() {
		return mapperPackageName == null ? String.format("%s.mappers", getBasePackageName()) : mapperPackageName;
	}
	
	
	
	public String getMapperPackagePath() {
		
		return getMapperPackageName().replaceAll("[\\.]", "/");
	}
	

	public void setMapperPackageName(String mapperPackageName) {
		this.mapperPackageName = mapperPackageName;
	}

	public String getRestPackageName() {
		return restPackageName == null ? String.format("%s.rest", getBasePackageName()) : restPackageName;
	}
	
	public String getRestPackagePath() {
		return getRestPackageName().replaceAll("[\\.]", "/");
	}

	public void setRestPackageName(String restPackageName) {
		this.restPackageName = restPackageName;
	}

	public String getSchemaFiles() {
		return schemaFiles;
	}
	
	public List<String> getSchemaFileList() {
		if(schemaFiles.trim().length()>0){
			return Arrays.asList(schemaFiles.split("[;, ]+"));
		}else{
			return new ArrayList<String>();
		}
		
	}

	public void setSchemaFiles(String schemaFiles) {
		this.schemaFiles = schemaFiles;
	}

	public boolean isGenDomainObject() {
		return genDomainObject;
	}

	public void setGenDomainObject(boolean genDomainObject) {
		this.genDomainObject = genDomainObject;
	}

	public boolean isGenMyBatisMapper() {
		return genMyBatisMapper;
	}

	public void setGenMyBatisMapper(boolean genMyBatisMapper) {
		this.genMyBatisMapper = genMyBatisMapper;
	}

	public boolean isGenRestController() {
		return genRestController;
	}

	public void setGenRestController(boolean genRestController) {
		this.genRestController = genRestController;
	}

	public boolean isGenSpringContext() {
		return genSpringContext;
	}

	public void setGenSpringContext(boolean genSpringContext) {
		this.genSpringContext = genSpringContext;
	}

	public boolean isSuffixSequenceIfExists() {
		return suffixSequenceIfExists;
	}

	public void setSuffixSequenceIfExists(boolean suffixSequenceIfExists) {
		this.suffixSequenceIfExists = suffixSequenceIfExists;
	}

	public void setSpringXmlBaseDir(String springXmlBaseDir) {
		this.springXmlBaseDir = springXmlBaseDir;
	}

	public CodeOverwriteStrategy getOverwriteStrategy() {
		return overwriteStrategy;
	}

	public void setOverwriteStrategy(CodeOverwriteStrategy overwriteStrategy) {
		this.overwriteStrategy = overwriteStrategy;
	}

	public CaselessHashSet getIncludes() {
		return includes;
	}

	public CaselessHashSet getExcludes() {
		return excludes;
	}

	public String getAngularAppName() {
		return angularAppName;
	}

	public void setAngularAppName(String angularAppName) {
		this.angularAppName = angularAppName;
	}

	public String getWebappContext() {
		return webappContext;
	}

	public void setWebappContext(String webappContext) {
		this.webappContext = webappContext;
	}

	public boolean isGenAngularView() {
		return genAngularView;
	}

	public void setGenAngularView(boolean genAngularView) {
		this.genAngularView = genAngularView;
	}

	public boolean isGenAngularRoute() {
		return genAngularRoute;
	}

	public boolean isGenAngular() {
		return this.isGenAngularController() || this.isGenAngularRoute() || isGenAngularView();
	}

	public void setGenAngularRoute(boolean genAngularRoute) {
		this.genAngularRoute = genAngularRoute;
	}

	public boolean isGenAngularController() {
		return genAngularController;
	}

	public void setWebBaseSrcDir(String webBaseSrcDir) {
		this.webBaseSrcDir = webBaseSrcDir;
	}

	public boolean isGenMyBatisSpringBeans() {
		return genMyBatisSpringBeans;
	}

	public void setGenMyBatisSpringBeans(boolean genMyBatisSpringBeans) {
		this.genMyBatisSpringBeans = genMyBatisSpringBeans;
	}

	public String getAppBootstrapPackage() {
		return appBootstrapPackage == null ? getBasePackageName() : appBootstrapPackage;
	}
	
	public String getAppBootstrapPackagePath() {
		return getAppBootstrapPackage().replaceAll("[\\.]", "/");
	}

	public void setAppBootstrapPackage(String appBootstrapPackage) {
		this.appBootstrapPackage = appBootstrapPackage;
	}

	public String getAppBootstrapClassName() {
		return appBootstrapClassName;
	}

	public void setAppBootstrapClassName(String appBootstrapClassName) {
		this.appBootstrapClassName = appBootstrapClassName;
	}

	public String getSSLIssuerDN() {
		return SSLIssuerDN;
	}

	public void setSSLIssuerDN(String sSLIssuerDN) {
		SSLIssuerDN = sSLIssuerDN;
	}

	public void setIncludes(CaselessHashSet includes) {
		this.includes = includes;
	}

	public void setExcludes(CaselessHashSet excludes) {
		this.excludes = excludes;
	}

	public String getBaseSrcDir() {
		return baseSrcDir == null ? "generated-src" : baseSrcDir;
	}

	public void setBaseSrcDir(String baseSrcDir) {
		this.baseSrcDir = baseSrcDir;
	}

	public String getBasePackageName() {
		return basePackageName == null ? "com.foo" : basePackageName;
	}
	
	public String getBasePackagePath() {
		return getBasePackageName().replaceAll("[\\.]", "/");
	}
	
	

	public void setBasePackageName(String basePackageName) {
		this.basePackageName = basePackageName;
	}

	public boolean isGenPomXml() {
		return genPomXml;
	}

	public void setGenPomXml(boolean genPomXml) {
		this.genPomXml = genPomXml;
	}

	public DataOutputType getOutputType() {
		return outputType;
	}

	public void setOutputType(DataOutputType outputType) {
		this.outputType = outputType;
	}

	public String getDataOutputDir() {
		return outputUri;
	}

	public void setDataOutputDir(String outputUri) {
		this.outputUri = outputUri;
	}

	public long getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(long numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isGenMyBatisXmlMapper() {
		return genMyBatisXmlMapper;
	}

	public void setGenMyBatisXmlMapper(boolean genMyBatisXmlMapper) {
		this.genMyBatisXmlMapper = genMyBatisXmlMapper;
	}

	public boolean isGenScaffoldTools() {
		return genScaffoldTools;
	}

	public void setGenScaffoldTools(boolean genScaffoldTools) {
		this.genScaffoldTools = genScaffoldTools;
	}

	public boolean isGenJunit() {
		return genJunit;
	}

	public void setGenJunit(boolean genJunit) {
		this.genJunit = genJunit;
	}

	public boolean isEnableSpringSecurity() {
		return enableSpringSecurity;
	}

	public void setEnableSpringSecurity(boolean enableSpringSecurity) {
		this.enableSpringSecurity = enableSpringSecurity;
	}

	public boolean isEnableSSL() {
		return enableSSL;
	}

	public void setEnableSSL(boolean enableSSL) {
		this.enableSSL = enableSSL;
	}

	public String getKeypass() {
		return keypass;
	}

	public void setKeypass(String keypass) {
		this.keypass = keypass;
	}

	public String getKeystoreName() {
		return keystoreName;
	}

	public void setKeystoreName(String keystoreName) {
		this.keystoreName = keystoreName;
	}

	public String getSslCertAlias() {
		return sslCertAlias;
	}

	public void setSslCertAlias(String sslCertAlias) {
		this.sslCertAlias = sslCertAlias;
	}

	public String getWebserverPort() {
		return webserverPort;
	}

	public void setWebserverPort(String webserverPort) {
		this.webserverPort = webserverPort;
	}

	public boolean isGenSchema() {
		return genSchema;
	}

	public void setGenSchema(boolean genSchema) {
		this.genSchema = genSchema;
	}

	public boolean isGenSeedData() {
		return genSeedData;
	}

	public void setGenSeedData(boolean genSeedData) {
		this.genSeedData = genSeedData;
	}

	public boolean isUseRestfulEnvelope() {
		return useRestfulEnvelope;
	}

	public void setUseRestfulEnvelope(boolean useRestfulEnvelope) {
		this.useRestfulEnvelope = useRestfulEnvelope;
	}


	

}
