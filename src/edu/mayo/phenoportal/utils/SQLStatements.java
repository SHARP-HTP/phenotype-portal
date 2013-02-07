package edu.mayo.phenoportal.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.mayo.phenoportal.server.authentication.AuthenticationServiceImpl;
import edu.mayo.phenoportal.server.upload.UploadItems;
import edu.mayo.phenoportal.shared.Drools;
import edu.mayo.phenoportal.shared.Execution;
import edu.mayo.phenoportal.shared.News;
import edu.mayo.phenoportal.shared.SharpNews;
import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.UserRoleRequest;
import edu.mayo.phenoportal.shared.database.CategoryColumns;
import edu.mayo.phenoportal.shared.database.DroolsColumns;
import edu.mayo.phenoportal.shared.database.ExecutionColumns;
import edu.mayo.phenoportal.shared.database.NewsColumns;
import edu.mayo.phenoportal.shared.database.SharpNewsColumns;
import edu.mayo.phenoportal.shared.database.UploadColumns;
import edu.mayo.phenoportal.shared.database.UserColumns;
import edu.mayo.phenoportal.shared.database.UserRoleRequestColumns;

public class SQLStatements {

    private static Logger logger = Logger.getLogger(SQLStatements.class.getName());

    public static String countAlgorithmSiblings(String parentId) {
        return "SELECT COUNT(*) FROM Upload " + "WHERE " + UploadColumns.ID.colName() + " = "
                + parentId + ";";
    }

    public static PreparedStatement insertExecutionStatement(Connection connection,
            Execution execution) throws SQLException {
        if (connection != null) {
            PreparedStatement ps = connection
                    .prepareStatement(String
                            .format("INSERT INTO Execution (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                    ExecutionColumns.ID.colName(),
                                    ExecutionColumns.USER_NAME.colName(),
                                    ExecutionColumns.ALG_NAME.colName(),
                                    ExecutionColumns.VERSION.colName(),
                                    ExecutionColumns.CATEGORY_NUM.colName(),
                                    ExecutionColumns.CATEGORY.colName(),
                                    ExecutionColumns.START_DATE.colName(),
                                    ExecutionColumns.END_DATE.colName(),
                                    ExecutionColumns.STATUS.colName(),
                                    ExecutionColumns.ELAPSED_TIME.colName(),
                                    ExecutionColumns.DATE_RANGE_FROM.colName(),
                                    ExecutionColumns.DATE_RANGE_TO.colName(),
                                    ExecutionColumns.XML_PATH.colName(),
                                    ExecutionColumns.IMAGE_PATH.colName(),
                                    ExecutionColumns.BPMN_PATH.colName(),
                                    ExecutionColumns.RULES_PATH.colName()));

            ps.setString(1, execution.getId());
            ps.setString(2, execution.getUser());
            ps.setString(3, execution.getAlgorithmName());
            ps.setString(4, execution.getAlgorithmVersion());
            ps.setString(5, execution.getAlgorithmCategoryId());
            ps.setString(6, execution.getAlgorithmCategoryPath());
            ps.setString(7, execution.getStartDate());
            ps.setString(8, execution.getEndDate());
            ps.setString(9, execution.getStatus());
            ps.setString(10, execution.getElapsedTime());
            ps.setString(11, execution.getDateRangeFrom());
            ps.setString(12, execution.getDateRangeTo());
            ps.setString(13, execution.getXmlPath());
            ps.setString(14, execution.getImage().getImagePath());
            ps.setString(15, execution.getBpmnPath());
            ps.setString(16, execution.getRulesPath());

            logger.log(Level.FINE, ps.toString());
            return ps;
        } else {
            throw new SQLException("Unable to establish a connection, connection is null.");
        }

    }

    /**
     * Returns string to create a prepared statement to insert upload items. Id,
     * Parameter Order: Name, User, Version, Description, Institution,
     * CreateDate, Comment, Xml Path, Xls Path, Html Path, Zip Path, Word Path,
     * Status, AssocLink, AssocName, Upload Date
     * 
     * @param formItems
     *            to insert
     * @return SQL statement string
     */
    public static String insertUploadStatement(UploadItems formItems) {

        return "INSERT INTO Upload " + "(" + UploadColumns.ID.colName() + ","
                + UploadColumns.NAME.colName() + "," + UploadColumns.USER.colName() + ","
                + UploadColumns.VERSION.colName() + "," + UploadColumns.DESCRIPTION.colName() + ","
                + UploadColumns.INSTITUTION.colName() + "," + UploadColumns.CREATEDATE.colName()
                + "," + UploadColumns.COMMENT.colName() + "," + UploadColumns.XML_FILE.colName()
                + "," + UploadColumns.XLS_FILE.colName() + "," + UploadColumns.HTML_FILE.colName()
                + "," + UploadColumns.ZIP_FILE.colName() + "," + UploadColumns.WORD_FILE.colName()
                + "," + UploadColumns.STATUS.colName() + "," + UploadColumns.ASSOC_LINK.colName()
                + "," + UploadColumns.ASSOC_NAME.colName() + ","
                + UploadColumns.UPLOAD_DATE.colName()
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
    }

