package edu.mayo.phenoportal.client.admin;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.mayo.phenoportal.client.datasource.NewsXmlDS;
import edu.mayo.phenoportal.shared.database.NewsColumns;

import java.util.Date;

public class NewsListGrid extends ListGrid {

    private final NewsXmlDS i_newsXmlDS;

    public NewsListGrid() {
        super();
        i_newsXmlDS = NewsXmlDS.getInstance();

        setWidth100();
        setHeight100();
        setShowAllRecords(true);
        setDataSource(i_newsXmlDS);

        // This will show the delete icon.
        setCanRemoveRecords(true);
        setWarnOnRemoval(true);
        setWarnOnRemovalMessage("Are you sure you want to remove this news item?  This cannot be undone.");
        setAnimateRemoveRecord(true);

        ListGridField idField = new ListGridField(NewsColumns.ID.colName(), "ID");
        // Don't allow the id to be edited.
        idField.setCanEdit(false);
        idField.setWidth(40);
        idField.setHidden(true);

        ListGridField dateField = new ListGridField(NewsColumns.DATE.colName(), "Date");
        // dateField.setWidth(100);
        dateField.setAlign(Alignment.LEFT);
        dateField.setType(ListGridFieldType.DATE);

        // format the display of the date
        final DateTimeFormat dateFormatter = DateTimeFormat.getFormat("MMMM dd, yyyy");
        dateField.setCellFormatter(new CellFormatter() {

            @Override
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value != null) {

                    try {
                        Date dateValue = new Date(Date.parse((String) value));
                        return dateFormatter.format(dateValue);
                    } catch (Exception e) {
                        return value.toString();
                    }
                } else {
                    return "";
                }
            }
        });

        ListGridField infoField = new ListGridField(NewsColumns.INFO.colName(), "Information");
        infoField.setWidth("*"); // take the remaining width.

        setFields(idField, dateField, infoField);

        setAutoFetchData(true);
        setCanEdit(true);
        setEditEvent(ListGridEditEvent.DOUBLECLICK);
        setModalEditing(true);
    }

}
