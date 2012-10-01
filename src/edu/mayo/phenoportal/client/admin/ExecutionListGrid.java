package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import edu.mayo.phenoportal.client.datasource.ExecutionXmlDS;
import edu.mayo.phenoportal.shared.database.ExecutionColumns;

public class ExecutionListGrid extends ListGrid {

    private final ExecutionXmlDS i_executionDS;

    public ExecutionListGrid() {
        super();
        i_executionDS = ExecutionXmlDS.getInstance();

        setWidth100();
        setHeight100();
        setShowAllRecords(true);
        setDataSource(i_executionDS);

        ListGridField userNameField = new ListGridField(ExecutionColumns.USER_NAME.colName(),
                ExecutionColumns.USER_NAME.normName());
        ListGridField algorithmNameField = new ListGridField(ExecutionColumns.ALG_NAME.colName(),
                ExecutionColumns.ALG_NAME.normName());
        algorithmNameField.setShowHover(true);

        ListGridField versionField = new ListGridField(ExecutionColumns.VERSION.colName(),
                ExecutionColumns.VERSION.normName());
        ListGridField categoryField = new ListGridField(ExecutionColumns.CATEGORY.colName(),
                ExecutionColumns.CATEGORY.normName());
        categoryField.setShowHover(true);

        ListGridField startDateField = new ListGridField(ExecutionColumns.START_DATE.colName(),
                ExecutionColumns.START_DATE.normName());
        ListGridField endDateField = new ListGridField(ExecutionColumns.END_DATE.colName(),
                ExecutionColumns.END_DATE.normName());
        ListGridField statusField = new ListGridField(ExecutionColumns.STATUS.colName(),
                ExecutionColumns.STATUS.normName());
        ListGridField elapsedField = new ListGridField(ExecutionColumns.ELAPSED_TIME.colName(),
                ExecutionColumns.ELAPSED_TIME.normName());

        setGroupByField(ExecutionColumns.USER_NAME.colName());
        setFields(userNameField, algorithmNameField, versionField, categoryField, startDateField,
                endDateField, elapsedField, statusField);

        setAutoFetchData(true);
        setCanEdit(false);
        setCanRemoveRecords(false);

        // setCanHover(true);
        // setShowHoverComponents(true);
        setHoverWidth(400);

    }
}
