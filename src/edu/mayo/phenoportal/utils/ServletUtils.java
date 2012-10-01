package edu.mayo.phenoportal.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ServletUtils {

    public static final String STARTUP_PROPERTIES = "data/Startup.properties";
    private static Properties startupProperties = null;
    private static Logger logger = Logger.getLogger(ServletUtils.class.getName());

    public static String getBasePath(HttpServletRequest request) {
        String dataPath = "";

        try {
            if (request != null) {
                HttpSession httpSession = request.getSession(true);
                ServletContext context = httpSession.getServletContext();
                String realContextPath = context.getRealPath(request.getContextPath());

                if (isDevelopmentMode(request)) {
                    dataPath = realContextPath;
                } else {
                    dataPath = realContextPath + "/../";
                }
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error: Attempted to get base path for a null request.", e);
        }
        return dataPath;
    }

    public static String getAlgorithmPath(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("algorithmPath");
    }

    public static String getExecutionResultsPath(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("executionResultsPath");
    }

    public static String getDroolsFilesPath(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("droolsFilesPath");
    }

    public static String getGuvnorUrl(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("guvnorUrl");
    }

    public static String getGuvnorUser(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("guvnorUserId");
    }

    public static String getGuvnorPass(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("guvnorPassword");
    }

    public static String getDesignerUrl(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("jbpmDesignerUrl");
    }

    public static String getRestBaseUrl(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("restBaseUrl");
    }

    public static String getRestUrl(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("restUrl");
    }

    public static String getRestUserId(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("restUserId");
    }

    public static String getRestPassword(HttpServletRequest request) {
        return getStartupProperties(request).getProperty("restPassword");
    }

    private static Properties getStartupProperties(HttpServletRequest request) {
        if (startupProperties == null) {
            Properties props = new Properties();
            try {
                String propsPath = ServletUtils.getBasePath(request) + STARTUP_PROPERTIES;
                FileInputStream in = new FileInputStream(propsPath);
                props.load(in);
            } catch (FileNotFoundException fnfe) {
                logger.log(Level.WARNING, "Error loading Startup.properties: ", fnfe);
            } catch (IOException ioe) {
                logger.log(Level.WARNING, "Error loading Startup.properties: ", ioe);
            }
            startupProperties = props;
        }

        return startupProperties;
    }

    private static boolean isDevelopmentMode(HttpServletRequest request) {
        return request.getHeader("Referer").contains("127.0.0.1");
    }

}
