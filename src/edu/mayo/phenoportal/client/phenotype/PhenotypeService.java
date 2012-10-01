package edu.mayo.phenoportal.client.phenotype;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.mayo.phenoportal.shared.Execution;
import edu.mayo.phenoportal.shared.News;
import edu.mayo.phenoportal.shared.SharpNews;
import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.UserRoleRequest;

@RemoteServiceRelativePath("RPCforPhenotypeCategories")
public interface PhenotypeService extends RemoteService {

    String getPhenotypeCategories(String categoryId) throws IllegalArgumentException;

    String getCriteria(String fileName, String parentId, String version)
            throws IllegalArgumentException;

    Execution executePhenotype(String fileName, String parentId, String version, Date fromDate,
            Date toDate, String userName) throws IllegalArgumentException;

    String getExecutions() throws IllegalArgumentException;

    String getUploaders() throws IllegalArgumentException;

    User getUser(String userId) throws IllegalArgumentException;

    String getUsers() throws IllegalArgumentException;

    Boolean updateUser(User user) throws IllegalArgumentException;

    Boolean removeUser(User user) throws IllegalArgumentException;

    Boolean requestPermissionUpgrade(User user) throws IllegalArgumentException;

    UserRoleRequest getUserRoleRequest(User user) throws IllegalArgumentException;

    HashMap<String, String> getHelpPages(HashMap<String, String> fileInfo)
            throws IllegalArgumentException;

    void initializeLogging() throws IllegalArgumentException;

    String getLatestUploadedAlgorithms() throws IllegalArgumentException;

    String getNews() throws IllegalArgumentException;

    Boolean addNews(News news) throws IllegalArgumentException;

    Boolean updateNews(News news) throws IllegalArgumentException;

    Boolean removeNews(News news) throws IllegalArgumentException;

    String getSharpNews() throws IllegalArgumentException;

    Boolean addSharpNews(SharpNews news) throws IllegalArgumentException;

    Boolean updateSharpNews(SharpNews news) throws IllegalArgumentException;

    Boolean removeSharpNews(SharpNews news) throws IllegalArgumentException;

    Boolean saveJbpm(String uuid, String jbpm, String title, String comment);

    Execution getLatestExecution(String algorithmName, String algorithmVersion,
            String algorithmCategoryId, String algorithmUser);

    Execution getDbStats(String type) throws IllegalArgumentException;

    String openEditor(Execution execution) throws IllegalArgumentException;

}
