package com.asksunny.codegen.shell;

import java.io.IOException;

import com.asksunny.codegen.CodeGenConfig;
import com.asksunny.codegen.CodeGenerator;
import com.asksunny.schema.Schema;

public class BootstrapShellGenerator extends CodeGenerator{

	public BootstrapShellGenerator(CodeGenConfig configuration) {
		super(configuration, Schema.EMPTY_SCHEMA);		
	}

	
	@Override
	public void doCodeGen() throws IOException 
	{
		
		
	}

	

}
