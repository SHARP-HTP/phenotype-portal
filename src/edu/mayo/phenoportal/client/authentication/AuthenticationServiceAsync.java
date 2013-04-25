package edu.mayo.phenoportal.client.authentication;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.mayo.phenoportal.shared.MatImport;
import edu.mayo.phenoportal.shared.User;

public interface AuthenticationServiceAsync {

    void registerUser(User user, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

    void updateUserPassword(User user, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void authenticateUser(String userName, String pw, AsyncCallback<User> callback)
            throws IllegalArgumentException;

    void isValidSession(AsyncCallback<User> callback) throws IllegalArgumentException;

    void terminateSession(AsyncCallback<Void> callback) throws IllegalArgumentException;

	void validateImportUser(MatImport matImport, AsyncCallback<User> async);
}
