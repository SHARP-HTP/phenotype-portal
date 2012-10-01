package edu.mayo.phenoportal.client.phenotype;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.mayo.phenoportal.shared.Execution;
import edu.mayo.phenoportal.shared.News;
import edu.mayo.phenoportal.shared.SharpNews;
import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.UserRoleRequest;

public interface PhenotypeServiceAsync {

    void getPhenotypeCategories(String categoryId, AsyncCallback<String> callback)
            throws IllegalArgumentException;

    void getCriteria(String fileName, String parentId, String version,
            AsyncCallback<String> callback) throws IllegalArgumentException;

    void executePhenotype(String fileName, String parentId, String version, Date fromDate,
            Date toDate, String userName, AsyncCallback<Execution> callback)
            throws IllegalArgumentException;

    void getExecutions(AsyncCallback<String> callback) throws IllegalArgumentException;

    void getUploaders(AsyncCallback<String> callback) throws IllegalArgumentException;

    void getUser(String userId, AsyncCallback<User> callback) throws IllegalArgumentException;

    void getUsers(AsyncCallback<String> callback) throws IllegalArgumentException;

    void updateUser(User user, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

    void removeUser(User user, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

    void requestPermissionUpgrade(User user, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void getUserRoleRequest(User user, AsyncCallback<UserRoleRequest> callback)
            throws IllegalArgumentException;

    void getHelpPages(HashMap<String, String> fileInfo,
            AsyncCallback<HashMap<String, String>> callback) throws IllegalArgumentException;

    void initializeLogging(AsyncCallback<Void> callback) throws IllegalArgumentException;

    void getLatestUploadedAlgorithms(AsyncCallback<String> callback)
            throws IllegalArgumentException;

    void getNews(AsyncCallback<String> callback) throws IllegalArgumentException;

    void addNews(News news, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

    void updateNews(News news, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

    void removeNews(News news, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

    void getSharpNews(AsyncCallback<String> callback) throws IllegalArgumentException;

    void addSharpNews(SharpNews news, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void updateSharpNews(SharpNews news, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void removeSharpNews(SharpNews news, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void saveJbpm(String uuid, String jbpm, String title, String comment,
            AsyncCallback<Boolean> callback);

    void getLatestExecution(String algorithmName, String algorithmVersion,
            String algorithmCategoryId, String algorithmUser, AsyncCallback<Execution> callback);

    void getDbStats(String type, AsyncCallback<Execution> callback) throws IllegalArgumentException;

    void openEditor(Execution execution, AsyncCallback<String> callback);

}
