package edu.mayo.phenoportal.server.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.mayo.phenoportal.client.authentication.AuthenticationService;
import edu.mayo.phenoportal.server.database.DBConnection;
import edu.mayo.phenoportal.server.utils.DateConverter;
import edu.mayo.phenoportal.server.utils.SmtpClient;
import edu.mayo.phenoportal.shared.MatImport;
import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.database.UserColumns;
import edu.mayo.phenoportal.utils.SQLStatements;
import edu.mayo.phenoportal.utils.ServletUtils;

public class AuthenticationServiceImpl extends RemoteServiceServlet implements
        AuthenticationService {

    private static Logger s_logger = Logger.getLogger(AuthenticationServiceImpl.class.getName());
    private static final long serialVersionUID = 1L;

    // TODO CME - this needs to be changed to 0 to not enable the user by
    // default
    // Change when we get the admin page set up to enable a user.
    public static final int DEFAULT_USER_ENABLED = 1; // enable is true
    public static final int DEFAULT_USER_ROLE = 3;

    public static final String USER = "user";

    @Override
    public User authenticateUser(String userName, String pw) throws IllegalArgumentException {

        String sessionId;

        // get the user from the DB. User will be null if user is invalid.
        User user = getUser(userName);

        // check the pw from the user obtained from the db. Validate it is
        // correct.
        if (user != null) {
            boolean valid = BCrypt.checkpw(pw, user.getPassword());

            if (valid) {
                // check for null user - send up user/pass does not exist
                sessionId = getCurrentSessionId();
                setSessionForUser(user);
                user.setSessionId(sessionId);
            } else {
                // if the pw is invalid, set the user object to null
                user = null;
            }

        } else {
            user = null;
        }

        return user;
    }

	public User validateImportUser(MatImport matImport) {
		String sessionId;

		User user = getUser(matImport.user);
		if (user != null) {
			sessionId = getCurrentSessionId();
			setSessionForUser(user);
			user.setSessionId(sessionId);
		}
		return user;
	}

    @Override
    public User isValidSession() throws IllegalArgumentException {
        HttpSession httpSession = getThreadLocalRequest().getSession(true);
        User user = (User) httpSession.getAttribute(USER);
        return user;
    }

    @Override
    public void terminateSession() throws IllegalArgumentException {
        HttpSession httpSession = getThreadLocalRequest().getSession(true);
        httpSession.invalidate(); // kill the session
    }

    private String generateHashPw(String pw) {
        String hashed = BCrypt.hashpw(pw, BCrypt.gensalt());
        return hashed;
    }

    /**
     * get the user's session id
     * 
     * @param user
     * @return
     */
    private void setSessionForUser(User user) {

        HttpSession httpSession = getThreadLocalRequest().getSession(true);
        httpSession.setAttribute(USER, user);
    }

    /**
     * Get the user's current session. May be null.
     * 
     * @return
     */
    private String getCurrentSessionId() {

        HttpSession httpSession = getThreadLocalRequest().getSession(true);
        return httpSession.getId();
    }

    @Override
    public Boolean registerUser(User user) throws IllegalArgumentException {
	    boolean success = registerMatUser(user);
	    if (success)
            success = setUser(user);
        return Boolean.valueOf(success);
    }

    @Override
    public Boolean updateUserPassword(User user) throws IllegalArgumentException {

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        conn = DBConnection.getDBConnection();

        boolean success = false;

        if (conn != null) {
            try {

                // need to store the hashed password
                String pwHashed = generateHashPw(user.getPassword());

                // update the user values
                st = conn.prepareStatement(SQLStatements.updateUserPasswordStatement(
                        user.getUserName(), pwHashed));

                int result = st.executeUpdate();
                success = true;

            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to update user password" + ex.getStackTrace(),
                        ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }

        return new Boolean(success);
    }

    /**
     * Gets the User information based on the userName. If the returned user is
     * null, then the user/pass combo does not exist in the database It's always
     * good to check for null. If the returned User is not null, it will contain
     * all the information
     * 
     * @param name
     * @return
     */
    private User getUser(String name) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        User user = null;
        conn = DBConnection.getDBConnection();

        if (conn != null) {
            try {

                // get the user based on the user name. we will check the pw
                // later
                st = conn.prepareStatement(SQLStatements.selectUserStatement(name));
                rs = st.executeQuery();

                if (rs.next()) {
                    user = new User();

                    user.setUserName(rs.getString(UserColumns.USER.colNum()));
                    user.setLastName(rs.getString(UserColumns.LNAME.colNum()));
                    user.setFirstName(rs.getString(UserColumns.FNAME.colNum()));
                    user.setEmail(rs.getString(UserColumns.EMAIL.colNum()));
                    user.setPassword(rs.getString(UserColumns.PASS.colNum()));
                    user.setRole(rs.getInt(UserColumns.ROLE.colNum()));
                    user.setEnable(rs.getInt(UserColumns.ENABLE.colNum()));
                    user.setRegistrationDate(rs.getString(UserColumns.REGISTRATIONDATE.colNum()));
                }
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE,
                        "Failed to get users for Authenticating" + ex.getStackTrace(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return user;
    }

    /**
     * Enters User information into the database.
     * 
     * @param user
     * @return
     */
    private boolean setUser(User user) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        conn = DBConnection.getDBConnection();
        boolean isSuccessful = false;

        if (conn != null) {
            try {
                long registrationDate = System.currentTimeMillis();
                user.setRegistrationDate(DateConverter.getTimeString(new Date(registrationDate)));
                st = conn.prepareStatement(SQLStatements.insertUserStatement(user, user.getPassword()));

                st.execute();
                sendRegistrationEmail(user);
                isSuccessful = true;
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to Set users in database" + ex.getStackTrace(),
                        ex);
                isSuccessful = false;

            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return isSuccessful;
    }

    private void sendRegistrationEmail(User user) {
        String host = ServletUtils.getSmtpHost();
        String from = ServletUtils.getSmtpFromAddress();
        String messageText = ServletUtils.getEmailContentsUserRegistration();
        String port = ServletUtils.getSmtpPort();
        String pw = ServletUtils.getSmtpPassword();

        SmtpClient.sendRegistrationSuccessEmail(host, from, pw, port, messageText, user);
    }

	private boolean registerMatUser(User user) {
		boolean result = false;
		String charset = "UTF-8";
		String url = ServletUtils.getMatEditorUrlInternal() + "/createUser";
		OutputStream output = null;
		InputStream response = null;

		try {
			String hashedpw = generateHashPw(user.getPassword());
			String query = String.format("userId=%s&firstName=%s&lastName=%s&email=%s&phone=%s&password=%s&htpid=%s",
			  URLEncoder.encode(user.getUserName(), charset),
			  URLEncoder.encode(user.getFirstName(), charset),
			  URLEncoder.encode(user.getLastName(), charset),
			  URLEncoder.encode(user.getEmail(), charset),
		      URLEncoder.encode(user.getPhoneNumber(), charset),
			  URLEncoder.encode(user.getPassword(), charset),
			  URLEncoder.encode(hashedpw, charset));

			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setConnectTimeout(0);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", charset);
			connection.setRequestProperty("Content-Length", Integer.toString(query.length()));
			connection.setUseCaches(false);
			connection.connect();
			output = connection.getOutputStream();
			output.write(query.getBytes(charset));
			output.flush();

			if (connection.getResponseCode() == 200) {
				response = connection.getInputStream();
				System.out.println(response.toString());
				user.setPassword(hashedpw);
				result = true;
			}
			else {
				result = false;
			}
		}
		catch (MalformedURLException mue) {
			s_logger.log(Level.WARNING, "Unable to create MAT user.", mue);
			result = false;
		}
		catch (IOException ioe) {
			s_logger.log(Level.WARNING, "Unable to create MAT user.", ioe);
			result = false;
		}
		finally {
			if (output != null) {
				try {
					output.close();
				} catch(Exception e) {
					s_logger.log(Level.FINE, "Failed to close outputStream.", e);
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					s_logger.log(Level.FINE, "Failed to close inputStream.", e);
				}
			}
		}

		return result;
	}

}
