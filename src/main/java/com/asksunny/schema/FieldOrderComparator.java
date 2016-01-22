package com.asksunny.schema;

import java.util.Comparator;

public class FieldOrderComparator implements Comparator<Field> {

	public FieldOrderComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Field arg0, Field arg1) {
		
		int i1 = arg0.getOrder()==0?arg0.getFieldIndex():arg0.getOrder();
		int i2 = arg1.getOrder()==0?arg1.getFieldIndex():arg1.getOrder();		
		return Integer.valueOf(i1).compareTo(Integer.valueOf(i2));
	}

}
