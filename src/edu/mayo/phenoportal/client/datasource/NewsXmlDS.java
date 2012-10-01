package edu.mayo.phenoportal.client.datasource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.shared.News;
import edu.mayo.phenoportal.shared.database.NewsColumns;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DataSource to hold the News Items.
 */
public class NewsXmlDS extends DataSource {

    private static final Logger lgr = Logger.getLogger(NewsXmlDS.class.getName());

    private static final String RECORD_X_PATH = "/newsItems/newsItem";
    public static final String ATTR_FROM_USER = "from_user";

    private static NewsXmlDS instance = null;

    public static NewsXmlDS getInstance() {
        if (instance == null) {
            instance = new NewsXmlDS("NewsXmlDS");
        }
        return instance;
    }

    public NewsXmlDS(String id) {

        setID(id);
        setRecordXPath(RECORD_X_PATH);

        DataSourceTextField idField = new DataSourceTextField(NewsColumns.ID.colName());
        idField.setPrimaryKey(true);
        idField.setHidden(true);

        DataSourceDateField dateField = new DataSourceDateField(NewsColumns.DATE.colName());
        DataSourceTextField infoField = new DataSourceTextField(NewsColumns.INFO.colName());

        setFields(idField, dateField, infoField);

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
        // super.transformResponse(response, request, data);
    }

    /**
     * Add a news item.
     */
    private void executeAdd(DSResponse response) {

        Record[] record = response.getData();
        final News news = getNews(record[0]);

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.addNews(news, new AsyncCallback<Boolean>() {

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
        News news = getNews(record[0]);

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.removeNews(news, new AsyncCallback<Boolean>() {

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
        News news = getNews(record[0]);

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.updateNews(news, new AsyncCallback<Boolean>() {

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
        service.getNews(new AsyncCallback<String>() {

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
                lgr.log(Level.SEVERE, "Failed to get news items - " + caught);
            }
        });

    }

    /**
     * Convert the Record into a News object.
     * 
     * @param record
     * @return
     */
    private News getNews(Record record) {
        News news = new News();

        news.setId(record.getAttribute(NewsColumns.ID.colName()));
        news.setDate(record.getAttribute(NewsColumns.DATE.colName()));
        news.setInformation(record.getAttribute(NewsColumns.INFO.colName()));

        return news;
    }
}
