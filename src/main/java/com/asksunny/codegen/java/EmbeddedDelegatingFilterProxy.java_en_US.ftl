package ${config.basePackageName};

import javax.servlet.Filter;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class EmbeddedDelegatingFilterProxy extends DelegatingFilterProxy {

	private DispatcherServlet dispatcherServlet;

	public EmbeddedDelegatingFilterProxy(Filter delegate) {
		super(delegate);
	}

	public EmbeddedDelegatingFilterProxy(String targetBeanName) {
		super(targetBeanName);
	}

	public EmbeddedDelegatingFilterProxy(String targetBeanName, WebApplicationContext wac) {
		super(targetBeanName, wac);
	}

	public EmbeddedDelegatingFilterProxy(String targetBeanName, DispatcherServlet dispatcherServlet) {
		super(targetBeanName, dispatcherServlet.getWebApplicationContext());
		this.dispatcherServlet = dispatcherServlet;
	}

	@Override
	protected WebApplicationContext findWebApplicationContext() {
		WebApplicationContext wac =null;		
		try {
			wac = super.findWebApplicationContext();
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		if (wac == null) {
			wac = this.dispatcherServlet.getWebApplicationContext();
		}
		
		
		
		return wac;
	}

}
