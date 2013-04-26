package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import edu.mayo.phenoportal.client.datasource.UploadersXmlDS;
import edu.mayo.phenoportal.shared.database.UploadColumns;

public class UploadListGrid extends ListGrid {

    private final UploadersXmlDS i_uploadersDS;

    public UploadListGrid() {
        super();
        i_uploadersDS = UploadersXmlDS.getInstance();

        setWidth100();
        setHeight100();
        setShowAllRecords(true);
        setDataSource(i_uploadersDS);

        ListGridField userNameField = new ListGridField(UploadColumns.USER.colName(),
                UploadColumns.USER.normName());
        ListGridField algorithmNameField = new ListGridField(UploadColumns.NAME.colName(),
                UploadColumns.NAME.normName());
        ListGridField versionField = new ListGridField(UploadColumns.VERSION.colName(),
                UploadColumns.VERSION.normName());
        ListGridField categoryField = new ListGridField(UploadColumns.PARENT_ID.colName(),
                UploadColumns.PARENT_ID.normName());
        ListGridField dateField = new ListGridField(UploadColumns.UPLOAD_DATE.colName(),
                UploadColumns.UPLOAD_DATE.normName());

        setGroupByField(UploadColumns.USER.colName());
        setFields(userNameField, algorithmNameField, versionField, categoryField, dateField);

        setAutoFetchData(true);
        setCanEdit(false);
        setCanRemoveRecords(false);

        draw();

    }
}
