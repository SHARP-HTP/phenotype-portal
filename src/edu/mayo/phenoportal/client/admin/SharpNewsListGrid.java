package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import edu.mayo.phenoportal.client.datasource.SharpNewsXmlDS;
import edu.mayo.phenoportal.shared.database.SharpNewsColumns;

public class SharpNewsListGrid extends ListGrid {

    private static final int CELL_HEIGHT = 60;

    private final SharpNewsXmlDS i_sharpNewsXmlDS;

    public SharpNewsListGrid() {
        super();
        i_sharpNewsXmlDS = SharpNewsXmlDS.getInstance();

        setWidth100();
        setHeight100();
        setShowAllRecords(true);
        setDataSource(i_sharpNewsXmlDS);

        // This will show the delete icon.
        setCanRemoveRecords(true);
        setWarnOnRemoval(true);
        setWarnOnRemovalMessage("Are you sure you want to remove this SHARP news item?  This cannot be undone.");
        setAnimateRemoveRecord(true);

        // increase the cell height to allow multiple rows of text to be
        // displayed.
        setCellHeight(CELL_HEIGHT);
        setWrapCells(true);

        ListGridField idField = new ListGridField(SharpNewsColumns.ID.colName(), "ID");
        // Don't allow the id to be edited.
        idField.setCanEdit(false);
        idField.setHidden(true);

        ListGridField infoField = new ListGridField(SharpNewsColumns.INFO.colName(),
                "SHARP Information");

        // use a text area as the editor
        TextAreaItem infoAreaItem = new TextAreaItem();
        infoAreaItem.setHeight(CELL_HEIGHT);
        infoField.setEditorType(infoAreaItem);

        setFields(idField, infoField);

        setAutoFetchData(true);
        setCanEdit(true);
        setEditEvent(ListGridEditEvent.DOUBLECLICK);
        setModalEditing(true);
    }
}
