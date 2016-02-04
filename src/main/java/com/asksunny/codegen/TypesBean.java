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
	
	public String getMyBatisParamStartTag() {
		return "#{";
	}
	
	public String getMyBatisParamEndTag() {
		return "}";
	}

	public TypesBean() {
		// TODO Auto-generated constructor stub
	}

}
