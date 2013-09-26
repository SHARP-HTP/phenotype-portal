package edu.mayo.phenoportal.server.authentication;

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
        return (User) httpSession.getAttribute(USER);
    }

    @Override
    public void terminateSession() throws IllegalArgumentException {
        HttpSession httpSession = getThreadLocalRequest().getSession(true);
        httpSession.invalidate(); // kill the session
    }

    private String generateHashPw(String pw) {
        return BCrypt.hashpw(pw, BCrypt.gensalt());
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
        return setUser(user);
    }

    @Override
    public Boolean updateUserPassword(User user) throws IllegalArgumentException {

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {

                // need to store the hashed password
                String pwHashed = generateHashPw(user.getPassword());

                // update the user values
                st = conn.prepareStatement(SQLStatements.updateUserPasswordStatement(
                        user.getUserName(), pwHashed));

                st.executeUpdate();
                success = true;

            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to update user password" + ex.getMessage(),
                        ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }

        return success;
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
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        User user = null;

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
                        "Failed to get users for Authenticating" + ex.getMessage(), ex);
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
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean isSuccessful = false;

        String pwHashed = generateHashPw(user.getPassword());

        if (conn != null) {
            try {
                long registrationDate = System.currentTimeMillis();
                user.setRegistrationDate(DateConverter.getTimeString(new Date(registrationDate)));
                st = conn.prepareStatement(SQLStatements.insertUserStatement(user, pwHashed));

                st.execute();
                sendRegistrationEmail(user);
                isSuccessful = true;
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to Set users in database" + ex.getMessage(),
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

}
