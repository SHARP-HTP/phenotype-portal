package edu.mayo.phenoportal.server.phenotype;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.mayo.phenoportal.server.utils.DOMXmlGenerator;

public class AlgorithmXmlGenerator extends DOMXmlGenerator {

    protected static final String ALGORITHMS = "algorithms";
    protected static final String ALGORITHM = "algorithm";

    protected static final String ID = "parentID";
    protected static final String NAME = "name";
    protected static final String VERSION = "version";
    protected static final String USER = "user";
    protected static final String DESCRIPTION = "description";
    protected static final String INSTITUTION = "institution";
    protected static final String UPLOAD_DATE = "uploadDate";
    protected static final String CREATION_DATE = "creationDate";
    protected static final String COMMENTS = "comments";
    protected static final String STATUS = "status";
    protected static final String ASSOCIATION_LINK = "assocLink";
    protected static final String ASSOCIATION_NAME = "assocName";

    public void createAlgorithmXml(String id, String name, String version, String user,
            String description, String institution, String uploadDate, String creationDate,
            String comments, String status, String assocLink, String assocName) {

        Element algorithmElement = i_document.createElement(ALGORITHM);

        // add the parentID to the Algorithm element
        Element idElement = i_document.createElement(ID);
        Text idText = i_document.createTextNode(id);
        idElement.appendChild(idText);
        algorithmElement.appendChild(idElement);

        // add the name to the Algorithm element
        Element nameElement = i_document.createElement(NAME);
        Text nameText = i_document.createTextNode(name);
        nameElement.appendChild(nameText);
        algorithmElement.appendChild(nameElement);

        // add the version to the Algorithm element
        Element versionElement = i_document.createElement(VERSION);
        Text versionText = i_document.createTextNode(version);
        versionElement.appendChild(versionText);
        algorithmElement.appendChild(versionElement);

        // add the user to the Algorithm element
        Element userElement = i_document.createElement(USER);
        Text userText = i_document.createTextNode(user);
        userElement.appendChild(userText);
        algorithmElement.appendChild(userElement);

        // add the description to the Algorithm element
        Element descriptionElement = i_document.createElement(DESCRIPTION);
        Text descriptionText = i_document.createTextNode(description);
        descriptionElement.appendChild(descriptionText);
        algorithmElement.appendChild(descriptionElement);

        // add the institution to the Algorithm element
        Element institutionElement = i_document.createElement(INSTITUTION);
        Text institutionText = i_document.createTextNode(institution);
        institutionElement.appendChild(institutionText);
        algorithmElement.appendChild(institutionElement);

        // add the uploadDate to the Algorithm element
        Element uploadDateElement = i_document.createElement(UPLOAD_DATE);
        Text uploadDateText = i_document.createTextNode(uploadDate);
        uploadDateElement.appendChild(uploadDateText);
        algorithmElement.appendChild(uploadDateElement);

        // add the creationDate to the Algorithm element
        Element creationDateElement = i_document.createElement(CREATION_DATE);
        Text creationDateText = i_document.createTextNode(creationDate);
        creationDateElement.appendChild(creationDateText);
        algorithmElement.appendChild(creationDateElement);

        // add the comments to the Algorithm element
        Element commentsElement = i_document.createElement(COMMENTS);
        Text commentsText = i_document.createTextNode(comments);
        commentsElement.appendChild(commentsText);
        algorithmElement.appendChild(commentsElement);

        // add the status to the Algorithm element
        Element statusElement = i_document.createElement(STATUS);
        Text statusText = i_document.createTextNode(status);
        statusElement.appendChild(statusText);
        algorithmElement.appendChild(statusElement);

        // add the assocLink to the Algorithm element
        Element assocLinkElement = i_document.createElement(ASSOCIATION_LINK);
        Text assocLinkText = i_document.createTextNode(assocLink);
        assocLinkElement.appendChild(assocLinkText);
        algorithmElement.appendChild(assocLinkElement);

        // add the assocName to the Algorithm element
        Element assocNameElement = i_document.createElement(ASSOCIATION_NAME);
        Text assocNameText = i_document.createTextNode(assocName);
        assocNameElement.appendChild(assocNameText);
        algorithmElement.appendChild(assocNameElement);

        i_rootElement.appendChild(algorithmElement);
    }
}
