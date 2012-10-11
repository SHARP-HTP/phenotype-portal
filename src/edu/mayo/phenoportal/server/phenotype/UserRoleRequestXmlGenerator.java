package edu.mayo.phenoportal.server.phenotype;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.mayo.phenoportal.server.utils.DOMXmlGenerator;

/**
 * Create an xml representation of the user role requests being pulled from the
 * server.
 */
public class UserRoleRequestXmlGenerator extends DOMXmlGenerator {

    protected static final String USER_ROLE_REQUESTS = "UserRoleRequests";
    protected static final String USER_ROLE_REQUEST = "UserRoleRequest";

    protected static final String ID = "Id";
    protected static final String USER_NAME = "UserName";
    protected static final String REQUEST_DATE = "RequestDate";
    protected static final String RESPONSE_DATE = "ResponseDate";
    protected static final String REQUEST_GRANTED = "RequestGranted";

    public void createUserRoleRquestXml(String id, String userName, String requestDate,
            String responseDate, String requestGranted) {

        Element userRoleRequestElement = i_document.createElement(USER_ROLE_REQUEST);

        // add the id to the User Role Request element
        Element idElement = i_document.createElement(ID);
        Text idText = i_document.createTextNode(id);
        idElement.appendChild(idText);
        userRoleRequestElement.appendChild(idElement);

        // add the userName to the User Role Request element
        Element userNameElement = i_document.createElement(USER_NAME);
        Text userNameText = i_document.createTextNode(userName);
        userNameElement.appendChild(userNameText);
        userRoleRequestElement.appendChild(userNameElement);

        // add the RequestDate to the User Role Request element
        Element requestDateElement = i_document.createElement(REQUEST_DATE);
        Text requestDateText = i_document.createTextNode(requestDate);
        requestDateElement.appendChild(requestDateText);
        userRoleRequestElement.appendChild(requestDateElement);

        // add the ResponseDate to the User Role Request element
        Element responseDateElement = i_document.createElement(RESPONSE_DATE);
        Text responseDateText = i_document.createTextNode(responseDate);
        responseDateElement.appendChild(responseDateText);
        userRoleRequestElement.appendChild(responseDateElement);

        // add the RequestGranted to the User Role Request element
        Element requestGrantedElement = i_document.createElement(REQUEST_GRANTED);
        Text requestGrantedText = i_document.createTextNode(requestGranted);
        requestGrantedElement.appendChild(requestGrantedText);
        userRoleRequestElement.appendChild(requestGrantedElement);

        i_rootElement.appendChild(userRoleRequestElement);
    }

}
