package com.asksunny.codegen;

import java.sql.Types;

public class TypesBean {

	public int getJdbcDateType() {
		return Types.DATE;
	}

	public int getJdbcTimeType() {
		return Types.TIME;
	}

	public int getJdbcTimestampType() {
		return Types.TIMESTAMP;
	}
	
	

	public TypesBean() {
		// TODO Auto-generated constructor stub
	}

}
