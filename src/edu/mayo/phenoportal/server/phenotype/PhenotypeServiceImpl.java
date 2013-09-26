package edu.mayo.phenoportal.server.phenotype;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mayo.edu.cts2.editor.server.Cts2EditorServiceProperties;

import org.apache.commons.io.FileUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.server.database.DBConnection;
import edu.mayo.phenoportal.server.upload.ImportServlet;
import edu.mayo.phenoportal.server.utils.DOMXmlParser;
import edu.mayo.phenoportal.server.utils.DateConverter;
import edu.mayo.phenoportal.server.utils.SmtpClient;
import edu.mayo.phenoportal.shared.Demographic;
import edu.mayo.phenoportal.shared.Execution;
import edu.mayo.phenoportal.shared.MatImport;
import edu.mayo.phenoportal.shared.News;
import edu.mayo.phenoportal.shared.SharpNews;
import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.UserRoleRequest;
import edu.mayo.phenoportal.shared.ValueSet;
import edu.mayo.phenoportal.shared.database.CategoryColumns;
import edu.mayo.phenoportal.shared.database.ExecutionColumns;
import edu.mayo.phenoportal.shared.database.ExecutionValueSetColumns;
import edu.mayo.phenoportal.shared.database.NewsColumns;
import edu.mayo.phenoportal.shared.database.SharpNewsColumns;
import edu.mayo.phenoportal.shared.database.UploadColumns;
import edu.mayo.phenoportal.shared.database.UserColumns;
import edu.mayo.phenoportal.shared.database.UserRoleRequestColumns;
import edu.mayo.phenoportal.utils.SQLStatements;
import edu.mayo.phenoportal.utils.ServletUtils;

public class PhenotypeServiceImpl extends RemoteServiceServlet implements PhenotypeService {

    private static Logger s_logger = Logger.getLogger(PhenotypeServiceImpl.class.getName());
    private static final long serialVersionUID = 2L;
    private static final String ERROR_HTML = "<b>Could not retrieve the criteria information.</b>";

    // XML Settings
    public static final String ROOT = "List";
    private static String i_fileName = "";

    @Override
    /*
     * Make database connection and queries Mysql to generate the PhenotypeTree
     * XML.
     */
    public String getPhenotypeCategories(String categoryId) throws IllegalArgumentException {

        // Create an instance of class DOMXmlGenerator
        CategoryXmlGenerator generator = new CategoryXmlGenerator();

        // Create the root element
        generator.createDocumentAndRootElement(ROOT);

        // if categoryId is null, default to "0"
        categoryId = categoryId == null ? "0" : categoryId;

        // Create the Categories Xml
        getCategoriesFromDB(generator, categoryId);

        // Create the Algorithms Xml
        getAlgorithmsFromDB(generator, categoryId);

        return generator.xmlToString();
    }

    // Get the categories and create the Xml
    private void getCategoriesFromDB(CategoryXmlGenerator generator, String categoryId) {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {
                st = conn.prepareStatement(SQLStatements.selectCategoriesStatement(categoryId));
                rs = st.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt(CategoryColumns.ID.getColNum());
                    String name = rs.getString(CategoryColumns.NAME.getColNum());
                    int parentId = rs.getInt(CategoryColumns.PARENTID.getColNum());
                    int count = rs.getInt(CategoryColumns.COUNT.getColNum());
                    int level = rs.getInt(CategoryColumns.LEVEL.getColNum());

                    generator.createPhenotypeCategoriesDOMTree(id, name, parentId, count, level);
                }

            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed fetching categories from DB" + ex.getMessage());

            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
    }

