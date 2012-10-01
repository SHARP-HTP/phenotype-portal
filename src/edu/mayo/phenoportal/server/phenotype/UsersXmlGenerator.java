package edu.mayo.phenoportal.server.phenotype;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.mayo.phenoportal.server.utils.DOMXmlGenerator;

/**
 * Create an xml representation of the user being pulled from the server.
 */
public class UsersXmlGenerator extends DOMXmlGenerator {

    protected static final String USERS = "Users";
    protected static final String USER = "User";
    protected static final String USER_ID = "UserId";
    protected static final String FIRST_NAME = "FirstName";
    protected static final String LAST_NAME = "LastName";
    protected static final String EMAIL = "Email";
    protected static final String ROLE = "Role";
    protected static final String ENABLED = "Enabled";
    protected static final String REGISTRATION_DATE = "RegistrationDate";

    protected static final String COUNTRY_OR_REGION = "CountryOrRegion";
    protected static final String STREET = "Street";
    protected static final String CITY = "City";
    protected static final String STATE = "State";
    protected static final String ZIP = "Zip";
    protected static final String PHONE = "Phone";

    public void createUserXml(String userid, String fname, String lname, String email, String role,
            String enabled, String registrationDate, String countryRegion, String street,
            String city, String state, String zip, String phone) {

        Element userElement = i_document.createElement(USER);

        // add the userId to the User element
        Element userIdElement = i_document.createElement(USER_ID);
        Text userText = i_document.createTextNode(userid);
        userIdElement.appendChild(userText);
        userElement.appendChild(userIdElement);

        // add the first name to the User element
        Element fnameElement = i_document.createElement(FIRST_NAME);
        Text fnameText = i_document.createTextNode(fname);
        fnameElement.appendChild(fnameText);
        userElement.appendChild(fnameElement);

        // add the last name to the User element
        Element lnameElement = i_document.createElement(LAST_NAME);
        Text lnameText = i_document.createTextNode(lname);
        lnameElement.appendChild(lnameText);
        userElement.appendChild(lnameElement);

        // add the email to the User element
        Element emailElement = i_document.createElement(EMAIL);
        Text emailText = i_document.createTextNode(email);
        emailElement.appendChild(emailText);
        userElement.appendChild(emailElement);

        // add the role to the User element
        Element roleElement = i_document.createElement(ROLE);
        Text roleText = i_document.createTextNode(role);
        roleElement.appendChild(roleText);
        userElement.appendChild(roleElement);

        // add the enabled to the User element
        Element enabledElement = i_document.createElement(ENABLED);
        Text enabledText = i_document.createTextNode(enabled);
        enabledElement.appendChild(enabledText);
        userElement.appendChild(enabledElement);

        // add the registration date to the User element
        Element registrationDateElement = i_document.createElement(REGISTRATION_DATE);
        Text registrationDateText = i_document.createTextNode(registrationDate);
        registrationDateElement.appendChild(registrationDateText);
        userElement.appendChild(registrationDateElement);

        // add the country/region to the User element
        Element countryRegionElement = i_document.createElement(COUNTRY_OR_REGION);
        Text countryRegionText = i_document.createTextNode(countryRegion);
        countryRegionElement.appendChild(countryRegionText);
        userElement.appendChild(countryRegionElement);

        // add the street to the User element
        Element streetAddressElement = i_document.createElement(STREET);
        Text streetAddressText = i_document.createTextNode(street);
        streetAddressElement.appendChild(streetAddressText);
        userElement.appendChild(streetAddressElement);

        // add the city to the User element
        Element citylement = i_document.createElement(CITY);
        Text cityRegionText = i_document.createTextNode(city);
        citylement.appendChild(cityRegionText);
        userElement.appendChild(citylement);

        // add the state to the User element
        Element stateElement = i_document.createElement(STATE);
        Text stateText = i_document.createTextNode(state);
        stateElement.appendChild(stateText);
        userElement.appendChild(stateElement);

        // add the zip to the User element
        Element ziplement = i_document.createElement(ZIP);
        Text zipText = i_document.createTextNode(zip);
        ziplement.appendChild(zipText);
        userElement.appendChild(ziplement);

        // add the phone to the User element
        Element phoneElement = i_document.createElement(PHONE);
        Text phoneText = i_document.createTextNode(phone);
        phoneElement.appendChild(phoneText);
        userElement.appendChild(phoneElement);

        i_rootElement.appendChild(userElement);
    }
}
