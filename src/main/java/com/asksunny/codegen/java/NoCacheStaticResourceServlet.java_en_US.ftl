package ${config.basePackageName};

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoCacheStaticResourceServlet extends HttpServlet {

	final static Logger logger = LoggerFactory.getLogger(NoCacheStaticResourceServlet.class);
	
	private static final long serialVersionUID = 1L;
	
	private File staticResourceCPBase = null;
	private String requestBasePath = null;
	

	public NoCacheStaticResourceServlet() 
	{
		logger.info("Init NoCacheStaticResourceServlet");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setHeader("Cache-control", "no-cache, no-store");
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Expires", "-1");
		String reqPath = req.getRequestURI();
		if (requestBasePath != null && reqPath.startsWith(requestBasePath)) {
			reqPath = reqPath.substring(requestBasePath.length());
		}
		if (reqPath.startsWith("/")) {
			reqPath = reqPath.substring(1);
		}
		logger.info("Request:{}, {}", staticResourceCPBase, reqPath);
		Path path = Paths.get(staticResourceCPBase.toString(), reqPath);
		resp.setContentType(Files.probeContentType(path));
		resp.setContentLengthLong(path.toFile().length());
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(path.toFile());
			IOUtils.copy(fin, resp.getOutputStream());
			resp.getOutputStream().flush();
		} finally {
			fin.close();
		}
		
				
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.info("Init with context path:[{}}", config.getServletContext().getContextPath());
	}

	public File getStaticResourceCPBase() {
		return staticResourceCPBase;
	}

	public void setStaticResourceCPBase(String staticResourceCPBase) {
		
		URL path  = getClass().getResource(staticResourceCPBase);
		String dirPath = path.getPath();
		if(dirPath!=null){
			this.staticResourceCPBase = new File(dirPath);
		}
	}

	public String getRequestBasePath() {
		return requestBasePath;
	}

	public void setRequestBasePath(String requestBasePath) {
		this.requestBasePath = requestBasePath;
	}

}
