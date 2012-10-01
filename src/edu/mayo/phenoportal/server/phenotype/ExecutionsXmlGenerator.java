package edu.mayo.phenoportal.server.phenotype;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.mayo.phenoportal.server.utils.DOMXmlGenerator;
import edu.mayo.phenoportal.shared.database.ExecutionColumns;

/**
 * Create an xml representation of the executions being pulled from the server.
 */
public class ExecutionsXmlGenerator extends DOMXmlGenerator {

    protected static final String EXECUTIONS = "Executions";
    protected static final String EXECUTION = "Execution";
    protected static final String USER_NAME = ExecutionColumns.USER_NAME.colName();
    protected static final String ALG_NAME = ExecutionColumns.ALG_NAME.colName();
    protected static final String VERSION = ExecutionColumns.VERSION.colName();
    protected static final String CATEGORY = ExecutionColumns.CATEGORY.colName();
    protected static final String START_DATE = ExecutionColumns.START_DATE.colName();
    protected static final String END_DATE = ExecutionColumns.END_DATE.colName();
    protected static final String STATUS = ExecutionColumns.STATUS.colName();
    protected static final String ELAPSED = ExecutionColumns.ELAPSED_TIME.colName();

    public void createExecutionXml(String username, String algname, String version,
            String category, String startDate, String endDate, String status, String elapsed) {

        Element executionElement = i_document.createElement(EXECUTION);

        Element userNameElement = i_document.createElement(USER_NAME);
        Text userText = i_document.createTextNode(username);
        userNameElement.appendChild(userText);
        executionElement.appendChild(userNameElement);

        Element algnameElement = i_document.createElement(ALG_NAME);
        Text algnameText = i_document.createTextNode(algname);
        algnameElement.appendChild(algnameText);
        executionElement.appendChild(algnameElement);

        Element versionElement = i_document.createElement(VERSION);
        Text versionText = i_document.createTextNode(version);
        versionElement.appendChild(versionText);
        executionElement.appendChild(versionElement);

        Element categoryElement = i_document.createElement(CATEGORY);
        Text categoryText = i_document.createTextNode(category);
        categoryElement.appendChild(categoryText);
        executionElement.appendChild(categoryElement);

        Element startDateElement = i_document.createElement(START_DATE);
        Text startDateText = i_document.createTextNode(startDate);
        startDateElement.appendChild(startDateText);
        executionElement.appendChild(startDateElement);

        Element endDateElement = i_document.createElement(END_DATE);
        Text endDateText = i_document.createTextNode(endDate);
        endDateElement.appendChild(endDateText);
        executionElement.appendChild(endDateElement);

        Element statusElement = i_document.createElement(STATUS);
        Text statusText = i_document.createTextNode(status);
        statusElement.appendChild(statusText);
        executionElement.appendChild(statusElement);

        Element elapsedElement = i_document.createElement(ELAPSED);
        Text elapsedText = i_document.createTextNode(elapsed);
        elapsedElement.appendChild(elapsedText);
        executionElement.appendChild(elapsedElement);

        i_rootElement.appendChild(executionElement);
    }
}
