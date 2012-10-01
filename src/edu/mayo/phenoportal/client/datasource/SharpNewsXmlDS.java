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
import edu.mayo.phenoportal.shared.SharpNews;
import edu.mayo.phenoportal.shared.database.NewsColumns;
import edu.mayo.phenoportal.shared.database.SharpNewsColumns;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DataSource to hold the SHARP news items
 */
public class SharpNewsXmlDS extends DataSource {

    private static final Logger lgr = Logger.getLogger(NewsXmlDS.class.getName());
    public static final String ATTR_FROM_USER = "from_user";

    private static final String RECORD_X_PATH = "/sharpNewsItems/sharpNewsItem";
    private static SharpNewsXmlDS instance = null;

    public static SharpNewsXmlDS getInstance() {
        if (instance == null) {
            instance = new SharpNewsXmlDS("SharpNewsXmlDS");
        }
        return instance;
    }

    public SharpNewsXmlDS(String id) {

        setID(id);
        setRecordXPath(RECORD_X_PATH);

        DataSourceTextField idField = new DataSourceTextField(NewsColumns.ID.colName());
        idField.setPrimaryKey(true);
        idField.setHidden(true);

        DataSourceTextField infoField = new DataSourceTextField(NewsColumns.INFO.colName());

        setFields(idField, infoField);

        setClientOnly(true);
    }

    @Override
    protected void transformResponse(DSResponse response, DSRequest request, Object data) {

        if (request.getOperationType() != null) {
            switch (request.getOperationType()) {

                case ADD: {

                    // check to see if the add was called from the user on the
                    // form.
                    Record[] record = response.getData();
                    String fromUser = record[0].getAttribute(ATTR_FROM_USER);
                    if (fromUser != null && fromUser.equals(ATTR_FROM_USER)) {
                        executeAdd(response);
                    }
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

    /**
     * Add a news item.
     */
    private void executeAdd(DSResponse response) {

        Record[] record = response.getData();
        final SharpNews news = getSharpNews(record[0]);

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.addSharpNews(news, new AsyncCallback<Boolean>() {

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
     * Remove a news item.
     */
    private void executeRemove(DSResponse response) {
        Record[] record = response.getData();
        SharpNews news = getSharpNews(record[0]);

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.removeSharpNews(news, new AsyncCallback<Boolean>() {

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
     * Update a news item.
     */
    private void executeUpdate(DSResponse response) {

        Record[] record = response.getData();
        SharpNews news = getSharpNews(record[0]);

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.updateSharpNews(news, new AsyncCallback<Boolean>() {

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
     * Fetch the news items by requesting them from the RPC call.
     */
    private void executeFetch(DSRequest request) {

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.getSharpNews(new AsyncCallback<String>() {

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
                lgr.log(Level.SEVERE, "Failed to get sharp news items - " + caught);
            }
        });
    }

    /**
     * Convert the Record into a SharpNews object.
     * 
     * @param record
     * @return
     */
    private SharpNews getSharpNews(Record record) {
        SharpNews news = new SharpNews();

        news.setId(record.getAttribute(SharpNewsColumns.ID.colName()));
        news.setInformation(record.getAttribute(SharpNewsColumns.INFO.colName()));

        return news;
    }

}
