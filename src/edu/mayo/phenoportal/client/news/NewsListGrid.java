package edu.mayo.phenoportal.client.news;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import edu.mayo.phenoportal.client.datasource.NewsXmlDS;
import edu.mayo.phenoportal.shared.database.NewsColumns;

import java.util.Date;

/**
 * ListGrid to display the news/updates for Phenoportal
 */
public class NewsListGrid extends ListGrid {

    private final NewsXmlDS i_newsXmlDS;

    public NewsListGrid() {
        super();

        i_newsXmlDS = NewsXmlDS.getInstance();

        setWidth100();
        setHeight("30%");
        setShowAllRecords(true);
        setDataSource(i_newsXmlDS);

        setShowEmptyMessage(false);

        setWrapCells(true);

        ListGridField dateField = new ListGridField(NewsColumns.DATE.colName(), "Date");
        dateField.setAlign(Alignment.LEFT);
        dateField.setType(ListGridFieldType.DATE);
        dateField.setWrap(false);

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

        ListGridField infoField = new ListGridField(NewsColumns.INFO.colName(), "News");

        dateField.setWidth("35%");
        infoField.setWidth("*");

        setFields(dateField, infoField);

        // set the initial sort
        SortSpecifier[] sortspec = new SortSpecifier[1];
        sortspec[0] = new SortSpecifier(NewsColumns.DATE.colName(), SortDirection.DESCENDING);
        setInitialSort(sortspec);

        setAutoFetchData(true);
        setCanEdit(false);

        setCanHover(true);
        setShowHover(true);
        setShowHoverComponents(true);
    }

    @Override
    protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {
        DetailViewer detailViewer = new DetailViewer();
        detailViewer.setWidth(400);

        // Define the fields that we want to display in the details popup. These
        // fields are populated from the selected record.
        DetailViewerField dateField = new DetailViewerField(NewsColumns.DATE.colName(),
                NewsColumns.DATE.normName());
        DetailViewerField newsField = new DetailViewerField(NewsColumns.INFO.colName(), "News");

        detailViewer.setFields(dateField, newsField);

        detailViewer.setData(new Record[] { record });
        return detailViewer;
    }
}
