package edu.mayo.phenotype.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class BasePhenoportalHttpServlet extends HttpServlet {

    private static final long serialVersionUID = 4286386055693424291L;
    private static Logger logger = Logger.getLogger(BasePhenoportalHttpServlet.class.getName());

    protected String getBasePath(HttpServletRequest request) {
        String basePath = getServletContext().getRealPath(request.getContextPath());

        if (!isDevelopmentMode(request)) {
            basePath += "/../";
        }

        return basePath;
    }

    protected String getAlgorithmPath(HttpServletRequest request) {
        Properties properties = getProperties(request);
        String path = null;
		if (properties != null) {
            path = properties.getProperty("algorithmPath");
		}
        return path;
    }

	protected String getValueSetServiceUrl(HttpServletRequest request) {
		Properties properties = getProperties(request);
		String path = null;
		if (properties != null) {
			path = properties.getProperty("cts2.valuesetdefinition.maintenance.url");
		}
		return path;
	}

	protected String getValueSetServiceUser(HttpServletRequest request) {
		Properties properties = getProperties(request);
		String path = null;
		if (properties != null) {
			path = properties.getProperty("cts2.valuesetdefinition.maintenance.user");
		}
		return path;
	}

	protected String getValueSetServicePassword(HttpServletRequest request) {
		Properties properties = getProperties(request);
		String path = null;
		if (properties != null) {
			path = properties.getProperty("cts2.valuesetdefinition.maintenance.password");
		}
		return path;
	}

	private Properties getProperties(HttpServletRequest request) {
		Properties startupProperties = new Properties();
		try {
			FileInputStream in = null;
			startupProperties = new Properties();
			String propsPath = getBasePath(request) + "data/Startup.properties";
			in = new FileInputStream(propsPath);
			startupProperties.load(in);
		} catch (FileNotFoundException fnfe) {
			logger.log(Level.SEVERE, "Error getting the algorithm path: ", fnfe);
		} catch (IOException ioe) {
			logger.log(Level.SEVERE, "Error getting the algorithm path: ", ioe);
		}
		return startupProperties;
	}

    /**
     * Determine if the app is in development mode. To do this get the request
     * URL and if it contains 127.0.0.1, then it is in development mode.
     * 
     * @return
     */
    private boolean isDevelopmentMode(HttpServletRequest request) {
	    String referrer = request.getHeader("Referer");
        return referrer != null && referrer.contains("127.0.0.1");
    }

}
