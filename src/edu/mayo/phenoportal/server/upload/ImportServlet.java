package edu.mayo.phenoportal.server.upload;

import edu.mayo.phenoportal.server.database.DBConnection;
import edu.mayo.phenoportal.shared.MatImport;
import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.database.UserColumns;
import edu.mayo.phenoportal.utils.SQLStatements;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportServlet extends HttpServlet implements HttpSessionListener {

	private static final long serialVersionUID = 543451297123974158L;
	private Logger logger = Logger.getLogger(ImportServlet.class.getName());

	private static Object matImportsLock = new Object();
	private static Map<String, MatImport> matImports = new HashMap<String, MatImport>();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		if (ServletFileUpload.isMultipartContent(request)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				List<FileItem> items = upload.parseRequest(request);
				Map<String, String> valueMap = new HashMap<String, String>(8);
				byte[] bytearr = null;
				for (FileItem item : items) {
					if (item.isFormField()) {
						valueMap.put(item.getFieldName(), item.getString());
					}
					else {
						bytearr = item.get();
					}
				}

				String userName = valueMap.get("userId");
				User user = getUser(userName);

				if (user != null) {
					String password = valueMap.get("password");
					if (password != null && !password.isEmpty() && password.equals(user.getPassword())) {
						if (user.getRole() < 3) {
							HttpSession session = request.getSession(true);
							session.setAttribute("SESSION_TYPE", "IMPORT");
							MatImport matImport = new MatImport();

							matImport.id = session.getId();
							matImport.user = userName;
							matImport.title = valueMap.get("title");
							matImport.date = valueMap.get("creationDate");
							matImport.version = valueMap.get("version");
							matImport.description = valueMap.get("description");
							matImport.institution = valueMap.get("institution");
							matImport.nqfName = valueMap.get("nqfName");
							matImport.nqfLink = valueMap.get("nqfLink");
							matImport.filearr = bytearr;

							File tmp = File.createTempFile(UUID.randomUUID().toString(), ".zip");
							FileUtils.writeByteArrayToFile(tmp, matImport.filearr);
							matImport.filePath = tmp.getAbsolutePath();

							session.setAttribute("MAT_IMPORT", matImport);

							synchronized (matImportsLock) {
								matImports.put(matImport.id, matImport);
							}

							String location = "http://" + request.getServerName() + ":" + request.getServerPort()
							  + request.getContextPath() + "?tokenId=" + session.getId();

							response.setHeader("Location", location);
							response.setStatus(202);
						} else {
							/* Not enough privileges */
							response.setHeader("message", "User doesn't have enough privileges to upload an algorithm.");
							response.setStatus(200);
						}
					} else {
						/* Authentication failed */
						response.setHeader("message", "Invalid username/password combination.");
						response.setStatus(200);
					}
				} else {
					/* User not found */
					response.setHeader("message", "Invalid user, user was not found.");
					response.setStatus(200);
				}
			} catch (FileUploadException e) {
				response.setHeader("message", "An unexpected error has occurred. Error: " + e.getMessage());
				response.setStatus(200);
			} catch (IOException e) {
				response.setHeader("message", "Error when copying byte[] to File. Error: " + e.getMessage());
				response.setStatus(200);
			}
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// Nothing to do
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		if (session.getAttribute("SESSION_TYPE") != null && session.getAttribute("SESSION_TYPE").equals("IMPORT")) {
			synchronized (matImportsLock) {
				matImports.remove(session.getId());
			}
		}
	}

	public static MatImport getMatImport(String tokenId) {
		synchronized (matImportsLock) {
			return matImports.get(tokenId);
		}
	}

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
				logger.log(Level.SEVERE,
				  "Failed to get users for Authenticating" + ex.getStackTrace(), ex);
			} finally {
				DBConnection.closeConnection(conn, st, rs);
			}
		}
		return user;
	}
}