    // Create Phenotype Algorithms
    private void getAlgorithmsFromDB(CategoryXmlGenerator generator, String categoryId)
            throws IllegalArgumentException {

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {
                st = conn.prepareStatement(SQLStatements.selectAlgorithmsStatement(categoryId));
                rs = st.executeQuery();

                int algorithmId;
                while (rs.next()) {
                    // create a unique id for the algorithm. This is needed for
                    // the UI tree.
                    algorithmId = rs.getInt(1);
                    int count = 0; // count is ignored for algorithms
                    int level = 4; // algorithms are always level 4
                    String algorithmName = rs.getString(2);
                    String algorithmDesc = rs.getString(3);
                    String algorithmVersion = rs.getString(4);
                    String algorithmUser = rs.getString(5);

                    generator.createPhenotypeAlgorithmsDOMTree(algorithmId, categoryId
                            + algorithmId, categoryId, count, level, algorithmName, algorithmDesc,
                            algorithmUser, algorithmVersion);
                }

            } catch (Exception ex) {

                s_logger.log(Level.SEVERE, "Failed fetching algorithms from DB" + ex.getMessage(),
                        ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
    }

    @Override
    public String getPopulationCriteria(AlgorithmData algorithmData) {
        /* TODO: check for cached version */
        String html = getHtml(algorithmData);
        StringBuilder sb = new StringBuilder();

        if (!html.equals(ERROR_HTML)) {
            String startTitle = "<title>";
            String endTitle = "</title>";
            String title = getHtmlSnippet(html, startTitle, endTitle);

            String startMatch = "<b>Initial Patient Population";
            String endMatch = "</div>";
            sb = new StringBuilder(getHtmlSnippet(html, startMatch, endMatch));
            sb.insert(
                    0,
                    "<h3>"
                            + title.substring(startTitle.length(),
                                    title.length() - endTitle.length()) + "</h3><ul><li>");
        } else {
            sb.append(html);
        }
        /* TODO: cache result */

        return sb.toString();
    }

    private final Object cts2ServerPropertiesLock = new Object();
    private boolean cts2RestPropertiesSet = false;

    @Override
    public Map<String, String> getDataCriteriaOids(AlgorithmData algorithmData) {
        /* TODO: check for cached version */
        String html = getHtml(algorithmData);

        String startMatch = "href=\"#toc\">Data criteria (QDM Data Elements)</a></h3>";
        String endMatch = "</div>";

        Map<String, String> oids = getOidsWithDescription(getHtmlSnippet(html, startMatch, endMatch));
        /* TODO: cache result */

        synchronized (cts2ServerPropertiesLock) {
            if (!cts2RestPropertiesSet) {
                Cts2EditorServiceProperties.setValueSetDefinitionMaintenanceUrl(ServletUtils
                        .getCts2RestUrl());
                Cts2EditorServiceProperties.setValueSetDefinitionMaintenanceCredentials(
                        ServletUtils.getCts2RestUser(), ServletUtils.getCts2RestPassword());
                Cts2EditorServiceProperties
                        .setValueSetDefinitionMaintenanceEntitiesUrl(ServletUtils
                                .getCts2EntityRestUrl());
                Cts2EditorServiceProperties.setValueSetRestPageSize(ServletUtils
                        .getCts2RestPageSize());
                cts2RestPropertiesSet = true;
            }
        }

        return oids;
    }

    @Override
    public Map<String, String> getSupplementalCriteriaOids(AlgorithmData algorithmData) {
        /* TODO: check for cached version */
        String html = getHtml(algorithmData);

        String startMatch = "href=\"#toc\">Supplemental Data Elements</a></h3>";
        String endMatch = "</div>";

        Map<String, String> oids = getOidsWithDescription(getHtmlSnippet(html, startMatch, endMatch));
        /* TODO: cache result */

        synchronized (cts2ServerPropertiesLock) {
            if (!cts2RestPropertiesSet) {
                Cts2EditorServiceProperties.setValueSetDefinitionMaintenanceUrl(ServletUtils
                        .getCts2RestUrl());
                Cts2EditorServiceProperties.setValueSetDefinitionMaintenanceCredentials(
                        ServletUtils.getCts2RestUser(), ServletUtils.getCts2RestPassword());
                Cts2EditorServiceProperties
                        .setValueSetDefinitionMaintenanceEntitiesUrl(ServletUtils
                                .getCts2EntityRestUrl());
                Cts2EditorServiceProperties.setValueSetRestPageSize(ServletUtils
                        .getCts2RestPageSize());
                cts2RestPropertiesSet = true;
            }
        }

        return oids;
    }

    private String getHtml(AlgorithmData algorithmData) {
        String completeHtml = null;

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st;
        ResultSet rs;

        if (conn != null) {
            try {
                st = conn.prepareStatement(SQLStatements.selectCriteriaStatement(algorithmData
                        .getId()));

                rs = st.executeQuery();

                while (rs.next()) {
                    String location = rs.getString(4);
                    String criteriaFileLocation = ServletUtils.getAlgorithmPath();

                    s_logger.fine("criteriaFileLocation:" + criteriaFileLocation);

                    String fileLocation = criteriaFileLocation + '/' + location;
                    s_logger.fine("fileLocation:" + fileLocation);

                    completeHtml = readFile(fileLocation, ERROR_HTML);
                }
            } catch (SQLException sqle) {
                s_logger.log(Level.SEVERE,
                        "failed to fetch Criteria from the HTML file" + sqle.getMessage(), sqle);
            }
        }
        if (completeHtml == null)
            completeHtml = ERROR_HTML;
        return completeHtml;
    }

    private String getHtmlSnippet(String document, String start, String end) {
        int idxStart = document.indexOf(start);
        idxStart = idxStart == -1 ? 0 : idxStart;

        String snippet = document.substring(idxStart);
        int idxEnd = snippet.indexOf(end);
        idxEnd = idxEnd == -1 ? snippet.length() : idxEnd + end.length();

        return snippet.substring(0, idxEnd);
    }

    private Map<String, String> getOidsWithDescription(String html) {
        String oidRegex = "\\((\\d+(\\.(?=\\d+))?)+\\)";
        Map<String, String> oids = new HashMap<String, String>();
        Pattern linePattern = Pattern.compile("(?<=<li>)([^<]*)(?=</li>)");
        Matcher lineMatcher = linePattern.matcher(html);
        while (lineMatcher.find()) {
            Pattern oidPattern = Pattern.compile(oidRegex);
            String desc = lineMatcher.group(0).trim();
            desc = desc.replaceAll("\"", "");
            Matcher oidMatcher = oidPattern.matcher(desc);
            if (oidMatcher.find()) {
                String oid = oidMatcher.group(0);
                oid = oid.substring(1, oid.length() - 1);
                oids.put(oid, desc.replaceAll(oidRegex, "").trim());
            }
        }
        return oids;
    }

    /**
     * Request to execute the phenotype. Will return List<Demographic> object
     * from server
     */

    @Override
    public Execution executePhenotype(AlgorithmData algorithmData, Date fromDate, Date toDate,
            String userName) throws IllegalArgumentException {

        Map<String, String> valueSets = new HashMap<String, String>();
        for (ValueSet vs : algorithmData.getValueSets()) {
            if (!vs.version.equals("1"))
                valueSets.put(vs.name, vs.version);
        }
        for (ValueSet vs : algorithmData.getSupplementalValueSets()) {
            if (!vs.version.equals("1"))
                valueSets.put(vs.name, vs.version);
        }

        String locationUrl;
        String executionStatus = "";
        Execution execution = new Execution();
        String xmlPathInfo = getXmlFile(algorithmData.getId());
        String xmlPath = ServletUtils.getAlgorithmPath() + '/' + xmlPathInfo;
        File xmlFile = new File(xmlPath);
        String executionDateRangeFrom = DateConverter.getDateString(fromDate);
        String executionDateRangeTo = DateConverter.getDateString(toDate);
        long startExecution = System.currentTimeMillis();

        try {
            /* TODO: Send the selected value sets to the executor */

            // execute the algorithm. This will return immediately with an id to
            // the resource that is executing.
            locationUrl = RestExecuter.getInstance().createExecution(xmlFile,
                    executionDateRangeFrom, executionDateRangeTo, valueSets);
            execution.setUrl(locationUrl);

            // poll on the status until it is complete
            try {
                while (!executionStatus.equals(RestExecuter.STATUS_COMPLETE)
                        && !executionStatus.equals(RestExecuter.STATUS_FAILED)) {
                    Thread.sleep(500);
                    executionStatus = RestExecuter.getInstance().pollStatus(locationUrl);
                }
            } catch (InterruptedException ie) {
                executionStatus = RestExecuter.STATUS_ERROR;
                s_logger.log(Level.SEVERE, "Rest execution not complete or failed.", ie);

            }

            // continue if the status was successful
            if (executionStatus.equals(RestExecuter.STATUS_COMPLETE)) {
                setFileName(algorithmData.getAlgorithmName(), algorithmData.getAlgorithmVersion(),
                        algorithmData.getParentId());
                persistExecution(execution);
            } else if (executionStatus.equals(RestExecuter.STATUS_FAILED)) {
                execution.setError(true);
                s_logger.log(Level.SEVERE, "Rest execution failed.");
            }

        } catch (Exception e) {
            s_logger.log(Level.SEVERE, "execution failed", e);
            executionStatus = RestExecuter.STATUS_ERROR;
            execution.setError(true);
        } finally {
            Connection conn = DBConnection.getDBConnection();
            try {
                long endExecution = System.currentTimeMillis();
                long elapsedTime = endExecution - startExecution;
                conn.setAutoCommit(false);

                Execution exeItem = new Execution();
                exeItem.setUser(userName);
                exeItem.setAlgorithmName(algorithmData.getAlgorithmName());
                exeItem.setAlgorithmVersion(algorithmData.getAlgorithmVersion());
                exeItem.setAlgorithmCategoryPath(getCategoryPath(algorithmData.getParentId()));
                exeItem.setAlgorithmCategoryId(algorithmData.getParentId());
                exeItem.setStartDate(DateConverter.getTimeString(new Date(startExecution)));
                exeItem.setEndDate(DateConverter.getTimeString(new Date(endExecution)));
                exeItem.setStatus(executionStatus);
                exeItem.setElapsedTime(elapsedTime + " ms");
                exeItem.setId(UUID.randomUUID().toString());
                exeItem.setDateRangeFrom(executionDateRangeFrom);
                exeItem.setDateRangeTo(executionDateRangeTo);
                exeItem.setXmlPath(execution.getXmlPath());
                exeItem.setImage(execution.getImage());
                exeItem.setBpmnPath(execution.getBpmnPath());
                exeItem.setRulesPath(execution.getRulesPath());

                insertExecution(conn, exeItem);
                insertExecutionValueSets(conn, exeItem, algorithmData);
            } catch (Exception e) {
                s_logger.log(Level.SEVERE, "Failed to insert execution details.", e);
                DBConnection.rollback(conn);
            } finally {
                DBConnection.commit(conn);
                DBConnection.close(conn);
            }
        }

        return execution;
    }

    @Override
    public Execution executeLastExecution(AlgorithmData algorithmData, String userName)
            throws IllegalArgumentException {

        Execution lastExecution = getLatestExecution(algorithmData.getAlgorithmName(),
                algorithmData.getAlgorithmVersion(), algorithmData.getParentId(), userName);

        List<ValueSet> valueSets = getExecutionValueSets(lastExecution.getId());

        // got latest execution and valuesets... now re-execute this one.
        algorithmData.setValueSets(valueSets);

        String dateRangeFrom = lastExecution.getDateRangeFrom();
        String dateRangeTo = lastExecution.getDateRangeTo();

        Date dateFrom = new Date(dateRangeFrom);
        Date dateTo = new Date(dateRangeTo);

        Execution newExecution = executePhenotype(algorithmData, dateFrom, dateTo, userName);

        return newExecution;
    }

    public void setFileName(String fileName, String version, String category) {
        this.i_fileName = fileName + "_" + version + "_" + category;
    }

    public String getFileName() {
        return i_fileName;
    }

    private void persistExecution(Execution executionResults) throws Exception {
        String locationUrl = executionResults.getUrl();
        executionResults.setId(UUID.randomUUID().toString());

        /* Save the execution results to the file system */
        String relativePath = executionResults.getId() + File.separator;
        String executionResultsPath = ServletUtils.getExecutionResultsPath() + File.separator
                + relativePath;

        /* Demographics */
        String returnedXml = RestExecuter.getInstance().getXml(locationUrl + "/xml");
        String demographicsFileName = "demographics.xml";
        File xmlFile = new File(executionResultsPath + demographicsFileName);
        FileUtils.writeStringToFile(xmlFile, returnedXml, "utf-8");
        List<Demographic> demographics = getDemographics(returnedXml);
        executionResults.setDemographics(demographics);
        executionResults.setXmlPath(relativePath + demographicsFileName);
    }

    public String getCategoryPath(String parentId) {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st;
        ResultSet rs;

        String currCategoryId = parentId;
        String categoryPath = "";

        while (!currCategoryId.equals("0")) { // stop when reach root
            try {
                // get the name of the current category
                st = conn.prepareStatement(SQLStatements
                        .selectCategoryNameStatement(currCategoryId));
                rs = st.executeQuery();
                while (rs.next()) {
                    categoryPath = rs.getString(1) + "/" + categoryPath;
                }

                // get the parent of the Category
                String categoryParentId = "-1";
                st = conn.prepareStatement(SQLStatements
                        .selectCategoryParentStatement(currCategoryId));
                rs = st.executeQuery();
                while (rs.next()) {
                    categoryParentId = rs.getString(1);
                }

                // take a step up the tree - the parent is now current
                currCategoryId = categoryParentId;
            } catch (SQLException ex) {

                s_logger.log(Level.INFO, "Failed to get the category path" + ex.getMessage(), ex);

            }
        }
        return categoryPath;
    }

    @Override
    public String getExecutions() throws IllegalArgumentException {
        // Create an instance of class DOMXmlGenerator
        ExecutionsXmlGenerator xmlGenerator = new ExecutionsXmlGenerator();

        // Create the root element
        xmlGenerator.createDocumentAndRootElement(ExecutionsXmlGenerator.EXECUTIONS);

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {
                st = conn.prepareStatement(SQLStatements.selectExecutionsStatement());
                rs = st.executeQuery();

                while (rs.next()) {
                    // create an xml node for this execution
                    xmlGenerator.createExecutionXml(
                            rs.getString(ExecutionColumns.USER_NAME.colNum()),
                            rs.getString(ExecutionColumns.ALG_NAME.colNum()),
                            rs.getString(ExecutionColumns.VERSION.colNum()),
                            rs.getString(ExecutionColumns.CATEGORY.colNum()),
                            rs.getString(ExecutionColumns.START_DATE.colNum()),
                            rs.getString(ExecutionColumns.END_DATE.colNum()),
                            rs.getString(ExecutionColumns.STATUS.colNum()),
                            rs.getString(ExecutionColumns.ELAPSED_TIME.colNum()));
                }
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed: getExecutions()" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return xmlGenerator.xmlToString();
    }

    @Override
    public String getUploaders() throws IllegalArgumentException {
        // Create an instance of class DOMXmlGenerator
        UploadersXmlGenerator xmlGenerator = new UploadersXmlGenerator();

        // Create the root element
        xmlGenerator.createDocumentAndRootElement(UploadersXmlGenerator.UPLOADERS);

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {
                st = conn.prepareStatement(SQLStatements.selectUploadersStatement());
                rs = st.executeQuery();

                while (rs.next()) {
                    // create an xml node for this user
                    xmlGenerator.createUploaderXml(rs.getString(UploadColumns.USER.colName()),
                            rs.getString(UploadColumns.NAME.colName()),
                            rs.getString(UploadColumns.VERSION.colName()),
                            rs.getString(UploadColumns.TYPE.colName()),
                            rs.getString(UploadColumns.PARENT_ID.colName()),
                            rs.getString(UploadColumns.UPLOAD_DATE.colName()));
                }
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to get uploaders" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return xmlGenerator.xmlToString();
    }

    @Override
    public User getUser(String userId) throws IllegalArgumentException {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        User user = new User();

        if (conn != null) {
            try {
                // get the user based on the user name. we will check the pw
                // later
                st = conn.prepareStatement(SQLStatements.selectUserStatement(userId));
                rs = st.executeQuery();

                while (rs.next()) {
                    // create a User object
                    user.setUserName(rs.getString(UserColumns.USER.colNum()));
                    user.setFirstName(rs.getString(UserColumns.FNAME.colNum()));
                    user.setLastName(rs.getString(UserColumns.LNAME.colNum()));
                    user.setEmail(rs.getString(UserColumns.EMAIL.colNum()));
                    user.setRole(rs.getInt(UserColumns.ROLE.colNum()));
                    user.setEnable(rs.getInt(UserColumns.ENABLE.colNum()));
                    user.setRegistrationDate(rs.getString(UserColumns.REGISTRATIONDATE.colNum()));
                    user.setCountryRegion(rs.getString(UserColumns.COUNTRY_OR_REGION.colNum()));
                    user.setAddress(rs.getString(UserColumns.STREET_ADDRESS.colNum()));
                    user.setCity(rs.getString(UserColumns.CITY.colNum()));
                    user.setState(rs.getString(UserColumns.STATE.colNum()));
                    user.setZipCode(rs.getString(UserColumns.ZIP.colNum()));
                    user.setPhoneNumber(rs.getString(UserColumns.PHONE_NUMBER.colNum()));
                }
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "exceptions while fetching users" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return user;
    }

    @Override
    public String getUsers() throws IllegalArgumentException {
        // Create an instance of class DOMXmlGenerator
        UsersXmlGenerator xmlGenerator = new UsersXmlGenerator();

        // Create the root element
        xmlGenerator.createDocumentAndRootElement(UsersXmlGenerator.USERS);

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {

                // get the user based on the user name. we will check the pw
                // later
                st = conn.prepareStatement(SQLStatements.selectUserStatement());
                rs = st.executeQuery();

                while (rs.next()) {
                    // create an xml node for this user
                    xmlGenerator.createUserXml(rs.getString(UserColumns.USER.colNum()),
                            rs.getString(UserColumns.FNAME.colNum()),
                            rs.getString(UserColumns.LNAME.colNum()),
                            rs.getString(UserColumns.EMAIL.colNum()),
                            rs.getString(UserColumns.ROLE.colNum()),
                            rs.getString(UserColumns.ENABLE.colNum()),
                            rs.getString(UserColumns.REGISTRATIONDATE.colNum()),
                            rs.getString(UserColumns.COUNTRY_OR_REGION.colNum()),
                            rs.getString(UserColumns.STREET_ADDRESS.colNum()),
                            rs.getString(UserColumns.CITY.colNum()),
                            rs.getString(UserColumns.STATE.colNum()),
                            rs.getString(UserColumns.ZIP.colNum()),
                            rs.getString(UserColumns.PHONE_NUMBER.colNum()));
                }
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "exceptions while fetching users" + ex.getMessage(), ex);

            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return xmlGenerator.xmlToString();
    }

    @Override
    public Boolean updateUser(User user) throws IllegalArgumentException {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {
                // update the user values
                st = conn.prepareStatement(SQLStatements.updateUserStatement(user));
                st.executeUpdate();
                success = true;
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to update users" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return success;
    }

    @Override
    public Boolean removeUser(User user) throws IllegalArgumentException {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {
                // remove the user
                st = conn.prepareStatement(SQLStatements.removeUserStatement(user));
                st.executeUpdate();
                success = true;

            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to remove users" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return success;
    }

    @Override
    public Boolean requestPermissionUpgrade(User user) throws IllegalArgumentException {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {
                long requestDate = System.currentTimeMillis();
                String requestDateStr = DateConverter.getTimeString(new Date(requestDate));

                // add request
                st = conn.prepareStatement(SQLStatements.insertUserRoleRequestStatement(user,
                        requestDateStr));
                st.executeUpdate();

                User fullUser = getUser(user.getUserName());

                // send an email to the admin and one to the user
                sendRequestPersmissionUpgradeEmailAdmin(fullUser);
                sendRequestPersmissionUpgradeEmailUser(fullUser);
                success = true;
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to remove users" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return success;
    }

    @Override
    public UserRoleRequest getUserRoleRequest(User user) throws IllegalArgumentException {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        UserRoleRequest userRoleRequest = new UserRoleRequest();

        if (conn != null) {
            try {

                // get the UserRoleRequest based on the user name. It may not
                // exist.
                st = conn.prepareStatement(SQLStatements.selectUserRoleRequestStatement(user));
                rs = st.executeQuery();

                while (rs.next()) {
                    // create a UserRoleRequest object
                    userRoleRequest.setId(rs.getString(UserRoleRequestColumns.ID.colNum()));
                    userRoleRequest.setUserName(rs.getString(UserRoleRequestColumns.USERNAME
                            .colNum()));
                    userRoleRequest.setRequestDate(rs.getString(UserRoleRequestColumns.REQUESTDATE
                            .colNum()));
                    userRoleRequest.setResponseDate(rs
                            .getString(UserRoleRequestColumns.RESPONSEDATE.colNum()));
                    userRoleRequest.setRequestGranted(rs
                            .getBoolean(UserRoleRequestColumns.REQUESTGRANTED.colNum()));
                }
            } catch (Exception ex) {

                s_logger.log(
                        Level.SEVERE,
                        "Failed to get UserRoleRequest for user " + user.getUserName() + ".\n"
                                + ex.getMessage(), ex);

            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return userRoleRequest;
    }

    @Override
    public String getUserRoleRequests() throws IllegalArgumentException {

        // Create an instance of class DOMXmlGenerator
        UserRoleRequestXmlGenerator xmlGenerator = new UserRoleRequestXmlGenerator();

        // Create the root element
        xmlGenerator.createDocumentAndRootElement(UserRoleRequestXmlGenerator.USER_ROLE_REQUESTS);

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {

                // get the all user role requests.
                st = conn.prepareStatement(SQLStatements.selectUserRoleRequestsStatement());
                rs = st.executeQuery();

                while (rs.next()) {
                    // create an xml node for this user role request
                    xmlGenerator.createUserRoleRquestXml(
                            rs.getString(UserRoleRequestColumns.ID.colNum()),
                            rs.getString(UserRoleRequestColumns.USERNAME.colNum()),
                            rs.getString(UserRoleRequestColumns.REQUESTDATE.colNum()),
                            rs.getString(UserRoleRequestColumns.RESPONSEDATE.colNum()),
                            rs.getString(UserRoleRequestColumns.REQUESTGRANTED.colNum()));
                }
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE,
                        "exceptions while fetching user role requests." + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return xmlGenerator.xmlToString();
    }

    @Override
    public Boolean updateUserRoleRequest(UserRoleRequest userRoleRequest)
            throws IllegalArgumentException {

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {
                // update the UserRoleRequest values
                st = conn.prepareStatement(SQLStatements
                        .updateUserRoleRequestStatement(userRoleRequest));
                int result = st.executeUpdate();

                if (result == 1) {
                    // now update the role based on if the request was granted
                    // or denied. 2 = execute, 3 = read only
                    int role = userRoleRequest.isRequestGranted() ? 2 : 3;

                    // update the User role
                    st = conn.prepareStatement(SQLStatements.updateUserRoleStatement(
                            userRoleRequest.getUserName(), role));
                    st.executeUpdate();

                    User user = getUser(userRoleRequest.getUserName());
                    sendResponsePersmissionUpgradeEmail(user, userRoleRequest.isRequestGranted());
                }
                success = true;
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to update UserRoleRequest" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return success;
    }

    @Override
    public HashMap<String, String> getHelpPages(HashMap<String, String> fileInfo)
            throws IllegalArgumentException {
        String errorHtml = "<b>Information not available</b>";

        HashMap<String, String> fileContents = new HashMap<String, String>();

        Set<String> keys = fileInfo.keySet();
        Iterator<String> it = keys.iterator();

        while (it.hasNext()) {
            String fileTitle = it.next();
            String fileName = fileInfo.get(fileTitle);

            String fileData = readFile(getServletContext().getContextPath() + File.pathSeparator
                    + fileName, errorHtml);
            fileContents.put(fileTitle, fileData);

        }
        return fileContents;
    }

    /**
     * Get the zip file path from the db.
     * 
     * @param algorithmId
     * @return
     */
    private String getZipFile(int algorithmId) {
        String zipPath = null;

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {
                st = conn.prepareStatement(SQLStatements.selectZipFileStatement(algorithmId));
                rs = st.executeQuery();
                if (rs.next()) {
                    zipPath = rs.getString(1);
                }
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to fetch zip files" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return zipPath;
    }

    private String getXmlFile(int algorithmId) {
        String xmlPath = null;

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {
                st = conn.prepareStatement(SQLStatements.selectXmlFileStatement(algorithmId));
                rs = st.executeQuery();
                if (rs.next()) {
                    xmlPath = rs.getString(1);
                }
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to fetch xml file" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return xmlPath;
    }

    private List<Demographic> getDemographics(String xml) {
        List<Demographic> demographics = new ArrayList<Demographic>();

        if (xml != null && xml.length() > 1) {
            DOMXmlParser parser = new DOMXmlParser();
            demographics = parser.parseXmlFromString(xml);
        }

        return demographics;
    }

    private String readFile(String fileName, String errorString) {

        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                sb.append(strLine);
            }
            in.close();
        } catch (Exception e) {// Catch exception if any
            s_logger.log(Level.SEVERE, "Error reading the file:" + e.getMessage());
            return errorString + "<b/>" + e.getMessage();
        }
        return sb.toString();
    }

    private String readFile(InputStream inStream, String errorString) {

        StringBuilder sb = new StringBuilder();
        try {
            DataInputStream in = new DataInputStream(inStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                sb.append(strLine);
            }
            in.close();
        } catch (Exception e) {// Catch exception if any
            s_logger.log(Level.SEVERE, "Error reading the file:" + e.getMessage());
            return errorString + "<br/>" + e.getMessage();
        }
        return sb.toString();
    }

    /**
     * Method to read the logging properties
     */

    @Override
    public void initializeLogging() throws IllegalArgumentException {
        ServletUtils.initializeLogging();
    }

    /**
     * Get the last n algorithms uploaded.
     */
    @Override
    public String getLatestUploadedAlgorithms() throws IllegalArgumentException {

        // Create an instance of class DOMXmlGenerator
        AlgorithmXmlGenerator xmlGenerator = new AlgorithmXmlGenerator();

        // Create the root element
        xmlGenerator.createDocumentAndRootElement(AlgorithmXmlGenerator.ALGORITHMS);

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {
                int limit = ServletUtils.getRecentlyUploadedLimit();

                // get the latest algorithms based on uploaded date.
                st = conn.prepareStatement(SQLStatements.selectRecentlyUploadedAlgorithms(limit));
                rs = st.executeQuery();

                while (rs.next()) {
                    // create an xml node for this algorithm
                    xmlGenerator.createAlgorithmXml(rs.getString(UploadColumns.PARENT_ID.colNum()),
                            rs.getString(UploadColumns.NAME.colNum()),
                            rs.getString(UploadColumns.VERSION.colNum()),
                            rs.getString(UploadColumns.USER.colNum()),
                            rs.getString(UploadColumns.DESCRIPTION.colNum()),
                            rs.getString(UploadColumns.INSTITUTION.colNum()),
                            rs.getString(UploadColumns.UPLOAD_DATE.colNum()),
                            rs.getString(UploadColumns.CREATEDATE.colNum()),
                            rs.getString(UploadColumns.COMMENT.colNum()),
                            rs.getString(UploadColumns.STATUS.colNum()),
                            rs.getString(UploadColumns.ASSOC_LINK.colNum()),
                            rs.getString(UploadColumns.ASSOC_NAME.colNum()));
                }
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE,
                        "exceptions while fetching algorithms" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return xmlGenerator.xmlToString();
    }

    /**
     * Get the news from the News table
     */
    @Override
    public String getNews() throws IllegalArgumentException {

        // Create an instance of class DOMXmlGenerator
        NewsXmlGenerator xmlGenerator = new NewsXmlGenerator();

        // Create the root element
        xmlGenerator.createDocumentAndRootElement(NewsXmlGenerator.NEW_ITEMS);

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {
                // get the news.
                st = conn.prepareStatement(SQLStatements.selectNews());
                rs = st.executeQuery();
                while (rs.next()) {
                    // create an xml node for this news item
                    xmlGenerator.createNewsXml(rs.getString(NewsColumns.ID.colNum()),
                            rs.getString(NewsColumns.DATE.colNum()),
                            rs.getString(NewsColumns.INFO.colNum()));
                }
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE,
                        "exceptions while fetching news items" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return xmlGenerator.xmlToString();
    }

    /**
     * Add a news item.
     */
    @Override
    public Boolean addNews(News news) throws IllegalArgumentException {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {
                // add the news item
                st = conn.prepareStatement(SQLStatements.insertNewsStatement(news));
                st.executeUpdate();
                success = true;
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to add news item" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return success;
    }

    /**
     * Update a news item.
     */
    @Override
    public Boolean updateNews(News news) throws IllegalArgumentException {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {
                // update the news values
                st = conn.prepareStatement(SQLStatements.updateNewsStatement(news));
                st.executeUpdate();
                success = true;
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to update news item" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return success;
    }

    @Override
    public Boolean removeNews(News news) throws IllegalArgumentException {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {
                // remove the news item
                st = conn.prepareStatement(SQLStatements.removeNewsStatement(news));
                st.executeUpdate();
                success = true;

            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to remove news item" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }

        return success;
    }

    /**
     * Get the Sharp News from the SharpNews table.
     */
    @Override
    public String getSharpNews() throws IllegalArgumentException {

        // Create an instance of class DOMXmlGenerator
        SharpNewsXmlGenerator xmlGenerator = new SharpNewsXmlGenerator();

        // Create the root element
        xmlGenerator.createDocumentAndRootElement(SharpNewsXmlGenerator.SHARP_NEWS_ITEMS);

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        if (conn != null) {
            try {

                // get the latest sharp news.
                st = conn.prepareStatement(SQLStatements.selectSharpNews());
                rs = st.executeQuery();

                while (rs.next()) {
                    // create an xml node for this news item
                    xmlGenerator.createSharpNewsXml(rs.getString(SharpNewsColumns.ID.colNum()),
                            rs.getString(SharpNewsColumns.INFO.colNum()));
                }
            } catch (Exception ex) {

                s_logger.log(Level.SEVERE,
                        "exceptions while fetching sharp news items" + ex.getMessage(), ex);

            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }

        return xmlGenerator.xmlToString();
    }

    @Override
    public Boolean addSharpNews(SharpNews news) throws IllegalArgumentException {

        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {
                // add the news item
                st = conn.prepareStatement(SQLStatements.insertSharpNewsStatement(news));
                st.executeUpdate();
                success = true;
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to add Sharp news item" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return success;
    }

    @Override
    public Boolean updateSharpNews(SharpNews news) throws IllegalArgumentException {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {
                // update the news values
                st = conn.prepareStatement(SQLStatements.updateSharpNewsStatement(news));
                st.executeUpdate();
                success = true;

            } catch (Exception ex) {

                s_logger.log(Level.SEVERE, "Failed to update Sharp news item" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return success;
    }

    @Override
    public Boolean removeSharpNews(SharpNews news) throws IllegalArgumentException {
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean success = false;

        if (conn != null) {
            try {
                // remove the news item
                st = conn.prepareStatement(SQLStatements.removeSharpNewsStatement(news));
                st.executeUpdate();
                success = true;
            } catch (Exception ex) {
                s_logger.log(Level.SEVERE, "Failed to remove Sharp news item" + ex.getMessage(), ex);
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return success;
    }

    private static final String DB_STATS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<Demographics>\n" + "\t<DemographicType>\n"
            + "\t\t<type>Initial Patient Population</type>\n" + "\t\t<DemographicCategory>\n"
            + "\t\t\t<name>gender</name>\n" + "\t\t\t<DemographicStat>\n"
            + "\t\t\t\t<label>female</label>\n" + "\t\t\t\t<value>18</value>\n"
            + "\t\t\t</DemographicStat>\n" + "\t\t\t<DemographicStat>\n"
            + "\t\t\t\t<label>male</label>\n" + "\t\t\t\t<value>18</value>\n"
            + "\t\t\t</DemographicStat>\n" + "\t\t</DemographicCategory>\n"
            + "\t\t<DemographicCategory>\n" + "\t\t\t<name>age</name>\n"
            + "\t\t\t<DemographicStat>\n" + "\t\t\t\t<label>(0,18)</label>\n"
            + "\t\t\t\t<value>12</value>\n" + "\t\t\t</DemographicStat>\n"
            + "\t\t\t<DemographicStat>\n" + "\t\t\t\t<label>(19,30)</label>\n"
            + "\t\t\t\t<value>9</value>\n" + "\t\t\t</DemographicStat>\n"
            + "\t\t\t<DemographicStat>\n" + "\t\t\t\t<label>(30,60)</label>\n"
            + "\t\t\t\t<value>8</value>\n" + "\t\t\t</DemographicStat>\n"
            + "\t\t\t<DemographicStat>\n" + "\t\t\t\t<label>(60,75)</label>\n"
            + "\t\t\t\t<value>6</value>\n" + "\t\t\t</DemographicStat>\n"
            + "\t\t\t<DemographicStat>\n" + "\t\t\t\t<label>(75,above)</label>\n"
            + "\t\t\t\t<value>1</value>\n" + "\t\t\t</DemographicStat>\n"
            + "\t\t</DemographicCategory>\n" + "\t\t<DemographicCategory>\n"
            + "\t\t\t<name>race</name>\n" + "\t\t\t<DemographicStat>\n"
            + "\t\t\t\t<label>American Indian</label>\n" + "\t\t\t\t<value>36</value>\n"
            + "\t\t\t</DemographicStat>\n" + "\t\t</DemographicCategory>\n"
            + "\t\t<DemographicCategory>\n" + "\t\t\t<name>ethnicity</name>\n"
            + "\t\t\t<DemographicStat>\n" + "\t\t\t\t<label>Non Hispanic or Latio</label>\n"
            + "\t\t\t\t<value>36</value>\n" + "\t\t\t</DemographicStat>\n"
            + "\t\t</DemographicCategory>\n" + "\t</DemographicType>\n" + "</Demographics>";

    @Override
    public Execution getDbStats(String type) throws IllegalArgumentException {
        // TODO - This is getting execution results from a file...
        // We need to make a REST call to get the actual data in the future.
        // String xml =
        // readFile(PhenotypeServiceImpl.class.getResourceAsStream("diseaseData.xml"),
        // "Error reading DB stats file.");

        List<Demographic> demographics = getDemographics(DB_STATS);
        Execution executionResults = new Execution();
        executionResults.setDemographics(demographics);

        return executionResults;
    }

    @Override
    public Execution getStaticDbStats() throws IllegalArgumentException {
        List<Demographic> demographics = getDemographics(DB_STATS);
        Execution executionResults = new Execution();
        executionResults.setDemographics(demographics);
        return executionResults;
    }

    private void insertExecution(Connection conn, Execution execution) throws SQLException {
        PreparedStatement st = SQLStatements.insertExecutionStatement(conn, execution);
        try {
            if (st != null) {
                st.execute();
            }
        } finally {
            DBConnection.close(st);
        }
    }

    private void insertExecutionValueSets(Connection conn, Execution execution,
            AlgorithmData algorithmData) {
        String query = SQLStatements.insertExecutionValueSetsStatement();
        PreparedStatement ps = null;

        try {
            for (ValueSet vs : algorithmData.getValueSets()) {
                ps = conn.prepareStatement(query);
                ps.setString(1, execution.getId());
                ps.setString(2, vs.name);
                ps.setString(3, vs.description);
                ps.setString(4, vs.version);
                ps.setString(5, vs.comment);
                ps.execute();
            }
            for (ValueSet vs : algorithmData.getSupplementalValueSets()) {
                ps = conn.prepareStatement(query);
                ps.setString(1, execution.getId());
                ps.setString(2, vs.name);
                ps.setString(3, vs.description);
                ps.setString(4, vs.version);
                ps.setString(5, vs.comment);
                ps.execute();
            }

        } catch (SQLException sqle) {
            System.out.println("Failed to insert ExecutionValueSets. Error: " + sqle.getMessage());
        } finally {
            DBConnection.close(ps);
        }
    }

    @Override
    public String getMatEditorUrl(User user) {
        String url;
        if (user != null) {
            url = ServletUtils.getMatEditorUrl() + "/Login.html?userId=" + user.getUserName()
                    + "&htpId=" + user.getPassword();
        } else {
            url = ServletUtils.getMatEditorUrl();
        }
        return url;
    }

    @Override
    public MatImport getMatImport(String tokenId) throws IllegalArgumentException {
        return ImportServlet.getMatImport(tokenId);
    }

    @Override
    public Execution getLatestExecution(String algorithmName, String algorithmVersion,
            String algorithmCategoryId, String algorithmUser) {
        Connection connection = DBConnection.getDBConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        Execution execution = new Execution();
        execution.setAlgorithmName(algorithmName);
        execution.setAlgorithmVersion(algorithmVersion);
        execution.setAlgorithmCategoryId(algorithmCategoryId);
        execution.setUser(algorithmUser);

        try {
            statement = SQLStatements.selectLatestUsersExecutionStatement(connection, execution);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                execution.setId(resultSet.getString(ExecutionColumns.ID.colName()));
                execution.setStartDate(resultSet.getString(ExecutionColumns.START_DATE.colName()));
                execution.setElapsedTime(resultSet.getString(ExecutionColumns.ELAPSED_TIME
                        .colName()));
                execution.setDateRangeFrom(resultSet.getString(ExecutionColumns.DATE_RANGE_FROM
                        .colName()));
                execution.setDateRangeTo(resultSet.getString(ExecutionColumns.DATE_RANGE_TO
                        .colName()));
                execution.setXmlPath(resultSet.getString(ExecutionColumns.XML_PATH.colName()));
                execution.setAlgorithmCategoryId(resultSet.getString(ExecutionColumns.CATEGORY_NUM
                        .colName()));
                execution.setAlgorithmCategoryPath(resultSet.getString(ExecutionColumns.CATEGORY
                        .colName()));
                execution.setBpmnPath(resultSet.getString(ExecutionColumns.BPMN_PATH.colName()));
                execution.setRulesPath(resultSet.getString(ExecutionColumns.RULES_PATH.colName()));

                /* Demographics */
                if (execution.getXmlPath() != null) {
                    File xmlFile = new File(ServletUtils.getExecutionResultsPath() + File.separator
                            + execution.getXmlPath());
                    String xmlString = FileUtils.readFileToString(xmlFile);
                    List<Demographic> demographics = getDemographics(xmlString);
                    execution.setDemographics(demographics);
                }
            }

        } catch (SQLException sqle) {
            s_logger.log(Level.WARNING, "Unable to get the latest users executions.", sqle);
        } catch (IOException ioe) {
            s_logger.log(Level.WARNING, "Unable to get the latest users executions.", ioe);
        } finally {
            DBConnection.closeConnection(connection, statement, resultSet);
        }

        return execution;
    }

    @Override
    public List<ValueSet> getExecutionValueSets(String executionId) {
        if (executionId == null || executionId.trim().isEmpty()) {
            throw new IllegalArgumentException("ExecutionId cannot be null or empty.");
        }

        List<ValueSet> valueSets = new ArrayList<ValueSet>();
        String query = SQLStatements.getExecutionValueSets();
        Connection connection = DBConnection.getDBConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, executionId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                valueSets.add(new ValueSet(resultSet.getString(ExecutionValueSetColumns.VALUE_SET
                        .getColumnName()), resultSet.getString(ExecutionValueSetColumns.DESCRIPTION
                        .getColumnName()), resultSet.getString(ExecutionValueSetColumns.VERSION
                        .getColumnName()), resultSet.getString(ExecutionValueSetColumns.COMMENT
                        .getColumnName())));
            }
        } catch (SQLException sqle) {
            s_logger.log(Level.WARNING, "Unable to get the value sets for execution " + executionId
                    + ".", sqle);
        } finally {
            DBConnection.closeConnection(connection, statement, resultSet);
        }

        return valueSets;
    }

    private void sendRequestPersmissionUpgradeEmailAdmin(User user) {
        String host = ServletUtils.getSmtpHost();
        String from = ServletUtils.getSmtpFromAddress();
        String messageText = ServletUtils.getEmailContentsUserRoleRequestAdmin();
        String port = ServletUtils.getSmtpPort();
        String pw = ServletUtils.getSmtpPassword();

        SmtpClient.sendRequestPersmissionUpgradeEmailAdmin(host, from, pw, port, messageText, user);
    }

    private void sendRequestPersmissionUpgradeEmailUser(User user) {
        String host = ServletUtils.getSmtpHost();
        String from = ServletUtils.getSmtpFromAddress();
        String messageText = ServletUtils.getEmailContentsUserRoleRequestUser();
        String port = ServletUtils.getSmtpPort();
        String pw = ServletUtils.getSmtpPassword();

        SmtpClient.sendRequestPersmissionUpgradeEmailUser(host, from, pw, port, messageText, user);
    }

    private void sendResponsePersmissionUpgradeEmail(User user, boolean granted) {
        String host = ServletUtils.getSmtpHost();
        String from = ServletUtils.getSmtpFromAddress();
        String messageText = granted ? ServletUtils.getEmailContentsUserRoleReplyGranted()
                : ServletUtils.getEmailContentsUserRoleReplyDenied();
        String port = ServletUtils.getSmtpPort();
        String pw = ServletUtils.getSmtpPassword();

        SmtpClient.sendResponsePersmissionUpgradeEmail(host, from, pw, port, messageText, user);
    }

}
