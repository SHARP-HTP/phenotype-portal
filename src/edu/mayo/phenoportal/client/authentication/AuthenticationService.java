package edu.mayo.phenoportal.client.authentication;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.mayo.phenoportal.shared.MatImport;
import edu.mayo.phenoportal.shared.User;

@RemoteServiceRelativePath("authentication")
public interface AuthenticationService extends RemoteService {

    Boolean registerUser(User user) throws IllegalArgumentException;

    Boolean updateUserPassword(User user) throws IllegalArgumentException;

    User authenticateUser(String userName, String pw) throws IllegalArgumentException;

    User isValidSession() throws IllegalArgumentException;

    void terminateSession() throws IllegalArgumentException;

	public User validateImportUser(MatImport matImport);
}
