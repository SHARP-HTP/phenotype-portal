package edu.mayo.phenoportal.client.datasource;

import java.util.logging.Level;
import java.util.logging.Logger;

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

/**
 * DataSource to hold the recently uploaded algorithms.
 */
public class UploadedAlgorithmXmlDS extends DataSource {

    private static final Logger lgr = Logger.getLogger(UploadedAlgorithmXmlDS.class.getName());

    private static final String RECORD_X_PATH = "/algorithms/algorithm";
    private static UploadedAlgorithmXmlDS instance = null;

    public static UploadedAlgorithmXmlDS getInstance() {
        if (instance == null) {
            instance = new UploadedAlgorithmXmlDS("UploadedAlgorithmXmlDS");
        }
        return instance;
    }

    public UploadedAlgorithmXmlDS(String id) {

        setID(id);
        setRecordXPath(RECORD_X_PATH);

        DataSourceTextField nameField = new DataSourceTextField(UploadColumns.NAME.colName());
        DataSourceTextField versionField = new DataSourceTextField(UploadColumns.VERSION.colName());
        DataSourceTextField userField = new DataSourceTextField(UploadColumns.USER.colName());
        DataSourceTextField descriptionField = new DataSourceTextField(
                UploadColumns.DESCRIPTION.colName());
        DataSourceTextField institutionField = new DataSourceTextField(
                UploadColumns.INSTITUTION.colName());
        DataSourceTextField uploadDateField = new DataSourceTextField(
                UploadColumns.UPLOAD_DATE.colName());
        DataSourceTextField creationDateField = new DataSourceTextField(
                UploadColumns.CREATEDATE.colName());
        DataSourceTextField commentsField = new DataSourceTextField(UploadColumns.COMMENT.colName());
        DataSourceTextField statusField = new DataSourceTextField(UploadColumns.STATUS.colName());
        DataSourceTextField assocLinkField = new DataSourceTextField(
                UploadColumns.ASSOC_LINK.colName());
        DataSourceTextField assocNameField = new DataSourceTextField(
                UploadColumns.ASSOC_NAME.colName());
        DataSourceTextField categoryField = new DataSourceTextField(UploadColumns.ID.colName());

        setFields(nameField, versionField, userField, descriptionField, institutionField,
                uploadDateField, creationDateField, commentsField, statusField, assocLinkField,
                assocNameField, categoryField);

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
        service.getLatestUploadedAlgorithms(new AsyncCallback<String>() {

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
                lgr.log(Level.SEVERE, "Failed to get recently uploaded algorithms - " + caught);
            }
        });

    }

}
