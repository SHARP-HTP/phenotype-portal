package edu.mayo.phenoportal.client.datasource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceTextField;

import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.shared.database.ExecutionColumns;

public class ExecutionXmlDS extends DataSource {

    private static final String RECORD_X_PATH = "/Executions/Execution";
    private static ExecutionXmlDS instance = null;

    public static ExecutionXmlDS getInstance() {
        if (instance == null) {
            instance = new ExecutionXmlDS("executionXmlDS");
        }
        return instance;
    }

    public ExecutionXmlDS(String id) {

        setID(id);
        setRecordXPath(RECORD_X_PATH);

        DataSourceTextField uploaderField = new DataSourceTextField(
                ExecutionColumns.USER_NAME.colName());
        uploaderField.setPrimaryKey(true);
        DataSourceTextField algorithmField = new DataSourceTextField(
                ExecutionColumns.ALG_NAME.colName());
        DataSourceTextField versionField = new DataSourceTextField(
                ExecutionColumns.VERSION.colName());
        DataSourceTextField categoryField = new DataSourceTextField(
                ExecutionColumns.CATEGORY.colName());
        DataSourceTextField startDateField = new DataSourceTextField(
                ExecutionColumns.START_DATE.colName());
        DataSourceTextField endDateField = new DataSourceTextField(
                ExecutionColumns.END_DATE.colName());
        DataSourceTextField statusField = new DataSourceTextField(ExecutionColumns.STATUS.colName());
        DataSourceTextField elapsedField = new DataSourceTextField(
                ExecutionColumns.ELAPSED_TIME.colName());

        setFields(uploaderField, algorithmField, versionField, categoryField, startDateField,
                endDateField, elapsedField, statusField);

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
                }
                    break;
                case UPDATE: {
                }
                    break;

                default:
                    break;
            }
        }
        super.transformResponse(response, request, data);
    }

    /**
     * Fetch the categories by requesting them from the RPC call.
     */
    private void executeFetch(DSRequest request) {

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.getExecutions(new AsyncCallback<String>() {

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
                // TODO log failure
                System.out.println("Failed to get Executions - " + caught);

            }
        });

    }

}
