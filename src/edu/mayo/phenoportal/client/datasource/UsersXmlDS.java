package edu.mayo.phenoportal.client.datasource;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

import edu.mayo.phenoportal.client.authentication.UserRoles;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.shared.User;

public class UsersXmlDS extends DataSource {

    private static final String RECORD_X_PATH = "/Users/User";
    private static UsersXmlDS instance = null;

    public static UsersXmlDS getInstance() {
        if (instance == null) {
            instance = new UsersXmlDS("usersXmlDS");
        }
        return instance;
    }

    public UsersXmlDS(String id) {

        setID(id);
        setRecordXPath(RECORD_X_PATH);

        DataSourceTextField userIdField = new DataSourceTextField("UserId");
        userIdField.setPrimaryKey(true);

        DataSourceTextField firstNameField = new DataSourceTextField("FirstName");
        DataSourceTextField lastNameField = new DataSourceTextField("LastName");
        DataSourceTextField emailField = new DataSourceTextField("Email");
        DataSourceIntegerField roleField = new DataSourceIntegerField("Role");
        DataSourceTextField registrationDateField = new DataSourceTextField("RegistrationDate");

        LinkedHashMap<String, String> roleValueMap = new LinkedHashMap<String, String>();
        roleValueMap.put(UserRoles.ADMIN + "", "<b>Admin</b>");
        roleValueMap.put(UserRoles.EXECUTE + "", "<b>Execute</b>");
        roleValueMap.put(UserRoles.READ + "", "<b>Read Only</b>");
        roleField.setValueMap(roleValueMap);

        DataSourceIntegerField enabledField = new DataSourceIntegerField("Enabled");

        LinkedHashMap<String, String> enableValueMap = new LinkedHashMap<String, String>();
        enableValueMap.put("1", "<b>Enabled</b>");
        enableValueMap.put("0", "<b>Disabled</b>");
        enabledField.setValueMap(enableValueMap);

        DataSourceTextField countryOrRegionField = new DataSourceTextField("CountryOrRegion");
        DataSourceTextField streetField = new DataSourceTextField("Street");
        DataSourceTextField cityField = new DataSourceTextField("City");
        DataSourceTextField stateField = new DataSourceTextField("State");
        DataSourceTextField zipField = new DataSourceTextField("Zip");
        DataSourceTextField phoneField = new DataSourceTextField("Phone");

        setFields(userIdField, firstNameField, lastNameField, emailField, roleField, enabledField,
                registrationDateField, countryOrRegionField, streetField, cityField, stateField,
                zipField, phoneField);

        setClientOnly(true);
    }

    @Override
    protected void transformResponse(DSResponse response, DSRequest request, Object data) {

        if (request.getOperationType() != null) {
            switch (request.getOperationType()) {

                case ADD: {
                }
                    break;
                case FETCH: {
                    executeFetch(request);
                }
                    break;
                case REMOVE: {
                    executeRemove(response);
                }
                    break;
                case UPDATE: {
                    executeUpdate(response);
                }
                    break;

                default:
                    break;
            }
        }
        super.transformResponse(response, request, data);
    }

    private void executeRemove(DSResponse response) {
        Record[] record = response.getData();
        User user = getUser(record[0]);

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.removeUser(user, new AsyncCallback<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                // nothing to do
            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO log the failure.
            }
        });
    }

    private void executeUpdate(DSResponse response) {

        Record[] record = response.getData();
        User user = getUser(record[0]);

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.updateUser(user, new AsyncCallback<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                // nothing to do
            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO log the failure.
            }
        });
    }

    /**
     * Fetch the categories by requesting them from the RPC call.
     */
    private void executeFetch(DSRequest request) {

        String userId = null;
        Criteria criteria = request.getCriteria();

        if (criteria != null) {
            userId = criteria.getAttribute("userId");
        }

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.getUsers(new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {

                Object results = XMLTools.selectNodes(result, RECORD_X_PATH);
                Record[] fetchRecords = recordsFromXML(results);

                if (fetchRecords != null) {
                    // add each record
                    for (Record record : fetchRecords) {

                        if (record.getAttribute("UserId").equalsIgnoreCase("admin")) {
                            record.setAttribute("enabled", false);
                        }
                        addData(record);
                    }
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO log failure
                System.out.println("Failed to get Users - " + caught);

            }
        });

    }

    private User getUser(Record record) {
        User user = new User();

        user.setUserName(record.getAttribute("UserId"));
        user.setFirstName(record.getAttribute("FirstName"));
        user.setLastName(record.getAttribute("LastName"));
        user.setEmail(record.getAttribute("Email"));
        user.setRole(record.getAttributeAsInt("Role"));
        user.setEnable(record.getAttributeAsInt("Enabled"));
        user.setRegistrationDate(record.getAttribute("RegistrationDate"));

        user.setCountryRegion(record.getAttribute("CountryOrRegion"));
        user.setAddress(record.getAttribute("Street"));
        user.setCity(record.getAttribute("City"));
        user.setState(record.getAttribute("State"));
        user.setZipCode(record.getAttribute("Zip"));
        user.setPhoneNumber(record.getAttribute("Phone"));

        return user;
    }

}
