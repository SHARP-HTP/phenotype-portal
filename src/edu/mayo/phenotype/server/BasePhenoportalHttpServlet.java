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

    private static final long serialVersionUID = 4286386055693424275L;
    private static Logger logger = Logger.getLogger(BasePhenoportalHttpServlet.class.getName());

    protected String getBasePath(HttpServletRequest request) {
        String basePath = getServletContext().getRealPath(request.getContextPath());

        if (!isDevelopmentMode(request)) {
            basePath += "/../";
        }

        return basePath;
    }

    protected String getAlgorithmPath(HttpServletRequest request) {
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

        String algoPath = startupProperties.getProperty("algorithmPath");
        return algoPath;
    }

    /**
     * Determine if the app is in development mode. To do this get the request
     * URL and if it contains 127.0.0.1, then it is in development mode.
     * 
     * @return
     */
    private boolean isDevelopmentMode(HttpServletRequest request) {
        return request.getHeader("Referer").contains("127.0.0.1");
    }

}
