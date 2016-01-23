package com.asksunny.schema;

import java.util.Comparator;

public class EntityOrderComparator implements Comparator<Entity> {

	public EntityOrderComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Entity arg0, Entity arg1) {		
		int i1 = arg0.getViewOrder();
		int i2 = arg1.getViewOrder();		
		return Integer.valueOf(i1).compareTo(Integer.valueOf(i2));
	}

}
