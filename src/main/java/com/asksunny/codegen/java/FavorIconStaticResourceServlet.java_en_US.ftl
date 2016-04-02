package ${config.basePackageName};

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FavorIconStaticResourceServlet extends HttpServlet {
	final static Logger logger = LoggerFactory.getLogger(FavorIconStaticResourceServlet.class);
	private byte[] favicon = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FavorIconStaticResourceServlet() {
		logger.info("Init FavorIconStaticResourceServlet");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Content-Length:5430
		// Content-Type:image/x-icon
		if (favicon != null) {
			resp.setContentType("image/x-icon");
			resp.setContentLength(favicon.length);
			resp.getOutputStream().write(favicon);
			resp.getOutputStream().flush();
			resp.getOutputStream().close();
		} else {
			resp.getWriter().print("<html><body>forget about it</body></html>");
			resp.getWriter().flush();
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

	public byte[] getFavicon() {
		return favicon;
	}

	public void setFavicon(String favicon) {
		InputStream in = getClass().getResourceAsStream(favicon);
		if (in != null) {
			try {
				this.favicon = IOUtils.toByteArray(in);
			} catch (IOException e) {
				;
			} finally {
				IOUtils.closeQuietly(in);
			}
		}
	}

}
