package edu.mayo.phenoportal.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ServletUtils {

    private static Properties startupProperties = null;
    private static Properties databaseProperties = null;
    private static Logger logger = Logger.getLogger(ServletUtils.class.getName());

	public static String getMode() {
		return getStartupProperties().getProperty("Mode");
	}

    public static String getAlgorithmPath() {
        return getStartupProperties().getProperty("algorithmPath");
    }

    public static String getExecutionResultsPath() {
        return getStartupProperties().getProperty("executionResultsPath");
    }

	public static String getSmtpFromAddress() {
		return getStartupProperties().getProperty("smtp.from.address");
	}

	public static String getSmtpHost() {
		return getStartupProperties().getProperty("smtp.host");
	}

	public static String getSmtpPort() {
		return getStartupProperties().getProperty("smtp.port");
	}

	public static String getSmtpPassword() {
		return getStartupProperties().getProperty("smtp.pw");
	}

	public static String getEmailContentsUserRegistration() {
		return getStartupProperties().getProperty("email.contents.user.registration");
	}

	public static String getEmailContentsUserRoleRequestAdmin() {
		return getStartupProperties().getProperty("email.contents.user.role.request_admin");
	}

	public static String getEmailContentsUserRoleRequestUser() {
		return getStartupProperties().getProperty("email.contents.user.role.request_user");
	}

	public static String getEmailContentsUserRoleReplyGranted() {
		return getStartupProperties().getProperty("email.contents.user.role.reply.granted");
	}

	public static String getEmailContentsUserRoleReplyDenied() {
		return getStartupProperties().getProperty("email.contents.user.role.reply.denied");
	}

    public static String getDroolsFilesPath() {
        return getStartupProperties().getProperty("droolsFilesPath");
    }

    public static String getGuvnorUrl() {
        return getStartupProperties().getProperty("guvnorUrl");
    }

    public static String getGuvnorUser() {
        return getStartupProperties().getProperty("guvnorUserId");
    }

    public static String getGuvnorPass() {
        return getStartupProperties().getProperty("guvnorPassword");
    }

    public static String getDesignerUrl() {
        return getStartupProperties().getProperty("jbpmDesignerUrl");
    }

    public static String getRestBaseUrl() {
        return getStartupProperties().getProperty("restBaseUrl");
    }

    public static String getRestUrl() {
        return getStartupProperties().getProperty("restUrl");
    }

    public static String getRestUserId() {
        return getStartupProperties().getProperty("restUserId");
    }

    public static String getRestPassword() {
        return getStartupProperties().getProperty("restPassword");
    }

	public static String getCts2RestUrl() {
		return getStartupProperties().getProperty("cts2.valuesetdefinition.maintenance.url");
	}

	public static String getCts2RestUser() {
		return getStartupProperties().getProperty("cts2.valuesetdefinition.maintenance.user");
	}

	public static String getCts2RestPassword() {
		return getStartupProperties().getProperty("cts2.valuesetdefinition.maintenance.password");
	}

	public static String getCts2EntityRestUrl() {
		return getStartupProperties().getProperty("cts2.valuesetdefinition.maintenance.entities.url");
	}

	public static int getCts2RestPageSize() {
		return Integer.parseInt(getStartupProperties().getProperty("cts2.valuesetdefinition.maintenance.pagesize"));
	}

	public static int getRecentlyUploadedLimit() {
		return Integer.parseInt(getStartupProperties().getProperty("recently.uploaded.algorithms"));
	}

	public static String getMatEditorUrl() {
		return getStartupProperties().getProperty("mat.editor.url");
	}

	public static String getMatEditorUrlInternal() {
		String url = getStartupProperties().getProperty("mat.editor.url.internal");
		if (url == null || url.trim().isEmpty()) {
			url = getStartupProperties().getProperty("mat.editor.url");
		}
		return url;
	}

	public static String getDatabaseUrl() {
		return getDatabaseProperties().getProperty(ServletUtils.getMode() + "." + "db.url");
	}

	public static String getDatabaseUser() {
		return getDatabaseProperties().getProperty(ServletUtils.getMode() + "." + "db.user");
	}

	public static String getDatabasePassword() {
		return getDatabaseProperties().getProperty(ServletUtils.getMode() + "." + "db.passwd");
	}

    private static Properties getStartupProperties() {
        if (startupProperties == null) {
            Properties props = new Properties();
            try {
	            props.load(ServletUtils.class.getResourceAsStream("Startup.properties"));
            } catch (FileNotFoundException fnfe) {
                logger.log(Level.WARNING, "Error loading Startup.properties: ", fnfe);
            } catch (IOException ioe) {
                logger.log(Level.WARNING, "Error loading Startup.properties: ", ioe);
            }
            startupProperties = props;
        }

        return startupProperties;
    }

	private static Properties getDatabaseProperties() {
		if (databaseProperties == null) {
			Properties props = new Properties();
			try {
				props.load(ServletUtils.class.getResourceAsStream("database.properties"));
			} catch (FileNotFoundException fnfe) {
				logger.log(Level.WARNING, "Error loading database.properties: ", fnfe);
			} catch (IOException ioe) {
				logger.log(Level.WARNING, "Error loading database.properties: ", ioe);
			}
			databaseProperties = props;
		}

		return databaseProperties;
	}

	public static void initializeLogging() {
		try {
			LogManager.getLogManager().readConfiguration(ServletUtils.class.getResourceAsStream("LogProperties.properties"));
		} catch (IOException ioe) {
			logger.log(Level.WARNING, "Unable to initialize logging.", ioe);
		}
	}

}
