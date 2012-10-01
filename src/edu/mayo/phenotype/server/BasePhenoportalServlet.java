package edu.mayo.phenotype.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base phenoportal servlet that gets the absolute path to the data directory.
 * This is needed to run in production mode on different app servers.
 */
public class BasePhenoportalServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 1L;

	private static String i_fileName = "";
	private Logger logger = Logger.getLogger(BasePhenoportalServlet.class.getName());
	private static Properties startupProperties = null;

	public String getBasePath() {
		String dataPath;

		HttpSession httpSession = getThreadLocalRequest().getSession(true);
		ServletContext context = httpSession.getServletContext();

		String realContextPath = context.getRealPath(getThreadLocalRequest().getContextPath());

		if (isDevelopmentMode()) {
			dataPath = realContextPath;
		}
		else {
			dataPath = realContextPath + "/../";
		}

		return dataPath;
	}

	public String getExecutionResultsPath() {
		return getStartupProperties().getProperty("executionResultsPath");
	}

	public String getDroolsFilesPath() {
		return getStartupProperties().getProperty("droolsFilesPath");
	}

	public String getGuvnorUrl() {
		return getStartupProperties().getProperty("guvnorUrl");
	}

	public String getGuvnorUser() {
		return getStartupProperties().getProperty("guvnorUserId");
	}

	public String getGuvnorPass() {
		return getStartupProperties().getProperty("guvnorPassword");
	}

	public String getDesignerUrl() {
		return getStartupProperties().getProperty("jbpmDesignerUrl");
	}

	protected Properties getStartupProperties() {
		if (startupProperties == null) {
			Properties props = new Properties();
			try {
				String propsPath = getBasePath() + "data/Startup.properties";
				FileInputStream in = new FileInputStream(propsPath);
				props.load(in);
			}
			catch (FileNotFoundException fnfe) {
				logger.log(Level.WARNING, "Error loading Startup.properties: ", fnfe);
			}
			catch (IOException ioe) {
				logger.log(Level.WARNING, "Error loading Startup.properties: ", ioe);
			}
			startupProperties = props;
		}

		return startupProperties;
	}

	public void setFileName(String fileName, String version, String category) {
		this.i_fileName = fileName + "_" + version + "_" + category;
	}

	public String getFileName() {
		return i_fileName;
	}

	/**
	 * Determine if the app is in development mode. To do this get the request
	 * URL and if it contains 127.0.0.1, then it is in development mode.
	 *
	 * @return
	 */
	private boolean isDevelopmentMode() {
		return getThreadLocalRequest().getHeader("Referer").contains("127.0.0.1");
	}

}
