package ${config.basePackageName};

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.springframework.beans.factory.FactoryBean;

public class DispatchTypeEnumSetFactoryBean implements FactoryBean<EnumSet<DispatcherType>>
{

	public DispatchTypeEnumSetFactoryBean() {		
	}

	@Override
	public EnumSet<DispatcherType> getObject() throws Exception {		
		return EnumSet.allOf(DispatcherType.class);
	}

	@Override
	public Class<?> getObjectType() {		
		return EnumSet.class;
	}

	@Override
	public boolean isSingleton() {	
		return false;
	}

}