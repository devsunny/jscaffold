## jScaffold
This is an application that scaffolds the spring framework MVC based Restful service angular bootstrap. It only takes single Relational database table creation DDL SQL script with some custom annotation aka SQL comment as input; it generates an angular and bootstrap front-end, Spring framework MVC Restful service and MyBatis Persistent as backend Java application.

The front-end template is based on  SB Admin v2.0 rewritten in AngularJS (https://github.com/start-angular/sb-admin-angular); it has embedded Jetty as HTTP engine and H2 in memory database configured.

The jScaffold can generate testing data as well, see the following example.


## Examples

```text
DDL annotation syntax

--#Type[,attribute]*
attribute := attribute_name '=' attribute_value
attribute_name := enum (MIN, MAX, VALUES, STEP, FORMAT, VARNAME, LABEL, UITYPE, REF, GROUPLEVEL, ORDER, ITEMSPERPAGE, GROUPFUNCTION, GROUPVIEW, IGNOREVIEW, IGNOREREST, ORDERBY, AUTOGEN, DRILLDOWN)
attribute_value := number literal, quote string literal, boolean lateral

example:
--#TIMESTAMP,format="yyyy-MM-dd HH:mm:ss",label="Created On",

```


```text
copy the following to a file "demo.ddl.sql" and add to classpath;

create table manufacturers --#itemsPerPage=10
(
id bigint not null auto_increment, --#SEQUENCE,min=1
active bit not null,  --#ENUM, values="0,1"
created_on datetime,  --#TIMESTAMP,format="yyyy-MM-dd HH:mm:ss",label="Created On"
link_rewrite varchar(255), --#label="Rewrite",max=32
meta_description varchar(255), --#label="Description",max=32
meta_keywords varchar(255), --#label="Keywords",max=32
meta_title varchar(255), --#label="Title",max=32
name varchar(255), --#max=32
updated_on datetime, --#label="Last Updated",TIMESTAMP,format="yyyy-MM-dd HH:mm:ss"
primary key (id), 
unique (name));

create table Account --#itemsPerPage=10
(
id bigint not null primary key auto_increment , --#SEQUENCE,min=1,groupfunction=count
Last_name varchar(32) NOT null, --#Last_NAME, label="Last Name"
first_name varchar(32) NOT null, --#FIRST_NAME, label="FIRST Name"
house_number int not null, --#min=1,max=100,label="House number"
Street varchar(128) not null, --#STREET
city varchar(128) not null, --#CITY
state varchar(62) not null, --#STATE,drilldown=1
zip_code varchar(10) not null --#ZIP,drilldown=2
);
```

##Examples how to scaffold the application:
```text
CodeGenConfig config = new CodeGenConfig();
config.setBaseSrcDir(".");
config.setBasePackageName("com.foo");
config.setWebappContext("test");		
config.setAppBootstrapClassName("WebAppBoostrap");
config.setOverwriteStrategy(CodeOverwriteStrategy.OVERWRITE);
config.setSchemaFiles("demo.ddl.sql");
JavaCodeGen javaGen = new JavaCodeGen(config);
javaGen.doCodeGen();		
```

##Examples how to generate test data:
```text
SQLScriptLexer lexer = new SQLScriptLexer(getClass().getResourceAsStream("/TestAngularGen.ddl.sql"));
SQLScriptParser tokenReader = new SQLScriptParser(lexer);
Schema schema = tokenReader.parseSql();		
BottomUpSchemaDataGenerator sgen = new BottomUpSchemaDataGenerator(schema);
SchemaDataConfig config = new SchemaDataConfig();
config.setNumberOfRecords(500);
config.setOutputType(SchemaOutputType.INSERT);
config.setOutputUri("src/test/resources");
//config.setDebug(true);
sgen.setConfig(config);
sgen.generateData();		
```








