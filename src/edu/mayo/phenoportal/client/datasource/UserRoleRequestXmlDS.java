package edu.mayo.phenoportal.client.datasource;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.shared.UserRoleRequest;

public class UserRoleRequestXmlDS extends DataSource {

    private static final String RECORD_X_PATH = "/UserRoleRequests/UserRoleRequest";
    private static UserRoleRequestXmlDS instance = null;

    public static UserRoleRequestXmlDS getInstance() {
        if (instance == null) {
            instance = new UserRoleRequestXmlDS("UserRoleRequestXmlDS");
        }
        return instance;
    }

    public UserRoleRequestXmlDS(String id) {

        setID(id);
        setRecordXPath(RECORD_X_PATH);

        DataSourceTextField userIdField = new DataSourceTextField("Id");
        userIdField.setPrimaryKey(true);

        DataSourceTextField userNameField = new DataSourceTextField("UserName");
        DataSourceTextField requestDateField = new DataSourceTextField("RequestDate");
        DataSourceTextField responseDateField = new DataSourceTextField("ResponseDate");

        DataSourceIntegerField requestGrantedField = new DataSourceIntegerField("RequestGranted");

        LinkedHashMap<String, String> grantedValueMap = new LinkedHashMap<String, String>();
        grantedValueMap.put("1", "<b>Granted</b>");
        grantedValueMap.put("0", "<b>Denied</b>");
        requestGrantedField.setValueMap(grantedValueMap);

        setFields(userIdField, userNameField, requestDateField, responseDateField,
                requestGrantedField);

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
                    // executeRemove(response);
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

    /**
     * Update the record with the backend database.
     * 
     * @param response
     */
    private void executeUpdate(DSResponse response) {

        Record[] record = response.getData();
        UserRoleRequest userRoleRequest = getUserRoleRequest(record[0]);

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.updateUserRoleRequest(userRoleRequest, new AsyncCallback<Boolean>() {

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

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.getUserRoleRequests(new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Object results = XMLTools.selectNodes(result, RECORD_X_PATH);
                Record[] fetchRecords = recordsFromXML(results);

                if (fetchRecords != null) {
                    // add each record
                    for (Record record : fetchRecords) {
                        addData(record);
                    }
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                System.out.println("Failed to get User Role Requests- " + caught);

            }
        });
    }

    private UserRoleRequest getUserRoleRequest(Record record) {

        UserRoleRequest urr = new UserRoleRequest();
        urr.setId(record.getAttribute("Id"));
        urr.setUserName(record.getAttribute("UserName"));
        urr.setRequestDate(record.getAttribute("RequestDate"));
        urr.setResponseDate(record.getAttribute("ResponseDate"));

        int grantedInt = 0;
        try {
            grantedInt = Integer.parseInt(record.getAttribute("RequestGranted"));
        } catch (NumberFormatException nfe) {
        }

        boolean granted = grantedInt == 0 ? false : true;
        urr.setRequestGranted(granted);

        return urr;
    }

}
