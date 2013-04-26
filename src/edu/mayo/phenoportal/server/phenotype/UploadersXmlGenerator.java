package edu.mayo.phenoportal.server.phenotype;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.mayo.phenoportal.server.utils.DOMXmlGenerator;
import edu.mayo.phenoportal.shared.database.UploadColumns;

/**
 * Create an xml representation of the uploaders being pulled from the server.
 */
public class UploadersXmlGenerator extends DOMXmlGenerator {

    protected static final String UPLOADERS = "Uploaders";
    protected static final String UPLOADER = "Uploader";
    protected static final String USER_NAME = UploadColumns.USER.colName();
    protected static final String ALG_NAME = UploadColumns.NAME.colName();
    protected static final String VERSION = UploadColumns.VERSION.colName();
    protected static final String CATEGORY = UploadColumns.PARENT_ID.colName();
    protected static final String UP_DATE = UploadColumns.UPLOAD_DATE.colName();

    public void createUploaderXml(String username, String algname, String version, String category,
            String update) {

        Element uploaderElement = i_document.createElement(UPLOADER);

        Element userNameElement = i_document.createElement(USER_NAME);
        Text userText = i_document.createTextNode(username);
        userNameElement.appendChild(userText);
        uploaderElement.appendChild(userNameElement);

        Element algnameElement = i_document.createElement(ALG_NAME);
        Text algnameText = i_document.createTextNode(algname);
        algnameElement.appendChild(algnameText);
        uploaderElement.appendChild(algnameElement);

        Element versionElement = i_document.createElement(VERSION);
        Text versionText = i_document.createTextNode(version);
        versionElement.appendChild(versionText);
        uploaderElement.appendChild(versionElement);

        Element categoryElement = i_document.createElement(CATEGORY);
        Text categoryText = i_document.createTextNode(category);
        categoryElement.appendChild(categoryText);
        uploaderElement.appendChild(categoryElement);

        Element updateElement = i_document.createElement(UP_DATE);
        Text updateText = i_document.createTextNode(update);
        updateElement.appendChild(updateText);
        uploaderElement.appendChild(updateElement);

        i_rootElement.appendChild(uploaderElement);
    }
}
