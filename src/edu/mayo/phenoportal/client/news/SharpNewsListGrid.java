package edu.mayo.phenoportal.client.news;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import edu.mayo.phenoportal.client.datasource.SharpNewsXmlDS;
import edu.mayo.phenoportal.shared.database.SharpNewsColumns;

public class SharpNewsListGrid extends ListGrid {

    private final static int GRID_CELL_HEIGHT = 130;

    private final SharpNewsXmlDS i_sharpNewsXmlDS;

    public SharpNewsListGrid() {
        super();

        i_sharpNewsXmlDS = SharpNewsXmlDS.getInstance();

        setWidth100();
        setHeight100();
        setShowAllRecords(true);
        setDataSource(i_sharpNewsXmlDS);

        setShowEmptyMessage(false);
        setShowHeader(false);

        setCellHeight(GRID_CELL_HEIGHT);
        setWrapCells(true);

        ListGridField infoField = new ListGridField(SharpNewsColumns.INFO.colName(), "Information");

        setFields(infoField);

        setAutoFetchData(true);
        setCanEdit(false);
    }
}