    public static String insertUserStatement(User user, String pwHashed) {
        return "INSERT INTO User " + "(" + UserColumns.USER.colName() + ","
                + UserColumns.FNAME.colName() + "," + UserColumns.LNAME.colName() + ","
                + UserColumns.EMAIL.colName() + "," + UserColumns.PASS.colName() + ","
                + UserColumns.ROLE.colName() + "," + UserColumns.ENABLE.colName() + ","
                + UserColumns.REGISTRATIONDATE.colName() + ","
                + UserColumns.COUNTRY_OR_REGION.colName() + ","
                + UserColumns.STREET_ADDRESS.colName() + "," + UserColumns.CITY.colName() + ","
                + UserColumns.STATE.colName() + "," + UserColumns.ZIP.colName() + ","
                + UserColumns.PHONE_NUMBER.colName() + ") VALUES (" + "'" + user.getUserName()
                + "'," + "'" + user.getFirstName() + "'," + "'" + user.getLastName() + "'," + "'"
                + user.getEmail() + "'," + "'" + pwHashed + "'," + "'"
                + AuthenticationServiceImpl.DEFAULT_USER_ROLE + "'," + "'"
                + AuthenticationServiceImpl.DEFAULT_USER_ENABLED + "'," + "'"
                + user.getRegistrationDate() + "'," + "'" + user.getCountryRegion() + "'," + "'"
                + user.getAddress() + "'," + "'" + user.getCity() + "'," + "'" + user.getState()
                + "'," + "'" + user.getZipCode() + "'," + "'" + user.getPhoneNumber() + "'" + ");";
    }

    public static String selectAlgorithmsStatement(String categoryId) {
        return "SELECT " + UploadColumns.NAME.colName() + ", "
                + UploadColumns.DESCRIPTION.colName() + ", " + UploadColumns.VERSION.colName()
                + ", " + UploadColumns.USER.colName() + " FROM Upload WHERE "
                + UploadColumns.ID.colName() + "= '" + categoryId + "'" + " ORDER by "
                + UploadColumns.NAME + ";";
    }

    public static String selectZipFileStatement(String fileName, String parentId, String version) {
        return "SELECT " + UploadColumns.ZIP_FILE.colName() + " FROM Upload WHERE "
                + UploadColumns.ID.colName() + "= '" + parentId + "' AND "
                + UploadColumns.NAME.colName() + "= '" + fileName + "' AND "
                + UploadColumns.VERSION.colName() + "= '" + version + "';";

    }

    public static String selectCriteriaStatement(String fileName, String parentId, String version) {
        return "SELECT " + UploadColumns.NAME.colName() + ", " + UploadColumns.ID.colName() + ", "
                + UploadColumns.VERSION.colName() + ", " + UploadColumns.HTML_FILE.colName()
                + " FROM Upload WHERE " + UploadColumns.ID.colName() + "= '" + parentId + "' AND "
                + UploadColumns.NAME.colName() + "= '" + fileName + "' AND "
                + UploadColumns.VERSION.colName() + "= '" + version + "';";
    }

    public static String selectCategoryNameStatement(String childId) {
        return "SELECT " + CategoryColumns.NAME + " FROM Category WHERE "
                + CategoryColumns.ID.getColName() + " =  '" + childId + "';";
    }

    public static String selectCategoryParentStatement(String childId) {
        return "SELECT " + CategoryColumns.PARENTID + " FROM Category WHERE "
                + CategoryColumns.ID.getColName() + " =  '" + childId + "';";
    }

    public static String selectCategorySiblingsStatement(String categoryParentId) {
        return "SELECT " + CategoryColumns.COUNT.getColName() + " FROM Category " + "WHERE "
                + CategoryColumns.PARENTID.getColName() + " = " + categoryParentId + ";";
    }

    public static String selectCategoriesStatement(String categoryId) {
        return "SELECT * FROM Category WHERE " + CategoryColumns.PARENTID.getColName() + " =  '"
                + categoryId + "'" + " ORDER by " + CategoryColumns.NAME + ";";
    }

    public static String selectExecutionsStatement() {
        return "SELECT * FROM Execution ORDER by " + ExecutionColumns.USER_NAME.colName() + ";";
    }

    public static String selectUploadStatement(String parentId, String fileName, String version) {
        return "SELECT * " + " FROM Upload where " + UploadColumns.ID.colName() + " = '" + parentId
                + "' and " + UploadColumns.NAME.colName() + " = '" + fileName + "' and "
                + UploadColumns.VERSION.colName() + " = '" + version + "';";
    }

