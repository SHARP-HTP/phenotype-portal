package edu.mayo.phenoportal.client.phenotype;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.shared.Execution;
import edu.mayo.phenoportal.shared.MatImport;
import edu.mayo.phenoportal.shared.News;
import edu.mayo.phenoportal.shared.SharpNews;
import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.UserRoleRequest;
import edu.mayo.phenoportal.shared.ValueSet;

public interface PhenotypeServiceAsync {

    void getPhenotypeCategories(String categoryId, AsyncCallback<String> callback)
            throws IllegalArgumentException;

    void executePhenotype(AlgorithmData algorithmData, Date fromDate, Date toDate, String userName,
            AsyncCallback<Execution> callback) throws IllegalArgumentException;

    void executeLastExecution(AlgorithmData algorithmData, String userName,
            AsyncCallback<Execution> callback) throws IllegalArgumentException;

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

    void getUserRoleRequests(AsyncCallback<String> callback) throws IllegalArgumentException;

    void updateUserRoleRequest(UserRoleRequest userRoleRequest, AsyncCallback<Boolean> callback)
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

    void getLatestExecution(String algorithmName, String algorithmVersion,
            String algorithmCategoryId, String algorithmUser, AsyncCallback<Execution> callback);

    void getDbStats(String type, AsyncCallback<Execution> callback) throws IllegalArgumentException;

    void getPopulationCriteria(AlgorithmData algorithmData, AsyncCallback<String> async);

    void getDataCriteriaOids(AlgorithmData algorithmData, AsyncCallback<Map<String, String>> async);

    void getSupplementalCriteriaOids(AlgorithmData algorithmData,
            AsyncCallback<Map<String, String>> async);

    void getMatEditorUrl(User user, AsyncCallback<String> async);

    void getMatImport(String tokenId, AsyncCallback<MatImport> async);

    void getExecutionValueSets(String executionId, AsyncCallback<List<ValueSet>> async);

    void getStaticDbStats(AsyncCallback<Execution> async);
}
