package edu.mayo.phenoportal.server.database;

import edu.mayo.phenoportal.utils.ServletUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This class when called will create a connection to the Mysql database. Reads
 * the Url, Username and Password from the database.properties file
 * database.properties is placed under Data folder.
 */

public class DBConnection {

    static Logger logger = Logger.getLogger(DBConnection.class.getName());
	static String url = ServletUtils.getDatabaseUrl();
	static String user = ServletUtils.getDatabaseUser();
	static String passwd = ServletUtils.getDatabasePassword();

    public static Connection getDBConnection() {
        Connection con = null;
        try {
            // registering the jdbc driver here.
            // need to register the jdbc driver with the java application for
            // tomcat 7 server (Jboss doesn't need it).
            Class.forName("com.mysql.jdbc.Driver");

            // Connect to the SQl server with the properties
            con = DriverManager.getConnection(url, user, passwd);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return con;
    }

    public static void closeConnection(Connection conn, Statement st, ResultSet rs) {
        DBConnection.close(rs);
        DBConnection.close(st);
        DBConnection.close(conn);
    }

    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, sqle.getMessage(), sqle);
        }
    }

    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, sqle.getMessage(), sqle);
        }
    }

    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, sqle.getMessage(), sqle);
        }
    }

    public static void commit(Connection connection) {
        try {
            if (connection != null) {
                connection.commit();
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, sqle.getMessage(), sqle);
        }
    }

    public static void rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, sqle.getMessage(), sqle);
        }
    }

}