    public static String selectUploadersStatement() {
        return "SELECT * FROM Upload ORDER by " + UploadColumns.USER.colName() + ";";
    }

    public static String selectUserStatement() {
        return "SELECT * FROM User;";
    }

    public static String selectUserStatement(String name) {
        return "SELECT * FROM User where " + UserColumns.USER.colName() + " = '" + name + "';";
    }

    public static String updateUserStatement(User user) {
        return "UPDATE User SET " + UserColumns.FNAME.colName() + "='"+ user.getFirstName() + "', "
                + UserColumns.LNAME.colName() + "='" + user.getLastName() + "', " 
                + UserColumns.EMAIL.colName()  + "='" + user.getEmail() + "', " 
                + UserColumns.ROLE.colName() + "='" + user.getRole() + "', " 
                + UserColumns.ENABLE.colName() + "='"+ user.getEnable() + "', " 
                // + UserColumns.REGISTRATIONDATE.colName() + "='" +
                // user.getRegistrationDate()
                // + "', "
                + UserColumns.COUNTRY_OR_REGION.colName() + "='" + user.getCountryRegion() + "', "
                + UserColumns.STREET_ADDRESS.colName() + "='" + user.getAddress() + "', "
                + UserColumns.CITY.colName() + "='" + user.getCity() + "', "
                + UserColumns.STATE.colName() + "='" + user.getState() + "', "
                + UserColumns.ZIP.colName() + "='" + user.getZipCode() + "', "
                + UserColumns.PHONE_NUMBER.colName() + "='" + user.getPhoneNumber() + "' "

                + " WHERE username='" + user.getUserName() + "';";
    }

    public static String updateUserPasswordStatement(String userId, String pw) {
        return "UPDATE User SET " + UserColumns.PASS.colName() + "='" + pw + "' "
                + " WHERE username='" + userId + "';";
    }

    public static String updateUserRoleStatement(String userId, int role) {
        return "UPDATE User SET " + UserColumns.ROLE.colName() + "='" + role + "' "
                + " WHERE username='" + userId + "';";
    }
    
    public static String updateCategoryParentCount(int numAlgSibs, String parentId) {
        return "UPDATE Category SET " + CategoryColumns.COUNT.getColName() + "= '" + numAlgSibs
                + "'" + " WHERE " + CategoryColumns.ID + " = " + parentId + ";";
    }

    public static String removeUserStatement(User user) {
        return "DELETE FROM User " + " WHERE username='" + user.getUserName() + "';";
    }

    public static String selectRecentlyUploadedAlgorithms(int limit) {
        return "SELECT * FROM Upload " + "ORDER BY " + UploadColumns.UPLOAD_DATE.colName()
                + " DESC " + "LIMIT " + limit + ";";
    }

    public static String selectNews() {
        return "SELECT * FROM News " + "ORDER BY " + NewsColumns.DATE.colName() + " ASC;";
    }

    public static String insertNewsStatement(News news) {
        return "Insert INTO News (" + NewsColumns.ID.colName() + "," + NewsColumns.DATE.colName()
                + "," + NewsColumns.INFO.colName() + ") " + "VALUES ('" + news.getId() + "', '"
                + news.getDate() + "', '" + news.getInformation() + "');";
    }

    public static String updateNewsStatement(News news) {
        return "UPDATE News SET " + NewsColumns.DATE.colName() + "='" + news.getDate() + "', "
                + NewsColumns.INFO.colName() + "='" + news.getInformation() + "' " + " WHERE id ='"
                + news.getId() + "';";
    }

    public static String removeNewsStatement(News news) {
        return "DELETE FROM News " + " WHERE id='" + news.getId() + "';";
    }

    public static String selectSharpNews() {
        return "SELECT * FROM SharpNews;";
    }

    public static String insertSharpNewsStatement(SharpNews news) {
        return "Insert INTO SharpNews (" + SharpNewsColumns.ID.colName() + ","
                + SharpNewsColumns.INFO.colName() + ") " + "VALUES ('" + news.getId() + "', '"
                + news.getInformation() + "');";
    }

    public static String updateSharpNewsStatement(SharpNews news) {
        return "UPDATE SharpNews SET " + SharpNewsColumns.INFO.colName() + "='"
                + news.getInformation() + "' " + " WHERE id ='" + news.getId() + "';";
    }

    public static String removeSharpNewsStatement(SharpNews news) {
        return "DELETE FROM SharpNews " + " WHERE id='" + news.getId() + "';";
    }

    public static String insertUserRoleRequestStatement(User user, String requestDate) {
        return "Insert INTO UserRoleRequest (" + UserRoleRequestColumns.USERNAME.colName() + ","
                + UserRoleRequestColumns.REQUESTDATE.colName() + ") " + "VALUES ('"
                + user.getUserName() + "', '" + requestDate + "');";
    }

