package edu.mayo.phenoportal.client.datasource;

import mayo.edu.cts2.editor.client.utils.RandomString;

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
import edu.mayo.phenoportal.shared.database.UploadColumns;

public class UploadersXmlDS extends DataSource {

    private static final String RECORD_X_PATH = "/Uploaders/Uploader";
    private static final String PRIMARY_KEY_ID = "primaryKey";

    private RandomString i_randomString;

    private static UploadersXmlDS instance = null;

    public static UploadersXmlDS getInstance() {
        if (instance == null) {
            instance = new UploadersXmlDS("uploadersXmlDS");
        }
        return instance;
    }

    public UploadersXmlDS(String id) {

        setID(id);
        setRecordXPath(RECORD_X_PATH);

        DataSourceTextField primaryKeyField = new DataSourceTextField(PRIMARY_KEY_ID);
        primaryKeyField.setPrimaryKey(true);

        DataSourceTextField uploaderField = new DataSourceTextField(UploadColumns.USER.colName());
        DataSourceTextField algorithmField = new DataSourceTextField(UploadColumns.NAME.colName());
        DataSourceTextField versionField = new DataSourceTextField(UploadColumns.VERSION.colName());
        DataSourceTextField categoryField = new DataSourceTextField(UploadColumns.ID.colName());
        DataSourceTextField dateField = new DataSourceTextField(UploadColumns.UPLOAD_DATE.colName());

        setFields(primaryKeyField, uploaderField, algorithmField, versionField, categoryField,
                dateField);

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
        service.getUploaders(new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {

                Object results = XMLTools.selectNodes(result, RECORD_X_PATH);
                Record[] fetchRecords = recordsFromXML(results);

                if (fetchRecords != null) {
                    // add each record
                    for (Record record : fetchRecords) {
                        if (record != null) {
                            // generate our own primary key
                            record.setAttribute(PRIMARY_KEY_ID, nextPrimaryKey());
                            addData(record);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO log failure
                System.out.println("Failed to get Uploaders - " + caught);

            }
        });

    }

    public String nextPrimaryKey() {
        if (i_randomString == null) {
            i_randomString = new RandomString(20);
        }
        return i_randomString.nextString();
    }

}