    public static String selectUserRoleRequestStatement(User user) {
        return "SELECT * FROM UserRoleRequest where " + UserRoleRequestColumns.USERNAME.colName()
                + " = '" + user.getUserName() + "';";
    }

    public static String selectUserRoleRequestsStatement() {
        return "SELECT * FROM UserRoleRequest;";
    }

    public static String updateUserRoleRequestStatement(UserRoleRequest userRoleRequest) {

        int granted = userRoleRequest.isRequestGranted() ? 1 : 0;

        return "UPDATE UserRoleRequest SET " + UserRoleRequestColumns.RESPONSEDATE.colName() + "='"
                + userRoleRequest.getResponseDate() + "', "
                + UserRoleRequestColumns.REQUESTGRANTED.colName() + "='" + granted + "'"
                + " WHERE username='" + userRoleRequest.getUserName() + "';";
    }

    public static PreparedStatement insertDroolsStatement(Connection connection, Drools drools)
            throws SQLException {
        if (connection != null) {
            PreparedStatement ps = connection
                    .prepareStatement(String
                            .format("INSERT INTO Drools (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                    DroolsColumns.ID.getColName(),
                                    DroolsColumns.PARENT_ID.getColName(),
                                    DroolsColumns.BPMN_PATH.getColName(),
                                    DroolsColumns.IMAGE_PATH.getColName(),
                                    DroolsColumns.RULES_PATH.getColName(),
                                    DroolsColumns.TITLE.getColName(),
                                    DroolsColumns.COMMENT.getColName(),
                                    DroolsColumns.USERNAME.getColName(),
                                    DroolsColumns.EDITABLE.getColName(),
                                    DroolsColumns.CATEGORY_ID.getColName()));
            ps.setString(1, drools.getId());
            ps.setString(2, drools.getParentId());
            ps.setString(3, drools.getBpmnPath());
            ps.setString(4, drools.getImagePath());
            ps.setString(5, drools.getRulesPath());
            ps.setString(6, drools.getTitle());
            ps.setString(7, drools.getComment());
            ps.setString(8, drools.getUsername());
            ps.setBoolean(9, drools.isEditable());
            ps.setInt(10, drools.getCategoryNum());

            logger.log(Level.FINE, ps.toString());
            return ps;
        } else {
            throw new SQLException("Unable to establish a connection, connection is null.");
        }
    }

    public static PreparedStatement selectDroolsStatement(Connection connection, String uuid)
            throws SQLException {
        if (connection != null) {
            PreparedStatement ps = connection.prepareStatement(String.format(
                    "SELECT * FROM Drools WHERE %s = ?", DroolsColumns.ID.getColName()));
            ps.setString(1, uuid);
            logger.log(Level.FINE, ps.toString());
            return ps;
        } else {
            throw new SQLException("Unable to establish a connection, connection is null.");
        }
    }

    public static PreparedStatement updateDroolsStatement(Connection connection, String uuid,
            String bpmn, String title, String comment) throws SQLException {
        if (connection != null) {
            PreparedStatement ps = connection.prepareStatement(String.format(
                    "UPDATE Drools SET %s = ?, %s = ?, %s = ? WHERE %s = ?",
                    DroolsColumns.BPMN_PATH.getColName(), DroolsColumns.TITLE.getColName(),
                    DroolsColumns.COMMENT.getColName(), DroolsColumns.ID.getColName()));
            ps.setString(1, bpmn);
            ps.setString(2, title);
            ps.setString(3, comment);
            ps.setString(4, uuid);

            logger.log(Level.FINE, ps.toString());
            return ps;
        } else {
            throw new SQLException("Unable to establish a connection, connection is null.");
        }
    }

    public static PreparedStatement selectLatestUsersExecutionStatement(Connection connection,
            Execution execution) throws SQLException {
        if (connection != null) {
            PreparedStatement ps = connection
                    .prepareStatement(String
                            .format("SELECT * FROM Execution WHERE %s=? AND %s=? AND %s=? AND %s=? ORDER BY timestamp DESC LIMIT 1",
                                    ExecutionColumns.USER_NAME.colName(),
                                    ExecutionColumns.ALG_NAME.colName(),
                                    ExecutionColumns.VERSION.colName(),
                                    ExecutionColumns.CATEGORY_NUM.colName()));
            ps.setString(1, execution.getUser());
            ps.setString(2, execution.getAlgorithmName());
            ps.setString(3, execution.getAlgorithmVersion());
            ps.setString(4, execution.getAlgorithmCategoryId());

            logger.log(Level.FINE, ps.toString());
            return ps;
        } else {
            throw new SQLException("Unable to establish a connection, connection is null.");
        }
    }
}
